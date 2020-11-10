
package org.springblade.modules.system.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.opencv.face.Face;
import org.springblade.common.cache.ParamCache;
import org.springblade.common.cache.SysCache;
import org.springblade.common.cache.UserCache;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.constant.TenantConstant;
import org.springblade.common.thread.JdkThreadPoolConfig;
import org.springblade.common.utils.FileUtil;
import org.springblade.common.utils.ZipUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tenant.BladeTenantProperties;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.*;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.extend.face.util.OpenCvUtil;
import org.springblade.modules.bdface.cache.FeatureCache;
import org.springblade.modules.bdface.entity.FaceFeature;
import org.springblade.modules.bdface.queue.FeatureEntity;
import org.springblade.modules.bdface.queue.FeatureQueue;
import org.springblade.modules.bdface.service.IBdInter;
import org.springblade.modules.bdface.service.IFaceFeatureService;
import org.springblade.modules.resource.builder.oss.OssBuilder;
import org.springblade.modules.system.entity.*;
import org.springblade.modules.system.excel.UserExcel;
import org.springblade.modules.system.mapper.UserMapper;
import org.springblade.modules.system.service.IRoleService;
import org.springblade.modules.system.service.IUserDeptService;
import org.springblade.modules.system.service.IUserOauthService;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springblade.common.constant.CommonConstant.DEFAULT_PARAM_PASSWORD;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {
	private static final String GUEST_NAME = "guest";


	@Autowired
	private  IUserDeptService userDeptService;

	@Autowired
	private  IUserOauthService userOauthService;

	@Autowired
	private  IRoleService roleService;

	@Autowired
	private  BladeTenantProperties tenantProperties;

	@Autowired
	private OssBuilder ossBuilder;


	@Autowired
	private IBdInter bdInter;

	@Autowired
	private IFaceFeatureService faceFeatureService;


	@Autowired
	private FeatureQueue featureQueue;

	@Autowired
	private FeatureCache featureCache;




	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean submit(User user) {
		if (StringUtil.isBlank(user.getTenantId())) {
			user.setTenantId(BladeConstant.ADMIN_TENANT_ID);
		}
		String tenantId = user.getTenantId();
		Tenant tenant = SysCache.getTenant(tenantId);
		if (Func.isNotEmpty(tenant)) {
			Integer accountNumber = tenant.getAccountNumber();
			if (tenantProperties.getLicense()) {
				String licenseKey = tenant.getLicenseKey();
				String decrypt = DesUtil.decryptFormHex(licenseKey, TenantConstant.DES_KEY);
				accountNumber = JsonUtil.parse(decrypt, Tenant.class).getAccountNumber();
			}
			Integer tenantCount = baseMapper.selectCount(Wrappers.<User>query().lambda().eq(User::getTenantId, tenantId));
			if (accountNumber != null && accountNumber > 0 && accountNumber <= tenantCount) {
				throw new ServiceException("当前租户已到最大账号额度!");
			}
		}
		if (Func.isNotEmpty(user.getPassword())) {
			user.setPassword(DigestUtil.encrypt(user.getPassword()));
		}
		Integer userCount = baseMapper.selectCount(Wrappers.<User>query().lambda().eq(User::getTenantId, tenantId).eq(User::getAccount, user.getAccount()));
		if (userCount > 0 && Func.isEmpty(user.getId())) {
			throw new ServiceException(StringUtil.format("当前用户 [{}] 已存在!", user.getAccount()));
		}
		return save(user) && submitUserDept(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUser(User user) {
		String tenantId = user.getTenantId();
		Integer userCount = baseMapper.selectCount(
			Wrappers.<User>query().lambda()
				.eq(User::getTenantId, tenantId)
				.eq(User::getAccount, user.getAccount())
				.notIn(User::getId, user.getId())
		);
		if (userCount > 0) {
			throw new ServiceException(StringUtil.format("当前用户 [{}] 已存在!", user.getAccount()));
		}

		boolean userInfoB = updateUserInfo(user);
		boolean deptB = submitUserDept(user);


		return  userInfoB && deptB;
	}

	@Override
	public boolean updateUserInfo(User user) {
		user.setPassword(null);
		return updateById(user);
	}

	private boolean submitUserDept(User user) {
		List<Long> deptIdList = Func.toLongList(user.getDeptId());
		List<UserDept> userDeptList = new ArrayList<>();
		deptIdList.forEach(deptId -> {
			UserDept userDept = new UserDept();
			userDept.setUserId(user.getId());
			userDept.setDeptId(deptId);
			userDeptList.add(userDept);
		});
		userDeptService.remove(Wrappers.<UserDept>update().lambda().eq(UserDept::getUserId, user.getId()));
		return userDeptService.saveBatch(userDeptList);
	}

	@Override
	public IPage<User> selectUserPage(IPage<User> page, User user, Long deptId, String tenantId) {
		List<Long> deptIdList = SysCache.getDeptChildIds(deptId);
		return page.setRecords(baseMapper.selectUserPage(page, user, deptIdList, tenantId));
	}

	@Override
	public User userByAccount(String tenantId, String account) {
		return baseMapper.selectOne(Wrappers.<User>query().lambda().eq(User::getTenantId, tenantId).eq(User::getAccount, account).eq(User::getIsDeleted, BladeConstant.DB_NOT_DELETED));
	}

	@Override
	public UserInfo userInfo(Long userId) {
		User user = baseMapper.selectById(userId);
		return buildUserInfo(user);
	}

	@Override
	public UserInfo userInfo(String tenantId, String account, String password) {
		User user = baseMapper.getUser(tenantId, account, password);
		return buildUserInfo(user);
	}

	private UserInfo buildUserInfo(User user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUser(user);
		if (Func.isNotEmpty(user)) {
			List<String> roleAlias = roleService.getRoleAliases(user.getRoleId());
			userInfo.setRoles(roleAlias);
		}
		return userInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserInfo userInfo(UserOauth userOauth) {
		UserOauth uo = userOauthService.getOne(Wrappers.<UserOauth>query().lambda().eq(UserOauth::getUuid, userOauth.getUuid()).eq(UserOauth::getSource, userOauth.getSource()));
		UserInfo userInfo;
		if (Func.isNotEmpty(uo) && Func.isNotEmpty(uo.getUserId())) {
			userInfo = this.userInfo(uo.getUserId());
			userInfo.setOauthId(Func.toStr(uo.getId()));
		} else {
			userInfo = new UserInfo();
			if (Func.isEmpty(uo)) {
				userOauthService.save(userOauth);
				userInfo.setOauthId(Func.toStr(userOauth.getId()));
			} else {
				userInfo.setOauthId(Func.toStr(uo.getId()));
			}
			User user = new User();
			user.setAccount(userOauth.getUsername());
			userInfo.setUser(user);
			userInfo.setRoles(Collections.singletonList(GUEST_NAME));
		}
		return userInfo;
	}

	@Override
	public boolean grant(String userIds, String roleIds) {
		User user = new User();
		user.setRoleId(roleIds);
		return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toLongList(userIds)));
	}

	@Override
	public boolean resetPassword(String userIds) {
		User user = new User();
		user.setPassword(DigestUtil.encrypt(CommonConstant.DEFAULT_PASSWORD));
		user.setUpdateTime(DateUtil.now());
		return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toLongList(userIds)));
	}

	@Override
	public boolean updatePassword(Long userId, String oldPassword, String newPassword, String newPassword1) {
		User user = getById(userId);
		if (!newPassword.equals(newPassword1)) {
			throw new ServiceException("请输入正确的确认密码!");
		}
		if (!user.getPassword().equals(DigestUtil.hex(oldPassword))) {
			throw new ServiceException("原密码不正确!");
		}
		return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.hex(newPassword)).eq(User::getId, userId));
	}

	@Override
	public boolean removeUser(String userIds) {
		if (Func.contains(Func.toLongArray(userIds), AuthUtil.getUserId())) {
			throw new ServiceException("不能删除本账号!");
		}
		return deleteLogic(Func.toLongList(userIds));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void importUser(List<UserExcel> data, Boolean isCovered) {
		data.forEach(userExcel -> {
			User user = Objects.requireNonNull(BeanUtil.copy(userExcel, User.class));
			// 设置部门ID
			user.setDeptId(SysCache.getDeptIds(userExcel.getTenantId(), userExcel.getDeptName()));
			// 设置岗位ID
			user.setPostId(SysCache.getPostIds(userExcel.getTenantId(), userExcel.getPostName()));
			// 设置角色ID
			user.setRoleId(SysCache.getRoleIds(userExcel.getTenantId(), userExcel.getRoleName()));
			// 设置租户ID
			if (!AuthUtil.isAdministrator() || StringUtil.isBlank(user.getTenantId())) {
				user.setTenantId(AuthUtil.getTenantId());
			}
			// 覆盖数据
			if (isCovered) {
				// 查询用户是否存在
				User oldUser = UserCache.getUser(userExcel.getTenantId(), userExcel.getAccount());
				if (oldUser != null && oldUser.getId() != null) {
					user.setId(oldUser.getId());
					this.updateUser(user);
					return;
				}
			}
			// 获取默认密码配置
			String initPassword = ParamCache.getValue(DEFAULT_PARAM_PASSWORD);
			user.setPassword(initPassword);
			this.submit(user);
		});
	}

	@Override
	public List<UserExcel> exportUser(Wrapper<User> queryWrapper) {
		List<UserExcel> userList = baseMapper.exportUser(queryWrapper);
		userList.forEach(user -> {
			user.setRoleName(StringUtil.join(SysCache.getRoleNames(user.getRoleId())));
			user.setDeptName(StringUtil.join(SysCache.getDeptNames(user.getDeptId())));
			user.setPostName(StringUtil.join(SysCache.getPostNames(user.getPostId())));
		});
		return userList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean registerGuest(User user, Long oauthId) {
		Tenant tenant = SysCache.getTenant(user.getTenantId());
		if (tenant == null || tenant.getId() == null) {
			throw new ApiException("租户信息错误!");
		}
		UserOauth userOauth = userOauthService.getById(oauthId);
		if (userOauth == null || userOauth.getId() == null) {
			throw new ApiException("第三方登陆信息错误!");
		}
		user.setRealName(user.getName());
		user.setAvatar(userOauth.getAvatar());
		user.setRoleId(StringPool.MINUS_ONE);
		user.setDeptId(StringPool.MINUS_ONE);
		user.setPostId(StringPool.MINUS_ONE);
		boolean userTemp = this.submit(user);
		userOauth.setUserId(user.getId());
		userOauth.setTenantId(user.getTenantId());
		boolean oauthTemp = userOauthService.updateById(userOauth);
		return (userTemp && oauthTemp);
	}


	@Override
	public R<List<JSONObject>> importImgZip(MultipartFile multipartFile) {

		List<JSONObject> data = new ArrayList<>(10);
		String tempPath = System.getProperty("java.io.tmpdir");

		Long random = System.currentTimeMillis();
		String tempFilePath = tempPath + "/zip" + "//" + random;

		cn.hutool.core.io.FileUtil.mkdir(tempFilePath);
		String tempFileFullPathName = tempFilePath + "//" + random + ".zip";

		try{
			File savedFile = new File(tempFileFullPathName);
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), savedFile);
			ZipUtil.unZip(savedFile, tempFilePath);
			File fileList = new File(tempFilePath);
			File[] files = fileList.listFiles();

			String tenantId = AuthUtil.getTenantId();




			for (int i = 0; i < files.length; i++) {
				try{
					File file = files[i];
					if (file.isFile()) {
						String fileName = file.getName();
						if(fileName.endsWith(".zip")){
							continue;
						}

						String[] hostInfoArray = fileName.split("\\.");
						String account = hostInfoArray[0];

						QueryWrapper<User> queryWrapper = new QueryWrapper();
						queryWrapper.lambda().eq(User::getTenantId,tenantId).eq(User::getAccount,account);


						User user = this.getOne(queryWrapper);
						if(user == null){
							continue;
						}


						//压缩
						ZipUtil.zipPic(file);



						FileInputStream inputStream = new FileInputStream(file);
						MultipartFile mFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
						JdkThreadPoolConfig.cachedThreadPool.execute(()->{
							try {
								saveUserFace(account,mFile,user);
							}catch (Exception e){
								e.printStackTrace();
							}

						});



					}
				}catch (Exception e){
					e.printStackTrace();
				}

			}

		}catch (Exception e){
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("thirdUserId", "");
			jsonObject.put("message", e.getMessage());
			data.add(jsonObject);
		}finally {
			//清除临时操作文件目录
			ZipUtil.clearFiles(tempFilePath);
		}
		if (data.isEmpty()) {
			return R.status(true);
		} else {
			return R.status(false);
		}
	}


	private boolean saveUserFace(String account, MultipartFile mFile,User user)throws Exception{
		if(StringUtil.isEmpty(account)){
			return false;
		}

		boolean ret = false;


		InputStream in = mFile.getInputStream();

		BladeFile bladeFile = ossBuilder.template("baidu-face").putFile(mFile.getOriginalFilename(), in);

		String url = bladeFile.getLink();
		if(StringUtils.isEmpty(url)){
			throw new NullPointerException("user's avatar is null");
		}

		user.setAvatar(url);

		ret = this.updateUser(user);


		saveOrUpdateFeature(user);

		return ret;

	}


	public R saveOrUpdateFeature(User user)throws Exception{
		if(user == null){
			throw new NullPointerException();
		}

		if(StringUtil.isEmpty(user.getAvatar())){
			return R.fail("user.getAvatar() is null");
		}

		if(user.getId() == null){
			return R.fail("user.getId()");
		}

		String url = user.getAvatar();
		long userId = user.getId();




		BufferedImage bufferedImage = org.springblade.extend.face.util.ImageUtil.readBufferImageFromPath(url);

		if(bufferedImage == null){
			return R.fail("bufferedImage is null");
		}

		byte[] bytes = OpenCvUtil.bufImg2Bytes(bufferedImage);

		String imgFilePath = FileUtil.bytesToFile(bytes);


		FeatureEntity entity = new FeatureEntity();
		entity.setFilePath(imgFilePath);
		featureQueue.get(entity);

		String featureBase64 = entity.getFeatureBase64();

		if(StringUtils.isEmpty(featureBase64)){
			return R.fail("featureBase64 is null");
		}

		//String featureBase64 = bdInter.loadPicFeatureBase64(url)


		boolean ret = false;

		FaceFeature faceFeature = new FaceFeature();
		faceFeature.setFeature(featureBase64);
		faceFeature.setUserId(userId);


		QueryWrapper<FaceFeature> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(FaceFeature::getUserId,userId);

		FaceFeature dbFace = faceFeatureService.getOne(queryWrapper);

		if(dbFace != null){
			//存在
			dbFace.setFeature(featureBase64);
			ret = faceFeatureService.updateById(dbFace);
		}else{
			//不存在
			ret =  faceFeatureService.save(faceFeature);
		}

		featureCache.add(userId,featureBase64);

		return R.status(ret);

	}

	@Override
	public R saveOrUpdateUserAndFeatureByThird(User user) {

		if(user == null){
			throw new NullPointerException("user is null");
		}

		String thirdUserId = user.getThirdUserId();

		if(StringUtils.isEmpty(thirdUserId)){
			throw new NullPointerException("thirdUserId is ull");
		}

		QueryWrapper<User>  queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getThirdUserId,thirdUserId);
		User userDb = this.getOne(queryWrapper);

		//User userParam = null;

		if(userDb == null ){
			//不存在
			user.setId(null);
			user.setTenantId("000000");
			this.saveOrUpdate(user);
			//userParam = user;
		}else {
			//存在
			//userParam = user;
			user.setId(userDb.getId());
		}

		try {
			saveOrUpdateFeature(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return R.status(true);
	}


}
