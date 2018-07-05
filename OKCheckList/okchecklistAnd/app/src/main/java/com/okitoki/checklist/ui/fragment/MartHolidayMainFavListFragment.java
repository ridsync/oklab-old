package com.okitoki.checklist.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;
import com.okitoki.checklist.okperm.OKPermission;
import com.okitoki.checklist.ui.HolidayWebViewActivity;
import com.okitoki.checklist.ui.MainActivity;
import com.okitoki.checklist.ui.SettingActivity;
import com.okitoki.checklist.ui.ViewHolder.FavMartRestInfoListViewHolder;
import com.okitoki.checklist.ui.base.HolydayBaseFragment;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by ojungwon on 2015-04-08.
 */
public class MartHolidayMainFavListFragment extends HolydayBaseFragment {

    public static final String TAG = MartHolidayMainFavListFragment.class.getSimpleName();

    protected FirebaseRecyclerAdapter<RestMartInfo, FavMartRestInfoListViewHolder> mAdapter;

    public MartHolidayMainFavListFragment() {}

    public static boolean isRefreshAdapter= false;

    RelativeLayout rlEmpty;
    ImageView iv_cart_item_empty;
    TextView tvSearch;
    TextView tvToday;
    ImageButton ibEmartIcon;
    ImageButton ibHomePIcon;
    ImageButton ibLotteMIcon;
    ImageButton ibCostcoIcon;

    // noticeview

    RelativeLayout rl_all_mart_notice_slidedrawer;
    TextView tv_btn_close;
    TextView tv_notice_desc;
    TextView tv_notice_desc_2;
    LinearLayout ll_emart_layout;
    TextView tv_emart_notice_link;
    LinearLayout ll_homeplus_layout;
    TextView tv_homeplus_notice_link;
    LinearLayout ll_lottemart_layout;
    TextView tv_lottemart_notice_link;
    LinearLayout ll_costco_layout;
    TextView tv_costco_notice_link;

    boolean isShowNotice = false;
    boolean isShowNoticeEmart = false;
    boolean isShowNoticeHomePlus = false;
    boolean isShowNoticeLottemart = false;
    boolean isShowNoticeCostco = false;
    String strNoticeDesc = "";
    String strNoticeDesc_2 = "";

    private HashMap<String,RestMartInfo> cachingFavMartInfo = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mart_holiday_main_list, container, false);

        rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rl_cart_item_empty);
        iv_cart_item_empty = (ImageView) rootView.findViewById(R.id.iv_cart_item_empty);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.mart_favorite_list);
        mRecycler.setHasFixedSize(true);

        tvSearch = (TextView)rootView.findViewById(R.id.btn_search);
        tvToday = (TextView)rootView.findViewById(R.id.tv_today_date);
        ibEmartIcon = (ImageButton)rootView.findViewById(R.id.tv_mart_icon_emart);
        ibHomePIcon = (ImageButton)rootView.findViewById(R.id.tv_mart_icon_homplus);
        ibLotteMIcon = (ImageButton)rootView.findViewById(R.id.tv_mart_icon_lottemart);
        ibCostcoIcon = (ImageButton)rootView.findViewById(R.id.tv_mart_icon_costco);

        rl_all_mart_notice_slidedrawer = (RelativeLayout)rootView.findViewById(R.id.rl_all_mart_notice_slidedrawer);
        tv_btn_close = (TextView) rootView.findViewById(R.id.tv_btn_close);
        tv_notice_desc = (TextView)rootView.findViewById(R.id.tv_notice_desc);
        tv_notice_desc_2 = (TextView)rootView.findViewById(R.id.tv_notice_desc_2);
        ll_emart_layout = (LinearLayout) rootView.findViewById(R.id.ll_emart_layout);
        tv_emart_notice_link = (TextView)rootView.findViewById(R.id.tv_emart_notice_link);
        ll_homeplus_layout = (LinearLayout) rootView.findViewById(R.id.ll_homeplus_layout);
        tv_homeplus_notice_link = (TextView)rootView.findViewById(R.id.tv_homeplus_notice_link);
        ll_lottemart_layout = (LinearLayout) rootView.findViewById(R.id.ll_lottemart_layout);
        tv_lottemart_notice_link = (TextView) rootView.findViewById(R.id.tv_lottemart_notice_link);
        ll_costco_layout = (LinearLayout) rootView.findViewById(R.id.ll_costco_layout);
        tv_costco_notice_link = (TextView) rootView.findViewById(R.id.tv_costco_notice_link);
        tv_costco_notice_link = (TextView) rootView.findViewById(R.id.tv_costco_notice_link);

        tvSearch.setOnClickListener(mOnclickListener);
        ibEmartIcon.setOnClickListener(mOnclickListener);
        ibHomePIcon.setOnClickListener(mOnclickListener);
        ibLotteMIcon.setOnClickListener(mOnclickListener);
        ibCostcoIcon.setOnClickListener(mOnclickListener);

        Log.d("MainFavCart", "FirebaseAuth: MainFavCart onCreateView:" );
        setAdapter();

        setNotice();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.holiday_menu, menu);
         menu.getItem(0).setIcon(R.drawable.ic_settings_white_24dp);
