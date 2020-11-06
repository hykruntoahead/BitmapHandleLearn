package com.ynkeen.app.bitmaphandlelearn;

import android.app.Application;
import android.content.Context;

/**
 * create by heyukun on 2020/10/31  10:55
 */
public class BhApplication extends Application {
    private static BhApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance==null){
            sInstance = this;
        }
    }

    public static BhApplication getInstance() {
        return sInstance;
    }
}
