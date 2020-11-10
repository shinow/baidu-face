
package org.springblade.modules.resource.builder.sms;

import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.SneakyThrows;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.SmsTemplate;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.core.sms.qiniu.QiniuSmsTemplate;
import org.springblade.modules.resource.entity.Sms;

/**
 * 七牛云短信构建类
 *
 * @author Chill
 */
public class QiniuSmsBuilder {

	@SneakyThrows
	public static SmsTemplate template(Sms sms, BladeRedis bladeRedis) {
		SmsProperties smsProperties = new SmsProperties();
		smsProperties.setTemplateId(sms.getTemplateId());
		smsProperties.setAccessKey(sms.getAccessKey());
		smsProperties.setSecretKey(sms.getSecretKey());
		smsProperties.setSignName(sms.getSignName());
		Auth auth = Auth.create(smsProperties.getAccessKey(), smsProperties.getSecretKey());
		SmsManager smsManager = new SmsManager(auth);
		return new QiniuSmsTemplate(smsProperties, smsManager, bladeRedis);
	}

}
