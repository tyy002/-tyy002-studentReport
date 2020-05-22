package com.gf.controller;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gf.entity.CaptchaImageVO;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@RestController
public class KaptchaContorller {

	@Resource
    DefaultKaptcha defaultKaptcha;
    
    @RequestMapping("/kaptcha")
    public void kaptcha(HttpServletResponse response,HttpSession session) throws Exception {
//    	String createText = defaultKaptcha.createText();
//    	CaptchaImageVO captchaImageVO = new CaptchaImageVO(createText,60*2);
//    	session.setAttribute("kaptchaKey",captchaImageVO);
//    	defaultKaptcha.createImage(createText);
    	System.out.println("1111111111");
    	
    	   response.setDateHeader("Expires", 0);
           response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
           response.addHeader("Cache-Control", "post-check=0, pre-check=0");
           response.setHeader("Pragma", "no-cache");
           response.setContentType("image/jpeg");

           String capText = defaultKaptcha.createText();

           session.setAttribute("kaptchaKey",new CaptchaImageVO(capText,2 * 60));

           try(ServletOutputStream out = response.getOutputStream()){
               BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
               ImageIO.write(bufferedImage,"jpg",out);
               out.flush();
           }
    }
}
