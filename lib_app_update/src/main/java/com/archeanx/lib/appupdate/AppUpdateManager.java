package com.archeanx.lib.appupdate;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * @author xz
 * app更新工具
 */

public class AppUpdateManager {


    private String mApkName;

    private static class SingletonHolder {
        private static AppUpdateManager instance = new AppUpdateManager();
    }

    private AppUpdateManager() {

    }

    public static AppUpdateManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 下载id
     */
    private long mDownloadId;

    private Context mContext;

    /**
     * apk 存储路径
     */
    private String mSaveFilePath;

    /**
     * 注册广播，监听通知栏的下载完成，和点击事件
     */
    private AppUpdateReceiver mAppUpdateReceiver;

    /**
     * 下载时 通知栏标题
     */
    private String mDownloadTitle;
    /**
     * 点击下载时，下载状态监听
     */
    private OnAppUpdateStatusListener mOnAppUpdateStatusListener;

    public void setOnAppUpdateStatusListener(OnAppUpdateStatusListener onAppUpdateStatusListener) {
        mOnAppUpdateStatusListener = onAppUpdateStatusListener;
    }

    /**
     * 初始化 context ,因为需要监听广播和弹出框，必须是非applicationContext
     * 建议在主页面 init，可以使 context 不会泄露内存，
     * 当然在其他页面使用也是可以的，记得release
     *
     * @param context 必须是非applicationContext
     */
    public void init(Context context) {
        mContext = context;
        mApkName = mContext.getApplicationContext().getPackageName() + ".apk";
    }

    /**
     * @param notificationTitle 通知栏头部
     * @param apkUrl            apk下载路径；
     */
    public void inspectVersion(String notificationTitle, final String apkUrl) {
        inspectVersion(notificationTitle, apkUrl, null, false);
    }

    /**
     * 检查下载环境
     *
     * @param notificationTitle 通知栏头部
     * @param apkUrl            apk下载路径；
     * @param tipMessage        提示信息
     * @param isForce           是否强制更新  true-强制
     */
    public void inspectVersion(String notificationTitle, final String apkUrl, String tipMessage, boolean isForce) {
        mDownloadTitle = notificationTitle;
        if (TextUtils.isEmpty(apkUrl)) {
            throw new NullPointerException("appUrl is null");
        }
        if (TextUtils.isEmpty(tipMessage)) {
            tipMessage = "发现新版本，请更新!";
        }

        //先判断是否已经在下载了
        if (!isBeginDownload()) {
            return;
        }

        /**
         * apk 文件夹 存储路径
         */
        File file = getSaveApkPath(mContext);
        if (!file.exists()) {
            //todo 需要做判断 手机空间是否足够的判断
            file.mkdirs();
        }
        mSaveFilePath = file.getPath() + "/" + mApkName;
        delApk(new File(mSaveFilePath));
        //如果服务器的AppCode大于本地的,表示需要更新
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("App更新提示!")
                .setCancelable(!isForce)
                .setMessage(tipMessage)
                .setPositiveButton("确定", null);
        if (!isForce) {
            builder.setNeutralButton("取消", null);
        }
        final AlertDialog show = builder.show();
        show.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUpdateUtil.inspectInstallApk(mContext)) {
                    show.dismiss();
                    downloadApk(apkUrl);
                }
            }
        });
    }


    /**
     * 开始下载
     *
     * @param apkUri apk下载路径
     */
    private void downloadApk(String apkUri) {
        Log.d("downloadApk", "下载了");
        mAppUpdateReceiver = new AppUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mContext.registerReceiver(mAppUpdateReceiver, intentFilter);

        //使用系统的工具下载
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUri));

        //在下载过程中通知栏会一直显示该下载的Notification，
        // 在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 设置下载中通知栏提示的标题
        request.setTitle(mDownloadTitle);
        // 设置下载中通知栏提示的介绍
        request.setDescription("应用正在下载,点击取消");

        //这个文件是你的应用所专用的,软件卸载后，下载的文件将随着卸载全部被删除
        request.setDestinationInExternalFilesDir(mContext.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, mApkName);

        request.setVisibleInDownloadsUi(true);
        //设置请求的Mime
        request.setMimeType("application/vnd.android.package-archive");


        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //下载的id
        mDownloadId = downloadManager.enqueue(request);

        mAppUpdateReceiver.setDownloadId(mDownloadId);
        mAppUpdateReceiver.setFileUrl(mSaveFilePath);
        if (mOnAppUpdateStatusListener != null) {
            mOnAppUpdateStatusListener.onStartDownload();
        }
    }

    /**
     * 是否可以下载
     */
    public boolean isBeginDownload() {
        if (mAppUpdateReceiver == null) {
            return true;
        }
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        int downloadStatus = -1;
        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(mDownloadId));
        if (cursor == null) {
            return true;
        }
        if (cursor.moveToFirst()) {
            downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        }
        switch (downloadStatus) {
            case DownloadManager.STATUS_FAILED:
                if (mOnAppUpdateStatusListener != null) {
                    mOnAppUpdateStatusListener.onFaile();
                }
                return true;
            case DownloadManager.STATUS_PAUSED:
                //"下载已暂停，请继续下载！"
                if (mOnAppUpdateStatusListener != null) {
                    mOnAppUpdateStatusListener.onPause();
                }
                return false;
            case DownloadManager.STATUS_PENDING:
                //the download is waiting to start
                //正在下载
                if (mOnAppUpdateStatusListener != null) {
                    mOnAppUpdateStatusListener.onRunning();
                }
                return false;
            case DownloadManager.STATUS_RUNNING:
                //the download is currently running
                //正在下载
                if (mOnAppUpdateStatusListener != null) {
                    mOnAppUpdateStatusListener.onRunning();
                }
                return false;
            case DownloadManager.STATUS_SUCCESSFUL:
                //the download has successfully completed
                //下载，请点击安装apk
                if (mOnAppUpdateStatusListener != null) {
                    mOnAppUpdateStatusListener.onDownloadSuccess();
                }
                return true;
            default:
                return true;
        }

    }

    /**
     * 重新下载文件之前 需要删除已存在的apk 否则出现安装包解析错误
     */
    private void delApk(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    /**
     * 释放这个界面的资源
     */
    public void release() {
        if (mContext != null && mAppUpdateReceiver != null) {
            mContext.unregisterReceiver(mAppUpdateReceiver);
        }
        mOnAppUpdateStatusListener = null;
        mSaveFilePath = null;
        mContext = null;
        SingletonHolder.instance = null;
    }


    private static File getSaveApkPath(Context context) {
        return context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }


}
