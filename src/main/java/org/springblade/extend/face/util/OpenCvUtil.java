package org.springblade.extend.face.util;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacv.FrameFilter;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springblade.core.tool.utils.StringUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpenCvUtil {

    /**
     * BufferImage转byte[]
     * @param original
     * @return
     */
    public static byte[] bufImg2Bytes(BufferedImage original){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(original, "png", bStream);
        } catch (IOException e) {
            throw new RuntimeException("bugImg读取失败:"+e.getMessage(),e);
        }
        return bStream.toByteArray();
    }

    /**
     * byte[]转BufferImage
     * @param imgBytes
     * @return
     */
    public static BufferedImage bytes2bufImg(byte[] imgBytes){
        BufferedImage tagImg = null;

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(imgBytes);
            tagImg = ImageIO.read(byteArrayInputStream);
            return tagImg;
        } catch (Exception e) {
            throw new RuntimeException("bugImg写入失败:"+e.getMessage(),e);
        }finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public static Mat bufferToMartix(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("image == null");
        }

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(image.getHeight(), image.getWidth(),  CvType.CV_8UC3);
        mat.put(0, 0, pixels);
        return mat;
    }


    public static Mat bytesToMartix(byte[] bytes){
        BufferedImage image = bytes2bufImg(bytes);
        return bufferToMartix(image);
    }





    public static byte[] Mat2Bytes(Mat matrix)throws Exception {
        if(matrix == null){
            throw new NullPointerException();
        }
        return Mat2Bytes(matrix,"pic.png");
    }
    public static byte[] Mat2Bytes(Mat matrix, String fileExtension)throws Exception {
        if(matrix == null){
            throw new NullPointerException();
        }

        if(StringUtil.isEmpty(fileExtension)){
            throw new NullPointerException();
        }


        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);

        byte[] bytes = mob.toArray();
		mob.release();

        return bytes;
    }


}
