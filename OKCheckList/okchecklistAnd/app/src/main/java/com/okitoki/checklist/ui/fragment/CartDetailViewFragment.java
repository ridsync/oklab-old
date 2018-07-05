package com.okitoki.checklist.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.network.OnNetworkListener;
import com.okitoki.checklist.network.model.ResUserList;
import com.okitoki.checklist.ui.AddCartActivity_;
import com.okitoki.checklist.ui.adapter.CartDetailItemRecvAdapter;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.ui.components.OKDefaultToolTip;
import com.okitoki.checklist.ui.components.OKToolTip;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.PMWakeLock;
import com.okitoki.checklist.utils.PreferUtil;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;

/**
 * Created by ojungwon on 2015-04-08.
 */
@EActivity(R.layout.fragment_cart_detail)
public class CartDetailViewFragment extends CoreActivity implements AlertDialogFragment.OnClickListener, OnNetworkListener<ResUserList>, CartDetailItemRecvAdapter.ItemCheckedListener, CartDetailItemRecvAdapter.ItemClickListener{

    public static final String TAG = CartDetailViewFragment.class.getSimpleName();

    private static volatile CartDetailViewFragment cartDetailViewFragment;

    private void setInstance(CartDetailViewFragment instance) {
        if(instance != null) {
            cartDetailViewFragment = instance;
        }
    }

    public static CartDetailViewFragment getInstance() {
        if(cartDetailViewFragment == null) {
            synchronized (CartDetailViewFragment.class) {
                if(cartDetailViewFragment == null) {
                    cartDetailViewFragment = new CartDetailViewFragment();
                }
            }
        }
        return cartDetailViewFragment;
    }

    @ViewById
    Toolbar toolbar_actionbar;

    @ViewById
    public RelativeLayout rl_cart_detail_completed;

    @ViewById
    public TextView tv_cart_title, tv_cart_completed;
    @ViewById
    public TextView tv_cart_date;
    @ViewById
    public TextView tv_mart_icon;
    @ViewById
    public TextView tv_items_total_count , tv_items_select_count , tv_cart_detail_completed;

    @ViewById
    public ImageView iv_cart_modify, iv_cart_delete, iv_cart_sort_toggle;

    @ViewById
    public RecyclerView rcv_cd_cart_listView;

    @ViewById
    public com.github.clans.fab.FloatingActionButton fab;

    private DBManager dbMgr = null;

    private SlideInBottomAnimationAdapter slideInAdapter;
    private CartDetailItemRecvAdapter mAdapter;
    private List<T_CART_ITEM> items = new ArrayList<>();
    private T_CART_INFO cartInfo;

    public T_CART_INFO getCartInfo() {
        return cartInfo;
    }

    private int itemsTotalCount = 0 ;
    private int itemsCheckedCount = 0 ;

