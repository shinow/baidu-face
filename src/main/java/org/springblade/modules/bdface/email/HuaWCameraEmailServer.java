package org.springblade.modules.bdface.email;


import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.bdface.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Component
public class HuaWCameraEmailServer {



	@Value("${device.email_port}")
	private  Integer emailPort;



	@Autowired
	private ICameraService cameraService;




	public  void run()throws Exception{
         SimpleSmtpServer  server = SimpleSmtpServer.start(emailPort,this);
         log.info("runing...");
    }


    public void invoke(List<SmtpMessage> messages)throws Exception{
		if(messages == null){
			return;
		}
		if(messages.size() == 0){
			return;
		}
		SmtpMessage smtpMessage = messages.get(0);




		String body = smtpMessage.getBody();
		InputStream inputStream = new ByteArrayInputStream(body.getBytes());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String line = null;
		List<String> list = new ArrayList<>(5);
		while ((line = bufferedReader.readLine()) != null){
			list.add(line);
		}

		if(list.size() <= 4){
			return;
		}



		String from = smtpMessage.getHeaderValue("From");
		String credentialsId = from.split("<")[0].trim();
		String base64 = list.get(4);


		log.info(credentialsId + ",email 识别");
		cameraService.faceRecogBase64CameraId(base64,credentialsId);


	}



}
