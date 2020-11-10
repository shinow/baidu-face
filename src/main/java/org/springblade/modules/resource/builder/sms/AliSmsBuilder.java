
package org.springblade.modules.resource.builder.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.SneakyThrows;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.sms.SmsTemplate;
import org.springblade.core.sms.aliyun.AliSmsTemplate;
import org.springblade.core.sms.props.SmsProperties;
import org.springblade.modules.resource.entity.Sms;

/**
 * 阿里云短信构建类
 *
 * @author Chill
 */
public class AliSmsBuilder {

	@SneakyThrows
	public static SmsTemplate template(Sms sms, BladeRedis bladeRedis) {
		SmsProperties smsProperties = new SmsProperties();
		smsProperties.setTemplateId(sms.getTemplateId());
		smsProperties.setAccessKey(sms.getAccessKey());
		smsProperties.setSecretKey(sms.getSecretKey());
		smsProperties.setRegionId(sms.getRegionId());
		smsProperties.setSignName(sms.getSignName());
		IClientProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKey(), smsProperties.getSecretKey());
		IAcsClient acsClient = new DefaultAcsClient(profile);
		return new AliSmsTemplate(smsProperties, acsClient, bladeRedis);
	}

}
