package com.okitoki.checklist.ui.components;

import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;

import tourguide.tourguide.ToolTip;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-02-20.
 */
public class OKDefaultToolTip extends ToolTip{

    public OKDefaultToolTip(){
        /* default values */
        mTitle = "";
        mDescription = "";
        mBackgroundColor = Color.parseColor("#3498db");
        mTextColor = Color.parseColor("#FFFFFF");

        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(900);
        mEnterAnimation.setFillAfter(true);
        mEnterAnimation.setInterpolator(new LinearInterpolator());
        mShadow = true;

        // TODO: exit animation
        mGravity = Gravity.CENTER;
    }

}
