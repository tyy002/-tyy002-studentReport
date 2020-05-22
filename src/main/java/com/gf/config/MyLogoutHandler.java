package com.gf.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import com.example.demo.util.DateTools;
import com.gf.entity.User;
import com.tmsps.ne4spring.base.IBaseService;

public class MyLogoutHandler implements LogoutHandler{
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// TODO Auto-generated method stub
	
		System.out.println("正在退出！");
		
	}

}
