package org.springblade.modules.bdface.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.opencv.core.Mat;
import org.springblade.core.tool.api.R;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.entity.Camera;
import org.springblade.modules.bdface.vo.CameraVO;
import org.springblade.modules.bdface.vo.FaceFeatureVO;

/**
 *  服务类
 *
 * @author BladeX
 * @since 2020-09-30
 */
public interface ICameraService extends IService<Camera> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param camera
	 * @return
	 */
	IPage<CameraVO> selectCameraPage(IPage<CameraVO> page, CameraVO camera);


	/**
	 * 摄像头识别 使用
	 * @param imgPath
	 * @param faceFeatureList
	 * @throws Exception
	 */
	void faceRecogUseByCamera(String imgPath,
							  byte[] bytes,
							  BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList,
							  CameraDTO camera )throws Exception;




	void faceRecogBase64CameraId(String base64,String credentialsId)throws Exception ;

}
