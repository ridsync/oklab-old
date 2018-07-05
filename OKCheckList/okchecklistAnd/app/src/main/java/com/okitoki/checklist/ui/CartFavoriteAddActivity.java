package com.okitoki.checklist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.ui.adapter.CartFavoriteRecvAdapter;
import com.okitoki.checklist.ui.adapter.ReloadRecyclerViewScrollListner;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.utils.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by ojungwon on 2015-04-08.
 */
@EActivity(R.layout.activity_favorite_add)
public class CartFavoriteAddActivity extends CoreActivity implements CartFavoriteRecvAdapter.ItemCheckedListener, CartFavoriteRecvAdapter.ItemClickListener{

    public static final String TAG = CartFavoriteAddActivity.class.getSimpleName();

    @ViewById
    Toolbar toolbar_actionbar;

    @ViewById
    public TextView tv_cart_title;

    @ViewById
    public ImageView iv_cart_stt_move;

    @ViewById
    public RelativeLayout rl_cart_item_empty;

    @ViewById
    public RecyclerView rcv_cf_cart_listView;


    @ViewById
    public com.github.clans.fab.FloatingActionButton fab;

    private DBManager dbMgr = null;

    private SlideInBottomAnimationAdapter slideInAdapter;
    private CartFavoriteRecvAdapter mAdapter;
    private ReloadRecyclerViewScrollListner mScrollListener;

    private List<T_CART_ITEM> items = new ArrayList<>();
    private ArrayList<T_CART_ITEM> addItems = new ArrayList<>();
    private T_CART_INFO cartInfo;

    private int itemsTotalCount = 0 ;
    private int itemsCheckedCount = 0 ;
    private int listOffset = 0 ;
    private int listLimit = 40 ;

    private boolean isFirstLoading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.d("onEvent", "onCreate ");
        super.onCreate(savedInstanceState);
        dbMgr = new DBManager();
//        HashMap<String,String> params = new HashMap<>();
//        params.put("firstSeq", "30");
//        params.put("listId", "1");
//        NaverTask task = new NaverTask(this, 0, listener);
//        task.setParam(params);
//        NetManager.startTask(task);
    }

    @Override
    protected void onStart() {
        Logger.d("onEvent", "onStart ");
        if (isFirstLoading){
            isFirstLoading= false;
//            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        Logger.d("onEvent", "onResume ");
        super.onResume();
    }

    @AfterViews
    public void init() {

        Logger.d("onEvent", "@AfterViews ");
        // 리스트뷰의 아이템뷰들과 대응되는 View가 있으면 넣어준다. 애니효과 !!
        ViewCompat.setTransitionName(tv_cart_title, getString(R.string.transition_title));
//        ViewCompat.setTransitionName(tv_item_content_list, getString(R.string.transition_date));

        fab.hide(false);
        toolbar_actionbar.setTitle(R.string.menu_mode_favadd);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcv_cf_cart_listView.setHasFixedSize(true);
        rcv_cf_cart_listView.setItemAnimator(new DefaultItemAnimator());
        rcv_cf_cart_listView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CartFavoriteRecvAdapter(this, R.layout.item_view_cart_fav_add, items, this);
        mAdapter.setItemCheckedListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rcv_cf_cart_listView.setLayoutManager(layoutManager);

        mScrollListener = new ReloadRecyclerViewScrollListner(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (slideInAdapter == null)
                    return;
                Logger.d(CartFavoriteAddActivity.this, "=== SubList LoadMore ===");
                listOffset = listOffset + listLimit;
                getCartItemDatas(false);
            }

            @Override
            public void onScrolledExt(RecyclerView recyclerView, int dx, int dy){
//                Logger.d("onScrolledExt", "dy = " + dy);
//                mIsExecuteFadeAnim = true;
            }

        };
        rcv_cf_cart_listView.addOnScrollListener(mScrollListener);

        slideInAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        slideInAdapter.setDuration(600);
        slideInAdapter.setFirstOnly(true);
        slideInAdapter.setInterpolator(new DecelerateInterpolator());
        rcv_cf_cart_listView.setAdapter(slideInAdapter);

        setStatFABbyItemCheckedAll();

        cartInfo =  EventBus.getDefault().removeStickyEvent(T_CART_INFO.class);
        if (cartInfo != null) {
            tv_cart_title.setText(cartInfo.getCart_title());
            getCartItemDatas(true);
        } else {
            tv_cart_title.setText("새로운 장바구니 추가");
            getCartItemDatas(true);
        }
    }

