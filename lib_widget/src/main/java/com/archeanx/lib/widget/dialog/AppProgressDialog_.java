package com.archeanx.lib.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archeanx.lib.widget.R;

public class AppProgressDialog_ {
    private static ArrayMap<String, AlertDialog> sDialogMap = new ArrayMap<>();

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
    public static AlertDialog show(Context context, CharSequence content, boolean cancelable) {
        if (sDialogMap.get(context.getPackageResourcePath()) == null) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.lw_dialog_progress_dialog, null);
            ProgressBar mProgress = dialogView.findViewById(R.id.lw_progress_bar);
            TextView mProgressNumber = dialogView.findViewById(R.id.lw_progress_content);

            mProgress.setIndeterminate(true);
            mProgressNumber.setText(content);

            AlertDialog alertDialog;
            //做小于21的兼容，保证dialog不会变形
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertDialog = new AlertDialog.Builder(context, R.style.lw_AppProgressDialog).create();
            } else {
                alertDialog = new AlertDialog.Builder(context).create();
            }


            alertDialog.setCancelable(cancelable);
            alertDialog.setView(dialogView);
            alertDialog.setCancelable(cancelable);
            //屏幕外点击消失
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            sDialogMap.put(context.getPackageResourcePath(), alertDialog);
            return alertDialog;
        } else {
            return sDialogMap.get(context.getPackageResourcePath());
        }

    }
}
