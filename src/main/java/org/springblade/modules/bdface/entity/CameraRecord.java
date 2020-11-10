/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.bdface.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2020-10-10
 */
@Data
@TableName("extend_iot_camera_record")
@ApiModel(value = "CameraRecord对象", description = "CameraRecord对象")
public class CameraRecord implements Serializable {

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
	* 摄像头序列号
	*/
		@ApiModelProperty(value = "摄像头序列号")
		private String cameraCredentialsId;
	/**
	* 全景图
	*/
		@ApiModelProperty(value = "全景图")
		private String picAllUrl;
	/**
	* 人脸抠图
	*/
		@ApiModelProperty(value = "人脸抠图")
		private String picPartUrl;
	/**
	* 创建时间
	*/
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	* 摄像头名称
	*/
	@ApiModelProperty(value = "摄像头名称")
	private String cameraName;

	/**
	* 设备所处区域 这里有值 值为 设备名称_后的内容
	*/
	@ApiModelProperty(value = "设备所处区域 这里有值 值为 设备名称_后的内容")
	private String region;


	/**
	 * 原始图片 bytes
	 */
	@TableField(exist = false)
	private byte[] picBytes;


	/**
	 * 原始图片base64
	 */
	@TableField(exist = false)
	private String base64;


}
