
package org.springblade.common.utils;

import org.springblade.core.tool.utils.Base64Util;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class CommonUtil {

	public static String imageToBase64(File file) {
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64Utils.encodeToString(Objects.requireNonNull(data));
	}

	public static MultipartFile base64ToMultipart(String base64) {
		try {
			byte[] b = null;
			b = Base64Util.decodeFromString(base64);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			return  bytesToMultipart(b);
		} catch (Exception ex) {

		}
		return null;
	}

	public static MultipartFile bytesToMultipart(byte[] bytes) {
		if(bytes == null){
			throw new NullPointerException("bytes is null");
		}

		try {
			return new BASE64DecodedMultipartFile(bytes,"data:image/png");
		} catch (Exception ex) {

		}
		return null;
	}


}
