package com.archeanx.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;


/**
 * @author xz
 * 缓存 帮助类 单例模式
 */

public final class PreferencesHelper {


    private SharedPreferences sp;

    private static final String SPK_DEVICE_ID = "deviceId";


    private PreferencesHelper() {

    }

    private void initHelper(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (!sp.contains(SPK_DEVICE_ID)) {
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            set(SPK_DEVICE_ID, deviceId);
        }
    }


    public static PreferencesHelper getInstance() {
        return PreferencesHelper.SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final PreferencesHelper sInstance = new PreferencesHelper();
    }

    public static void init(@NonNull Context context) {
        // 初始化Context
        getInstance().initHelper(context);

    }

    public SharedPreferences getPre() {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        return sp;
    }


    /////////////////--------------////////


    /**
     * 获取设备id
     *
     * @return
     */
    public String getDeviceId() {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        return sp.getString(SPK_DEVICE_ID, null);
    }


    /**
     * 缓存String信息
     */
    public void set(String key, String value) {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 缓存int信息
     */
    public void set(String key, int value) {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 缓存long信息
     */
    public void set(String key, long value) {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 缓存boolean信息
     */
    public void set(String key, boolean value) {
        if (sp == null) {
            throw new RuntimeException("Please init the Context before PreferencesUtil");
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
