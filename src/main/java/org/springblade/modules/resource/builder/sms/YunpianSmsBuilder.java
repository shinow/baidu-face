
package org.springblade.modules.resource.builder.sms;

import com.yunpian.sdk.YunpianClient;
import lombok.SneakyThrows;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.SmsTemplate;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.core.sms.yunpian.YunpianSmsTemplate;
import org.springblade.modules.resource.entity.Sms;

/**
 * 云片短信构建类
 *
 * @author Chill
 */
public class YunpianSmsBuilder {

	@SneakyThrows
	public static SmsTemplate template(Sms sms, BladeRedis bladeRedis) {
		SmsProperties smsProperties = new SmsProperties();
		smsProperties.setTemplateId(sms.getTemplateId());
		smsProperties.setAccessKey(sms.getAccessKey());
		smsProperties.setSignName(sms.getSignName());
		YunpianClient client = new YunpianClient(smsProperties.getAccessKey()).init();
		return new YunpianSmsTemplate(smsProperties, client, bladeRedis);
	}

}
