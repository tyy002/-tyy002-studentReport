package com.gf.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
	


	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String userAgent = request.getHeader("user-agent");
		System.out.println("userAgent"+userAgent);
		System.out.println("退出！");
        if (userAgent.indexOf("Android") != -1) {
        	response.sendRedirect("/login");
        } else if ((userAgent.indexOf("iPhone") != -1) || (userAgent.indexOf("iPad") != -1)) {
        	response.sendRedirect("/login");
        } else {
        	response.sendRedirect("/login");
        }
		
	}

}
