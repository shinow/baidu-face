package org.springblade.modules.bdface.service;

import org.opencv.core.Mat;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;

import java.util.List;

public interface IBdInter {

	/**
	 * 初始化 sdk
	 * @return
	 */
	boolean initSdk()throws Exception;


	/**
	 * 获取图片中的 人脸特征值
	 * @param imgPath
	 * @return
	 */
	String loadPicFeatureBase64(String imgPath)throws Exception;


	/**
	 * 特征值转 特征值结构体
	 * @param feature
	 * @return
	 * @throws Exception
	 */
	BaiDuFaceSDK.BDFaceFeature.ByReference featureStrToFaceFeature(String feature)throws Exception;



	/**
	 * 特征提取，
	 * 多个人脸
	 * @param featureList
	 * @param addr
	 * @return
	 */
	int bdface_feature_List_jackoakMat(BaiDuFaceSDK.BDFaceFeatureList.ByReference  featureList,long addr);


	int  bdface_feature_List_jackoak(String path,BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList );


	int bdface_feature_List_jackoakMat2(BaiDuFaceSDK.BDFaceFeatureList.ByReference  featureList,String base64);



	/**
	 * 比对相似度  推荐 85%
	 *
	 * 百度算法 推荐 85%
	 * @param featuresMain
	 * @param featuresTwo
	 * @return
	 */
	float bdface_feature_compare_jackoak(BaiDuFaceSDK.BDFaceFeature.ByReference featuresMain, BaiDuFaceSDK.BDFaceFeature.ByReference featuresTwo);




}
