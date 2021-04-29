package com.codetiger.we;


import android.app.Application;

/**
 * 描述：Application类
 *
 */
public class WeTalkApp extends Application {

    private static WeTalkApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        WeInit.initTimber();
        WeInit.initOKHttp(this);
    }

    public static WeTalkApp getContext() {
        return context;
    }
}
