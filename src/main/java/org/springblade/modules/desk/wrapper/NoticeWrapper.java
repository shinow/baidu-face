
package org.springblade.modules.desk.wrapper;

import org.springblade.common.cache.DictCache;
import org.springblade.common.enums.DictEnum;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.desk.entity.Notice;
import org.springblade.modules.desk.vo.NoticeVO;

import java.util.Objects;

/**
 * Notice包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class NoticeWrapper extends BaseEntityWrapper<Notice, NoticeVO> {

	public static NoticeWrapper build() {
		return new NoticeWrapper();
	}

	@Override
	public NoticeVO entityVO(Notice notice) {
		NoticeVO noticeVO = Objects.requireNonNull(BeanUtil.copy(notice, NoticeVO.class));
		String dictValue = DictCache.getValue(DictEnum.NOTICE, noticeVO.getCategory());
		noticeVO.setCategoryName(dictValue);
		return noticeVO;
	}

}
