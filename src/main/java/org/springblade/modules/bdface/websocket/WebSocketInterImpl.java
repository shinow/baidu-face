package org.springblade.modules.bdface.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class WebSocketInterImpl implements IWebSocketInter {


	@Autowired
	private JavaWebSocketClient javaWebSocketClient;

	@Value("${device.heart.websocketServerUrl}")
	private  String websocketServerUrl;


	@Value("${device.heart.device_switch}")
	private  Boolean deviceSwitch;



	private WebSocketClient webSocketClient;


	@Autowired
	private IUserService userService;


	@Override
	public void sendMessage(String message) {
		if(!deviceSwitch){
			return;
		}

		synchronized (webSocketClient){
			if(webSocketClient == null){
				webSocketClient = javaWebSocketClient.getClient(websocketServerUrl,this);
			}
			webSocketClient.send(message);
		}

	}

	/**
	 * 30秒心跳
	 */
	@Scheduled(fixedRate = 30000)
	public void scheduledTask() {
		if(!deviceSwitch){
			return;
		}

		log.info("心跳：" + LocalDateTime.now());

		if(StringUtils.isEmpty(websocketServerUrl)){
			return;
		}

		synchronized (this){
			if(webSocketClient == null){
				webSocketClient = javaWebSocketClient.getClient(websocketServerUrl,this);
			}

			if(webSocketClient != null){
				try {
					webSocketClient.send("ping");
				}catch (Exception e){
					e.printStackTrace();
					webSocketClient = javaWebSocketClient.getClient(websocketServerUrl,this);
				}

			}

		}
	}


	@Override
	public void onMessage(String message) {
		try{
			log.info(message);
			if("pong".equals(message)){
				return;
			}

			JSONObject commond = JSONObject.parseObject(message);
			String method = commond.getString("method");

			if("personnelData.removePersons".equals(method)){
				//移除人员
			}else if("personnelData.savePersons".equals(method)){
				//新增人员

				JSONArray params = commond.getJSONArray("params");

				for(int i = 0; i < params.size(); i++){
					JSONObject Person = params.getJSONObject(i);

					Person = Person.getJSONObject("Person");

					String Code = Person.getString("Code");
					String Name = Person.getString("Name");
					JSONArray URL = Person.getJSONArray("URL");
					String url = URL != null ? URL.getString(0) : "";



					try {
						User user = new User();
						user.setAvatar(url);
						user.setThirdUserId(Code);
						user.setName(Name);
						user.setRealName(Name);

						userService.saveOrUpdateUserAndFeatureByThird(user);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}


			}else if("privilegeGroup.bindPerson".equals(method)){
				//绑定人员
			}else{
				// 忽略改命令
			}
		}catch (Exception e){
			e.printStackTrace();
		}




	}





}
