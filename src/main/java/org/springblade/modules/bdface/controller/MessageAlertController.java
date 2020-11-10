package org.springblade.modules.bdface.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.bdface.entity.MessageAlert;
import org.springblade.modules.bdface.vo.MessageAlertVO;
import org.springblade.modules.bdface.service.IMessageAlertService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2020-10-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade_bdface/messagealert")
@Api(value = "", tags = "接口")
public class MessageAlertController extends BladeController {

	private final IMessageAlertService messageAlertService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入messageAlert")
	public R<MessageAlert> detail(MessageAlert messageAlert) {
		MessageAlert detail = messageAlertService.getOne(Condition.getQueryWrapper(messageAlert));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入messageAlert")
	public R<IPage<MessageAlert>> list(MessageAlert messageAlert, Query query) {
		IPage<MessageAlert> pages = messageAlertService.page(Condition.getPage(query), Condition.getQueryWrapper(messageAlert));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入messageAlert")
	public R<IPage<MessageAlertVO>> page(MessageAlertVO messageAlert, Query query) {
		IPage<MessageAlertVO> pages = messageAlertService.selectMessageAlertPage(Condition.getPage(query), messageAlert);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入messageAlert")
	public R save(@Valid @RequestBody MessageAlert messageAlert) {
		return R.status(messageAlertService.save(messageAlert));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入messageAlert")
	public R update(@Valid @RequestBody MessageAlert messageAlert) {
		return R.status(messageAlertService.updateById(messageAlert));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入messageAlert")
	public R submit(@Valid @RequestBody MessageAlert messageAlert) {
		return R.status(messageAlertService.saveOrUpdate(messageAlert));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(messageAlertService.removeByIds(Func.toLongList(ids)));
	}

	
}