//    @Subscribe(sticky = true)
//    public void onEvent(T_CART_INFO event) {
//        Logger.d("onEvent", "Subscribe CartSelectedEvent");
//        this.cartInfo = event;
//        tv_cart_title.setText(event.getCart_title());
//
////        getCartItemDatas( );
//
////        setStatFABbyItemCheckedAll();
//
//    }

    @Click(R.id.iv_cart_stt_move)
    public void onClickMoveStt(View v){

    }

    @Click(R.id.fab)
    public void onClickFabAddCartItem(View v){
        // cartInfo 완료처리 아이템저장하고 넘어가기.
        // TODO cartInfo가 있으면 이전화면으로 ResultOK 해서 데이터 넘겨주고
        // TODO 메인에서 왔으면 StartActivity CartAdd화면 으로 데이터 넘겨주자.
        if(saveCompletedBuyAllItems()){
            EventBus.getDefault().postSticky(addItems);
            if(cartInfo!=null){
                setResult(RESULT_OK);
                finish();
            }else {
                Intent i = new Intent(this, AddCartActivity_.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        setResult(RESULT_OK);
//        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // TODO CartItemRecvAdapter아이템 클릭리스너 구현
    // 데이터 모델 변경필요함 아이템은 필요없이 position만 넘기도록 해야 범용적으로 사용할듯
    @Override
    public void itemClicked(int position , T_CART_ITEM item, View view) {

    }

    @Override
    public void itemLongClicked(int position , T_CART_ITEM item) {

    }

    @Override
    public void itemChecked(int position, boolean isChecked) {
            if(isChecked){
                if (itemsCheckedCount<itemsTotalCount)
                    itemsCheckedCount++;
            } else {
                if (itemsCheckedCount > 0)
                    itemsCheckedCount--;
            }
            setStatFABbyItemCheckedAll();
    }

    private synchronized void  setStatFABbyItemCheckedAll(){
        if (itemsCheckedCount > 0){
            fab.show(true);
        } else {
            fab.hide(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_fav_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_mode_deselect:
                itemsCheckedCount = 0;
                listOffset = 0;
                for (int i = 0; i < itemsTotalCount; i++) {
                    if (items.get(i).isCheckView_fl()){
                        items.get(i).setCheckView_fl(false);
                    }
                }
                slideInAdapter.notifyDataSetChanged();
                rcv_cf_cart_listView.scrollToPosition(0);
                setStatFABbyItemCheckedAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getCartItemDatas(boolean isClear) {

        if(isClear) {
            items.clear();
            listOffset = 0;
        }
        List<T_CART_ITEM> data = dbMgr.getAllFavItemListPaging(listLimit, listOffset);
        if(data != null){
            items.addAll(data);
        }

        itemsTotalCount = items.size();

        if(isClear){
            itemsCheckedCount = 0;
            for (int i = 0; i < itemsTotalCount; i++) {
                if (items.get(i).isCheckView_fl()){
                    itemsCheckedCount++;
                }
            }
        }

        slideInAdapter.notifyDataSetChanged();
        if(isClear)
            rcv_cf_cart_listView.scrollToPosition(0);
//        tv_items_total_count.setText("수량 : "+itemsTotalCount);

        if(itemsTotalCount > 0){
            rl_cart_item_empty.setVisibility(View.GONE);
        } else {
            rl_cart_item_empty.setVisibility(View.VISIBLE);
        }

    }

    private boolean saveCompletedBuyAllItems() {
        boolean result = false;
        try {
            dbMgr.beginTransaction();
            // Items Insert
            if (cartInfo != null) { // 기존 카트정보
                for (int i = 0; i < items.size(); i++) {
                    T_CART_ITEM cartItem = items.get(i);
                    if (cartItem.isCheckView_fl()){
                        if (cartItem.getItem_id() > 0 ){
                            T_CART_ITEM newItem = new T_CART_ITEM();
                            newItem.setItem_name(cartItem.getItem_name());
                            newItem.setQty(1);
                            newItem.setCart_id(cartInfo.getCart_id());
                            newItem.setReg_date(new Date());
                            addItems.add(newItem);
                        }
                    }
                }
                dbMgr.commit();
            } else { // 신규 카트

                for (int i = 0; i < items.size(); i++) {
                    T_CART_ITEM cartItem = items.get(i);
                    if (cartItem.isCheckView_fl()){
                        T_CART_ITEM newItem = new T_CART_ITEM();
                        newItem.setItem_name(cartItem.getItem_name());
                        newItem.setQty(1);
                        addItems.add(newItem);
                    }
                }
            }
            result = true;
        }catch (Exception e){
            dbMgr.rollback();
        }finally {
            dbMgr.endTransaction();
        }
        return result;
    }

    private boolean saveCartItemDatas() {
        boolean result = false;

        try {
            dbMgr.beginTransaction();
            // Items Insert
//            T_CART_INFO cartInfo = new T_CART_INFO();
//            cartInfo.setCart_title(et_item_title.getText().toString());
//            cartInfo.setStore_id(tv_mart_icon.getText().toString()); //TODO 마트코드정의
//            cartInfo.setBuy_date(selectDay.getTime());
//            cartInfo.setReg_date(new Date());
//            dbMgr.insertCartInfo(cartInfo);

            for (int i = 0; i < items.size(); i++) {
                dbMgr.updateCartItem(items.get(i));
            }
            dbMgr.commit();
            result = true;
        }catch (Exception e){
            dbMgr.rollback();
        }finally {
            dbMgr.endTransaction();
        }
        return result;
    }

}
