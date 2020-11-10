package org.springblade.extend.face.sdk;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SdkPath {

    //public static String dllBasePath = System.getProperty("user.dir");

    /*static  {

        String standSe = File.separator;

        log.info("File.separator: " + File.separator);

        if(standSe.equals("\\")){
            dllBasePath = dllBasePath.replace("/","\\");
        }

        if(standSe.equals("/")){
            dllBasePath = dllBasePath.replace("\\","/");
        }

        System.out.println("initDllBasePath: " + dllBasePath);
    }*/

    public static String dllBasePath = System.getProperty("user.dir");

    static  {
        dllBasePath = dllBasePath.replace("\\","/");
        System.out.println("initDllBasePath: " + dllBasePath);
    }

}
