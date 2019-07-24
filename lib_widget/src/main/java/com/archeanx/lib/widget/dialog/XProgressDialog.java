package com.archeanx.lib.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
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

    public static AlertDialog show(Context context) {
        return show(context, "加载中...", true, null);
    }

    public static AlertDialog show(Context context, CharSequence content) {
        return show(context, content, true, null);
    }

    /**
     * @param context    context
     * @param content    内容
     * @param cancelable 是否允许返回键关闭diaolog
     * @return
     */
    public static XProgressDialog show(Context context, CharSequence content, boolean cancelable, OnCancelListener cancelListener) {
        if (sDialog != null) {
            if (sDialog.isShowing()) {
                sDialog.cancel();
            }
        }

        XProgressDialog alertDialog;
        //做小于21的兼容，保证dialog不会变形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new XProgressDialog(context, R.style.lw_AppProgressDialog);
        } else {
            alertDialog = new XProgressDialog(context);
        }
        alertDialog.setMessage(content);
        alertDialog.setCancelable(cancelable);
        alertDialog.setOnCancelListener(cancelListener);
        alertDialog.show();
        sDialog = alertDialog;
        return alertDialog;
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


    protected XProgressDialog(Context context) {
        super(context);
    }

    protected XProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected XProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
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

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgressContent != null) {
            mProgressContent.setText(message);
        }
    }
}
