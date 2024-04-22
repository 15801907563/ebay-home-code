package com.test.ebay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		HeaderInterceptor headerInterceptor = new HeaderInterceptor();
		registry.addInterceptor(headerInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/admin/getBase64User")
		.excludePathPatterns("/error");

	}
}
