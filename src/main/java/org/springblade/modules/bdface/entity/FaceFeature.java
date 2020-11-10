package org.springblade.modules.bdface.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2020-09-30
 */
@Data
@TableName("extend_iot_face_feature")
@ApiModel(value = "FaceFeature对象", description = "FaceFeature对象")
public class FaceFeature implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		private Long id;
	/**
	* 用户id
	*/
		@ApiModelProperty(value = "用户id")
		private Long userId;
	/**
	* 人脸特征值
	*/
		@ApiModelProperty(value = "人脸特征值")
		private String feature;


}
