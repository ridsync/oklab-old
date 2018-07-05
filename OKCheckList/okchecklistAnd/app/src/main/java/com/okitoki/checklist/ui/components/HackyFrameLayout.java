package com.okitoki.checklist.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by andman on 2016-01-13.
 */
public class HackyFrameLayout extends FrameLayout{

    public HackyFrameLayout(Context context) {
        super(context);
    }

    public HackyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
