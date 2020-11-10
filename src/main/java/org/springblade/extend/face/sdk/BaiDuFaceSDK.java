package org.springblade.extend.face.sdk;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.util.Arrays;
import java.util.List;

public interface BaiDuFaceSDK extends Library {



    String windowsSdkPath = SdkPath.dllBasePath + "\\dll\\bd-face-sdk";
    //String linuxSdkPath = "暂无";
    BaiDuFaceSDK INSTANCE = Native.loadLibrary( windowsSdkPath, BaiDuFaceSDK.class);
    /**
     * 加载 license
     * @param licensePath
     * @return
     */
    int init_license_jackoak(String licensePath);


    /**
     * 创建sdk实例对象
     * @return
     */
    Pointer  bdface_create_instance_jackoak();


    /**
     * 人脸检测能力加载
     * @param path
     * @param instance
     *
     *
     * const std::string detect_rgb_model_path =
     *  "C:/Users/admin/Desktop/baidu/face-sdk-models/detect/detect_rgb-customized-pa-paddle.model.float32-0.0.6.1";
     *
     * @return
     */
    int load_detect_ability_jackoak(String path,Pointer instance);


    /**
     * 关键点能力加载
     */
    int load_align_ability_jackoak(String path,Pointer instance);


    /**
     * 抠图人脸能力加载
     * @param instance
     * @return
     */
    int load_crop_face_ability(Pointer instance);


    /**
     * 加载特征点提取能力
     * @param path
     * @param instance
     * @return
     */
    int load_feature_ability_jackoak(String path,Pointer instance);



    /**
     * 特征提取
     * @param featureList  多个人脸特征
     * @param path
     * @param instance
     * @return
     */
    int bdface_feature_List_jackoak(BDFaceFeatureList.ByReference featureList, String path, Pointer instance);


    /**
     * 特征提取，
     * 多个人脸
     * @param featureList
     * @param addr
     * @param instance
     * @return
     */
    int  bdface_feature_List_jackoakMat(BDFaceFeatureList.ByReference featureList, NativeLong addr, Pointer instance);

    //int  bdface_feature_List_jackoakMat2(bdface::BDFaceFeatureList* featureList,  bdface::BDFaceInstance face_instance,int height,int width,unsigned char* bytes,int dataLength);
    int  bdface_feature_List_jackoakMat2(BDFaceFeatureList.ByReference featureList, Pointer instance,String base64);



    /**
     * 特征值比对 百分制
     * 最大 100
     * 80可认为同一人
     * @return
     */
    float bdface_feature_compare_jackoak(BDFaceFeature.ByReference featuresMain, BDFaceFeature.ByReference featuresTwo, Pointer instance);



    /* struct BDFaceFeature {
        int size;               // feature维度
        float* data;            // feature的数据指针
    };*/
     class BDFaceFeature extends Structure
    {
        public int size;
        public Pointer data;

        public BDFaceFeature(){
            this.setAlignType(ALIGN_DEFAULT);
        }

        public BDFaceFeature(Pointer pointer){
            super(pointer);
        }


        public static class ByReference extends BDFaceFeature implements Structure.ByReference
        {

            public ByReference( ){
            }
        }



        public static class ByValue extends BDFaceFeature implements Structure.ByValue
        {
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("size","data");
        }
    }


    /**
     * @brief   人脸feature数据列表
     */
    /*struct BDFaceFeatureList {
        int num;                    // feature个数
        BDFaceFeature* features;    // feature数据指针
    };*/


    /**
     * static int	ALIGN_DEFAULT
     * 使用平台默认对齐方式。
     * static int	ALIGN_GNUC
     * 经过32位x86 linux / gcc验证; 对齐字段大小，最大4个字节
     * static int	ALIGN_MSVC
     * 经过w32 / msvc验证；根据字段大小对齐
     * static int	ALIGN_DEFAULT
     * 不对齐，将所有字段放在最近的1字节边界上
     */
    class BDFaceFeatureList extends Structure
    {
        public int num;
        public BDFaceFeature.ByReference features = new BDFaceFeature.ByReference();

        public BDFaceFeatureList(){
            this.setAlignType(ALIGN_DEFAULT);
        }


        public static class ByReference extends BDFaceFeatureList implements Structure.ByReference
        {
            public ByReference()
            {
            }
        }

        public static class ByValue extends BDFaceFeatureList implements Structure.ByValue
        {
            public ByValue()
            {
            }
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("num","features");
        }
    }
















   /* struct sonStruct {
        int size;               // feature维度
        float* data;            // feature的数据指针
    };*/

   class sonStruct extends Structure{
       public int size;               // feature维度
       public IntByReference data = new IntByReference();            // feature的数据指针

       public sonStruct(){
           this.setAlignType(ALIGN_DEFAULT);
       }


       public static class ByReference extends sonStruct implements Structure.ByReference
       {
       }

       public static class ByValue extends sonStruct implements Structure.ByValue
       {
       }

       @Override
       protected List<String> getFieldOrder() {
           return Arrays.asList("size","data");
       }
   }



    class MyStruct extends Structure{
        public NativeLong size;

        public Pointer data;

        public static class ByReference extends MyStruct implements Structure.ByReference{
        }
        public static class ByValue extends MyStruct implements Structure.ByValue{
        }


        public MyStruct(){
            this.setAlignType(ALIGN_DEFAULT);
        }



        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("size","data");
        }
    }

    int addNormal(MyStruct.ByReference myStruct);

    void addPrt(MyStruct.ByReference pms, IntByReference sum);

    void test(MyStruct.ByReference pms);
}
