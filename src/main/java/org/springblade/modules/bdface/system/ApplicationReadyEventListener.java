package org.springblade.modules.bdface.system;

import lombok.extern.slf4j.Slf4j;
import org.springblade.common.thread.JdkThreadPoolConfig;
import org.springblade.modules.bdface.cache.CameraCache;
import org.springblade.modules.bdface.email.HuaWCameraEmailServer;
import org.springblade.modules.bdface.queue.FeatureQueue;
import org.springblade.modules.bdface.queue.PicQueue;
import org.springblade.modules.bdface.service.IBdInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ApplicationContext applicationContext;


	@Autowired
	private IBdInter bdInter;

	@Autowired
	private CameraCache cameraCache;

	@Autowired
	private PicQueue picQueue;


	@Autowired
	private HuaWCameraEmailServer huaWCameraEmailServer;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

		try {
			System.out.println("File.separator...................... " + File.separator);
			bdInter.initSdk();
		}catch (Exception e){
			log.error("启动 sdk 失败，即将退出程序");
			SpringApplication.exit(applicationContext);
			e.printStackTrace();
		}



		JdkThreadPoolConfig.cachedThreadPool.execute(()->{
			try {
				//图片识别 队列运行
				picQueue.run();
			}catch (Exception e){
				e.printStackTrace();
			}

		});





		JdkThreadPoolConfig.cachedThreadPool.execute(()->{
			try {
				//开始 解析rtsp
				cameraCache.startCameraRtspAll();
			}catch (Exception e){
				e.printStackTrace();
			}

		});


		JdkThreadPoolConfig.cachedThreadPool.execute(()->{
			try {
				// 华为邮件服务监听
				huaWCameraEmailServer.run();
			}catch (Exception e){
				e.printStackTrace();
			}

		});






    }
}
