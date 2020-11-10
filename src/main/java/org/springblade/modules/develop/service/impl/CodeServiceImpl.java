
package org.springblade.modules.develop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.modules.develop.entity.Code;
import org.springblade.modules.develop.mapper.CodeMapper;
import org.springblade.modules.develop.service.ICodeService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

	@Override
	public boolean submit(Code code) {
		code.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		return saveOrUpdate(code);
	}

}
