package org.springblade.extend.face.util;

import org.springblade.core.tool.utils.StringUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Base64;

public class Base64Util {


    public static byte[] floatArrayToByteArray(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(4 * floats.length);
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(floats);
        return buffer.array();
    }


    public static float[] byteArrayToFloatArray(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        FloatBuffer fb = buffer.asFloatBuffer();
        float[] floatArray = new float[fb.limit()];
        fb.get(floatArray);
        return floatArray;
    }

    public static String bytesToBase64(float[] bytes)throws Exception{
        if(bytes == null){
            throw new NullPointerException();
        }

        String base64Str = Base64.getEncoder().encodeToString(floatArrayToByteArray(bytes));
        return base64Str;
    }

    public static String bytesToBase64Bytes(byte[] bytes)throws Exception{
        if(bytes == null){
            throw new NullPointerException();
        }

        String base64Str = Base64.getEncoder().encodeToString(bytes);
        return base64Str;
    }

    public static byte[] base64ToBytes(String featureStr)throws Exception{

        if(StringUtil.isEmpty(featureStr)){
            throw new NullPointerException("featureStr is null");
        }

        byte[] bytes = Base64.getDecoder().decode(featureStr);
        return bytes;
    }


}
