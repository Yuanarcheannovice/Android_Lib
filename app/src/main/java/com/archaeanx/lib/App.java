package com.archaeanx.lib;

import android.app.Application;
import android.os.Handler;

public class App extends Application {
    public static volatile Handler sAppHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        sAppHandler = new Handler(getApplicationContext().getMainLooper());
    }
}
