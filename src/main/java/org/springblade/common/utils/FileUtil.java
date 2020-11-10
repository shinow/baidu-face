package org.springblade.common.utils;

import org.springblade.core.tool.utils.StringUtil;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {

	public static String bytesToFile(byte[] bytes){
		if (StringUtil.isEmpty(bytes)){
			throw new NullPointerException();
		}


		String FileName = System.currentTimeMillis() + ".png";
		String tempPath = getTempPath();
		String imgFilePath = tempPath + "/" + FileName;

		File baseTemp = new File(tempPath);
		if(!baseTemp.exists()){
			baseTemp.mkdir();
		}

		File outFile = new File(imgFilePath);


		OutputStream out = null;
		try
		{
			//Base64解码
			byte[] b = bytes;
			for(int i = 0; i< b.length; ++i)
			{
				if(b[i]<0)
				{//调整异常数据
					b[i] += 256;
				}
			}

			out = new FileOutputStream(outFile);
			out.write(b);
			out.flush();
			return imgFilePath;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imgFilePath;
	}


	//base64字符串转化成图片
	public static String base64ToFile(String base64){
		if (StringUtil.isEmpty(base64)){
			throw new NullPointerException();
		}

		BASE64Decoder decoder = new BASE64Decoder();
		//Base64解码
		byte[] bytes = new byte[0];
		try {
			bytes = decoder.decodeBuffer(base64);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytesToFile(bytes);

	}


	public static String getTempPath(){
		String basePath = System.getProperty("user.dir");
		basePath = basePath.replace("\\","/");
		basePath += "/tempImg";
		return basePath;
	}
}
