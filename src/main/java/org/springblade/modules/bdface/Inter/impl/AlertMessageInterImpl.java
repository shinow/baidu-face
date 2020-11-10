package org.springblade.modules.bdface.Inter.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.utils.Md5Utils;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.bdface.Inter.MessageInter;
import org.springblade.modules.bdface.entity.CameraRecord;
import org.springblade.modules.bdface.entity.MessageAlert;
import org.springblade.modules.bdface.service.IMessageAlertService;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.entity.UserInfo;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
public class AlertMessageInterImpl implements MessageInter {


	private int timeout = 5000;

	@Autowired
	private IMessageAlertService messageAlertService;


	@Autowired
	private IUserService userService;


	@Override
	public void invoke(CameraRecord record) {

		if(record.getUserId() == null){
			return;
		}

		List<MessageAlert> alertList = messageAlertService.list();

		UserInfo userInfo = userService.userInfo(record.getUserId());
		if(userInfo == null){
			return;
		}


		StringBuilder text = new StringBuilder();


		List<String>  roles = userInfo.getRoles();
		if(roles != null){
			if(roles.contains(CommonConstant.teacher)){
				//教师
				text.append("教师,");

			}
			if(roles.contains(CommonConstant.student)){
				//学生
				text.append("学生,");
			}
			if(roles.contains(CommonConstant.visitor)){
				//访客
				text.append("访客,");
			}
			if(roles.contains(CommonConstant.blacklist)){
				//黑名单
				text.append("黑名单,");
			}

			if(roles.contains(CommonConstant.vip)){
				//vip
				text.append("vip,");
			}
		}


		User user = userInfo.getUser();
		if(user != null){
			String userRealName = StringUtil.isEmpty(user.getRealName()) ? user.getName() : user.getRealName();

			if(StringUtil.isEmpty(userRealName)){
				userRealName = "未命名";
			}

			text.append("，");
			text.append(userRealName);


		}

		if(!StringUtil.isEmpty(record.getRegion())){
			text.append("通过");
			text.append(record.getRegion());
		}



		/**
		 * led：从上到下第几个led灯
		 * mode：LED亮灯模式，1为常量，2为闪亮，3为转亮
		 * last：持续时间
		 * text：要朗读的语音内容
		 * tone：提示音编号
		 * sign：签名，算法为：md5(led的值+mode的值+last的值+text的值+tone的值+apikey)
		 */
		for(MessageAlert alert:alertList){
			String url = alert.getUrl();
			String apiKey = alert.getApiKey();

			JSONObject json = new JSONObject();

			json.put("led",alert.getLed());
			json.put("mode",alert.getMode());
			json.put("last",alert.getLast());





			json.put("text",text);
			json.put("tone",alert.getTone());

			final StringBuilder sb = new StringBuilder();
			sb.append(json.get("led"));
			sb.append(json.get("mode"));
			sb.append(json.get("last"));
			sb.append(json.get("text"));
			sb.append(json.get("tone"));
			sb.append(apiKey);

			log.info(sb.toString());

			System.out.println(sb.toString());

			String sign = null;
			try {
				sign = Md5Utils.getMD5(sb.toString().getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			json.put("sign",sign);

			HttpUtil.post(url,json,timeout);

		}

		log.info("AlertMessageInterImpl alert.... ");
	}


}
