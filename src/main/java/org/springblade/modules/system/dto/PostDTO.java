
package org.springblade.modules.system.dto;

import org.springblade.modules.system.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位表数据传输对象实体类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDTO extends Post {
	private static final long serialVersionUID = 1L;

}
