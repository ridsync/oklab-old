package com.okitoki.checklist.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.ui.components.SquareImageView;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ridsync
 *  TODO 뷰페이저를 FragmentPagerAdapter로 프로그먼트로 처리해야됨.
 *  TODO 내부적으로 Dialog 및 Netowork처리를 Fragment안에서 하도록.
 */
public class PhotoDetailAdapter extends PagerAdapter {
    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mArrPhotoInfo;
    private TypedArray mArrCategoryIcon;
    private String[] mArrCateogoryText;
    private int mPosition;
    private long mUserId;
    private int mCountSize = 0;
    private TextView selectView;

    public PhotoDetailAdapter(Activity context){
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<String> photoInfoArrayList) {
        if(photoInfoArrayList != null) {
            mArrPhotoInfo = photoInfoArrayList;
            mCountSize = mArrPhotoInfo.size();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int nCount = 0;
        if(mArrPhotoInfo != null) {
            nCount = mCountSize;
        }
        return nCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final View pager, final int position) {
        final View view = mInflater.inflate(R.layout.item_photodetail, null);
        view.setTag("view" + position);
//        view.setId(position);
        final String photoInfo = mArrPhotoInfo.get(position);
//        getPhotoInfo(view, pager, position, photoInfo);
        setItem(view, pager, position, photoInfo);
        Logger.d(this, "POSITION : " + position);

        return view;
    }

    private void setItem(final View view, final View pager, final int position,final String photoInfo){
        setViewItem(view, position, photoInfo);
        ((ViewPager) pager).addView(view, 0);
    }

    private void getPhotoInfo(final View view, final View pager, final int position,final String photoInfo) {
            setItem(view, pager, position, photoInfo);
            return;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager)pager).removeView((View) view);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    @Override
    public Parcelable saveState() { return null; }
    @Override
    public void startUpdate(View arg0) {}
    @Override
    public void finishUpdate(View arg0) {}

    private PhotoViewAttacher mAttacher;

    private void setViewItem(final View view, int nPosition, final String photoInfo) {
        LinearLayout llCommentBox = null;

        // Image 영역 hedigt 설정.
        // LL_POFILE_PTOHTODETAIL_IMAGELAYOUT

        // Default Image
        ImageView sqImageview = (ImageView)view.findViewById(R.id.IV_POFILE_PTOHTODETAIL_IMAGE);

        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(sqImageview);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Lets attach some listeners, not required though!

        Glide.with(mContext)
                .load(photoInfo)
//                .fallback(R.drawable.profile_man_gray)
                .crossFade(nPosition > 0 ? 200 : 0)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
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
                .into(sqImageview);

    }

    public void onDestroy() {
        // Need to call clean-up
        if(mAttacher!=null)
             mAttacher.cleanup();
    }

}
