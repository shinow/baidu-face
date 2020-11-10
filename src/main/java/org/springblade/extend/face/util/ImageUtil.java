package org.springblade.extend.face.util;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtil {


    public static BufferedImage readBufferImageFromPath(String path) {

    	if(path == null){
    		throw new NullPointerException();
		}

        BufferedImage image = null;
        try {
            if(path.startsWith("http") || path.startsWith("https")){
                URL url = new URL(path);
                image = ImageIO.read(url);
            }else{
                image = ImageIO.read(new FileInputStream(new File(path)));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


}
