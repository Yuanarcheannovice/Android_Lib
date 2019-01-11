package com.archeanx.lib.tv.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class SystemUtil {
    public static String getString(Context context, String key) {
        String result = "";
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressLint("PrivateApi") Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method getString = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[1];
            params[0] = key;
            result = (String) getString.invoke(SystemProperties, params);
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            //如果key超过32个字符则抛出该异常
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
}
