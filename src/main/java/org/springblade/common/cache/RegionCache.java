
package org.springblade.common.cache;

import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.system.entity.Region;
import org.springblade.modules.system.service.IRegionService;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 行政区划缓存工具类
 *
 * @author Chill
 */
public class RegionCache {
	public static final int PROVINCE_LEVEL = 1;
	public static final int CITY_LEVEL = 2;
	public static final int DISTRICT_LEVEL = 3;
	public static final int TOWN_LEVEL = 4;
	public static final int VILLAGE_LEVEL = 5;

	private static final String REGION_CODE = "region:code:";

	private static final IRegionService regionService;

	static {
		regionService = SpringUtil.getBean(IRegionService.class);
	}

	/**
	 * 获取行政区划实体
	 *
	 * @param code 区划编号
	 * @return Param
	 */
	public static Region getByCode(String code) {
		return CacheUtil.get(SYS_CACHE, REGION_CODE, code, () -> regionService.getById(code));
	}

}
