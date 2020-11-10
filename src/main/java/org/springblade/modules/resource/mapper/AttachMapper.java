
package org.springblade.modules.resource.mapper;

import org.springblade.modules.resource.entity.Attach;
import org.springblade.modules.resource.vo.AttachVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 附件表 Mapper 接口
 *
 * @author Chill
 */
public interface AttachMapper extends BaseMapper<Attach> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param attach
	 * @return
	 */
	List<AttachVO> selectAttachPage(IPage page, AttachVO attach);

}
