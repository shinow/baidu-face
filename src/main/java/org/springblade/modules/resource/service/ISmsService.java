
package org.springblade.modules.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.resource.entity.Sms;
import org.springblade.modules.resource.vo.SmsVO;

/**
 * 短信配置表 服务类
 *
 * @author BladeX
 */
public interface ISmsService extends BaseService<Sms> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param sms
	 * @return
	 */
	IPage<SmsVO> selectSmsPage(IPage<SmsVO> page, SmsVO sms);

	/**
	 * 提交oss信息
	 *
	 * @param oss
	 * @return
	 */
	boolean submit(Sms oss);

	/**
	 * 启动配置
	 *
	 * @param id
	 * @return
	 */
	boolean enable(Long id);

}
