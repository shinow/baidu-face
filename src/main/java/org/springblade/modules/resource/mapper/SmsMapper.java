
package org.springblade.modules.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.resource.entity.Sms;
import org.springblade.modules.resource.vo.SmsVO;

import java.util.List;

/**
 * 短信配置表 Mapper 接口
 *
 * @author BladeX
 */
public interface SmsMapper extends BaseMapper<Sms> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param sms
	 * @return
	 */
	List<SmsVO> selectSmsPage(IPage page, SmsVO sms);

}
