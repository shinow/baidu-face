/*
package org.springblade.extend.face.util;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springblade.extend.face.entity.FaceData;
import org.springblade.extend.face.sdk.BaiDuFaceSDK;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Backup {


    public static BaiDuFaceSDK.BDFaceFeature.ByReference loadPicFeature(String imgPath, BaiDuFaceSDK instance, Pointer sdk)throws Exception{
        BaiDuFaceSDK.BDFaceFeature.ByReference features = new BaiDuFaceSDK.BDFaceFeature.ByReference();
        instance.bdface_feature_jackoak(features,imgPath,sdk);
        return features;
    }


    public static float[] loadPicFeatureBytes(String imgPath,BaiDuFaceSDK instance,Pointer sdk)throws Exception{
        BaiDuFaceSDK.BDFaceFeature.ByReference features = loadPicFeature(imgPath,instance,sdk);
        Pointer pointer = features.data;

        float[] featureBytes = pointer.getFloatArray(0,features.size);
        return featureBytes;
    }


    */
/**
     * 获取照片中的 特征值
     * @param imgPath
     * @param instance
     * @param sdk
     * @return
     * @throws Exception
     *//*

    public static String loadPicFeatureBase64(String imgPath,BaiDuFaceSDK instance,Pointer sdk) throws Exception{
        float[] featureBytes = loadPicFeatureBytes(imgPath,instance,sdk);
        String featureBase64 = Base64Util.bytesToBase64(featureBytes);
        return featureBase64;
    }






    */
/**
     * 初始化sdk
     * @param instance
     * @return
     *//*

    public static Pointer initSdk(BaiDuFaceSDK instance)throws Exception{
        log.info("init sdk starting");

        String basePath = System.getProperty("user.dir");
        basePath = basePath.replace("\\","/");
        String licensePath = basePath + "/dll/license";
        String modelBasePath = basePath + "/dll/face-sdk-models";


        //加载license
        int init_license_jackoak = instance.init_license_jackoak(licensePath);
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
        int detect = instance.load_detect_ability_jackoak(detectPath, sdk);
        if(detect < 0){
            throw new Exception("加载 人脸检测能力,load face rec ablility fail");
        }

        //关键点能力加载
        String alignPath = modelBasePath + "/align/align_rgb-customized-pa-paddle.model.float32-6.4.0.2";
        int align = instance.load_align_ability_jackoak(alignPath,sdk);
        if(align < 0 ){
            throw new Exception("关键点能力加载 fail");
        }

        String featurePath = modelBasePath + "/feature/feature_live-mnasnet-pa-paddle.model.float32-2.0.81.1";
        int reature = instance.load_feature_ability_jackoak(featurePath,sdk);


        if(reature < 0){
            throw new Exception("加载特征点提取能力 fail");
        }

        log.info("init sdk success");
        return sdk;
    }

    */
/**
     * floats 转 BDFaceFeature 结构体 数据结构
     * @param bytes
     * @return
     *//*

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


    */
/**
     * base64 特征值比对
     * @param args
     * @throws Exception
     *//*

    public static void main123(String[] args) throws Exception{


        //阀值
        int picSize = 10000;

        String baseMain = "EBF4MhXIKQmaHAwanLO8hgHvGMoEDexX97GHrwzvcekxBGSdNNezDEeAwuBDl9aYoFVm1CcSH3QoP1vLLDLkO1BDT/BUs02PWEekT91lq4nBZF3VROfhTUjfEK/MPWHc8A5CnYvMtPiH23GKfWX4NZ/SF5ZkjvYyafobQWyV582QiH87lIT25xqAZ4YcPDBhAN2vpoSJ67EIMbqkjFJZTbBZ18m1VTThud82VT57juWi/ZUhpc9pFamAjtusmj96UdHB0dSTegBZejPI3GCxiEESs0VFfNNbSCv3aEzu92DwjEnydMpNi3jyQYJ8Ln5I4fNjVmR9+BBoWVV17MldeZD6s3iUjmPzmQZe6J7nbd6BIgYzhHhw1QgLLl8MEl6osRD7rzUCO4q54kUlvEBsuaJF2WikjywKK7+LSi2cZr7QBalE1PBEfdmSCffeBzmfwcOCjUVmSQPJWHDtzF6gQ3JKnHT3bp6JeBTG/3yxwYXgoDgBZgBYaul40/rsUKzMEANEvRWQoBuZ/mDlHGr2wgGmDRsEWfRZiWzVJYw4GNAwQCRvtYZ7DTgzvTg8YM4koBrVBaSJStWpWqvlraGoqVH9/7bVQXSs3YbuHlzg6pXA9NHUxPgP4koSIDRM7aMocMnBA3RUEzL4ff5dfNQ76GCCqm3kMTHwabriJG2S0+U=";
        byte[] bytesMain = Base64Util.base64ToBytes(baseMain);


        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
        Pointer sdk =  initSdk(instance);

        //特征库准备
        List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureList = new ArrayList<>();
        for(int i = 0; i < picSize; i++){
            BaiDuFaceSDK.BDFaceFeature.ByReference feature = bytesToFeature(bytesMain);
            featureList.add(feature);
        }


        //开始
        long start = System.currentTimeMillis();
        String imgPath1 = "F:\\aideeper\\workspace\\core\\project\\baidu-face\\blade-extend-face-baidu\\dll\\images\\22.png";
        BaiDuFaceSDK.BDFaceFeature.ByReference feature2 = loadPicFeature(imgPath1,instance,sdk);


        float score = 0;
        for(BaiDuFaceSDK.BDFaceFeature.ByReference featureFor : featureList){
            score = instance.bdface_feature_compare_jackoak(featureFor,feature2,sdk);
        }


        log.info("score-------------------------jackoak----------------------: " + score);

        long end = System.currentTimeMillis();
        long spend = end - start;

        //结束
        log.info("底库大小: " + picSize + ",spend time : " + spend + " ms");

    }


    */
