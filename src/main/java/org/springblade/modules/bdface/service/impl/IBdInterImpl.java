package org.springblade.modules.bdface.service.impl;

import org.opencv.core.Mat;
import org.springblade.core.tool.utils.FileUtil;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.extend.face.util.SdkUtil;
import org.springblade.modules.bdface.service.IBdInter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


@Service
public class IBdInterImpl implements IBdInter {




	@Override
	public boolean initSdk()throws Exception {

		synchronized (IBdInter.class){
			if(SdkUtil.getBdSdk() == null){
				return SdkUtil.initSdk();
			}else {
				throw new Exception("非法操作，已经初始化 过 sdk， already init sdk");
			}
		}

	}

	@Override
	public String loadPicFeatureBase64(String imgFilePath){

		try {
			return SdkUtil.loadPicFeatureBase64(imgFilePath);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			FileUtil.deleteQuietly(new File(imgFilePath));
		}

		throw new NullPointerException();

	}

	@Override
	public BaiDuFaceSDK.BDFaceFeature.ByReference featureStrToFaceFeature(String feature) throws Exception {
		return SdkUtil.featureStrToFaceFeature(feature);
	}

	@Override
	public int bdface_feature_List_jackoakMat(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList, long addr) {
		return SdkUtil.bdface_feature_List_jackoakMat(featureList,addr);
	}

	@Override
	public int bdface_feature_List_jackoakMat2(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList, String base64) {
		return SdkUtil.bdface_feature_List_jackoakMat2(featureList,base64);
	}



	@Override
	public float bdface_feature_compare_jackoak(BaiDuFaceSDK.BDFaceFeature.ByReference featuresMain, BaiDuFaceSDK.BDFaceFeature.ByReference featuresTwo) {
		return SdkUtil.bdface_feature_compare_jackoak(featuresMain,featuresTwo);
	}

	@Override
	public int bdface_feature_List_jackoak(String imgFilePath,BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList) {

		try {
			return SdkUtil.bdface_feature_List_jackoak(featureList,imgFilePath);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			FileUtil.deleteQuietly(new File(imgFilePath));
		}

		return -1;


	}
}
