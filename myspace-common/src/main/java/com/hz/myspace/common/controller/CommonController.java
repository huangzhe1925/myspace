package com.hz.myspace.common.controller;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hz.myspace.common.utils.MSEnvironmentPropertyUtil;

@Controller
@RequestMapping(value = "mscommon")
public class CommonController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public MSEnvironmentPropertyUtil propertyUtil;

	@RequestMapping(value = "testprop")
	@ResponseBody
	public String getEnvProp() {
		StringBuilder sb = new StringBuilder("");
		if (propertyUtil.getCommon() != null) {
			for (Entry<String, String> entry : propertyUtil.getCommon().entrySet()) {
				sb.append(entry.getKey() + " : " + entry.getValue()+"\n");
			}
		}
		logger.debug("This is a debug message");  
        logger.info("This is an info message");  
        logger.warn("This is a warn message");  
        logger.error("This is an error message");  
		logger.info(sb.toString());
		logger.debug(sb.toString());
		return sb.toString();
	}

}
