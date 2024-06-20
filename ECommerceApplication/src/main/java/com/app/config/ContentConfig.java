package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ContentConfig implements WebMvcConfigurer {
	
	// 配置內容協商策略
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

		configurer.favorParameter(true)
				  .parameterName("mediaType")
				  .defaultContentType(MediaType.APPLICATION_JSON)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML);

	}

	// 配置跨域資源共享（CORS）
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("api/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "OPTION")
				.allowCredentials(true)
				.allowedHeaders("*");
	}
	
	
}
