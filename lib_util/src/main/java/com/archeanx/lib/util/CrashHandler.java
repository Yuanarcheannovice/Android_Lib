package com.archeanx.lib.util;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by xcoder_xz on 2017/1/1 0001.
 * 处理app的一些异常，并记录上传到特定的服务器
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();


    private FileOutputStream fs;
    private BufferedOutputStream bos;


    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public CrashHandler() {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, final Throwable e) {
//        if ( mDefaultHandler != null) {
//            //把错误收集保存到SD卡中
//            //如果用户没有处理则让系统默认的异常处理器来处理
//        } else {
        handleException(e);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Log.e("appErr", e.getMessage());
                e.printStackTrace();
                Looper.loop();
            }
        }.start();

        try {
            //睡眠3秒后，关闭app
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            mDefaultHandler.uncaughtException(t, e);
        }
        mDefaultHandler.uncaughtException(t, e);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private Boolean handleException(Throwable ex) {
//        if (ex == null) {
//            return false;
//        }
//        try {
//            //判断路径有没有,如果没有就创建
//            File dir = new File(getSaveLogPath());
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            File file = new File(dir, getLogName());
//            //写入log，加上版本号，和app名称
//            //得到版本号
//            PackageManager manager = App.sAppContext.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(App.sAppContext.getPackageName(), 0);
//            String appVersionName = info.versionName; // 版本名
//            int currentVersionCode = info.versionCode; // 版本号
//            String release = Build.VERSION.RELEASE;//获取版本名
//            String model = Build.MODEL;//获取手机型号
//
//            AppErrMoudle appErrMoudle = new AppErrMoudle();
//            appErrMoudle.setVersionName(appVersionName);
//            appErrMoudle.setVersionCode(currentVersionCode + "");
//            appErrMoudle.setErrPhone("手机型号" + model + "-" + "手机版本号" + release);
//            appErrMoudle.setErrNewTime(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));
//            //写入错误信息
//            StringBuffer sb = new StringBuffer();
//            StackTraceElement[] elements = ex.getStackTrace();
//            for (StackTraceElement ste : elements) {
//                sb.append(ste.toString() + "\r\n");
//            }
//            appErrMoudle.setErrContent("错误信息:" + ex.getMessage() + "-" + "错误详细:" + sb.toString());
//            String appErr = GsonUtil.objectToJsonString(appErrMoudle);
//
//            fs = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fs);
//            bos.write(appErr.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bos.flush();
//                bos.close();
//                fs.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return true;
    }


//    /**
//     * @return
//     * @方法说明：生成错误log文件
//     * @方法名称：getSaveLogPath()
//     * @返回值：String
//     */
//    public String getSaveLogPath() {
//        AppPathManager.ifFolderExit(AppPathManager.getErrorLogPath().toString());
//        return AppPathManager.getErrorLogPath().toString();
//    }

    //得到图片名
    public String getLogName() {
           return DateTime.formatDate(new Date(), DateTime.GROUP_BY_EACH_DAYSM) + ".txt";
    }


}
