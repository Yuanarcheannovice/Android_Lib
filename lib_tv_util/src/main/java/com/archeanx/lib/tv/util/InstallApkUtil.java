package com.archeanx.lib.tv.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Created by xz on 2018/4/23.
 * apk安装工具类
 */

public class InstallApkUtil {

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (PackageInfo pi : pinfo) {
            if (pi.packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @return
     */
    public static boolean isSystemApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (PackageInfo pi : pinfo) {
            if (pi.packageName.equalsIgnoreCase(context.getPackageName())) {
                ApplicationInfo p = pi.applicationInfo;
                if ((p.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 安装/替换apk
     */
    public static void install(Context context, String packageName, String filePath) {
//        if (isSystemApp(context)) {
            install(packageName, filePath, null);
//        } else {
//            installApk(context, new File(filePath));
//        }
    }

    /**
     * 安装APK文件
     */
    private static void installApk(Context context, File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 通过Intent安装APK文件
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 安装/替换apk
     */
    public static void install(String packageName, String filePath) {
        install(packageName, filePath, null);
    }

    /**
     * 安装
     */
    public static void install(final String packageName, final String filePath, final OnApkInstallListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                if (filePath == null || filePath.length() == 0 || file == null) {
                    if (listener != null) {
                        listener.onInstall(false, "文件不存在或文件路径有问题");
                    }
                    return;
                }
                String[] args = {"pm", "install", "-r", filePath};
                ProcessBuilder processBuilder = new ProcessBuilder(args);
                Process process = null;
                BufferedReader successResult = null;
                BufferedReader errorResult = null;
                StringBuilder successMsg = new StringBuilder();
                StringBuilder errorMsg = new StringBuilder();
                try {
                    process = processBuilder.start();
                    successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String s;
                    while ((s = successResult.readLine()) != null) {
                        successMsg.append(s);
                    }
                    while ((s = errorResult.readLine()) != null) {
                        errorMsg.append(s);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (successResult != null) {
                            successResult.close();
                        }
                        if (errorResult != null) {
                            errorResult.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (process != null) {
                        process.destroy();
                    }
                }
                if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
                    if (listener != null) {
                        listener.onInstall(true, "安装成功");
                    }
                } else {
                    if (listener != null) {
                        listener.onInstall(false, "安装失败" + errorMsg.toString());
                    }
                }
            }
        }).start();

    }

    /**
     * 卸载
     */
    public static void uninstall(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            Method[] methods = pm != null ? pm.getClass().getDeclaredMethods() : null;
            Method mDel = null;
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    if (method.getName().toString().equals("deletePackage")) {
                        mDel = method;
                        break;
                    }
                }
            }
            if (mDel != null) {
                mDel.setAccessible(true);
                mDel.invoke(pm, packageName, null, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getAppVersion(Context context, String packageName) {
        String version = "";
        PackageManager pckMan = context.getPackageManager();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
        for (PackageInfo pInfo : packageInfo) {
            if (TextUtils.equals(packageName, pInfo.packageName)) {
                return pInfo.versionName;
            }


//            item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
//            item.put("packageName", pInfo.packageName);
//            item.put("versionCode", pInfo.versionCode);
//            item.put("versionName", pInfo.versionName);
//            item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
        }
        return version;
    }

    public interface OnApkInstallListener {
        void onInstall(boolean isInstall, String message);
    }

}
