package com.okitoki.checklist.android.AnimationCompat;

import android.annotation.TargetApi;
import android.support.v4.app.SharedElementCallback;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.okitoki.checklist.R;

import java.util.List;

/**
 * @author okc
 * @version 1.0
 * @see
 *
 * // https://github.com/alexjlockwood/custom-lollipop-transitions
 *
 * http://developer.android.com/intl/ko/training/transitions/custom-transitions.html#CreateAnimator
 *
 *
 * @since 2016-01-25.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EnterSharedElementCallback extends SharedElementCallback {
    private static final String TAG = "EnterSharedElementCallback";

    private  float mStartTextSize = 0;
    private  float mEndTextSize= 0;
//    private final int mStartColor;
//    private final int mEndColor;

    public EnterSharedElementCallback(Context context) {
        Resources res = context.getResources();
        mStartTextSize = res.getDimensionPixelSize(R.dimen.small_text_size);
        mEndTextSize = res.getDimensionPixelSize(R.dimen.large_text_size);
//        mStartColor = res.getColor(R.color.blue);
//        mEndColor = res.getColor(R.color.light_blue);
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Log.i(TAG, "=== onSharedElementStart(List<String>, List<View>, List<View>)");
        TextView textView = (TextView) sharedElements.get(0);
        TextView textView1 = (TextView) sharedElements.get(1);
        TextView textView2 = (TextView) sharedElements.get(2);

        // Setup the TextView's start values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
//        textView.setTextColor(mStartColor);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        Log.i(TAG, "=== onSharedElementEnd(List<String>, List<View>, List<View>)");
        TextView textView = (TextView) sharedElements.get(0);
        TextView textView1 = (TextView) sharedElements.get(1);
        TextView textView2 = (TextView) sharedElements.get(2);

        // Record the TextView's old width/height.
        int oldWidth = textView.getMeasuredWidth();
        int oldHeight = textView.getMeasuredHeight();

        // Setup the TextView's end values.
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEndTextSize);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStartTextSize);
//        textView.setTextColor(mEndColor);

        // Re-measure the TextView (since the text size has changed).
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthSpec, heightSpec);

        // Record the TextView's new width/height.
        int newWidth = textView.getMeasuredWidth();
        int newHeight = textView.getMeasuredHeight();

        // Layout the TextView in the center of its container, accounting for its new width/height.
        int widthDiff = newWidth - oldWidth;
        int heightDiff = newHeight - oldHeight;
        textView.layout(textView.getLeft() - widthDiff / 2, textView.getTop() - heightDiff / 2,
                textView.getRight() + widthDiff / 2, textView.getBottom() + heightDiff / 2);
    }
}