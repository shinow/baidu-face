//package org.springblade.extend.face;
//
//
//import com.sun.jna.NativeLong;
//import com.sun.jna.Pointer;
//import lombok.extern.slf4j.Slf4j;
//import org.opencv.core.Mat;
//import org.opencv.videoio.VideoCapture;
//import org.springblade.extend.face.sdk.BaiDuFaceSDK;
//import org.springblade.extend.face.temp.FeatureStoreUtil;
//import org.springblade.extend.face.util.ImageUtil;
//import org.springblade.extend.face.util.OpenCvUtil;
//import org.springblade.extend.face.util.SdkUtil;
//
//import java.awt.image.BufferedImage;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//
//@Slf4j
//public class FaceApplication {
//
//
//
//    public static void main123456(String[] args) throws Exception{
//
//
//
//
//        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
//        SdkUtil.initSdk();
//        Pointer sdk =  SdkUtil.getBdSdk();
//
//        String file = "rtsp://admin:jackoak123456@192.168.3.127/LiveMedia/ch1/Media1";
//        //String file = "rtsp://admin:jackoak123456@192.168.3.118/LiveMedia/ch1/Media1";
//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        int threshold =  85;
//        int picSize = 10000;
//
//        List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureList = FeatureStoreUtil.featureStore(picSize);
//
//        LinkedList<Mat> stack = new LinkedList<>();
//
//        executorService.execute(()->{
//            Mat matFaceData = null;
//            BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();
//
//            while(true){
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                    synchronized (stack){
//                        if(stack.isEmpty()){
//                            continue;
//                        }
//                        matFaceData = stack.pop();
//                        if(matFaceData == null){
//                            continue;
//                        }
//
//                        long start = System.currentTimeMillis();
//                        long addr = matFaceData.getNativeObjAddr();
//                        int matRet = instance.bdface_feature_List_jackoakMat(faceFeatureList,new NativeLong(addr), sdk);
//
//                        if(matRet < 0){
////                            log.info("提取特征值失败");
//                            faceFeatureList.read();
//                            faceFeatureList.features.read();
//
//                            matFaceData.release();
//                            stack.addLast(matFaceData);
//                            continue;
//                        }
//
//
//                        List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = SdkUtil.getBDFaceFeatureArray(faceFeatureList);
//
//                        float score = 0;
//                        for(BaiDuFaceSDK.BDFaceFeature.ByReference featureMain : listFeature){
//                            for(BaiDuFaceSDK.BDFaceFeature.ByReference featureFor : featureList){
//                                score = instance.bdface_feature_compare_jackoak(featureFor,featureMain,sdk);
//                            }
//                            featureMain.read();
//                            featureMain = null;
//
//                        }
//                        matFaceData.release();
//                        stack.addLast(matFaceData);
//
//
//                        if(score >= threshold){
//                            log.info("score: -----------------jackoak----------------" + score);
//                            long end = System.currentTimeMillis();
//                            long spend = end - start;
//                            //结束
//                            log.info("底库大小: " + picSize + ",spend time : " + spend + " ms");
//                        }
//
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//
//
//        });
//
//
//
//
//        VideoCapture vc = new VideoCapture();
//        boolean isOpen = vc.open(file);
//
//        if(!isOpen){
//            log.error("videoCapture is close");
//            return;
//        }
//
//
//
//        Mat mat1 = new Mat();
//        Mat mat2 = new Mat();
//        stack.push(mat1);
//        stack.push(mat2);
//
//
//        Mat mat = null;
//
//        boolean readMat = false;
//        while (true){
//            try {
//                synchronized (stack){
//                    if(stack.isEmpty()){
//                        throw new NullPointerException();
//                    }
//
//                    mat = stack.removeLast();
//                    if(mat == null){
//                        throw new NullPointerException();
//                    }
//                    mat.release();
//                }
//
//                readMat = vc.read(mat);
//
//                synchronized (stack){
//                    if(!readMat){
//                        continue;
//                    }
//
//                    stack.push(mat);
//
//                   /* HighGui.imshow("test", mat);
//                    HighGui.waitKey(1);*/
//                }
//
//
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//        }
//
//
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//    public static void main(String[] args) throws Exception{
//        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
//        SdkUtil.initSdk();
//        Pointer sdk =  SdkUtil.getBdSdk();
//
//        int picSize = 5000;
//
//        List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureList = FeatureStoreUtil.featureStore(picSize);
//
////        String imgPath = "F:\\aideeper\\workspace\\core\\project\\baidu-face\\blade-extend-face-baidu\\dll\\images\\22.png";
//        //String imgPath = "http://dev.image.aideeper.com/baidu-face/upload/20200930/be1d3fc80417f71c6917aa2482752b47.jpg";
//        String imgPath = "C:\\Users\\admin\\Desktop\\aideeper\\face-img\\1.jpg";
//
//
//
//        BufferedImage bufferedImage = ImageUtil.readBufferImageFromPath(imgPath);
//        Mat mat = OpenCvUtil.bufferToMartix(bufferedImage);
//
//        BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();
//
//        int matRet = instance.bdface_feature_List_jackoakMat(faceFeatureList,new NativeLong(mat.getNativeObjAddr()), sdk);
//
//        if(matRet < 0){
//            log.info("提取特征值失败");
//            return;
//        }
//
//
//        List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = SdkUtil.getBDFaceFeatureArray(faceFeatureList);
//
//        float score = 0;
//        for(BaiDuFaceSDK.BDFaceFeature.ByReference featureMain : listFeature){
//            for(BaiDuFaceSDK.BDFaceFeature.ByReference featureFor : featureList){
//                score = instance.bdface_feature_compare_jackoak(featureFor,featureMain,sdk);
//            }
//        }
//
//
//        log.info("score: -----------------jackoak----------------" + score);
//
//
//    }
//
//
//    public static void main111111(String[] args) throws Exception{
//        BaiDuFaceSDK instance = BaiDuFaceSDK.INSTANCE;
//        SdkUtil.initSdk();
//        Pointer sdk =  SdkUtil.getBdSdk();
//
//        int picSize = 5000;
//
//        List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureList = FeatureStoreUtil.featureStore(picSize);
//
//        String imgPath = "C:\\Users\\admin\\Desktop\\aideeper\\face-img\\13071194151.jpg";
//
//
//
//
//
//        BaiDuFaceSDK.BDFaceFeatureList.ByReference  faceFeatureList = new BaiDuFaceSDK.BDFaceFeatureList.ByReference();
//
//        int matRet = instance.bdface_feature_List_jackoak(faceFeatureList,imgPath,sdk );
//
//        if(matRet < 0){
//            log.info("提取特征值失败");
//            return;
//        }
//
//
//        List<BaiDuFaceSDK.BDFaceFeature.ByReference> listFeature = SdkUtil.getBDFaceFeatureArray(faceFeatureList);
//
//        float score = 0;
//        for(BaiDuFaceSDK.BDFaceFeature.ByReference featureMain : listFeature){
//            for(BaiDuFaceSDK.BDFaceFeature.ByReference featureFor : featureList){
//                score = instance.bdface_feature_compare_jackoak(featureFor,featureMain,sdk);
//            }
//        }
//
//
//        log.info("score: -----------------jackoak----------------" + score);
//
//    }
//
//
//
//
//
//
//
//
//}
