
package org.springblade.modules.resource.service.impl;

import org.springblade.modules.resource.entity.Attach;
import org.springblade.modules.resource.vo.AttachVO;
import org.springblade.modules.resource.mapper.AttachMapper;
import org.springblade.modules.resource.service.IAttachService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 附件表 服务实现类
 *
 * @author Chill
 */
@Service
public class AttachServiceImpl extends BaseServiceImpl<AttachMapper, Attach> implements IAttachService {

	@Override
	public IPage<AttachVO> selectAttachPage(IPage<AttachVO> page, AttachVO attach) {
		return page.setRecords(baseMapper.selectAttachPage(page, attach));
	}

}
