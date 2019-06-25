package com.archeanx.lib.appupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * @author xz
 * app更新
 */
public class AppUpdateUtil {

    /**
     * 安装APK文件
     *
     * @param context context
     */
    public static boolean inspectInstallApk(Context context) {
        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 判断是否有权限
            boolean haveInstallPermission = context.getApplicationContext().getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //权限没有打开则提示用户去手动打开,开启当前应用的权限管理页
                Uri packageUri = Uri.parse("package:" + context.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(context.getApplicationContext(), "请打开安装应用权限才能更新安装App", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    /**
     * 安装apk
     *
     * @param context context
     * @param file    文件
     */
    public static void installApk(Context context, File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (!inspectInstallApk(context)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, "com.archeanx.lib.appupdate.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        }
        context.startActivity(intent);
    }

}
