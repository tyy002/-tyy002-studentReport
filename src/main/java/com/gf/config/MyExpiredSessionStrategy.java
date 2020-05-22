package com.gf.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.example.demo.util.json.JsonTools;

public class MyExpiredSessionStrategy  implements SessionInformationExpiredStrategy{

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		System.out.println("你的账号已在其它设备登录，已被迫下线");
//		Map<String,String> hashMap = new HashMap<String,String>();
//		hashMap.put("msg", "你的账号已在其它设备登录，已被迫下线");
//		hashMap.put("code", "100");
//		event.getResponse().setContentType("application/json;charset:UTF-8");
//		event.getResponse().getWriter().write(JsonTools.toJson(hashMap));
	}

}
