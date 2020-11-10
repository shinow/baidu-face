package org.springblade.modules.bdface.cache;

import com.google.api.client.util.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.thread.JdkThreadPoolConfig;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.bdface.config.FaceRecConfig;
import org.springblade.modules.bdface.service.ICameraRecordService;
import org.springblade.modules.bdface.vo.FaceFeatureVO;
import org.springframework.scheduling.annotation.Async;

import java.util.*;

@Slf4j
public class MessageCache {

	private final LinkedList<List<FaceFeatureVO>> messageList = new LinkedList<>();

	private final Map<Long,FaceFeatureVO> userHistoryFace = new HashMap<>();


	private final List<FaceFeatureVO> BLANK_LIST = new ArrayList<>();

	private FaceRecConfig faceRecConfig;


	private ICameraRecordService iCameraRecordService;



	@Async("taskExecutor")
	public void add(List<FaceFeatureVO> featureVOList){

		synchronized (messageList){
			if(featureVOList == null){

				if(messageList.size() > 0){
					if(messageList.get(0) == BLANK_LIST){
						return;
					}
				}
				featureVOList = BLANK_LIST;
			}


			messageList.addFirst(featureVOList);

			//控制历史记录长度
			if(messageList.size() >= 4){
				messageList.removeLast();
			}

			// 取差集
			Map<Long,FaceFeatureVO> mapLeft = getDifference(messageList);
			List<FaceFeatureVO> listLeft = new ArrayList<>(mapLeft.values());


			//推送 识别结果
			sendMessage(listLeft);


		}


	}

	private Map<Long,FaceFeatureVO>   getDifference(LinkedList<List<FaceFeatureVO>> messageList){
		synchronized (messageList){

			List<FaceFeatureVO> list1 = null;
			List<FaceFeatureVO> list2 = null;


			if(messageList.size() == 0){
				return  new HashMap<>();
			}


			list1 = messageList.getFirst();
			if(messageList.size() == 1){
				//发送全部list1
				sendMessage(list1);
				return  new HashMap<>();
			}

			list2 = messageList.get(1);

			//取差集 发送

			Map<Long,FaceFeatureVO> map1 = new HashMap<>();
			Map<Long,FaceFeatureVO> map2 = new HashMap<>();


			for(FaceFeatureVO faceFor : list1){
				map1.put(faceFor.getUserId(),faceFor);
			}

			for(FaceFeatureVO faceFor : list2){
				map2.put(faceFor.getUserId(),faceFor);
			}

			Map<Long,FaceFeatureVO> mapLeft = getDifferenceSetByGuava(map1,map2);

			return mapLeft;



		}
	}

	private void sendMessage(List<FaceFeatureVO> list){

		if(list == null){
			throw new NullPointerException();
		}

		if(list.size() == 0){
			return;
		}

		if(iCameraRecordService == null){
			iCameraRecordService = SpringUtil.getBean(ICameraRecordService.class);
		}


		long currentTimeMillis = 0;
		long faceMillis = 0;

		for (FaceFeatureVO faceFor : list){

			currentTimeMillis = System.currentTimeMillis();


			if(userHistoryFace.containsKey(faceFor.getUserId())){
				//有历史记录
				FaceFeatureVO hisFace = userHistoryFace.get(faceFor.getUserId());
				faceMillis = hisFace.getCurrentTimeMillis();

				if(faceRecConfig == null){
					faceRecConfig = SpringUtil.getBean(FaceRecConfig.class);
				}
				long recInterval = faceRecConfig.getRecInterval() == 0 ? 5000 : faceRecConfig.getRecInterval();

				if(currentTimeMillis - faceMillis >= recInterval){
					log.info("有历史记录，userId: " + faceFor.getUserId() + ",score: " + faceFor.getScore());




					JdkThreadPoolConfig.cachedThreadPool.execute(()->{
						try {
							//开始 解析rtsp
							iCameraRecordService.saveRecord(faceFor);
						}catch (Exception e){
							e.printStackTrace();
						}

					});


					userHistoryFace.put(faceFor.getUserId(),faceFor);
				}
				continue;
			}


			log.info("userId: " + faceFor.getUserId() + ",score: " + faceFor.getScore());

			JdkThreadPoolConfig.cachedThreadPool.execute(()->{
				try {
					//开始 解析rtsp
					iCameraRecordService.saveRecord(faceFor);
				}catch (Exception e){
					e.printStackTrace();
				}

			});



			userHistoryFace.put(faceFor.getUserId(),faceFor);


		}

	}

	private  Map<Long, FaceFeatureVO> getDifferenceSetByGuava(Map<Long, FaceFeatureVO> bigMap, Map<Long, FaceFeatureVO> smallMap) {
		Set<Long> bigMapKey = bigMap.keySet();
		Set<Long> smallMapKey = smallMap.keySet();
		Set<Long> differenceSet = Sets.difference(bigMapKey, smallMapKey);
		Map<Long, FaceFeatureVO> result = Maps.newHashMap();
		for (Long key : differenceSet) {
			result.put(key, bigMap.get(key));
		}
		return result;
	}




}
