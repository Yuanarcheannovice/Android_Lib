package com.archeanx.lib.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by DEV on 2018/4/20.
 */

public class FileUtil {

    /**
     * 初始化某个目录
     */
    public static File initFloder(String url) {
        File file = new File(url);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    public static String loadFromSDFile(File file) {
        String result = null;
        try {
            int length = (int) file.length();
            byte[] buff = new byte[length];
            FileInputStream fin = new FileInputStream(file);
            fin.read(buff);
            fin.close();
            result = new String(buff, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Assets复制文件到SD中
     *
     * @param context
     * @param assetsPath
     * @param sdPath
     */
    public static void copyAssetsToDst(Context context, String assetsPath, String sdPath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);
            if (fileNames.length > 0) {
                File file = new File(sdPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!assetsPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, assetsPath + File.separator + fileName, sdPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, sdPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(sdPath);
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
