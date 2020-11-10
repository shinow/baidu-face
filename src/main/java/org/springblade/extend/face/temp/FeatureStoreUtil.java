package org.springblade.extend.face.temp;

import org.springblade.extend.face.sdk.BaiDuFaceSDK;
import org.springblade.extend.face.util.Base64Util;
import org.springblade.extend.face.util.SdkUtil;

import java.util.ArrayList;
import java.util.List;

public class FeatureStoreUtil {


    public static List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureStore(int picSize)throws Exception{
        //特征库准备

        String baseMain = "EBF4MhXIKQmaHAwanLO8hgHvGMoEDexX97GHrwzvcekxBGSdNNezDEeAwuBDl9aYoFVm1CcSH3QoP1vLLDLkO1BDT/BUs02PWEekT91lq4nBZF3VROfhTUjfEK/MPWHc8A5CnYvMtPiH23GKfWX4NZ/SF5ZkjvYyafobQWyV582QiH87lIT25xqAZ4YcPDBhAN2vpoSJ67EIMbqkjFJZTbBZ18m1VTThud82VT57juWi/ZUhpc9pFamAjtusmj96UdHB0dSTegBZejPI3GCxiEESs0VFfNNbSCv3aEzu92DwjEnydMpNi3jyQYJ8Ln5I4fNjVmR9+BBoWVV17MldeZD6s3iUjmPzmQZe6J7nbd6BIgYzhHhw1QgLLl8MEl6osRD7rzUCO4q54kUlvEBsuaJF2WikjywKK7+LSi2cZr7QBalE1PBEfdmSCffeBzmfwcOCjUVmSQPJWHDtzF6gQ3JKnHT3bp6JeBTG/3yxwYXgoDgBZgBYaul40/rsUKzMEANEvRWQoBuZ/mDlHGr2wgGmDRsEWfRZiWzVJYw4GNAwQCRvtYZ7DTgzvTg8YM4koBrVBaSJStWpWqvlraGoqVH9/7bVQXSs3YbuHlzg6pXA9NHUxPgP4koSIDRM7aMocMnBA3RUEzL4ff5dfNQ76GCCqm3kMTHwabriJG2S0+U=";
        byte[] bytesMain = Base64Util.base64ToBytes(baseMain);

        List<BaiDuFaceSDK.BDFaceFeature.ByReference> featureList = new ArrayList<>();
        for(int i = 0; i < picSize; i++){
            BaiDuFaceSDK.BDFaceFeature.ByReference feature = SdkUtil.bytesToFeature(bytesMain);
            featureList.add(feature);
        }

        return featureList;
    }

}
