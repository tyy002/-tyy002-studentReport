package com.gf.config;


import com.gf.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.util.DigestUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userService;
    
    //认证
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        //校验用户
        auth.userDetailsService( userService ).passwordEncoder( new PasswordEncoder() {
            //对密码进行加密
            @Override
            public String encode(CharSequence charSequence) {
                System.out.println(charSequence.toString());
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }
            //对密码进行判断匹配
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                boolean res = s.equals( encode );
                return res;
            }
        } );
       
    }

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","index","/login","/login.html","/kaptcha","/login-error","/401","/css/**","/js/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .successHandler(new MyLoginSuccessHandler()) //登陆成功  注意：defaultSuccessUrl和successHandler 不能一起使用，二者只能选其一    否则 successHandler 无效
                .failureHandler(new MyLoginFilaHandler())   //登录失败   同上二者只能选其一
                .and()
                .rememberMe()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPointHandler()); //未登录或权限不足  注意：authenticationEntryPoint 和accessDeniedPage 不能同时使用，否则 accessDeniedPage 无效
        http.logout()
        .deleteCookies("JSESSIONID") //删除浏览器中cookie的JSESSIONID  可以多删
        .addLogoutHandler(new MyLogoutHandler())
        .logoutSuccessHandler(new MyLogoutSuccessHandler());//退出登录   注意：logoutSuccessHandler和logoutSuccessUrl 不能同时使用，否则logoutSuccessHandler失效
        
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .invalidSessionUrl("/login")//session超时
        .sessionFixation().migrateSession()
        .maximumSessions(1)   //session最多一个
        .maxSessionsPreventsLogin(false).expiredSessionStrategy(new MyExpiredSessionStrategy()); 
        
        
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin().disable();//开放iframe
    }


}
