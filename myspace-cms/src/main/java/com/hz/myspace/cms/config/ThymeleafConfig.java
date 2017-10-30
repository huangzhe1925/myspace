package com.hz.myspace.cms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class ThymeleafConfig {

	@Autowired
	private TemplateEngine templateEngine;
	
	@PostConstruct
	public void configFileResovler() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		// XHTML is the default mode, but we set it anyway for better
		// understanding of code
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("D:/templatess/");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheable(false);
		templateEngine.addTemplateResolver(templateResolver);
	}
}
