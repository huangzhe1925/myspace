package com.hz.myspace.cms.controller.mgnt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "cmsviewmgnt")
public class CMSViewManageController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "console")
	public String consolePage(){
		return "cmsmgntview/cmsconsole";
	}
	
	@RequestMapping(value = "index")
	public String consolePageX(){
		return "index";
	}
	
	@RequestMapping(value = "indexx")
	public String consolePageXX(){
		return "indexx";
	}
	
	@RequestMapping(value = "consoletest")
	public String consoleTestPage(@RequestParam String testPage){
		return "cmsmgntview/"+testPage;
	}
	
	

}
