package com.okitoki.checklist.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;
import com.okitoki.checklist.ui.ViewHolder.FavMartRestInfoListViewHolder;
import com.okitoki.checklist.ui.base.HolidayCoreActivity;
import com.okitoki.checklist.utils.JavaUtil;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by ojungwon on 2015-04-08.
 */
public class MartHolidayDetailMartListActivity extends HolidayCoreActivity {

    public static final String TAG = MartHolidayDetailMartListActivity.class.getSimpleName();

    protected FirebaseRecyclerAdapter<RestMartInfo, FavMartRestInfoListViewHolder> mAdapter;

    public MartHolidayDetailMartListActivity() {}

    String selectMart = null;
    String selectRegion = null;
    private SlideInBottomAnimationAdapter slideInAdapter;

    Toolbar toolbar_actionbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);


        // TODO 마트정보 확인 쿼리준비
        selectMart = getIntent().getStringExtra("selectMartName");
        selectRegion = getIntent().getStringExtra("selectMartRegion");

        setContentView(R.layout.activity_mart_detail_list);


        toolbar_actionbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        String martTitle = "이마트";
        int color = getResources().getColor(R.color.yellow_A700);
        if("emart".equalsIgnoreCase(selectMart) ){
            martTitle = "이마트";
            color = getResources().getColor(R.color.yellow_A700);
        } else if("homeplus".equalsIgnoreCase(selectMart)){
            martTitle = "홈플러스";
            color = getResources().getColor(R.color.fab_red_color);
        } else if("lottemart".equalsIgnoreCase(selectMart)){
            martTitle = "롯데마트";
            color = getResources().getColor(R.color.mart_color_lotte);
        } else if("costco".equalsIgnoreCase(selectMart)){
            martTitle = "코스트코";
            color = getResources().getColor(R.color.main_primary_dark_color);
        }
//        toolbar_actionbar.setBackgroundColor(color);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(color);
//        }

        if("costco".equalsIgnoreCase(selectMart)){
            toolbar_actionbar.setTitle(martTitle + " 지점리스트 (12개 매장)");
        } else {
            toolbar_actionbar.setTitle(martTitle + " 지점리스트 ( " + selectRegion + " )");
        }

        mRecycler = (RecyclerView) findViewById(R.id.rcv_cf_mart_listView);
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        // Set up FirebaseRecyclerAdapter with the Query
        Query RestEmartsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<RestMartInfo, FavMartRestInfoListViewHolder>(RestMartInfo.class, R.layout.item_mart_detail_list,
                FavMartRestInfoListViewHolder.class, RestEmartsQuery) {
            @Override
            protected void populateViewHolder(final FavMartRestInfoListViewHolder viewHolder, final RestMartInfo model, final int position) {
                final DatabaseReference restMartRef = getRef(position);

                // Determine if the current user has liked this post and set UI accordingly
                if (model.favusers.containsKey(getUid())) {
                    viewHolder.ivFavMark.setImageResource(R.drawable.ic_favorites_on);
                } else {
                    viewHolder.ivFavMark.setImageResource(R.drawable.ic_favorites_off);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(true, model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 1) Each MartList Update
                        if(JavaUtil.isDoubleClick()) return ;

                        if(model.getMartCode() == RestUtil.MART_CODE_COSTCO){
                            DatabaseReference martListRef= mDatabase.child(selectMart).child(restMartRef.getKey());
                            onFavClicked(martListRef);
                        } else {
                            DatabaseReference martListRef= mDatabase.child(selectMart).child(selectRegion).child(restMartRef.getKey());
                            onFavClicked(martListRef);
                        }

                        // 2) usersfavmartlist Update
                        if (model.favusers.containsKey(getUid())) {
                            DatabaseReference favRef = mDatabase.getRef()
                                    .child("usersfavmartlist").child(getUid()).child(restMartRef.getKey());
                            favRef.removeValue();
                        } else {
                            writeNewPost(restMartRef.getKey(), model);

                            OKCartApplication.martRestAddCount++;

                        }

                        MartHolidayMainFavListFragment.isRefreshAdapter = true;

                    }
                });

                model.setRefkey(restMartRef.getKey());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(JavaUtil.isDoubleClick()) return ;

                        EventBus.getDefault().postSticky(model);
                        Intent i = new Intent(mContext, MartHolidayMartDetailInfoActivity.class);
                        startActivityForResult(i,0);
                        overridePendingTransition(0,0);
                    }
                });

            }
        };

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(false);
        mManager.setStackFromEnd(false);
        mRecycler.setLayoutManager(mManager);
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
        Query recentPostsQuery;
        if("costco".equalsIgnoreCase(selectMart) ){
            recentPostsQuery = databaseReference.child(selectMart).orderByChild("pointName");;
        } else {
            recentPostsQuery = databaseReference.child(selectMart).child(selectRegion).orderByChild("pointName");
        }
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

    private void writeNewPost(String key, RestMartInfo postData) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
//        String key = mDatabase.child("userfavmart").push().getKey();
        String uId = getUid();

        Map<String, Object> postValues = postData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/usersfavmartlist/" + uId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


    // [START post_stars_transaction]
    private void onFavClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                RestMartInfo p = mutableData.getValue(RestMartInfo.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.favusers.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.favusersCount = p.favusersCount - 1;
                    p.favusers.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.favusersCount = p.favusersCount + 1;
                    p.favusers.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]
}
