package org.springblade.modules.bdface.queue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;
import org.springblade.common.utils.FileUtil;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.extend.face.util.OpenCvUtil;
import org.springblade.modules.bdface.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PicQueue {

	@Autowired
	private ICameraService cameraService;


	private BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();


	private final List<LinkedList<PicEntity>> stackList = new ArrayList<>();

	private final LinkedList<PicEntity> thirdPicList = new LinkedList<>();


	@Autowired
	private FeatureQueue featureQueue;


	public PicQueue(){
		stackList.add(thirdPicList);
	}


	public void addStack(LinkedList<PicEntity> stack){
		synchronized (stackList){
			stackList.add(stack);
		}

	}


	public void add(PicEntity picEntity){
		thirdPicList.addFirst(picEntity);

		if(thirdPicList.size() >= 10){
			thirdPicList.removeLast();
		}

	}

	public void run(){

		while (true){
			try {
				TimeUnit.MILLISECONDS.sleep(50);

				PicEntity entity = null;
				synchronized (stackList){
					for(LinkedList<PicEntity> stacks : stackList){
						synchronized (stacks){
							if(stacks.isEmpty()){
								continue;
							}
							entity = stacks.pop();

							try {
								invoke(entity);
							}catch (Exception e){
								e.printStackTrace();
							}
							stacks.addLast(entity);
						}


						featureQueue.run();

					}
				}


			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}




	}



	private void invoke(PicEntity entity){

		if(entity == null){
			throw new NullPointerException();
		}

		Mat mat = entity.getMat();
		if(entity.getBytes() == null){
			long elementSize = mat.elemSize();
			if(elementSize <= 0){
				return;
			}
		}




		String imgFilePath = entity.getImgFilePath();

		if(mat == null && StringUtils.isEmpty(imgFilePath)){
			throw new NullPointerException();
		}



		if(StringUtils.isEmpty(imgFilePath)){
			if(entity.getBytes() == null){
				try {
					byte[] bytes = OpenCvUtil.Mat2Bytes(mat);
					entity.setBytes(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			imgFilePath = FileUtil.bytesToFile(entity.getBytes());
		}





		byte[] bytes = entity.getBytes();

		mat.release();
		entity.setBytes(null);
		try {
			log.info("camera rec .................");
			cameraService.faceRecogUseByCamera(imgFilePath,bytes,faceFeatureList,entity.getCameraDTO());
		}catch (Exception e){
			e.printStackTrace();
		}

	}




}
