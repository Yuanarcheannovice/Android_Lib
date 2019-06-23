package com.archeanx.lib.appupdate;

/**
 * 下载状态监听
 */
public abstract class OnAppUpdateStatusListener {

    /**
     * 开始下载
     */
    public void onStartDownload() {

    }

    /**
     * 下载错误
     */
    public void onFaile() {

    }

    /**
     * 下载暂停
     */
    public void onPause() {

    }

    /**
     * 下载中
     */
    public void onRunning() {

    }

    /**
     * 下载完成
     */
    public void onDownloadSuccess() {

    }
}
