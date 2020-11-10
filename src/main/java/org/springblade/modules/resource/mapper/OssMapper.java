
package org.springblade.modules.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.resource.entity.Oss;
import org.springblade.modules.resource.vo.OssVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author BladeX
 */
public interface OssMapper extends BaseMapper<Oss> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param oss
	 * @return
	 */
	List<OssVO> selectOssPage(IPage page, OssVO oss);

}
