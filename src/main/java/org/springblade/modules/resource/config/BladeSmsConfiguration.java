
package org.springblade.modules.resource.config;

import lombok.AllArgsConstructor;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.modules.resource.builder.sms.SmsBuilder;
import org.springblade.modules.resource.service.ISmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sms配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
public class BladeSmsConfiguration {

	private final SmsProperties smsProperties;

	private final ISmsService smsService;

	private final BladeRedis bladeRedis;

	@Bean
	public SmsBuilder smsBuilder() {
		return new SmsBuilder(smsProperties, smsService, bladeRedis);
	}

}
