package org.springblade.modules.bdface.service;

import org.springblade.modules.bdface.entity.CameraRecord;
import org.springblade.modules.bdface.vo.CameraRecordVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.bdface.vo.FaceFeatureVO;

/**
 *  服务类
 *
 * @author BladeX
 * @since 2020-10-10
 */
public interface ICameraRecordService extends IService<CameraRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param cameraRecord
	 * @return
	 */
	IPage<CameraRecordVO> selectCameraRecordPage(IPage<CameraRecordVO> page, CameraRecordVO cameraRecord);

	void saveRecord(FaceFeatureVO faceFeatureVO);

}
