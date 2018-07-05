package com.okitoki.checklist.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.database.model.PhotoEvent;
import com.okitoki.checklist.ui.adapter.PhotoDetailAdapter;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.ui.components.TouchLinearLayout;
import com.okitoki.checklist.utils.AUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by andman on 2017-01-09.
 */
public class PhotoDetailActivity extends CoreActivity  {

    private ArrayList<String> mArrPhotoInfo = new ArrayList<>();

    RelativeLayout mRlSwipeLayout;
    @Bind(R.id.LL_CONTENT_LAYOUT)
    TouchLinearLayout mLContentLayout;
    @Bind(R.id.VP_PHOTODETAIL)
    public ViewPager mViewPager;
    PhotoDetailAdapter mAdapter;
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photodetail_list);
        ButterKnife.bind(this);
        initFirst();
        AUtil.hideIME(mContext, mViewPager);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.onDestroy();
    }

    private void initFirst() {

        PhotoEvent ebPhotoInfo = EventBus.getDefault().removeStickyEvent(PhotoEvent.class);
        if (ebPhotoInfo != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            mArrPhotoInfo = new Gson().fromJson(ebPhotoInfo.photoPath,type);
        } else {
            // 데이터없음표시
        }

        if (mArrPhotoInfo != null) {  // PhotoInfoList를 그대로 사용하여 그려주면됨.
            if (mAdapter == null) {
                mAdapter = new PhotoDetailAdapter(this);
                mViewPager.setAdapter(mAdapter);
                mAdapter.setData(mArrPhotoInfo);
                //                mAdapter.setListener(mListener);
            }
        }
        mViewPager.setCurrentItem(ebPhotoInfo.getSelectPhoto());
        mAdapter.notifyDataSetChanged();

    }

}
