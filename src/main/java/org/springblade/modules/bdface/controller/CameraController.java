package org.springblade.modules.bdface.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.utils.FileUtil;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.modules.bdface.cache.CameraCache;
import org.springblade.modules.bdface.dto.CameraDTO;
import org.springblade.modules.bdface.entity.Camera;
import org.springblade.modules.bdface.queue.PicEntity;
import org.springblade.modules.bdface.queue.PicQueue;
import org.springblade.modules.bdface.service.ICameraService;
import org.springblade.modules.bdface.vo.CameraVO;
import org.springblade.modules.bdface.vo.FaceFeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;


@Slf4j
@RestController
@RequestMapping("blade_bdface/camera")
@Api(value = "", tags = "接口")
public class CameraController extends BladeController {

	@Autowired
	private  ICameraService cameraService;



	@Autowired
	private PicQueue picQueue;




	@Autowired
	private CameraCache cameraCache;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入camera")
	public R<Camera> detail(Camera camera) {
		Camera detail = cameraService.getOne(Condition.getQueryWrapper(camera));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入camera")
	public R<IPage<Camera>> list(Camera camera, Query query) {
		IPage<Camera> pages = cameraService.page(Condition.getPage(query), Condition.getQueryWrapper(camera));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入camera")
	public R<IPage<CameraVO>> page(CameraVO camera, Query query) {
		IPage<CameraVO> pages = cameraService.selectCameraPage(Condition.getPage(query), camera);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入camera")
	public R save(@Valid @RequestBody Camera camera) {
		return R.status(cameraService.save(camera));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入camera")
	public R update(@Valid @RequestBody Camera camera) {
		return R.status(cameraService.updateById(camera));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入camera")
	public R submit(@Valid @RequestBody Camera camera) {
		return R.status(cameraService.saveOrUpdate(camera));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(cameraService.removeByIds(Func.toLongList(ids)));
	}




	@PostMapping("/faceRecogUseBy3d")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "人脸识别", notes = "picEntity")
	public R<String> faceRecogUseBy3d( @Valid PicEntity picEntity)throws Exception {
		if(picEntity == null){
			throw new NullPointerException();
		}

		if(picEntity.getBase64()== null){
			return R.fail("base64 is null");
		}

		if(picEntity.getCredentialsId() == null){
			return R.fail("credentialsId is null");
		}


		String base64 =  picEntity.getBase64();
		String credentialsId = picEntity.getCredentialsId();

		cameraService.faceRecogBase64CameraId(base64,credentialsId);

		return R.status(true);



	}
}
