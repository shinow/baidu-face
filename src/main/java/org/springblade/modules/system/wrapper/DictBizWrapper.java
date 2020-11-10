
package org.springblade.modules.system.wrapper;

import org.springblade.common.cache.DictBizCache;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.system.entity.DictBiz;
import org.springblade.modules.system.vo.DictBizVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class DictBizWrapper extends BaseEntityWrapper<DictBiz, DictBizVO> {

	public static DictBizWrapper build() {
		return new DictBizWrapper();
	}

	@Override
	public DictBizVO entityVO(DictBiz dict) {
		DictBizVO dictVO = Objects.requireNonNull(BeanUtil.copy(dict, DictBizVO.class));
		if (Func.equals(dict.getParentId(), BladeConstant.TOP_PARENT_ID)) {
			dictVO.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			DictBiz parent = DictBizCache.getById(dict.getParentId());
			dictVO.setParentName(parent.getDictValue());
		}
		return dictVO;
	}

	public List<DictBizVO> listNodeVO(List<DictBiz> list) {
		List<DictBizVO> collect = list.stream().map(dict -> BeanUtil.copy(dict, DictBizVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}
