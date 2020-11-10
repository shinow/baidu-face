
package org.springblade.modules.resource.builder.sms;

import com.github.qcloudsms.SmsMultiSender;
import lombok.SneakyThrows;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.SmsTemplate;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.core.sms.tencent.TencentSmsTemplate;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.resource.entity.Sms;

/**
 * 腾讯云短信构建类
 *
 * @author Chill
 */
public class TencentSmsBuilder {

	@SneakyThrows
	public static SmsTemplate template(Sms sms, BladeRedis bladeRedis) {
		SmsProperties smsProperties = new SmsProperties();
		smsProperties.setTemplateId(sms.getTemplateId());
		smsProperties.setAccessKey(sms.getAccessKey());
		smsProperties.setSecretKey(sms.getSecretKey());
		smsProperties.setSignName(sms.getSignName());
		SmsMultiSender smsSender = new SmsMultiSender(Func.toInt(smsProperties.getAccessKey()), sms.getSecretKey());
		return new TencentSmsTemplate(smsProperties, smsSender, bladeRedis);
	}

}