//        MenuItem menuItem = menu.getItem(0);
//        button = (ImageView) menuItem.getActionView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting:
//                Toast.makeText(getActivity().getApplicationContext(), "menu_mode_delete", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, SettingActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Log.d(TAG, "FirebaseAuth:getQuery ");
        Query recentPostsQuery = null;
        if( FirebaseAuth.getInstance().getCurrentUser() !=null){ // ※ App업데이트시 user가 널인 버그... 수정하느라.. 삽질
            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            recentPostsQuery = databaseReference.child("usersfavmartlist").child(uId).orderByChild("martCode");
        } else {
            recentPostsQuery =databaseReference.child("usersfavmartlist").child("asdf");
        }
        return recentPostsQuery;
    }

    public void setAdapter(){

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(false);
        mManager.setStackFromEnd(false);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query RestEmartsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<RestMartInfo, FavMartRestInfoListViewHolder>(RestMartInfo.class, R.layout.item_mart_favorite,
                FavMartRestInfoListViewHolder.class, RestEmartsQuery) {
            @Override
            protected void populateViewHolder(final FavMartRestInfoListViewHolder viewHolder, final RestMartInfo model, final int position) {
                final DatabaseReference restEmartRef = getRef(position);
                if(restEmartRef ==null) return;

                String martName = AUtil.getMartNameByCode(model.getMartCode());

                // 리스트로 어뎁터 리스트그려질대 , 별도 지점정보 쿼리하여 뷰 업데이트 ?
                final RestMartInfo cachingData = cachingFavMartInfo.get(model.getPointCode()+model.getPointName());
                Logger.d(TAG, "getFavMart:populateViewHolder model = " + model.getPointName() + "cachingBind = " +cachingData);
                if(cachingData !=null){
                    // 1) 캐싱데이터로 바인딩 및 클릭리스너
                    viewHolder.bindToPost(true, cachingData, null);
                    cachingData.setRefkey(restEmartRef.getKey());
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(JavaUtil.isDoubleClick()) return ;
                            EventBus.getDefault().postSticky(cachingData);

                            Intent i = new Intent(mContext, MartHolidayMartDetailInfoActivity.class);
                            i.putExtra(AppConst.INTENT_EXTRA_IS_FAV_VIEW, true);
                            startActivityForResult(i,0);

                            Bundle bundle2 = new Bundle();
                            bundle2.putString(AppConst.FA_PARAM_NORMAL, "normal");
                            // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_REST_MART_FAV_CLICK, bundle2);

//                            Intent detailIntent = new Intent(getActivity(), MartHolidayMartDetailInfoActivity.class);
//                            detailIntent.putExtra(AppConst.INTENT_EXTRA_IS_FAV_VIEW, true);
//                            String titleName = getString(R.string.transition_title);
//                            String dateName = getString(R.string.transition_date);
//                            String icon = getString(R.string.transition_body);
//                            String star = getString(R.string.transition_star);
//                            Pair<View, String> titlePair = Pair.create(viewHolder.getMartName(), titleName);
//                            Pair<View, String> datePair = Pair.create(viewHolder.getDateView(), dateName);
//                            Pair<View, String> iconPair = Pair.create(viewHolder.getIvIconView(), icon);
//                            Pair<View, String> starPair = Pair.create(viewHolder.getIvStarView(), star);
//                            @SuppressWarnings("unchecked")
//                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), titlePair,datePair, iconPair, starPair);
//                            ActivityCompat.startActivityForResult(getActivity(), detailIntent, AppConst.REQUEST_CODE_ACT_CARTD_DETAIL, options.toBundle());
                        }
                    });
                    return;
                } else {
                    viewHolder.bindToPost(false, model, null);
                }
                // 데이터 쿼리 리스너 콜백후 bind
                DatabaseReference ref;
                if(model.getMartCode() == RestUtil.MART_CODE_COSTCO){
                    ref = mDatabase.child(martName).child(restEmartRef.getKey());
                } else {
                    ref =mDatabase.child(martName).child(model.getRegionName()).child(restEmartRef.getKey());
                }
                //        mDatabase.child(martName).child(model.getRegionName()).orderByChild("pointName").equalTo(model.getPointName()).addListenerForSingleValueEvent(
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(mAdapter!=null && mAdapter.getItemCount() > 0){
                                    rlEmpty.setVisibility(View.INVISIBLE);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                                // Get user value
                                final RestMartInfo martInfo = dataSnapshot.getValue(RestMartInfo.class);
                                //                        HashMap<String,String> favMartInfo = (HashMap<String, String>) dataSnapshot.getValue(true);
                                Logger.d(TAG, "getFavMart:onDataChange martInfo = " + martInfo);
                                if(martInfo==null) return;

                                //                        Logger.d(TAG, "getFavMart:onDataChange favMartInfo = " + favMartInfo);
                                // Bind Post to ViewHolder, setting OnClickListener for the star button
                                viewHolder.bindToPost(true, martInfo, null);
                                // 2) 쿼리후 바인딩 및 클릭리스너
                                martInfo.setRefkey(restEmartRef.getKey());
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(JavaUtil.isDoubleClick()) return ;
                                        EventBus.getDefault().postSticky(martInfo);

                                        Intent i = new Intent(mContext, MartHolidayMartDetailInfoActivity.class);
                                        i.putExtra(AppConst.INTENT_EXTRA_IS_FAV_VIEW, true);
                                        startActivityForResult(i,0);

                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString(AppConst.FA_PARAM_NORMAL, "normal");
                                        // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_REST_MART_FAV_CLICK, bundle2);

//                                    Intent detailIntent = new Intent(getActivity(), MartHolidayMartDetailInfoActivity.class);
//                                    detailIntent.putExtra(AppConst.INTENT_EXTRA_IS_FAV_VIEW, true);
//                                    String titleName = getString(R.string.transition_title);
//                                    String dateName = getString(R.string.transition_date);
//                                    String icon = getString(R.string.transition_body);
//                                    String star = getString(R.string.transition_star);
//                                    Pair<View, String> titlePair = Pair.create(viewHolder.getMartName(), titleName);
//                                    Pair<View, String> datePair = Pair.create(viewHolder.getDateView(), dateName);
//                                    Pair<View, String> iconPair = Pair.create(viewHolder.getIvIconView(), icon);
//                                    Pair<View, String> starPair = Pair.create(viewHolder.getIvStarView(), star);
//                                    @SuppressWarnings("unchecked")
//                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), datePair, iconPair, starPair);
//                                    ActivityCompat.startActivityForResult(getActivity(), detailIntent, AppConst.REQUEST_CODE_ACT_CARTD_DETAIL, options.toBundle());
//                                    ((MainActivity)getActivity()).startActivityWithAnim(MartHolidayMartDetailInfoActivity.class);
                                    }
                                });
                                cachingFavMartInfo.put(martInfo.getPointCode()+martInfo.getPointName(),martInfo);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Logger.d(TAG, "getFavMart:onCancelled " + databaseError.toException());
                            }
                        });

            }
        };
        mRecycler.setAdapter(mAdapter);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd(EEE)");
        String time = sdfNow.format(new Date(System.currentTimeMillis()));
        tvToday.setText(time);
