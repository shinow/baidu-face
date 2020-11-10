package org.springblade.modules.bdface.cache;

import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.modules.bdface.dto.FaceFeatureDTO;
import org.springblade.modules.bdface.entity.FaceFeature;
import org.springblade.modules.bdface.service.IBdInter;
import org.springblade.modules.bdface.service.IFaceFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeatureCache {

	@Autowired
	private IBdInter bdInter;

	private final Map<Long, FaceFeatureDTO> featureMap = new HashMap<>();


	private final LinkedList<FaceFeatureDTO> featureList = new LinkedList();

	@Autowired
	private IFaceFeatureService faceFeatureService;

	private boolean lock = false;


	public  List<FaceFeatureDTO> getFeatureStore()throws Exception{

		if(lock){
			return featureList;
		}else{
			lock = true;
		}


		if(featureList.size() != 0){
			return featureList;
		}


		List<FaceFeature> featureEntityList = faceFeatureService.list();

		if(featureEntityList == null){
			return null;
		}


		for(FaceFeature featureFor : featureEntityList){
			String feature = featureFor.getFeature();

			if(StringUtil.isEmpty(feature)){
				continue;
			}

			BaiDuFaceSDK.BDFaceFeature.ByReference faceFeatureStruct =  bdInter.featureStrToFaceFeature(feature);

			FaceFeatureDTO faceFeatureDTO = BeanUtil.copy(featureFor,FaceFeatureDTO.class);
			faceFeatureDTO.setFeatureStruct(faceFeatureStruct);
			featureMap.put(featureFor.getUserId(),faceFeatureDTO);
		}

		flushFeatureList();
		return featureList;
	}


	public FaceFeatureDTO add(Long userId,String feature)throws Exception{
		BaiDuFaceSDK.BDFaceFeature.ByReference faceFeatureStruct =  bdInter.featureStrToFaceFeature(feature);

		FaceFeatureDTO faceFeatureDTO = new FaceFeatureDTO();
		faceFeatureDTO.setUserId(userId);
		faceFeatureDTO.setFeatureStruct(faceFeatureStruct);

		featureMap.put(userId,faceFeatureDTO);


		flushFeatureList();

		return faceFeatureDTO;

	}


	public FaceFeatureDTO remove(Long userId){
		FaceFeatureDTO feature = featureMap.remove(userId);

		flushFeatureList();

		return feature;
	}


	private void flushFeatureList(){
		LinkedList<FaceFeatureDTO> featureListTemp = new LinkedList<>(featureMap.values());
		synchronized (featureList){
			featureList.clear();
			for(FaceFeatureDTO faceFeatureDTO : featureListTemp){
				featureList.addFirst(faceFeatureDTO);
			}
		}

	}
}
