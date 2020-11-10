
package org.springblade.common.config;


import org.springblade.core.secure.registry.SecureRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Blade配置
 *
 * @author Chill
 */
@Configuration
public class BladeConfiguration implements WebMvcConfigurer {

	/**
	 * 安全框架配置
	 */
	@Bean
	public SecureRegistry secureRegistry() {
		SecureRegistry secureRegistry = new SecureRegistry();
		secureRegistry.setEnabled(true);
		secureRegistry.excludePathPatterns("/blade-auth/**");
		secureRegistry.excludePathPatterns("/blade-log/**");
		secureRegistry.excludePathPatterns("/blade-system/menu/routes");
		secureRegistry.excludePathPatterns("/blade-system/menu/auth-routes");
		secureRegistry.excludePathPatterns("/blade-system/menu/top-menu");
		secureRegistry.excludePathPatterns("/blade-system/tenant/info");
		secureRegistry.excludePathPatterns("/blade-flow/process/resource-view");
		secureRegistry.excludePathPatterns("/blade-flow/process/diagram-view");
		secureRegistry.excludePathPatterns("/blade-flow/manager/check-upload");
		secureRegistry.excludePathPatterns("/doc.html");
		secureRegistry.excludePathPatterns("/js/**");
		secureRegistry.excludePathPatterns("/webjars/**");
		secureRegistry.excludePathPatterns("/swagger-resources/**");
		secureRegistry.excludePathPatterns("/druid/**");
		return secureRegistry;
	}

	/**
	 * 跨域配置
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/cors/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("*")
			.maxAge(3600)
			.allowCredentials(true);
	}

}
