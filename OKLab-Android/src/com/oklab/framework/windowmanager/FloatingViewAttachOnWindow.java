package com.oklab.framework.windowmanager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.oklab.BaseActivity;
import com.oklab.R;

/**
 * 윈도우에 View를 띄우는 방법 on Service에서 해야 계속 떠있는단다.
 * WindowManager.Param에 따라 Touch도 가능하고 움직 일 수 있다.
 * SYSTEM_ALERT_WINDOW 퍼미션 필요함.
 * http://itmir.tistory.com/548
 * http://www.hardcopyworld.com/ngine/android/index.php/archives/162
 * http://www.kmshack.kr/%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%B6%81-%ED%99%88%EC%9D%98-chat-heads-%EC%B2%98%EB%9F%BC-%EB%AA%A8%EB%93%A0%ED%99%94%EB%A9%B4%EC%97%90-%EB%96%A0%EC%9E%88%EB%8A%94-%EB%B7%B0-%EA%B5%AC%ED%98%84/
 * Created by ojungwon on 2015-02-04.
 */
public class FloatingViewAttachOnWindow extends BaseActivity {

    private ViewGroup layoutGraphView;
    private ViewGroup layoutGraphVerticalrods;
    private ImageView mView;
    WindowManager mManager;
    WindowManager.LayoutParams mParams;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        setVerticalRodsView();
    }

    private void setVerticalRodsView() {
        mManager = (WindowManager) getSystemService( Context.WINDOW_SERVICE );

        mView = new ImageView(this);
        mView.setBackgroundResource(R.drawable.doll);
//		mView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
        mView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchX = event.getRawX();
                        mTouchY = event.getRawY();
                        mViewX = mParams.x;
                        mViewY = mParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) (event.getRawX() - mTouchX);
                        int y = (int) (event.getRawY() - mTouchY);

                        mParams.x = mViewX + x;
                        mParams.y = mViewY + y;
                        mManager.updateViewLayout(mView, mParams);
                        break;
                }
                return false;
            }
        });
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT );
        mParams.gravity = Gravity.CENTER;
        mParams.width = 100;
        mParams.height = 100;
        mManager.addView(mView, mParams );

    }
}
