package com.example.administrator.toplinenews;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
