
package org.springblade.modules.resource.vo;

import org.springblade.modules.resource.entity.Attach;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 附件表视图实体类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AttachVO对象", description = "附件表")
public class AttachVO extends Attach {
	private static final long serialVersionUID = 1L;

}
