
package org.springblade.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.support.Kv;
import org.springblade.modules.system.entity.Menu;
import org.springblade.modules.system.vo.MenuVO;

import java.util.List;
import java.util.Map;

/**
 * 服务类
 *
 * @author Chill
 */
public interface IMenuService extends IService<Menu> {

	/**
	 * 懒加载列表
	 *
	 * @param parentId
	 * @param param
	 * @return
	 */
	List<MenuVO> lazyList(Long parentId, Map<String, Object> param);

	/**
	 * 懒加载菜单列表
	 *
	 * @param parentId
	 * @param param
	 * @return
	 */
	List<MenuVO> lazyMenuList(Long parentId, Map<String, Object> param);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<MenuVO> routes(String roleId, Long topMenuId);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<MenuVO> routesExt(String roleId, Long topMenuId);

	/**
	 * 按钮树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuVO> buttons(String roleId);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<MenuVO> tree();

	/**
	 * 授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantTree(BladeUser user);

	/**
	 * 顶部菜单树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantTopTree(BladeUser user);

	/**
	 * 数据权限授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantDataScopeTree(BladeUser user);

	/**
	 * 接口权限授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantApiScopeTree(BladeUser user);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> roleTreeKeys(String roleIds);

	/**
	 * 默认选中节点
	 *
	 * @param topMenuIds
	 * @return
	 */
	List<String> topTreeKeys(String topMenuIds);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> dataScopeTreeKeys(String roleIds);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> apiScopeTreeKeys(String roleIds);

	/**
	 * 获取配置的角色权限
	 *
	 * @param user
	 * @return
	 */
	List<Kv> authRoutes(BladeUser user);

	/**
	 * 删除菜单
	 *
	 * @param ids
	 * @return
	 */
	boolean removeMenu(String ids);

	/**
	 * 提交
	 *
	 * @param menu
	 * @return
	 */
	boolean submit(Menu menu);

}
