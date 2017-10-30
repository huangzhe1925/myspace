package com.hz.myspace.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
		@PropertySource(value = "classpath:config/application.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:config/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true), })
@ConfigurationProperties(prefix="ms")
public class MSEnvironmentPropertyUtil {

	private Map<String, String> common = new HashMap<String, String>();

	public Map<String, String> getCommon() {
		return common;
	}

	public void setCommon(Map<String, String> common) {
		this.common = common;
	}

	@SuppressWarnings("unchecked")
	public static String printMapValues(Map<String, String>... maps) {
		StringBuilder sb = new StringBuilder();
		Arrays.asList(maps).stream()
				.forEach(m -> m.entrySet().stream().forEach(
						en -> sb.append(en.getKey() + " " + en.getValue() + "\n")));

		return sb.toString();

	}

}
