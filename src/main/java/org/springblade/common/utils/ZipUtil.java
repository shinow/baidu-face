package org.springblade.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.springframework.util.FileCopyUtils.BUFFER_SIZE;

/**
 * Create by byl
 *
 * @version 1.0.0
 * @Author byl
 * @date 2019/11/4 18:41
 * @Description desc:
 */
public class ZipUtil {
    /**
     * zip解压
     *
     * @param srcFile     zip源文件
     * @param destDirPath 解压后的目标文件夹
     * @throws RuntimeException 解压失败会抛出运行时异常
     */
    public static void unZip(File srcFile, String destDirPath) throws RuntimeException {
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("gbk"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    String substring = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
                    System.out.println("解压 : " + substring);
                    File targetFile = new File(destDirPath + "/" + substring);
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("解压完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除文件和目录
    public static void clearFiles(String workspaceRootPath) {
        File file = new File(workspaceRootPath);
        if (file.exists()) {
            deleteFile(file,workspaceRootPath);
        }
    }

    private static void deleteFile(File file,String workspaceRootPath) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i],workspaceRootPath);
            }
        }
        if(!file.getPath().substring(1).equals(workspaceRootPath.substring(1,workspaceRootPath.length()-1))) {
            file.delete();
        }
    }



	public static void zipPic(File file)throws Exception{
		/**
		 * 压缩
		 *
		 * 大于 1 M       1 * 1024 * 1 * 1024
		 * 大于 600kb     1 * 1024 * 1 * 600
		 * 图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
		 *
		 */
		float outputQuality = 0.5f;

		long fileLength = file.length();
		if( fileLength > 1 * 1024 * 1 * 600 &&  fileLength <  1 * 1024 * 1 * 1024){
			// 600kb 到 1M
			outputQuality = 0.5f;
		}else if(fileLength >= 1 * 1024 * 1 * 1024  &&  fileLength <  1 * 1024 * 2 * 1024){
			// 1m  到 2m
			outputQuality = 0.4f;
		}else if(fileLength >= 1 * 1024 * 2 * 1024  &&  fileLength <  1 * 1024 * 4 * 1024){
			// 2m  到 4m
			outputQuality = 0.2f;
		}else if(fileLength >= 1 * 1024 * 4 * 1024){
			// 大于4m
			outputQuality = 0.1f;
		}

		if(fileLength > 1 * 1024 * 1 * 600 ){
			Thumbnails.of(file).scale(1f).outputQuality(outputQuality).toFile(file);
		}

	}



	public static byte[] zipBytes(byte[] bytes)throws Exception{

    	if(bytes ==null){
    		throw new NullPointerException("bytes is null");
		}

    	byte[] retBytes = null;

		/**
		 * 压缩
		 *
		 * 大于 1 M       1 * 1024 * 1 * 1024
		 * 大于 600kb     1 * 1024 * 1 * 600
		 * 图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
		 *
		 */
		float outputQuality = 0.5f;

		long fileLength = bytes.length;

		if( fileLength > 1 * 1024 * 1 * 600 &&  fileLength <  1 * 1024 * 1 * 1024){
			// 600kb 到 1M
			outputQuality = 0.5f;
		}else if(fileLength >= 1 * 1024 * 1 * 1024  &&  fileLength <  1 * 1024 * 2 * 1024){
			// 1m  到 2m
			outputQuality = 0.4f;
		}else if(fileLength >= 1 * 1024 * 2 * 1024  &&  fileLength <  1 * 1024 * 4 * 1024){
			// 2m  到 4m
			outputQuality = 0.2f;
		}else if(fileLength >= 1 * 1024 * 4 * 1024){
			// 大于4m
			outputQuality = 0.1f;
		}

		retBytes = compressPhotoByQuality(bytes,outputQuality,600);

		return retBytes;

	}

	public static byte[] compressPhotoByQuality(byte[] bytes,Float quality,long maxKb) throws IOException{
		if(bytes == null){
			return bytes;
		}
		//logger.info("开始按质量压缩图片({}kb)。",bytes.length/1024);
		// 如果配置的1，则不再处理,多说无益
		if(quality == 1){
			//logger.info("quality=1,不执行压缩。");
			return bytes;
		}
		// 满足目标kb值，则返回
		long fileSize = bytes.length;
		if (fileSize <= maxKb * 1024) {
			//logger.info("图片文件{}kb<={}kb,不再压缩质量。",fileSize/1024,maxkb);
			return bytes;
		}
		// Closing a <tt>ByteArrayOutputStream</tt> has no effect.   因此无需close
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		BufferedImage bim = ImageIO.read(byteArrayInputStream);


		int imgWidth = bim.getWidth();
		int imgHeight = bim.getHeight();
		// 如果不处理size,只用quality,可能导致一致压缩不到目标值，一致递归在当前方法中！！
		int desWidth = new BigDecimal(imgWidth).multiply(new BigDecimal(0.7)).intValue();
		int desHeight = new BigDecimal(imgHeight).multiply(new BigDecimal(0.7)).intValue();
		//logger.info("图片文将按照width={}*height={}进行压缩，画质quality={}。",desWidth,desHeight,quality);
		Thumbnails.of(new ByteArrayInputStream(bytes)).size(desWidth, desHeight).outputQuality(quality).outputFormat("png").toOutputStream(out);
		//递归
		byte[] nextBytes = out.toByteArray();

		byteArrayInputStream.close();
		out.close();

		return compressPhotoByQuality(nextBytes, quality, maxKb);
	}



}
