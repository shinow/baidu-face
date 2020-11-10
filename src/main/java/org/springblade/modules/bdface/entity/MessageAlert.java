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
 * @since 2020-10-10
 */
@Data
@TableName("extend_iot_message_alert")
@ApiModel(value = "MessageAlert对象", description = "MessageAlert对象")
public class MessageAlert implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		private Long id;
	/**
	* 报警器 ip
	*/
		@ApiModelProperty(value = "报警器 ip")
		private String ip;
	/**
	* 报警器 url
	*/
		@ApiModelProperty(value = "报警器 url")
		private String url;
	/**
	* 报警器api key
	*/
		@ApiModelProperty(value = "报警器api key")
		private String apiKey;




	/**
	 * 从上到下第几个led灯
	 */
	@ApiModelProperty(value = "从上到下第几个led灯")
	private String led;

	/**
	 * LED亮灯模式，1为常量，2为闪亮，3为转亮
	 */
	@ApiModelProperty(value = "LED亮灯模式，1为常量，2为闪亮，3为转亮")
	private String mode;


	/**
	 * 持续时间
	 */
	@ApiModelProperty(value = "持续时间")
	private String last;

	/**
	 * 提示音编号
	 */
	@ApiModelProperty(value = "提示音编号")
	private String tone;

}
