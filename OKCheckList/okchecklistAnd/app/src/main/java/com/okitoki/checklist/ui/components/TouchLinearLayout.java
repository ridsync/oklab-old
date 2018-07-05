package com.okitoki.checklist.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by andman on 2016-01-08.
 */
public class TouchLinearLayout extends LinearLayout{

    public OnTouchListener mOnTouchListener;
    public TouchLinearLayout(Context context) {
        super(context);
    }

    public TouchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnTouchListener!=null)
            mOnTouchListener.onTouch(getRootView(),ev);
        return super.onInterceptTouchEvent(ev);
    }

    public void setOnCustomTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }
}
