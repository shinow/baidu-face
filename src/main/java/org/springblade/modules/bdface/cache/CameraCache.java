package org.springblade.modules.bdface.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.springblade.common.thread.JdkThreadPoolConfig;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.entity.Camera;
import org.springblade.modules.bdface.queue.PicEntity;
import org.springblade.modules.bdface.queue.PicQueue;
import org.springblade.modules.bdface.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CameraCache {



	private final  Map<String, CameraDTO> cameras = new HashMap<>();

	@Autowired
	private ICameraService cameraService;


	@Autowired
	private PicQueue picQueue;




	public CameraDTO getCamera(String id){
		return  cameras.get(id);
	}



	public void startRec(String id){
		log.info("开始人脸识别 ,cameraId: " + id);

		if(id == null){
			throw new NullPointerException();
		}

		CameraDTO camera = cameras.get(id);
		if(camera == null){
			throw new NullPointerException();
		}

		LinkedList<PicEntity> stack = camera.getStack();
		if(stack == null){
			throw new NullPointerException("stack is null");
		}

		picQueue.addStack(stack);

	}



	public void addCamera(CameraDTO camera){
		synchronized (cameras){
			cameras.put(camera.getCredentialsId()+"",camera);
		}
	}



	public boolean stopCameraRtsp(String id){
		synchronized (cameras){
			CameraDTO camera = cameras.get(id);
			if(camera == null){
				return false;
			}else{
				camera.setFlag(1);
			}
			return true;
		}
	}

	public boolean startCameraRtsp(String id){
		log.info("开始解析 rtsp 流，cameraId : " + id);

		synchronized (cameras){
			CameraDTO camera = cameras.get(id);
			if(camera == null){
				log.info("dont find camera,id: " + id);
				return false;
			}else{
				camera.setFlag(0);
			}


			if(camera.isRunning()){
				log.info("camera is running,id: " + id);
				return false;
			}

			camera.setRunning(true);

			String rtsp = camera.getRtsp();
			if(StringUtil.isEmpty(rtsp)){
				log.error("rtsp is empty");
				return false;
			}

			VideoCapture vc = new VideoCapture();

			// 设置 帧率，10帧
			vc.set(5,10);


			boolean isOpen = vc.open(rtsp);

			if(!isOpen){
				log.error("videoCapture is close");
				return  false;
			}


			JdkThreadPoolConfig.cachedThreadPool.execute(()->{
				try {
					cameraRun(vc,id);
				}catch (Exception e){
					e.printStackTrace();
				}

			});
		}


		return true;
	}


	public void startCameraRtspAll(){

		/*QueryWrapper<Camera> queryWrapper = new QueryWrapper();
		queryWrapper.lambda().eq(Camera::getFlag,0);
		List<Camera> cameraList = cameraService.list(queryWrapper);*/

		List<Camera> cameraList = cameraService.list();

		if(cameraList == null ||cameraList.size() == 0){
			return ;
		}

		for(Camera cameraFor : cameraList){
			CameraDTO cameraDTO = BeanUtil.copy(cameraFor,CameraDTO.class);
			addCamera(cameraDTO);
		}

		synchronized (cameras){
			cameras.forEach((key,value)->{
				try{
					if(value.getFlag() == 0){
						if(startCameraRtsp(key)){
							startRec(key);
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}


			});
		}
		return ;
	}


	private void cameraRun(VideoCapture vc,String id){

		CameraDTO camera = cameras.get(id);
		LinkedList<PicEntity> stack = camera.getStack();

		if(stack == null){
			throw new NullPointerException("stack is null");
		}



		PicEntity picEntity1 = new PicEntity();
		PicEntity picEntity2 = new PicEntity();

		stack.push(picEntity1);
		stack.push(picEntity2);

		picEntity1.setCameraDTO(camera);
		picEntity2.setCameraDTO(camera);


		Mat mat = null;
		PicEntity picEntity = null;
		while (camera.getFlag() == 0){
			synchronized (stack){
				if(stack.isEmpty()){
					stopAndReleaseMat(id,picEntity1.getMat(),picEntity2.getMat());
					throw new NullPointerException();
				}
				picEntity = stack.removeLast();

				mat = picEntity.getMat();

				if(mat == null){
					stopAndReleaseMat(id,picEntity1.getMat(),picEntity2.getMat());
					throw new NullPointerException();
				}
				mat.release();
			}
			vc.read(mat);
			synchronized (stack){
				stack.push(picEntity);
			}
		}
	}

	private void stopAndReleaseMat(String id ,Mat mat1,Mat mat2){
		stopCameraRtsp(id);
		if(mat1 != null){
			mat1.release();
		}

		if(mat2 != null){
			mat2.release();
		}
	}



}
