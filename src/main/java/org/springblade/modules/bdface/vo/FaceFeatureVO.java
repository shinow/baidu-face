package org.springblade.modules.bdface.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opencv.core.Mat;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.entity.FaceFeature;

/**
 * 视图实体类
 *
 * @author BladeX
 * @since 2020-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FaceFeatureVO对象", description = "FaceFeatureVO对象")
public class FaceFeatureVO extends FaceFeature {
	private static final long serialVersionUID = 1L;

	/**
	 * 人脸识别比分
	 */
	private Float score;

	/**
	 * 对象创建时间
	 */
	private Long currentTimeMillis;


	/**
	 * 图片
	 */
	private String picUrl;

	private transient byte[] picBytes;

	private transient CameraDTO cameraDTO;



	public FaceFeatureVO(){
		this.currentTimeMillis = System.currentTimeMillis();
	}


}
