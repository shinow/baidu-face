
package org.springblade.modules.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.tool.api.R;
import org.springblade.modules.resource.entity.Oss;
import org.springblade.modules.resource.vo.OssVO;

/**
 * 服务类
 *
 * @author BladeX
 */
public interface IOssService extends BaseService<Oss> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param oss
	 * @return
	 */
	IPage<OssVO> selectOssPage(IPage<OssVO> page, OssVO oss);

	/**
	 * 提交oss信息
	 *
	 * @param oss
	 * @return
	 */
	boolean submit(Oss oss);

	/**
	 * 启动配置
	 *
	 * @param id
	 * @return
	 */
	boolean enable(Long id);



}
