package com.okitoki.checklist.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by andman on 2016-01-13.
 * // https://github.com/chrisbanes/PhotoView
 */
public class PhotoFullScreenActivity extends AppCompatActivity {

    private PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photo_fullscreenview);

        ImageView mImageView = (ImageView) findViewById(R.id.iv_photoview);

        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Lets attach some listeners, not required though!

        String photoPaths = getIntent().getExtras().getString(AppConst.BUNDLE_PHOTOPATH, "");
        Glide.with(this)
                .load(photoPaths)
//                .fallback(R.drawable.profile_man_gray)
//                    .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        mAttacher.update();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAttacher != null)
                                    mAttacher.update();
                            }
                        }, 300);
                        return false;
                    }
                })
                .into(mImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Tracker t = SQApp.getDefaultTracker();
//        t.setScreenName("사진첩확대");
//        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Need to call clean-up
        mAttacher.cleanup();
    }

}