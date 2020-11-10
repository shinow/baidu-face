
package org.springblade.modules.system.vo;

import org.springblade.modules.system.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 岗位表视图实体类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PostVO对象", description = "岗位表")
public class PostVO extends Post {
	private static final long serialVersionUID = 1L;

	/**
	 * 岗位分类名
	 */
	private String categoryName;

}
