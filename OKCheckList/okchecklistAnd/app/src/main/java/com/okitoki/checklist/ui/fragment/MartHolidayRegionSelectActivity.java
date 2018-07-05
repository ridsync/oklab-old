package com.okitoki.checklist.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.holiday.RestMartRegion;
import com.okitoki.checklist.ui.ViewHolder.FavMartRegionViewHolder;
import com.okitoki.checklist.ui.base.HolidayCoreActivity;
import com.okitoki.checklist.utils.JavaUtil;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by ojungwon on 2015-04-08.
 */
public class MartHolidayRegionSelectActivity extends HolidayCoreActivity {

    public static final String TAG = MartHolidayRegionSelectActivity.class.getSimpleName();

    protected FirebaseRecyclerAdapter<RestMartRegion, FavMartRegionViewHolder> mAdapter;

    public MartHolidayRegionSelectActivity() {}

    String selectMartName = "emart";
    String selectMartRegion = "emartRegionList";
    private SlideInBottomAnimationAdapter slideInAdapter;

    Toolbar toolbar_actionbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);

        // TODO 마트정보 확인 쿼리준비
        selectMartName = getIntent().getStringExtra("selectMartName");
        selectMartRegion = getIntent().getStringExtra("selectMartRegion");


        setContentView(R.layout.activity_region_select);

        toolbar_actionbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar_actionbar.setTitle(R.string.menu_mode_region_select);
        int color = getResources().getColor(R.color.yellow_A700);
        if("emart".equalsIgnoreCase(selectMartName) ){
            color = getResources().getColor(R.color.yellow_A700);
        } else if("homeplus".equalsIgnoreCase(selectMartName)){
            color = getResources().getColor(R.color.fab_red_color);
        } else if("lottemart".equalsIgnoreCase(selectMartName)){
            color = getResources().getColor(R.color.mart_color_lotte);
        } else if("costco".equalsIgnoreCase(selectMartName)){
            color = getResources().getColor(R.color.main_primary_dark_color);
        }
//        toolbar_actionbar.setBackgroundColor(color);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(color);
//        }

        mRecycler = (RecyclerView) findViewById(R.id.rcv_cf_mart_listView);
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        // Set up FirebaseRecyclerAdapter with the Query
        Query RestEmartsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<RestMartRegion, FavMartRegionViewHolder>(RestMartRegion.class, R.layout.item_view_cart_fav_add,
                FavMartRegionViewHolder.class, RestEmartsQuery) {
            @Override
            protected void populateViewHolder(final FavMartRegionViewHolder viewHolder, final RestMartRegion model, final int position) {
                final DatabaseReference RestEmartRef = getRef(position);

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 해당지역 마트 리스트화면 이동
                        if(JavaUtil.isDoubleClick()) return ;

                        Intent i = new Intent(mContext, MartHolidayDetailMartListActivity.class);
                        i.putExtra("selectMartName", selectMartName);
                        i.putExtra("selectMartRegion",model.getRegionName());
                        startActivity(i);

                        Bundle bundle2 = new Bundle();
                        bundle2.putString(AppConst.FA_PARAM_MART_NAME, selectMartName);
                        bundle2.putString(AppConst.FA_PARAM_MART_REGION, model.getRegionName());
                        // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_REST_MART_REGION_CLICK, bundle2);
                    }
                });

            }
        };

        // Set up Layout Manager, reverse layout
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecycler.setLayoutManager(layoutManager);
//
//        slideInAdapter = new SlideInBottomAnimationAdapter(mAdapter);
//        slideInAdapter.setDuration(600);
//        slideInAdapter.setFirstOnly(true);
//        slideInAdapter.setInterpolator(new DecelerateInterpolator());

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child(selectMartRegion);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
