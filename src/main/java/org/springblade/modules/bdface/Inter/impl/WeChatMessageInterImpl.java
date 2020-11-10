package org.springblade.modules.bdface.Inter.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.utils.ZipUtil;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.modules.bdface.Inter.MessageInter;
import org.springblade.modules.bdface.entity.CameraRecord;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WeChatMessageInterImpl implements MessageInter {

	@Autowired
	private IUserService iUserService;



	@Value("${device.heart.callBackUrl}")
	private  String callBackUrl;


	@Value("${device.heart.device_switch}")
	private  Boolean deviceSwitch;


	@Override
	public void invoke(CameraRecord record) {

		if(!deviceSwitch){
			return;
		}

		try {

			byte[] bytesPic = record.getPicBytes();
			String base64 = null;
			if(bytesPic != null){
				//压缩
				bytesPic = ZipUtil.zipBytes(bytesPic);
				base64 = Base64Util.bytesToBase64Bytes(bytesPic);
			}

			String credentialsId = record.getCameraCredentialsId();

			Long userId = record.getUserId();
			User user = iUserService.getById(userId);
			if(user == null){
				return;
			}

			String thirdUserId = user.getThirdUserId();


			JSONObject job = new JSONObject();
			job.put("userId",thirdUserId);
			job.put("credentialsId",credentialsId);
			job.put("eventType",1); //1-刷脸 2-刷卡

			job.put("picture",base64);

			String result = HttpRequest.post(callBackUrl)
				.header("Content-Type", "application/json")
				.body(job.toJSONString())
				.execute().body();

			log.info("result: " + result);


		} catch (Exception e) {
			e.printStackTrace();
		}





	}


}
