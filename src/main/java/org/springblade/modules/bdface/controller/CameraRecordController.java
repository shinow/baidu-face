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
import org.springblade.modules.bdface.entity.CameraRecord;
import org.springblade.modules.bdface.vo.CameraRecordVO;
import org.springblade.modules.bdface.service.ICameraRecordService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2020-10-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade_bdface/camerarecord")
@Api(value = "", tags = "接口")
public class CameraRecordController extends BladeController {

	private final ICameraRecordService cameraRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入cameraRecord")
	public R<CameraRecord> detail(CameraRecord cameraRecord) {
		CameraRecord detail = cameraRecordService.getOne(Condition.getQueryWrapper(cameraRecord));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入cameraRecord")
	public R<IPage<CameraRecord>> list(CameraRecord cameraRecord, Query query) {
		IPage<CameraRecord> pages = cameraRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(cameraRecord).lambda().orderByDesc(CameraRecord::getCreateTime));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入cameraRecord")
	public R<IPage<CameraRecordVO>> page(CameraRecordVO cameraRecord, Query query) {
		IPage<CameraRecordVO> pages = cameraRecordService.selectCameraRecordPage(Condition.getPage(query), cameraRecord);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入cameraRecord")
	public R save(@Valid @RequestBody CameraRecord cameraRecord) {
		return R.status(cameraRecordService.save(cameraRecord));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入cameraRecord")
	public R update(@Valid @RequestBody CameraRecord cameraRecord) {
		return R.status(cameraRecordService.updateById(cameraRecord));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入cameraRecord")
	public R submit(@Valid @RequestBody CameraRecord cameraRecord) {
		return R.status(cameraRecordService.saveOrUpdate(cameraRecord));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(cameraRecordService.removeByIds(Func.toLongList(ids)));
	}

	
}
