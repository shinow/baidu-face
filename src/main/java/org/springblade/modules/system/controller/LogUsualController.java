
package org.springblade.modules.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.model.LogUsual;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tenant.annotation.NonDS;
import org.springblade.core.tool.api.R;
import org.springblade.modules.system.service.ILogUsualService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@NonDS
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_LOG_NAME + "/usual")
public class LogUsualController {

	private final ILogUsualService logService;

	/**
	 * 查询单条
	 */
	@GetMapping("/detail")
	public R<LogUsual> detail(LogUsual log) {
		return R.data(logService.getOne(Condition.getQueryWrapper(log)));
	}

	/**
	 * 查询多条(分页)
	 */
	@GetMapping("/list")
	public R<IPage<LogUsual>> list(@ApiIgnore @RequestParam Map<String, Object> log, Query query) {
		IPage<LogUsual> pages = logService.page(Condition.getPage(query), Condition.getQueryWrapper(log, LogUsual.class));
		return R.data(pages);
	}

}
