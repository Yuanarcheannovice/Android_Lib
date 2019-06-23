package com.archeanx.lib.appupdate;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;


/**
 * @author xz
 * 通知栏下载文件广播
 */

public class AppUpdateReceiver extends BroadcastReceiver {
    /**
     * 下载id
     */
    private long mDownloadId;
    /**
     * 下载文件保存地址
     */
    private String fileUrl;

    private DownloadManager mDownloadManager;


    public void setFileUrl(String apkPath) {
        this.fileUrl = apkPath;
    }

    public void setDownloadId(long downloadId) {
        this.mDownloadId = downloadId;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //点击通知栏取消下载
        if (TextUtils.equals(intent.getAction(), DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            mDownloadManager.remove(mDownloadId);
        }

        //先判断下载 通知id
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (completeDownloadId != mDownloadId) {
            return;
        }
        //是否是下载完成(包括停止下载)
        if (TextUtils.equals(intent.getAction(), DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            //查询下载状态
            Cursor c = mDownloadManager.query(new DownloadManager.Query().setFilterById(mDownloadId));
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    //下载成功，安装apk
                    installApk(context, new File(fileUrl));
                } else if (status == DownloadManager.STATUS_FAILED) {
                    Toast.makeText(context.getApplicationContext(), "下载错误，更新App失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * 安装APK文件
     */
    private void installApk(Context context, File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 通过Intent安装APK文件
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //获取文件后缀
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        intent.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase()));
        context.startActivity(intent);
        mDownloadManager.remove(mDownloadId);
    }
}
