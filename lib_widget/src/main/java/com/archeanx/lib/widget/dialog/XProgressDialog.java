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


    private static final ArrayMap<String, XProgressDialog> sDialogMap = new ArrayMap<>();
    private ProgressBar mProgress;
    private TextView mProgressContent;

    public static AlertDialog show(Context context) {
        return show(context, "加载中...", true);
    }

    public static AlertDialog show(Context context, CharSequence content) {
        return show(context, content, true);
    }

    /**
     * @param context    context
     * @param content    内容
     * @param cancelable 是否允许返回键关闭diaolog
     * @return
     */
    public static XProgressDialog show(Context context, CharSequence content, boolean cancelable) {
        XProgressDialog alertDialog;
        if (sDialogMap.get(context.getPackageResourcePath()) == null) {
            //做小于21的兼容，保证dialog不会变形
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog = new XProgressDialog(context, R.style.lw_AppProgressDialog);
            } else {
                alertDialog = new XProgressDialog(context);
            }
            sDialogMap.put(context.getPackageResourcePath(), alertDialog);
        } else {
            alertDialog = sDialogMap.get(context.getPackageResourcePath());
        }
        if (alertDialog != null) {
            alertDialog.setMessage(content);
            alertDialog.setCancelable(cancelable);
            alertDialog.show();
        } else {
            sDialogMap.remove(context.getPackageResourcePath());
        }

        return alertDialog;
    }

    /**
     * 关闭dialog
     *
     * @param context context
     */
    public static void onDismiss(Context context) {
        if (sDialogMap.get(context.getPackageResourcePath()) != null) {
            XProgressDialog dialog = sDialogMap.get(context.getPackageResourcePath());
            if (null != dialog) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 释放dialog
     * @param context
     */
    public static void release(Context context) {
        if (sDialogMap.get(context.getPackageResourcePath()) != null) {
            XProgressDialog dialog = sDialogMap.get(context.getPackageResourcePath());
            if (null != dialog) {
                dialog.dismiss();
            }
            sDialogMap.remove(context.getPackageResourcePath());
        }
    }

    public static void releaseAll() {

        sDialogMap.clear();
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
        super.onCreate(savedInstanceState);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.lw_dialog_progress_dialog, null);
        mProgress = dialogView.findViewById(R.id.lw_progress_bar);
        mProgressContent = dialogView.findViewById(R.id.lw_progress_content);

        mProgress.setIndeterminate(true);

        AlertDialog alertDialog;
        //做小于21的兼容，保证dialog不会变形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(getContext(), R.style.lw_AppProgressDialog).create();
        } else {
            alertDialog = new AlertDialog.Builder(getContext()).create();
        }

        alertDialog.setView(dialogView);
        //屏幕外点击消失
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgressContent != null) {
            mProgressContent.setText(message);
        }
    }
}