    private boolean isFirstLoading = true;
    private boolean isVisibleCompletedView;
    private boolean isTouring;
    private boolean isSortedCheckState = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.d("onEvent", "onCreate ");
        super.onCreate(savedInstanceState);
        setInstance(this);
        setEnterSharedElementCallback();
//        HashMap<String,String> params = new HashMap<>();
//        params.put("firstSeq", "30");
//        params.put("listId", "1");
//        NaverTask task = new NaverTask(this, 0, listener);
//        task.setParam(params);
//        NetManager.startTask(task);

    }

    @Override
    public void onStart() {
        Logger.d("onEvent", "onStart ");
        dbMgr = new DBManager();
        if (isFirstLoading){
            isFirstLoading= false;
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        PMWakeLock.releaseCpuLock(getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void  setEnterSharedElementCallback(){
//        getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
//        getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(this));
    }

    @Override
    public void onNetSuccess(ResUserList data, int reqType) {
        Logger.d(CartDetailViewFragment.this, "onNetSuccess  " + reqType + " / " + data.toString());
    }

    @Override
    public void onNetFail(int retCode, String strError, int reqType) {
        Logger.d(CartDetailViewFragment.this, "onNetFail  " + reqType + " / " +  retCode );
    }

    @Override
    public void onLoadingDialog(int reqType) {

    }

    @Override
    public void onLoadingDialogClose(int reqType) {

    }

    @Override
    public void onResume() {
        Logger.d("onEvent", "onResume ");
        super.onResume();
        PMWakeLock.acquireCpuWakeLockKeep(getApplicationContext());
    }

    @AfterViews
    public void init() {

//        isSortedCheckState = PreferUtil.getPreferenceBoolean("cartCheckedSort", getApplicationContext());
//        if(isSortedCheckState){
//            iv_cart_sort_toggle.setImageResource(R.drawable.ic_playlist_add_check_white_24dp);
//        } else {
//            iv_cart_sort_toggle.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
//        }

        Logger.d("onEvent", "@AfterViews ");
        // 리스트뷰의 아이템뷰들과 대응되는 View가 있으면 넣어준다. 애니효과 !!
        ViewCompat.setTransitionName(tv_cart_title, getString(R.string.transition_title));
        ViewCompat.setTransitionName(tv_mart_icon, getString(R.string.transition_body));
        ViewCompat.setTransitionName(tv_cart_date, getString(R.string.transition_date));

        fab.hide(false);

        toolbar_actionbar.setTitle(R.string.menu_mode_detaillist);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcv_cd_cart_listView.setHasFixedSize(true);
        rcv_cd_cart_listView.setItemAnimator(new DefaultItemAnimator());
        rcv_cd_cart_listView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CartDetailItemRecvAdapter(this, R.layout.item_view_cart_info_detail, items, this);
        mAdapter.setItemCheckedListener(this);
        slideInAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        slideInAdapter.setDuration(600);
        slideInAdapter.setFirstOnly(true);
        slideInAdapter.setInterpolator(new DecelerateInterpolator());
        rcv_cd_cart_listView.setAdapter(slideInAdapter);


        rcv_cd_cart_listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rcv_cd_cart_listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);// make sure this only run once

            boolean isTourCompleted = PreferUtil.getPreferenceBoolean(AppConst.PREF_TOUR1_COMPLETED, CartDetailViewFragment.this);
            if(!isTourCompleted)
                initTours();
            }
        });
    }

    public TourGuide mTutorialHandler;
    public Activity mActivity;
    private Animation mEnterAnimation, mExitAnimation, mToolEnterAnimation;
    public static int OVERLAY_METHOD = 1;
    public static int OVERLAY_LISTENER_METHOD = 2;

    private void initTours() {
        isTouring = true;
        mActivity = this;
        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);
        mEnterAnimation.setInterpolator(new DecelerateInterpolator());

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(100);
        mExitAnimation.setFillAfter(true);
        mExitAnimation.setInterpolator(new AccelerateInterpolator());

        mToolEnterAnimation = new TranslateAnimation(0f, 0f, 130f, 0f);
        mToolEnterAnimation.setDuration(300);
        mToolEnterAnimation.setFillAfter(true);
        mToolEnterAnimation.setInterpolator(new DecelerateInterpolator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFinishing() || rcv_cd_cart_listView ==null) return;

                ImageView serchView = (ImageView)rcv_cd_cart_listView.findViewHolderForLayoutPosition(0).itemView.findViewById(R.id.iv_btn_search);
                runOverlay_ContinueMethod(serchView);
                rl_cart_detail_completed.setVisibility(View.VISIBLE);
