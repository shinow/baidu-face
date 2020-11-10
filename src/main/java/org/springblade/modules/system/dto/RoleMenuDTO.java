
package org.springblade.modules.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.modules.system.entity.RoleMenu;

/**
 * 数据传输对象实体类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenuDTO extends RoleMenu {
	private static final long serialVersionUID = 1L;

}
