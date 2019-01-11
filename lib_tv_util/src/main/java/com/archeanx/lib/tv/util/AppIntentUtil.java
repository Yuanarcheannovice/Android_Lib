package com.archeanx.lib.tv.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class AppIntentUtil {
    public static void onJumpSettting(Context context){
        try {
            //跳转设置 判断是不是茁壮的系统,不是茁壮系统，跳系统设置
            String phoneInfo = SystemUtil.getString(context, "ro.build.description");
            if (!TextUtils.isEmpty(phoneInfo) && phoneInfo.contains("hncatv")) {
                Intent intent = new Intent();
                intent.setClassName("tv.ipanel.join.app.hunancafinal", "tv.ipanel.join.app.ca.HomeActivity");
                context.startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.Settings");
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
