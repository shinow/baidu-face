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

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2020-09-30
 */
@Data
@TableName("extend_iot_camera")
@ApiModel(value = "Camera对象", description = "Camera对象")
public class Camera implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		private Long id;
	/**
	* 摄像头ip
	*/
		@ApiModelProperty(value = "摄像头ip")
		private String ip;
	/**
	* 用户名
	*/
		@ApiModelProperty(value = "用户名")
		private String userName;
	/**
	* 密码
	*/
		@ApiModelProperty(value = "密码")
		private String pass;
	/**
	* rtsp路径
	*/
		@ApiModelProperty(value = "rtsp路径")
		private String rtsp;
	/**
	* 设备序列号
	*/
		@ApiModelProperty(value = "设备序列号")
		private String credentialsId;


	/**
	 * 摄像头名称
	 */
	@ApiModelProperty(value = "摄像头名称")
	private String cameraName;


	/**
	 * 阈值
	 */
	private Integer threshold;

	/**
	 * 型号
	 */
	private String type;

	/**
	 * 启用、禁用 0：启用，1：禁用
	 */
	private Integer flag;



}
