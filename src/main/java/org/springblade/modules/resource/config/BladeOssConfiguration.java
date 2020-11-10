
package org.springblade.modules.resource.config;

import lombok.AllArgsConstructor;
import org.springblade.core.oss.props.OssProperties;
import org.springblade.modules.resource.builder.oss.OssBuilder;
import org.springblade.modules.resource.service.IOssService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Oss配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
public class BladeOssConfiguration {

	private final OssProperties ossProperties;

	private final IOssService ossService;

	@Bean
	public OssBuilder ossBuilder() {
		return new OssBuilder(ossProperties, ossService);
	}

}
