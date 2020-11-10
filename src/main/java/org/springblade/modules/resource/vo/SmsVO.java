
package org.springblade.modules.resource.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.modules.resource.entity.Sms;

/**
 * 短信配置表视图实体类
 *
 * @author BladeX
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SmsVO对象", description = "短信配置表")
public class SmsVO extends Sms {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类名
	 */
	private String categoryName;

	/**
	 * 是否启用
	 */
	private String statusName;

}
