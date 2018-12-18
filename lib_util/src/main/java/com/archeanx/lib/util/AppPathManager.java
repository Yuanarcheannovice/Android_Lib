package com.archeanx.lib.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;


/**
 * app的文件处理
 * 修改备注:所有的安卓开发者都应该按照这个方向走，不要再随意地再SD卡里建立各式各样的文件夹， 而应该将所有应用数据都在/Android/data/内进行读写
 * 统一的规范并不违背开源的初衷，
 * 甚至有利于整个生态圈的有序发展。
 *
 * @author xz
 */
public class AppPathManager {
    private static int i = 0;
    /**
     * 更新包Apk路径
     */
    public static final String APP_UPDATE = "/sdcard/dj_update/";
    /**
     * pdf下载保存路径
     */
    public static final String APP_PDF_URL = "/sdcard/dj_pdf/";

    /**
     * 初始话文件管理类
     */
    public static void initPathManager(Context context) {
        //设置app默认文件路径
        AppPathManager.setAppPath(context);
        ifFolderExit(APP_UPDATE);
        ifFolderExit(APP_PDF_URL);
    }

    /**
     * 方法说明:返回多个sd卡的该应用私有数据区的files目录
     * 方法名称:getExternalRootFilesCachePath
     * return：/storage/sdcard0 or sdcard1/Android/data/<包名>/files
     * 返回值:StringBuffer
     */
    public static StringBuffer getExternalRootFilesCachePath(Context context) {
        if (context.getExternalCacheDir() == null) {
            return new StringBuffer(context.getCacheDir()
                    .getAbsolutePath()).append("/");
        }
        return new StringBuffer(context.getExternalFilesDir(
                Environment.MEDIA_MOUNTED).getAbsolutePath()).append("/");
    }

    /**
     * 方法说明:返回多个sd卡下该应用私有数据库的缓存目录
     * 方法名称:getExternalRootCachePath
     * return：/storage/sdcard0 or sdcard1/Android/data/<包名>/caches
     * 返回值:StringBuffer
     */
    public static StringBuffer getExternalRootCachePath(Context context) {
        if (context.getExternalCacheDir() == null) {
            return new StringBuffer(context.getCacheDir()
                    .getAbsolutePath()).append("/");
        }
        return new StringBuffer(context.getExternalCacheDir()
                .getAbsolutePath()).append("/");
    }


    /**
     * 设置app的初始目录
     */
    private static void setAppPath(Context context) {
        File fileAppPath = null;
        try {
            fileAppPath = new File(getExternalRootFilesCachePath(context).toString());
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
            fileAppPath = new File(getExternalRootCachePath(context).toString());
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * return
     * 方法说明:清除缓存
     * 方法名称:ManualClearCache
     * 返回值:boolean
     */
    public static void ManualClearCache(Context context) {
        File file = new File(getExternalRootCachePath(context).toString());
        DeleteFileAndDir(file);
    }

    /**
     * 方法说明:清除appfiles
     * 方法名称:ManualClearFiles
     * 返回值:void
     */
    public static void ManualClearFiles(Context context) {
        File file = new File(getExternalRootFilesCachePath(context).toString());
        DeleteFileAndDir(file);
    }

    /**
     * 方法说明:递归删除文件和文件夹
     * 方法名称:DeleteFileAndDir
     * 返回值:void
     */
    public static void DeleteFileAndDir(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFileAndDir(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 方法说明:递归删除文件
     * 方法名称:DeleteFile
     * 返回值:void
     */
    public static void DeleteFileNoDir(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFileAndDir(f);
                }
                file.delete();
            }
        }

    }


    /**
     * 判断文件夹是否存在，
     * 不存在则创建
     * <p>
     * return
     */
    public static Boolean ifFolderExit(String filePath) {
        File fileAppPath;
        try {
            fileAppPath = new File(filePath);
            if (!fileAppPath.exists()) {
                fileAppPath.mkdirs();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static String getAppDownloadPath() {
        return APP_UPDATE;
    }

    public static String getAppPdfUrl() {
        return APP_PDF_URL;
    }

    /**
     * Launcher网页路径
     */
    public static String getSdLauncherHtmlUrl() {
        //  return  getExternalRootFilesCachePath().append(LAUNCHER_HTML_URL).append("www/index.html").toString();
        return "/sdcard/dj_rc/www/index.html";
    }

    /**
     * 更新文件路径
     */
    public static String getLauncherUpdateUrl() {
        return "/sdcard/dj_rc/www/UpdateLauncher.json";
    }


}
