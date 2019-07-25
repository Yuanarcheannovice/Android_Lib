package com.archeanx.lib.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archeanx.lib.widget.R;

/**
 * @author xz
 * 进度dialog
 */
public class XProgressDialog extends AlertDialog {
    private static XProgressDialog sDialog;
    private TextView mProgressContent;
    private CharSequence mMessage;

    public static XProgressDialog show(Context context) {
        return show(context, "加载中...", true, null);
    }

    public static XProgressDialog show(Context context, CharSequence content) {
        return show(context, content, true, null);
    }

    /**
     * @param context    context
     * @param message    内容
     * @param cancelable 是否允许返回键关闭diaolog
     * @return XProgressDialog
     * 对于单前页面重复使用 XProgressDialog 时，做了优化，重复使用时，不会new多次，而会使用上次dialog
     * todo 需要注意的是，当页面销毁时，必须 使用onDismiss()方法，解除引用， 避免内存泄漏
     */
    public static XProgressDialog show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        if (sDialog != null) {
            if (sDialog.getXContext().hashCode() != context.hashCode()) {
                if (sDialog.isShowing()) {
                    sDialog.cancel();
                }
                sDialog = null;
            }
        }
        if (sDialog == null) {
            //做小于21的兼容，保证dialog不会变形
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sDialog = new XProgressDialog(context, R.style.lw_AppProgressDialog);
            } else {
                sDialog = new XProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            }
        }
        sDialog.setCancelable(cancelable);
        sDialog.setOnCancelListener(cancelListener);
        sDialog.setMessage(message);
        sDialog.show();
        return sDialog;
    }

    /**
     * 关闭dialog
     */
    public static void onDismiss() {
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                sDialog.cancel();
            }
            sDialog = null;
        }
    }

    /**
     * 关闭dialog
     */
    public static void onClose(){
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                sDialog.cancel();
            }
        }
    }

    /**
     * 释放
     */
    public static void onRelease(){
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                sDialog.cancel();
            }
            sDialog = null;
        }
    }

    /**
     * dialog.getContext的hasCode值 和 传入的context 不一致
     * 所以需要自己 提取context
     */
    private Context mContext;


    protected XProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public Context getXContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.lw_dialog_progress_dialog, null);
        ProgressBar progress = dialogView.findViewById(R.id.lw_progress_bar);
        mProgressContent = dialogView.findViewById(R.id.lw_progress_content);

        progress.setIndeterminate(true);

        setView(dialogView);
        //屏幕外点击消失
        setCanceledOnTouchOutside(false);

        mProgressContent.setText(mMessage);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgressContent != null) {
            mProgressContent.setText(message);
        } else {
            mMessage = message;
        }
    }
}
