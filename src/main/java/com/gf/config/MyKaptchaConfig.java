package com.gf.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
public class MyKaptchaConfig {

	@Value("${kaptcha.border}")
	private String border;
	
	@Value("${kaptcha.border.color}")
	private String color;
	
	@Value("${kaptcha.image.width}")
	private String width;
	
	@Value("${kaptcha.image.height}")
	private String height;
	
	@Value("${kaptcha.session.key}")
	private String key;
	
	@Value("${kaptcha.textproducer.font.color}")
	private String font_color;
	
	@Value("${kaptcha.textproducer.font.size}")
	private String font_size;
	
	@Value("${kaptcha.textproducer.char.length}")
	private String char_length;
	
	@Value("${kaptcha.textproducer.font.names}")
	private String font_names;
	
	
	@Bean
	public DefaultKaptcha getKaptcha() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", border);
		properties.setProperty("kaptcha.border.color", color);
		properties.setProperty("kaptcha.image.width", width);
		properties.setProperty("kaptcha.image.height", height);
		properties.setProperty("kaptcha.session.key", key);
		properties.setProperty("kaptcha.textproducer.font.color", font_color);
		properties.setProperty("kaptcha.textproducer.font.size", font_size);
		properties.setProperty("kaptcha.textproducer.char.length", char_length);
		properties.setProperty("kaptcha.textproducer.font.names", font_names);
		
		defaultKaptcha.setConfig(new Config(properties));
		return defaultKaptcha;
	}
	
}
