
package org.springblade.modules.desk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.modules.desk.entity.Notice;
import org.springblade.modules.desk.mapper.NoticeMapper;
import org.springblade.modules.desk.service.INoticeService;
import org.springblade.modules.desk.vo.NoticeVO;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Override
	public IPage<NoticeVO> selectNoticePage(IPage<NoticeVO> page, NoticeVO notice) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		notice.setTenantId(AuthUtil.getTenantId());
		return page.setRecords(baseMapper.selectNoticePage(page, notice));
	}

}
