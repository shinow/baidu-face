
package org.springblade.modules.resource.wrapper;

import org.springblade.common.cache.DictCache;
import org.springblade.common.enums.DictEnum;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.resource.entity.Oss;
import org.springblade.modules.resource.vo.OssVO;

import java.util.Objects;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author BladeX
 */
public class OssWrapper extends BaseEntityWrapper<Oss, OssVO> {

	public static OssWrapper build() {
		return new OssWrapper();
	}

	@Override
	public OssVO entityVO(Oss oss) {
		OssVO ossVO = Objects.requireNonNull(BeanUtil.copy(oss, OssVO.class));
		String categoryName = DictCache.getValue(DictEnum.OSS, oss.getCategory());
		String statusName = DictCache.getValue(DictEnum.YES_NO, oss.getStatus());
		ossVO.setCategoryName(categoryName);
		ossVO.setStatusName(statusName);
		return ossVO;
	}

}
