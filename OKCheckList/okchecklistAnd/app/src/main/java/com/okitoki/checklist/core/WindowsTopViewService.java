package com.okitoki.checklist.core;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-21.
 */
public class WindowsTopViewService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param  Used to name the worker thread, important only for debugging.
     */
    public WindowsTopViewService() {
        super(WindowsTopViewService.class.getName());
    }

    @Override
    public IBinder onBind(Intent arg0) { return null; }

    FrameLayout mPopupView;
    WindowManager mWindowManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mPopupView  = (FrameLayout)LayoutInflater.from(this).inflate(R.layout.item_view_main_cart, null, false);
        ((TextView)mPopupView.findViewById(R.id.tv_item_title)).setText("이 뷰는 항상 위에 있다.");                        //텍스트 설정
        ((TextView)mPopupView.findViewById(R.id.tv_item_title)).setTextColor(Color.BLUE);                                  //글자 색상
        ((TextView)mPopupView.findViewById(R.id.tv_item_date)).setText("date:2011/22/22");                                //글자 색상
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
//                0,300,
//                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.CENTER | Gravity.CENTER;
        lp.windowAnimations = android.R.style.Animation_Toast; // Animation?
        mWindowManager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        mWindowManager.addView(mPopupView, lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mWindowManager != null) {        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(mPopupView != null) mWindowManager.removeView(mPopupView);
        }
    }
}