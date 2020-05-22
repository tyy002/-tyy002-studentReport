package com.gf.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.example.demo.util.DateTools;
import com.gf.entity.User;
import com.tmsps.ne4spring.base.IBaseService;

public class MyLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.onAuthenticationSuccess(request, response, authentication);//默认是跳转页面
		
		System.out.println("登录成功！");
	}

}
