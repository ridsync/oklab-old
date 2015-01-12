package com.oklab;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

/**
 * Created by ojungwon on 2015-01-12.
 */
public class OKLabApp extends Application{

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
        super.onCreate();

        // Thread Policy , Vm Policy 를 설정 해주기위한 코드 at GINGERBREAD 이상 버젼에서만 가능
        // 기본 정책 또는 Customn정책 위반되면 알려주도록 !!!
        // GB 이상부터 기본적용됨 Cause Main Thread에서 Netowork사용시 Exception 발생
        // REf. http://javaexpert.tistory.com483   &&  http://developer.android.com/reference/android/os/StrictMode.html

        // DetectedAll ^^
//        if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().penaltyLog().build());
//        }

        // 감시대상 설정
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        builder.detectLeakedSqlLiteObjects();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            builder.detectActivityLeaks().detectLeakedClosableObjects();
        }
        // 패널티 설정
        builder.penaltyLog();
        // VmPolicy 설정
        StrictMode.VmPolicy vmp = builder.build();
        StrictMode.setVmPolicy(vmp);

    }
}
