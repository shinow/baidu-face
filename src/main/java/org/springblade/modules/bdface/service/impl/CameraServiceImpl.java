package org.springblade.modules.bdface.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;
import org.springblade.common.utils.FileUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.extend.face.util.OpenCvUtil;
import org.springblade.extend.face.util.SdkUtil;
import org.springblade.modules.bdface.cache.CameraCache;
import org.springblade.modules.bdface.cache.FeatureCache;
import org.springblade.modules.bdface.cache.MessageCache;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.dto.FaceFeatureDTO;
import org.springblade.modules.bdface.entity.Camera;
import org.springblade.modules.bdface.mapper.CameraMapper;
import org.springblade.modules.bdface.queue.PicEntity;
import org.springblade.modules.bdface.queue.PicQueue;
import org.springblade.modules.bdface.service.IBdInter;
import org.springblade.modules.bdface.service.ICameraService;
import org.springblade.modules.bdface.vo.CameraVO;
import org.springblade.modules.bdface.vo.FaceFeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2020-09-30
 */

@Slf4j
@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {


	@Value("${bd-face.threshold}")
	private int threshold = 85;

	@Autowired
	private IBdInter bdInter;


	@Autowired
	private FeatureCache featureCache;


	@Autowired
	private CameraCache cameraCache;


	@Autowired
	private PicQueue picQueue;



	@Override
	public IPage<CameraVO> selectCameraPage(IPage<CameraVO> page, CameraVO camera) {
		return page.setRecords(baseMapper.selectCameraPage(page, camera));
	}

	@Override
	public void faceRecogUseByCamera(String imgPath,
									 byte[] bytes,
									 BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList,
									 CameraDTO camera) throws Exception{
		if(StringUtils.isEmpty(imgPath)){
			throw new NullPointerException();
		}

		Integer threshold = camera.getThreshold();


		int matRet = bdInter.bdface_feature_List_jackoak(imgPath,faceFeatureList);

		if(matRet < 0){
			//log.info(" 提取特征值失败");
			faceFeatureList.read();
			faceFeatureList.features.read();
			MessageCache messageCache = camera.getMessageCache();
			messageCache.add(null);

			return;
		}

		if(threshold <= 0){
			threshold = this.threshold;
		}

		List<FaceFeatureVO> retList = faceRec(bytes,faceFeatureList,threshold,camera);


		if(retList == null){
			return;
		}

		if(retList.size() == 0){
			return;
		}

		MessageCache messageCache = camera.getMessageCache();
		messageCache.add(retList);
	}

	@Override
	public void faceRecogBase64CameraId(String base64, String credentialsId)throws Exception {
		if(StringUtils.isEmpty(base64)){
			return;
		}

		if(StringUtils.isEmpty(credentialsId)){
			return;
		}

		CameraDTO cameraDTO = cameraCache.getCamera(credentialsId);

		if(cameraDTO == null){
			return;
		}

		synchronized (CameraServiceImpl.class){
			PicEntity picEntity = new PicEntity();

			picEntity.setBase64(base64);
			picEntity.setCredentialsId(credentialsId);



			byte[] bytes = Base64Util.base64ToBytes(base64);
			String imgFilePath = FileUtil.base64ToFile(base64);


//			Mat mat = OpenCvUtil.bytesToMartix(bytes);

//			picEntity.setMat(mat);
			picEntity.setBytes(bytes);
			picEntity.setBase64(base64);
			picEntity.setCameraDTO(cameraDTO);
			picEntity.setImgFilePath(imgFilePath);

			picQueue.add(picEntity);

		}


	}


	private List<FaceFeatureVO> faceRec(byte[] bytes,
										BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList,
										int threshold,
										CameraDTO camera)throws Exception{


		List<FaceFeatureVO> ret = new ArrayList<>();
		try{
			List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = SdkUtil.getBDFaceFeatureArray(faceFeatureList);
			List<FaceFeatureDTO> dtoList = featureCache.getFeatureStore();

			if(dtoList == null){
				return ret;
			}


			for(BaiDuFaceSDK.BDFaceFeature.ByReference featureMain : listFeature){
				for(FaceFeatureDTO featureFor : dtoList){
					float score = bdInter.bdface_feature_compare_jackoak(featureFor.getFeatureStruct(),featureMain);

					log.info("score: " + score);

					if(score >= threshold){
						FaceFeatureVO featureVO = BeanUtil.copy(featureFor,FaceFeatureVO.class);
						featureVO.setScore(score);

						featureVO.setPicBytes(bytes);
						featureVO.setCameraDTO(camera);

						ret.add(featureVO);
					}

				}
				featureMain.read();
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			//释放资源
			faceFeatureList.read();
			faceFeatureList.features.read();
		}





		return ret;
	}

}