/**
     * 图片比对
     * @param args
     * @throws Exception
     *//*

    public static void main2(String[] args) throws Exception{
        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
        Pointer sdk =  initSdk(instance);
        String imgPath1 = "F:\\aideeper\\workspace\\core\\project\\baidu-face\\blade-extend-face-baidu\\dll\\images\\a2.png";

        BaiDuFaceSDK.BDFaceFeature.ByReference feature1  = loadPicFeature(imgPath1,instance,sdk);
        BaiDuFaceSDK.BDFaceFeature.ByReference feature2  = loadPicFeature(imgPath1,instance,sdk);

        float score = instance.bdface_feature_compare_jackoak(feature1,feature2,sdk);

        log.info("score-------------------------jackoak----------------------: " + score);
    }




    */
/**
     * 图片 提取 base64特征值
     * @param args
     * @throws Exception
     *//*

    public static void main22(String[] args) throws Exception{
        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
        Pointer sdk =  initSdk(instance);
        String imgPath1 = "F:\\aideeper\\workspace\\core\\project\\baidu-face\\blade-extend-face-baidu\\dll\\images\\22.png";

        String featureBase64  = loadPicFeatureBase64(imgPath1,instance,sdk);
        log.info("featureBase64: " + featureBase64);
    }


    */
/**
     * openCv 显示图片
     * @param args
     * @throws Exception
     *//*

    public static void main111(String[] args) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("opencv = " + Core.VERSION);

        String imgPath = "F:\\aideeper\\workspace\\core\\project\\baidu-face\\blade-extend-face-baidu\\dll\\images\\22.png";


        Mat mat = Imgcodecs.imread(imgPath);


        byte[] data = new byte[1024];
        mat.get(0,0,data);

        OpenCvUtil.showPic(mat);
    }




    public static void main111111(String[] args) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("opencv = " + Core.VERSION);

        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
        Pointer sdk =  initSdk(instance);

        String file = "rtsp://admin:jackoak123456@192.168.3.127/LiveMedia/ch1/Media1";

        ExecutorService executorService = Executors.newCachedThreadPool();
        Mat mat = new Mat();

        FaceData data = new FaceData();

        executorService.execute(()->{

            while(true){
                if(data == null) {
                    continue;
                }
                byte[] bytes = data.getData();
                if(bytes == null){
                    continue;
                }


                long addr = mat.getNativeObjAddr();
                BaiDuFaceSDK.BDFaceFeature.ByReference features = new BaiDuFaceSDK.BDFaceFeature.ByReference();
                int matRet = instance.bdface_feature_jackoakMat(features,addr, sdk);

                log.info("mat ret : " + matRet);
                //mat.release();

                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        });




        VideoCapture vc = new VideoCapture();
        boolean isOpen = vc.open(file);

        if(!isOpen){
            log.error("videoCapture is close");
            return;
        }


        vc.read(mat);
        HighGui.imshow("test", mat);

        long addr = mat.getNativeObjAddr();

        BaiDuFaceSDK.BDFaceFeature.ByReference features = new BaiDuFaceSDK.BDFaceFeature.ByReference();
        instance.bdface_feature_jackoakMat(features,addr, sdk);

        Pointer pointer = features.data;
        float[] featureBytes = pointer.getFloatArray(0,features.size);


        HighGui.waitKey();


    }
}
*/
