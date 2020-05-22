package com.gf.controller;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.DateTools;
import com.example.demo.util.tree.TreeTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.CaptchaImageVO;
import com.gf.entity.User;
import com.gf.service.PermissionService;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@Controller
public class MainController extends ProjBaseAction{

	@Autowired
	PermissionService permissonService;
	
    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index2";
    }
    @RequestMapping("/showindex")
    public String showindex(Model model) {
    	return "index";
    }
    

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/loginOut")
    public String loginOut() {
    	
    	SecurityUtils.logout();
    	return "redirect:login";
    }

   
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute( "loginError"  , true);
        return "login";
    }

    @GetMapping("/401")
    public String accessDenied() {
        return "401";
    }

   
    @GetMapping("/role/index")
    public String role_index() {
    	return "role/list";
    }
  

    @RequestMapping(value="/permission/index")
    public String permission_index() {
   
       return "permission/list";
    }
   
    @GetMapping("/user/password")
    public String password() {
    	return "user/password";
    }
    
    


}
