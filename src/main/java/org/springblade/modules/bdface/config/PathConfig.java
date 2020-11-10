/*
package org.springblade.modules.bdface.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Slf4j
@Configuration
public class PathConfig extends WebMvcConfigurationSupport {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("addResourceHandlers................................");
		registry.addResourceHandler("/cdn/**").addResourceLocations("classpath:/static/");

		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/svg/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/util/**").addResourceLocations("classpath:/static/");


		super.addResourceHandlers(registry);
	}



}
*/
