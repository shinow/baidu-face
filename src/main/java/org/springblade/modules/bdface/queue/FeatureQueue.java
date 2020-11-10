package org.springblade.modules.bdface.queue;

import org.apache.commons.lang3.StringUtils;
import org.springblade.modules.bdface.service.IBdInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class FeatureQueue {


	private  FeatureEntity featureEntity = null;




	@Autowired
	private IBdInter bdInter;

	public void run(){
		String filePath = null;
		try {
			synchronized (this){
				if(featureEntity == null){
					return;
				}
				filePath = featureEntity.getFilePath();
				if(StringUtils.isEmpty(filePath)){
					return;
				}

				String featureBase64 = bdInter.loadPicFeatureBase64(filePath);
				featureEntity.setFeatureBase64(featureBase64);
				featureEntity = null;
				this.notifyAll();
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void get(FeatureEntity entity){
		try {
			synchronized (this){
				this.featureEntity = entity;
				this.notifyAll();
			}
			int i = 0;
			while (true){
				if(i >= 8){
					break;
				}
				i ++;
				TimeUnit.MILLISECONDS.sleep(300);
				if(StringUtils.isNotEmpty(entity.getFeatureBase64())){
					break;
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}


	}


}
