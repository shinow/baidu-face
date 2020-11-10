
package org.springblade.modules.desk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.desk.entity.Notice;
import org.springblade.modules.desk.vo.NoticeVO;

/**
 * 服务类
 *
 * @author Chill
 */
public interface INoticeService extends BaseService<Notice> {

	/**
	 * 自定义分页
	 * @param page
	 * @param notice
	 * @return
	 */
	IPage<NoticeVO> selectNoticePage(IPage<NoticeVO> page, NoticeVO notice);

}