//        rl_cart_detail_completed.startAnimation(AnimationUtils.loadAnimation(this,R.anim.show_from_bottom));
                isVisibleCompletedView = true;
            }
        },300);
    }

    @Subscribe(sticky = true)
    public void onEvent(T_CART_INFO event) {
        Logger.d("onEvent", "Subscribe CartSelectedEvent");
        this.cartInfo = event;
        AUtil.setViewMartIcon(tv_mart_icon, event.getStore_id());
        tv_cart_title.setText(event.getCart_title());
        String sDate = AUtil.getDateExp(event.getBuy_date());
        tv_cart_date.setText(sDate);
//        tv_item_content_list.setText(event.getCartItems().toString());

        getCartItemDatas(cartInfo.getCart_id());

        setStatFABbyItemCheckedAll(false);

        EventBus.getDefault().unregister(this);
    }

    @Click(R.id.iv_cart_sort_toggle)
    public void onClickSortToggle(View v){

        if(JavaUtil.isDoubleClick()) return ;

//        if(isSortedCheckState){
//            iv_cart_sort_toggle.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
//            isSortedCheckState = false;
//            PreferUtil.setSharedPreference("cartCheckedSort", false, getApplicationContext());
//            itemIdSortList(items);
//        } else {
//            iv_cart_sort_toggle.setImageResource(R.drawable.ic_playlist_add_check_white_24dp);
            isSortedCheckState  = true;
//            PreferUtil.setSharedPreference("cartCheckedSort", true, getApplicationContext());
            sortItemList(items);
            slideInAdapter.notifyDataSetChanged();
            rcv_cd_cart_listView.smoothScrollToPosition(0);
//        }
    }
    @Click(R.id.iv_cart_modify)
    public void onClickModify(View v){

        if(JavaUtil.isDoubleClick()) return ;

        if (saveCartItemDatas()){
            Intent i = new Intent(this, AddCartActivity_.class);
            i.putExtra("cart_id", cartInfo.getCart_id());
            startActivityForResult(i, AppConst.REQUEST_CODE_ACT_MODIFY_CART);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {

        }
    }

    @Click({R.id.fab , R.id.tv_cart_completed})
    public void onClickGotCartAllItems(View v){

        if(JavaUtil.isDoubleClick()) return ;

        if(isTouring){
            mTutorialHandler.mOverlay.mOnClickListener.onClick(null);
        } else {
            // cartInfo 완료처리 아이템저장하고 넘어가기.
            if(saveCompletedBuyAllItems()){
                Intent intent = new Intent();
                intent.putExtra("refresh", false);
                setResult(RESULT_OK,intent);
                ActivityCompat.finishAfterTransition(this);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppConst.REQUEST_CODE_ACT_MODIFY_CART){
            if (resultCode == Activity.RESULT_OK){
                try {
                    if(cartInfo !=null && dbMgr != null){
                        cartInfo = dbMgr.getCartInfo(cartInfo.getCart_id());
                        cartInfo.refresh();
                        onEvent(dbMgr.getCartInfo(cartInfo.getCart_id()));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("refresh", false);
                    setResult(RESULT_OK,intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.iv_cart_delete)
    public void onClickDelete(View v){

        if(JavaUtil.isDoubleClick()) return ;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    dbMgr.deleteCartInfo(cartInfo);
                    setResult(RESULT_OK);
                    finish();
                    super.onPositiveActionClicked(fragment);
                }
                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };
            builder.title(getString(R.string.dialog_msg_delete_cartinfo))
                    .positiveAction(getString(R.string.btn_confirm))
                    .negativeAction(getString(R.string.fab_back));
            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), "test");
        } else {
            AlertDialogFragment.Builder dlgBuilder = new AlertDialogFragment.Builder(getApplicationContext(), R.style.DialogTheme);
            dlgBuilder.setTitle(R.string.alert_title);
            dlgBuilder.setMessage(R.string.dialog_msg_delete_cartinfo);
            dlgBuilder.setPositiveButton(R.string.btn_confirm, this);
            dlgBuilder.setNegativeButton(R.string.fab_back, this);
            dlgBuilder.setCancelable(false);
            dlgBuilder.show(getFragmentManager(), "Warning");
        }
    }

    @Override
    public void onClickPositive(AlertDialogFragment dialog) {
        dbMgr.deleteCartInfo(cartInfo);
        setResult(RESULT_OK);
        finish();
    }
    @Override
    public void onClickNegative(AlertDialogFragment dialog) {
    }
    @Override
    public void onClickNeutral(AlertDialogFragment dialog) {
    }

    @Override
    public void onBackPressed() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            final SharedElementCallback enterCallback = new EnterSharedElementCallback(this);
//            setEnterSharedElementCallback(new SharedElementCallback() {
//                @Override
//                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                        enterCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
//                    }
//                }
//
//                @Override
//                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        enterCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
//                    }
//                }
//            });
//        }

        // 저장하고 넘어가기.
        if(saveCartItemDatas()){
            Intent intent = new Intent();
            intent.putExtra("refresh", false);
            setResult(RESULT_OK, intent);
            ActivityCompat.finishAfterTransition(this);
        } else {
            super.onBackPressed();
        }
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
            setStatFABbyItemCheckedAll(true);
    }

    private synchronized void  setStatFABbyItemCheckedAll(boolean isPerformCheck){
        if (itemsCheckedCount >= itemsTotalCount){
            if(isPerformCheck){
                tv_cart_detail_completed.setText(R.string.detail_purchase_ask_comepleted);
                tv_cart_completed.setText(R.string.detail_btn_purchase_comepleted);
            } else {
                tv_cart_detail_completed.setText(R.string.detail_purchase_comepleted);
                tv_cart_completed.setText(R.string.btn_confirm);
            }

//            fab.show(true);
            rl_cart_detail_completed.setVisibility(View.VISIBLE);
            rl_cart_detail_completed.startAnimation(AnimationUtils.loadAnimation(this,R.anim.show_from_bottom));
            isVisibleCompletedView = true;
        } else {
            if (isVisibleCompletedView){
                rl_cart_detail_completed.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
                isVisibleCompletedView = false;
            }
//            fab.hide(true);
        }
        tv_items_select_count.setText(itemsCheckedCount + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_mode_delete:
                onClickDelete(null);
//                if(deleteCartInfo()){
//                    Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
//                    setResult(RESULT_OK);
//                    finish();
//                }
                return true;
            case R.id.menu_mode_modify:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getCartItemDatas(int cartId) {
        if(cartId <= 0) return;

        itemsCheckedCount = 0;

        items.clear();
        items.addAll(dbMgr.queryCartItemByCartInfoId(cartId,"item_id"));
        if(isSortedCheckState)
            sortItemList(items);

        itemsTotalCount = items.size();
        for (int i = 0; i < itemsTotalCount; i++) {
            if (items.get(i).isCheck_fl()){
                itemsCheckedCount++;
            }
        }
        slideInAdapter.notifyDataSetChanged();
        rcv_cd_cart_listView.scrollToPosition(0);

        tv_items_total_count.setText(itemsTotalCount+"");
    }

    private boolean saveCompletedBuyAllItems() {
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

            cartInfo.setIsGotCartAllitem(true);
            dbMgr.updateCartInfo(this.cartInfo);

            dbMgr.commit();

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

            if (itemsCheckedCount < itemsTotalCount) {
                cartInfo.setIsGotCartAllitem(false);
                cartInfo.setCheckedCount(itemsCheckedCount);
            } else {
                cartInfo.setIsGotCartAllitem(true);
                cartInfo.setCheckedCount(itemsTotalCount);
            }
            dbMgr.updateCartInfo(this.cartInfo);

            dbMgr.commit();
            result = true;
        }catch (Exception e){
            dbMgr.rollback();
        }finally {
            dbMgr.endTransaction();
        }
        return result;
    }

    private boolean deleteCartInfo() {
        boolean result = false;

        try {
            dbMgr.beginTransaction();
            dbMgr.deleteCartInfo(cartInfo);
            dbMgr.commit();
            result = true;
        }catch (Exception e){
            dbMgr.rollback();
        }finally {
            dbMgr.endTransaction();
        }
        return result;
    }

    private void runOverlay_ContinueMethod(ImageView serchView){
        // the return handler is used to manipulate the cleanup of all the tutorial elements
        TourGuide tourGuide1 = TourGuide.init(this)
                .setToolTip(new OKDefaultToolTip()
//                                .setEnterAnimation(mToolEnterAnimation)
                                .setTitle("카트수정")
                                .setDescription("현재 카트정보 수정화면으로 이동합니다.")
                                .setGravity( Gravity.LEFT)
//                                .setEnterAnimation(mEnterAnimation)
                                .setBackgroundColor(getResources().getColor(R.color.main_accent_color))
                )
                .playLater(iv_cart_modify);

        TourGuide tourGuide2 = TourGuide.init(this)
                .setToolTip(new OKToolTip()
//                                .setEnterAnimation(mToolEnterAnimation)
                                .setTitle("최저가 검색")
                                .setDescription("해당 아이템의 가격정보를 검색하는 화면으로 이동합니다.")
                                .setGravity(Gravity.BOTTOM | Gravity.LEFT)
                                .setBackgroundColor(getResources().getColor(R.color.main_accent_color))
                )
                .playLater(serchView);

        TourGuide tourGuide3 = TourGuide.init(this)
                .setToolTip(new OKToolTip()
//                                .setEnterAnimation(mToolEnterAnimation)
                                .setTitle("구매 완료")
                                .setDescription("체크리스트화면을 확인하며 실제 물품을 구매할때,\n전체 아이템이 체크되면 '구매완료'버튼으로 완료합니다.")
                                .setGravity(Gravity.TOP)
                                .setBackgroundColor(getResources().getColor(R.color.main_accent_color))
                )
                .setOverlay(new Overlay()
                        .setEnterAnimation(mEnterAnimation)
//                        .setExitAnimation(mExitAnimation)
                        .setBackgroundColor(getResources().getColor(R.color.transparent_black_fifty))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTutorialHandler.cleanUp();
                                PreferUtil.setSharedPreference(AppConst.PREF_TOUR1_COMPLETED, true, CartDetailViewFragment.this);
                                isTouring = false;
                                if (isVisibleCompletedView){
                                    rl_cart_detail_completed.startAnimation(AnimationUtils.loadAnimation(CartDetailViewFragment.this, R.anim.hide_to_bottom));
                                    isVisibleCompletedView = false;
                                }
                            }
                        })
                )
                        // note that there is not Overlay here, so the default one will be used
                .playLater(tv_cart_completed);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1,tourGuide2,tourGuide3)
                .setDefaultOverlay(new Overlay()
                                .setBackgroundColor(getResources().getColor(R.color.transparent_black_fifty))
                                .setEnterAnimation(mEnterAnimation)
//                                .setExitAnimation(mExitAnimation)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTutorialHandler.next();
                                    }
                                })
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.OverlayListener)
                .build();

        mTutorialHandler = TourGuide.init(this).playInSequence(sequence);

    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     */
    private void sortItemList(List<T_CART_ITEM> list) {
        if(list!=null && list.size()>0){
            // 증복제거
//            List<T_CART_ITEM> sliet = new ArrayList<T_CART_ITEM>(new HashSet<>(list));
//            list.clear();
//            list.addAll(sliet);
            // 정렬
            Collections.sort(list, new ChatSortComparator());
        }
    }

    private void itemIdSortList(List<T_CART_ITEM> list) {
        if(list!=null && list.size()>0){
            // 증복제거
//            List<T_CART_ITEM> sliet = new ArrayList<T_CART_ITEM>(new HashSet<>(list));
//            list.clear();
//            list.addAll(sliet);
            // 정렬
            Collections.sort(list, new ItemIdSortComparator());
        }
    }

    static class ItemIdSortComparator implements Comparator<T_CART_ITEM> {
        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(T_CART_ITEM lhs, T_CART_ITEM rhs) {
            return lhs.getItem_id() < rhs.getItem_id() ? 1 : (lhs.getItem_id() ==  rhs.getItem_id() ? 0 : -1);
        }

    }

    static class ChatSortComparator implements Comparator<T_CART_ITEM> {
        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(T_CART_ITEM lhs, T_CART_ITEM rhs) {
            int ret;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                ret = Boolean.compare(lhs.isCheck_fl(), rhs.isCheck_fl());
            } else {
                int b1 = rhs.isCheck_fl() ? 1 : 0;
                int b2 = lhs.isCheck_fl() ? 1 : 0;
                ret =  b2 - b1;
            }
            if (ret == 0 ){
              ret  = lhs.getItem_id() < rhs.getItem_id() ? 1 : (lhs.getItem_id() ==  rhs.getItem_id() ? 0 : -1);
            }
            return ret;
        }

    }
}
