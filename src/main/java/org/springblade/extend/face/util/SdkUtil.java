package org.springblade.extend.face.util;

import com.sun.jna.*;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class SdkUtil {

    private static Pointer BD_SDK;

    public static Pointer getBdSdk(){
        return BD_SDK;
    }


    /**
     * floats 转 BDFaceFeature 结构体 数据结构
     * @param bytes
     * @return
     */
    public static BaiDuFaceSDK.BDFaceFeature.ByReference bytesToFeature(float[] bytes){
        int length = bytes.length;

        int floatSize = Native.getNativeSize(Float.class);
        Pointer pointer = new Memory(length * floatSize);
        pointer.write(0,bytes,0,length);

        BaiDuFaceSDK.BDFaceFeature.ByReference features = new BaiDuFaceSDK.BDFaceFeature.ByReference();
        features.size = length;
        features.data = pointer;

        return features;
    }

    public static BaiDuFaceSDK.BDFaceFeature.ByReference bytesToFeature(byte[] bytes){
        float[] data = Base64Util.byteArrayToFloatArray(bytes);
        return bytesToFeature(data);
    }


    public static BaiDuFaceSDK.BDFaceFeature.ByReference featureStrToFaceFeature(String feature)throws Exception{
        byte[] bytes = Base64Util.base64ToBytes(feature);

        return bytesToFeature(bytes);
    }


    public static List<BaiDuFaceSDK.BDFaceFeature.ByReference> loadPicFeature(Mat matFaceData)throws Exception{
        BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();

        long addr = matFaceData.getNativeObjAddr();


        BaiDuFaceSDK.INSTANCE.bdface_feature_List_jackoakMat(faceFeatureList,new NativeLong(addr),BD_SDK);



        List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = SdkUtil.getBDFaceFeatureArray(faceFeatureList);

        faceFeatureList.read();
        faceFeatureList.features.read();

        return listFeature;
    }

    public static BaiDuFaceSDK.BDFaceFeature.ByReference loadPicFeatureSingle(String imgPath)throws Exception{
        List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = loadPicFeatureList(imgPath);
        if(listFeature.size() > 0){
            return listFeature.get(0);
        }
        return null;
    }


    public static List<BaiDuFaceSDK.BDFaceFeature.ByReference> loadPicFeatureList(String imgPath)throws Exception{
        BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();

		BaiDuFaceSDK.INSTANCE.bdface_feature_List_jackoak(faceFeatureList,imgPath,BD_SDK);

        faceFeatureList.read();
        faceFeatureList.features.read();

        return SdkUtil.getBDFaceFeatureArray(faceFeatureList);
    }


    public static float[] loadPicFeatureBytes(String imgPath)throws Exception{
        BaiDuFaceSDK.BDFaceFeature.ByReference features = loadPicFeatureSingle(imgPath);
        Pointer pointer = features.data;
        float[] featureBytes = pointer.getFloatArray(0,features.size);
        return featureBytes;
    }


    /**
     * 获取照片中的 特征值
     * @param imgPath 文件
     * @return
     * @throws Exception
     */
    public static String loadPicFeatureBase64(String imgPath) throws Exception{
        float[] featureBytes = loadPicFeatureBytes(imgPath);
        String featureBase64 = Base64Util.bytesToBase64(featureBytes);
        return featureBase64;
    }

    public static List<BaiDuFaceSDK.BDFaceFeature.ByReference> getBDFaceFeatureArray(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList){
        int num = featureList.num;

        if(num == 0){
            return null;
        }

        List<BaiDuFaceSDK.BDFaceFeature.ByReference> ret = new ArrayList<>();

        Structure[] featureArray =  featureList.features.toArray(num);

        for(Structure featureFor : featureArray){
            BaiDuFaceSDK.BDFaceFeature.ByReference bDFaceFeature = (BaiDuFaceSDK.BDFaceFeature.ByReference) featureFor;
            ret.add(bDFaceFeature);
        }

        return ret;
    }



    /**
     * 初始化sdk
     */
    public static boolean initSdk()throws Exception{



        log.info("init sdk starting");

        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;






        String basePath = System.getProperty("user.dir");
//        basePath = basePath.replace("\\","/");
        String licensePath = basePath + "/dll/license";
        String modelBasePath = basePath + "/dll/face-sdk-models";


        //加载opencv 库
        String openCvDllPath = basePath + "/dll/opencv_java430.dll";

        System.load(pathValidate(openCvDllPath));


        //加载license
        int init_license_jackoak = instance.init_license_jackoak(pathValidate(licensePath));
        if(init_license_jackoak < 0){
            throw new Exception("加载license,load license fail");
        }


        //创建sdk实例
        Pointer sdk = instance.bdface_create_instance_jackoak();
        if(sdk == null){
            throw new Exception("创建sdk实例,init sdk fail");
        }

        //加载 人脸检测能力
        String detectPath = modelBasePath + "/detect/detect_rgb-customized-pa-paddle.model.float32-0.0.6.1";
        int detect = instance.load_detect_ability_jackoak(pathValidate(detectPath), sdk);
        if(detect < 0){
            throw new Exception("加载 人脸检测能力,load face rec ablility fail");
        }

        //关键点能力加载
        String alignPath = modelBasePath + "/align/align_rgb-customized-pa-paddle.model.float32-6.4.0.2";
        int align = instance.load_align_ability_jackoak(pathValidate(alignPath),sdk);
        if(align < 0 ){
            throw new Exception("关键点能力加载 fail");
        }


        //抠图人脸能力加载
       /* int crop = instance.load_crop_face_ability(sdk);
        if(crop < 0){
            throw new Exception("抠图人脸能力加载 fail");
        }*/


        //加载特征点提取能力
        String featurePath = modelBasePath + "/feature/feature_live-mnasnet-pa-paddle.model.float32-2.0.81.1";
        int reature = instance.load_feature_ability_jackoak(pathValidate(featurePath),sdk);


        if(reature < 0){
            throw new Exception("加载特征点提取能力 fail");
        }

        log.info("init sdk success");

        BD_SDK = sdk;

        return true;

    }


    public static float bdface_feature_compare_jackoak(BaiDuFaceSDK.BDFaceFeature.ByReference featuresMain, BaiDuFaceSDK.BDFaceFeature.ByReference featuresTwo){
        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
        return instance.bdface_feature_compare_jackoak(featuresMain,featuresTwo,BD_SDK);
    }


    /**
     * 特征提取，
     * 多个人脸
     * @param featureList
     * @param addr
     * @return
     */
    public static  int bdface_feature_List_jackoakMat(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList, long addr){
        return  BaiDuFaceSDK.INSTANCE.bdface_feature_List_jackoakMat(featureList,new NativeLong(addr),BD_SDK);
    }



    public static int  bdface_feature_List_jackoakMat2(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList,  String base64){
        return  BaiDuFaceSDK.INSTANCE.bdface_feature_List_jackoakMat2(featureList,BD_SDK,base64);
    }


    public static int  bdface_feature_List_jackoak(BaiDuFaceSDK.BDFaceFeatureList.ByReference featureList,  String path){
        return  BaiDuFaceSDK.INSTANCE.bdface_feature_List_jackoak(featureList,path,BD_SDK);
    }


    private static String pathValidate(String inPath){
        return inPath;
    }

}
