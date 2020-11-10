
package org.springblade.modules.desk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.desk.entity.Notice;
import org.springblade.modules.desk.vo.NoticeVO;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Chill
 */
public interface NoticeMapper extends BaseMapper<Notice> {

	/**
	 * 前N条数据
	 *
	 * @param number 数量
	 * @return List<Notice>
	 */
	List<Notice> topList(Integer number);

	/**
	 * 自定义分页
	 *
	 * @param page   分页
	 * @param notice 实体
	 * @return List<NoticeVO>
	 */
	List<NoticeVO> selectNoticePage(IPage page, NoticeVO notice);

}
