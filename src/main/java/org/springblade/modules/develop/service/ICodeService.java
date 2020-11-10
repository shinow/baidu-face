
package org.springblade.modules.develop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.develop.entity.Code;

/**
 * 服务类
 *
 * @author Chill
 */
public interface ICodeService extends IService<Code> {

	/**
	 * 提交
	 *
	 * @param code
	 * @return
	 */
	boolean submit(Code code);

}
