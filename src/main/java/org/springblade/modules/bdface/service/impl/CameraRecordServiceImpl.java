package org.springblade.modules.bdface.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.thread.JdkThreadPoolConfig;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.ZipUtil;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.modules.bdface.Inter.MessageInter;
import org.springblade.modules.bdface.config.FaceRecConfig;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.entity.CameraRecord;
import org.springblade.modules.bdface.mapper.CameraRecordMapper;
import org.springblade.modules.bdface.service.ICameraRecordService;
import org.springblade.modules.bdface.vo.CameraRecordVO;
import org.springblade.modules.bdface.vo.FaceFeatureVO;
import org.springblade.modules.resource.builder.oss.OssBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2020-10-10
 */
@Slf4j
@Service
public class CameraRecordServiceImpl extends ServiceImpl<CameraRecordMapper, CameraRecord> implements ICameraRecordService {

	@Autowired
	private OssBuilder ossBuilder;


	@Autowired
	private FaceRecConfig faceRecConfig;


	@Override
	public IPage<CameraRecordVO> selectCameraRecordPage(IPage<CameraRecordVO> page, CameraRecordVO cameraRecord) {
		return page.setRecords(baseMapper.selectCameraRecordPage(page, cameraRecord));
	}

	@Async
	@Override
	public void saveRecord(FaceFeatureVO faceVO) {

		CameraDTO cameraDTO = faceVO.getCameraDTO();

		CameraRecord record = new CameraRecord();
		record.setUserId(faceVO.getUserId());

		if(cameraDTO != null){
			record.setCameraCredentialsId(cameraDTO.getCredentialsId());
			record.setCameraName(cameraDTO.getCameraName());


			String region = cameraDTO.getCameraName();
			try{
				if(region.contains("_")){
					String array[] = region.split("_");
					region = array[array.length-1];
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			record.setRegion(region);
		}


		Map<String,MessageInter> beanMap = SpringUtil.getContext().getBeansOfType(MessageInter.class);


		MultipartFile mFile = null;
		InputStream in = null;



		byte[] bytesPic = faceVO.getPicBytes();
		record.setPicBytes(bytesPic);

		beanMap.forEach((key,value)->{
			try {
				JdkThreadPoolConfig.cachedThreadPool.execute(()->{
					value.invoke(record);
				});
			}catch (Exception e){
				e.printStackTrace();
			}

		});


		/*try {

			byte[] bytesPic = faceVO.getPicBytes();
			if(bytesPic != null){
				//压缩
				bytesPic = ZipUtil.zipBytes(bytesPic);
				mFile = CommonUtil.bytesToMultipart(bytesPic);
				in = mFile.getInputStream();

				String base64 = Base64Util.bytesToBase64Bytes(bytesPic);
				record.setBase64(base64);
				record.setPicBytes(bytesPic);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}*/

		try {

			if(faceRecConfig.isSavefacepic()){
				if(bytesPic != null){
					//压缩
					bytesPic = ZipUtil.zipBytes(bytesPic);
					mFile = CommonUtil.bytesToMultipart(bytesPic);
					in = mFile.getInputStream();

					String base64 = Base64Util.bytesToBase64Bytes(bytesPic);
					record.setBase64(base64);

					BladeFile bladeFile = ossBuilder.template("baidu-face-camera").putFile(mFile.getOriginalFilename(), in);
					faceVO.setPicUrl(bladeFile.getLink());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}







		record.setPicAllUrl(faceVO.getPicUrl());
		record.setPicPartUrl(faceVO.getPicUrl());

		record.setCreateTime(DateUtil.now());
		this.save(record);




		//log.info("faceFeatureVO.getPicUrl(): " + faceVO.getPicUrl());
	}


}
