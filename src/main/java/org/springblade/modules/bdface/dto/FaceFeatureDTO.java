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
package org.springblade.modules.bdface.dto;

import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.modules.bdface.entity.FaceFeature;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据传输对象实体类
 *
 * @author BladeX
 * @since 2020-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FaceFeatureDTO extends FaceFeature {
	private static final long serialVersionUID = 1L;

	private BaiDuFaceSDK.BDFaceFeature.ByReference featureStruct;

}
