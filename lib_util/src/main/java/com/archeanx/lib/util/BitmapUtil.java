package com.archeanx.lib.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    private static final String SD_PATH = "/sdcard/jiahe/monitor/pic";

    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, String port) {
        File filePic;
        try {
            filePic = new File(SD_PATH+"/" + port + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }
}
