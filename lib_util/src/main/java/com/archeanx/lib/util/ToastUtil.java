package com.archeanx.lib.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author xz
 * 单例
 */
public class ToastUtil {

    private Toast mToast;

    private long sOneTime = 0L;
    /**
     * 上一次需要发送的消息
     */
    private String mOldmsg;

    /**
     * 上下文对象
     */
    private Context mContext;

    private Handler handler;
    /**
     * toast文字
     */
    private TextView mToastTv;
    /**
     * taost背景
     */
    private View mToastlayout;

    /**
     * 显示的位置
     */
    private int mGravity = Gravity.BOTTOM;
    /**
     * X 轴偏移量
     */
    private int mXOffset = 0;
    /**
     * Y 轴偏移量
     */
    private int mYOffset = 200;
    /**
     * toast文字颜色
     */
    @ColorInt
    private int mTextColor = 0;

    /**
     * 文字大小 (px)
     */
    private float mTextSize = 0;

    /**
     * toast 背景
     */
    private int mToastBackgroundResource = 0;


    private ToastUtil() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static ToastUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final ToastUtil INSTANCE = new ToastUtil();
    }

    /**
     * 初始化Context
     *
     * @param context Application
     */
    public static void init(@NonNull Context context) {
        getInstance().mContext = context;
    }

    /**
     * 显示一个短暂的toast
     *
     * @param str 需要显示的文字
     */
    private void toast(String str) {
        toast(str, false);
    }

    /**
     * @param str    需要显示的内容
     * @param isLong 是否长时间显示
     */
    private void toast(String str, boolean isLong) {
        if (mContext == null) {
            throw new RuntimeException("Please init the Context before showToast");
        }
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
            //toast显示位置
            mToast.setGravity(mGravity, mXOffset, mYOffset);
            mToastlayout = LayoutInflater.from(mContext).inflate(R.layout.lib_util_toast_layout, null);
            if (mToastBackgroundResource != 0) {
                mToastlayout.setBackgroundResource(mToastBackgroundResource);
            }
            mToastTv = mToastlayout.findViewById(R.id.lib_util_toast_txt);
            if (mTextColor != 0) {
                mToastTv.setTextColor(mTextColor);
            }
            if (mTextSize != 0) {
                mToastTv.setTextSize(mTextSize);
            }
            mToast.setView(mToastlayout);
        }
        long sTwoTime = System.currentTimeMillis();
        if (TextUtils.equals(mOldmsg, str)) {
            if (sTwoTime - sOneTime > Toast.LENGTH_SHORT) {
                mToastTv.setText(str);
                mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                mToast.show();
            }
        } else {
            mOldmsg = str;
            mToastTv.setText(str);
            mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            mToast.show();
        }

        sOneTime = sTwoTime;
    }


    /**
     * 设置toast 位置
     *
     * @param gravity Gravity.CENTER
     * @param xOffset 0
     * @param yOffset 200
     * @return toastutil
     * @see android.view.Gravity
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        if (getInstance().mToast == null) {
            getInstance().mGravity = gravity;
            getInstance().mXOffset = xOffset;
            getInstance().mYOffset = yOffset;
        } else {
            getInstance().mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

    /**
     * 设置toast文字颜色
     *
     * @param textColor 颜色值 非 IdRes
     */
    public static void setTextColor(@ColorInt int textColor) {
        if (getInstance().mToastTv == null) {
            getInstance().mTextColor = textColor;
        } else {
            getInstance().mToastTv.setTextColor(textColor);
        }
    }

    /**
     * 设置文字大小 sp
     * 单位sp
     * @param textSize 文字大小 The scaled pixel size.
     */
    public static void setTextSize(float textSize) {
        if (getInstance().mToastTv == null) {
            getInstance().mTextSize = textSize;
        } else {
            getInstance().mToastTv.setTextSize(textSize);
        }
    }

    /**
     * 设置toast 背景
     *
     * @param toastBackground 背景 drawable Res
     */
    public static void setToastBackground(@DrawableRes int toastBackground) {
        if (getInstance().mToastlayout == null) {
            getInstance().mToastBackgroundResource = toastBackground;
        } else {
            getInstance().mToastlayout.setBackgroundResource(toastBackground);
        }
    }


    private void showLongStr(String str) {
        if (!TextUtils.isEmpty(str)) {
            toast(str, true);
        }
    }


    private void showStr(String str) {
        if (!TextUtils.isEmpty(str)) {
            toast(str);
        }
    }

    private void showToThread(final String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                getInstance().showStr(str);
            }
        });

    }

    private void showStr(@StringRes int resId) {
        toast(mContext.getResources().getString(resId));
    }

    public static void showThread(String str) {
        getInstance().showToThread(str);
    }


    public static void show(String str) {
        getInstance().showStr(str);
    }

    public static void showLong(String str) {
        getInstance().showLongStr(str);
    }

    public static void show(@StringRes int resId) {
        getInstance().showStr(resId);
    }


    public static void showException(Exception apiException) {
        getInstance().showStr(apiException.getMessage());
    }


    /**
     * 暂无更多数据吐司
     */
    public static void showNoData() {
        getInstance().showStr("暂无更多数据");
    }

    /**
     * 无网络吐司
     *
     * @param
     */

    public static void showNoNet() {
        getInstance().showStr("网络繁忙,请检查网络!");
    }

    public static void noServiceData() {
        getInstance().showStr("获取失败,服务器繁忙!");
    }


    public static void doException(Exception exc) {
        exc.getLocalizedMessage();
        if (TextUtils.equals(exc.toString(), "java.net.SocketTimeoutException")) {
            getInstance().showStr("服务器连接超时，请稍后再试");
        } else {
            showNoNet();
        }
    }

}
