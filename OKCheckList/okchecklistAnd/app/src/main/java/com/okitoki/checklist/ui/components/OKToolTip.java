package com.okitoki.checklist.ui.components;

import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import tourguide.tourguide.ToolTip;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-02-20.
 */
public class OKToolTip extends ToolTip{

    public OKToolTip(){
        /* default values */
        mTitle = "";
        mDescription = "";
        mBackgroundColor = Color.parseColor("#3498db");
        mTextColor = Color.parseColor("#FFFFFF");

//        mEnterAnimation = new AlphaAnimation(0f, 1f);
//        mEnterAnimation.setDuration(600);
//        mEnterAnimation.setFillAfter(true);
//        mEnterAnimation.setInterpolator(new DecelerateInterpolator());
        mEnterAnimation = new TranslateAnimation(0f, 0f, 130f, 0f);
        mEnterAnimation.setDuration(500);
        mEnterAnimation.setFillAfter(true);
        mEnterAnimation.setInterpolator(new DecelerateInterpolator());
        mShadow = true;

        // TODO: exit animation
        mGravity = Gravity.CENTER;
    }

}
