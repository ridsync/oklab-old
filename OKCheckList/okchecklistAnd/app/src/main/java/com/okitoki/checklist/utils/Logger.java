package com.okitoki.checklist.utils;

import android.util.Log;

import com.okitoki.checklist.BuildConfig;

/**
 * Created by okc on 2015-06-21.
 */
public class Logger {

    public static void d(Object obj,String msg){
        if (! BuildConfig.DEBUG) return;
        String TAG = obj.getClass().getSimpleName();
        Log.d(obj.getClass().getSimpleName(),msg);
    }
    public static void i(Object obj,String msg){
        if (! BuildConfig.DEBUG) return;
        String TAG = obj.getClass().getSimpleName();
        Log.i(obj.getClass().getSimpleName(), msg);
    }

}