//
//        Calendar calendar = Calendar.getInstance();
//        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
//        String todayWeek = AUtil.week[dayofWeek - 1] ;
//        if("토".equalsIgnoreCase(todayWeek)){
//            int color = getResources().getColor(R.color.fab_blue_color);
//            tvToday.setTextColor( color );
//        } else if ( "일".equalsIgnoreCase(todayWeek) ){
//            int color = getResources().getColor(R.color.red_color);
//            tvToday.setTextColor( color );
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter!=null && isRefreshAdapter){
            isRefreshAdapter = false;
            cachingFavMartInfo.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    OKPermission.PermissionListener permissionlistener = new OKPermission.PermissionListener() {
        @Override
        public void onPermissionGranted(ArrayList<String> deniedPermissions) {

                Intent i = new Intent(mContext, MartHolidayGPSSearchActivity.class);
                startActivity(i);
            OKCartApplication.martRestNearByClickCount++;
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }

    };

    View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(JavaUtil.isDoubleClick()) return ;

            if(v.getId() == R.id.btn_search){

                Bundle bundle2 = new Bundle();
                bundle2.putString(AppConst.FA_PARAM_NORMAL, "normal");
                // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_REST_GPS_SEARCH, bundle2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    new OKPermission(getActivity())
                            .setIsRetryPermRequest(true)
                            .setPermissionListener(permissionlistener)
                            .setDeniedTitleText(R.string.alert_title)
                            .setAllDeniedMessages("현재 위치검색(GPS)을 위한 권한이 필요합니다.")
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                } else {
                        Intent i = new Intent(mContext, MartHolidayGPSSearchActivity.class);
                        startActivity(i);
                    OKCartApplication.martRestNearByClickCount++;
                }
                return;
            }


            String martName = "emart";
            String martRegion = "emartRegionList";
                if(v.getId() == R.id.tv_mart_icon_emart){
                    martName = "emart";
                    martRegion = "emartRegionList";
                } else if (v.getId() == R.id.tv_mart_icon_homplus){
                    martName = "homeplus";
                    martRegion = "homeplusRegionList";
                } else if (v.getId() == R.id.tv_mart_icon_lottemart){
                    martName = "lottemart";
                    martRegion = "lottemartRegionList";
                } else if (v.getId() == R.id.tv_mart_icon_costco){
                    martName = "costco";
                    martRegion = "costco";
                }
            Intent i;
            if (v.getId() == R.id.tv_mart_icon_costco){
                i = new Intent(mContext, MartHolidayDetailMartListActivity.class);
            } else {
                i = new Intent(mContext, MartHolidayRegionSelectActivity.class);
            }
            i.putExtra("selectMartName",martName);
            i.putExtra("selectMartRegion",martRegion);
            startActivity(i);

            Bundle bundle2 = new Bundle();
            bundle2.putString(AppConst.FA_PARAM_MART_NAME, martName);
            // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_REST_MART_SELECT, bundle2);

        }
    };

    private void setNotice() {

    }

}
