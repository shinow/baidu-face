
package org.springblade.modules.resource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 实体类
 *
 * @author BladeX
 */
@Data
@TableName("blade_oss")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Oss对象", description = "Oss对象")
public class Oss extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 所属分类
	 */
	@ApiModelProperty(value = "所属分类")
	private Integer category;

	/**
	 * 资源编号
	 */
	@ApiModelProperty(value = "资源编号")
	private String ossCode;

	/**
	 * oss地址
	 */
	@ApiModelProperty(value = "资源地址")
	private String endpoint;
	/**
	 * accessKey
	 */
	@ApiModelProperty(value = "accessKey")
	private String accessKey;
	/**
	 * secretKey
	 */
	@ApiModelProperty(value = "secretKey")
	private String secretKey;
	/**
	 * 空间名
	 */
	@ApiModelProperty(value = "空间名")
	private String bucketName;
	/**
	 * 应用ID TencentCOS需要
	 */
	@ApiModelProperty(value = "应用ID")
	private String appId;
	/**
	 * 地域简称 TencentCOS需要
	 */
	@ApiModelProperty(value = "地域简称")
	private String region;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
