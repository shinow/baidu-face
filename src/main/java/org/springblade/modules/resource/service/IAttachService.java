
package org.springblade.modules.resource.service;

import org.springblade.modules.resource.entity.Attach;
import org.springblade.modules.resource.vo.AttachVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 附件表 服务类
 *
 * @author Chill
 */
public interface IAttachService extends BaseService<Attach> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param attach
	 * @return
	 */
	IPage<AttachVO> selectAttachPage(IPage<AttachVO> page, AttachVO attach);

}
