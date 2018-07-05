package com.okitoki.checklist.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.floatingactionmenu.FadingBackgroundView;
import com.flask.floatingactionmenu.FloatingActionButton;
import com.flask.floatingactionmenu.FloatingActionMenu;
import com.flask.floatingactionmenu.FloatingActionToggleButton;
import com.flask.floatingactionmenu.OnFloatingActionMenuSelectedListener;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.ui.AddCartActivity_;
import com.okitoki.checklist.ui.CartFavoriteAddActivity_;
import com.okitoki.checklist.ui.adapter.MainCartRecvAdapter;
import com.okitoki.checklist.ui.base.BaseFragment;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.PreferUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by ojungwon on 2015-04-08.
 */
@EFragment(R.layout.fragment_main_cart_recyclerview)
public class MainCartListViewFragment extends BaseFragment implements MainCartRecvAdapter.ItemClickListener {

    public static final String TAG = MainCartListViewFragment.class.getSimpleName();

//    @ViewById
//    SwipeRefreshLayout swipeRefreshLayout;

    @ViewById
    RecyclerView recycler_view;
    SlideInBottomAnimationAdapter slideInAdapter;
    @ViewById
    FadingBackgroundView fadingBg;

    @ViewById
    ImageView iv_cart_empty;

    @ViewById
    TextView tv_cart_empty;

    @ViewById
    FloatingActionMenu fam;

    @ViewById
    public FloatingActionToggleButton fab_toggle;
    public OnFAMSelectedListener famSelectedListener;

    private ArrayList<T_CART_INFO> mCartInfos = new ArrayList<>();

    private DBManager dbMgr = null;
    private boolean isGridLayout;

    private ListItemDecoration  listItemdeco = null;
    private GridItemDecoration gridItemdeco = null;
    private boolean isTouring = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setExitSharedElementCallback(new EnterSharedElementCallback(getActivity()));

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            Transition fade = new Fade();
//            fade.excludeTarget(android.R.id.navigationBarBackground, true);
//            fade.excludeTarget(android.R.id.statusBarBackground, true);
//            setExitTransition(fade);
//        }

//        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
//
//        for (int i = 0; i < accounts.length; i++) {
//            Logger.d("accounts", "accounts : " + accounts[i].toString());
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    public class ListItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public ListItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
//            outRect.left = space;
//            outRect.right = space;
//            outRect.bottom = space;
//            outRect.top = space;

//            // Add top margin only for the first item to avoid double space between items
//            if(parent.getChildLayoutPosition(view) == 0)
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        listItemdeco = new ListItemDecoration(spacingInPixels);
        gridItemdeco = new GridItemDecoration(spacingInPixels);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public class GridItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public GridItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
//            outRect.left = space;
//            outRect.right = space;
            outRect.bottom = space / 2;
//            outRect.top = space;

//            // Add top margin only for the first item to avoid double space between items
//            if(parent.getChildLayoutPosition(view) == 0)
            int position = parent.getChildLayoutPosition(view);
            if(position % 2 == 0 ){
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                outRect.left = space / 2;
                outRect.right = space;
            }
        }
    }

    @AfterViews
    public void afterViews(){

        View root = getView();
//        onCreateSwipeToRefresh(swipeRefreshLayout);
        // RecyclerView
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
            int resId = R.layout.item_view_main_cart;
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view.removeItemDecoration(gridItemdeco);
            recycler_view.addItemDecoration(listItemdeco);
//        recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        final MainCartRecvAdapter mAdapter = new MainCartRecvAdapter(getActivity(), resId,  mCartInfos, this);

        slideInAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        slideInAdapter.setDuration(600);
        slideInAdapter.setFirstOnly(true);
        slideInAdapter.setInterpolator(new DecelerateInterpolator());

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isShowFam = false;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Logger.d("TAG", " dy = " + dy);
                if (Math.abs(dy) > 0 ){
                    if(isShowFam){
                        fab_toggle.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out));
                        isShowFam = false;
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && ! isShowFam ){
                    Logger.d("TAG", " SCROLL_STATE_IDLE "+ newState);
                    fab_toggle.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_in));
                    isShowFam = true;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recycler_view.setAdapter(slideInAdapter);
        // TODO ItemClickListener 대체용이지만  버그존재 : 내부 ClickListener를 가진 뷰의 클릭이후 클릭되버리는 문제... ㅡ,ㅢ;
//        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                toast("onItemLongClick " + position);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//                // ...
//                toast("onItemLongClick " + position);
//            }
//        }));
        // FAB Menu Setting
        fam = (FloatingActionMenu) root.findViewById(R.id.fam);
//        fam.setLabelText("");
        fam.setFadingBackgroundView(fadingBg);
        fadingBg.setBackgroundResource(R.color.transparent_black_five);
        fadingBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (famSelectedListener != null)
                    famSelectedListener.setOnFloatingActionMenuSelectedListener(null);
                return false;
            }
        });
        fam.setOnFloatingActionMenuSelectedListener(new OnFloatingActionMenuSelectedListener() {
            @Override
            public void onFloatingActionMenuSelected(FloatingActionButton fab) {
                if (fab instanceof FloatingActionToggleButton) {
//                    FloatingActionToggleButton fatb = (FloatingActionToggleButton) fab;
//                    if (fatb.isToggleOn()) toast(fab.getLabelText());
                } else {
//                    toast(fab.getLabelText());
//                    fam.removeFloatingActionButton(fab);
                }

                if (famSelectedListener != null)
                    famSelectedListener.setOnFloatingActionMenuSelectedListener(fab);


            }
        });

//        FloatingActionButton fabSearchNaver = (FloatingActionButton) root.findViewById(R.id.fab_main_cart_search_naver);
//        fabSearchNaver.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onFabSearchNaver();
//            }
//        });

        FloatingActionButton fabCarAdd = (FloatingActionButton) root.findViewById(R.id.fab_main_cart_add);
        fabCarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabCartAdd();
            }
        });

//        FloatingActionButton fabFavAdd = (FloatingActionButton) root.findViewById(R.id.fab_main_cart_favorite_add);
//        fabFavAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onFabCartFavAdd();
//            }
//        });

        getCartInfosData(true);

    }

    private void getCartInfosData(boolean refresh) {
        dbMgr = new DBManager();
        ArrayList<T_CART_INFO> list = (ArrayList<T_CART_INFO>)dbMgr.getAllCartInfoDesc("reg_date");
        if (list != null && list.size() > 0){
            iv_cart_empty.setVisibility(View.GONE);
            tv_cart_empty.setVisibility(View.GONE);
            mCartInfos.clear();
            mCartInfos.addAll(list);
            slideInAdapter.notifyDataSetChanged();

           if(refresh)
               recycler_view.scrollToPosition(0);

        } else {
            iv_cart_empty.setVisibility(View.VISIBLE);
            tv_cart_empty.setVisibility(View.VISIBLE);
            // TODO 데이터 없음 표시
        }
    }

//    @Click(R.id.fab_main_cart_add)
    public void onFabCartAdd(){

        Bundle bundle2 = new Bundle();
        bundle2.putString(AppConst.FA_PARAM_NORMAL, "normaltest");
        // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_CART_ADD, bundle2);

//            // TODO 하단우측으로부터 대각선으로 나오는 애니?
            Intent intent = new Intent(getActivity(), AddCartActivity_.class);
            startActivityForResult(intent, AppConst.REQUEST_CODE_ACT_ADD_CART);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab_toggle.toggle();
                }
            },200);
        // 서비스 테스트
//        getActivity().startService(new Intent(getActivity(), WindowsTopViewService.class));

        }

    public void onFabCartFavAdd(){

        // TODO 하단우측으로부터 대각선으로 나오는 애니?
        EventBus.getDefault().removeStickyEvent(T_CART_INFO.class);
        Intent i = new Intent(getActivity(), CartFavoriteAddActivity_.class);
        startActivityForResult(i, AppConst.REQUEST_CODE_ACT_ADD_CART);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab_toggle.toggle();
            }
        },200);

        Bundle bundle2 = new Bundle();
        bundle2.putString(AppConst.FA_PARAM_NORMAL, "normaltest");
        // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_FAV_CART_ADD, bundle2);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        getActivity().stopService(new Intent(getActivity(), WindowsTopViewService.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            boolean isRefresh = true;
            if(data!=null)
                isRefresh = data.getBooleanExtra("refresh",true);
            getCartInfosData(isRefresh);
            AUtil.hideIME(getActivity().getApplicationContext(), tv_cart_empty);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    // TODO MainCartRecvAdapter 아이템 클릭리스너 구현
    // 데이터 모델 변경필요함 아이템은 필요없이 position만 넘기도록 해야 범용적으로 사용할듯
    // TODO RecyclerViewAdapter 를 범용적으로 만들어서 각 화면별로 공통적용 할 필요가있나?
    @Override
    public void itemClicked(int position , T_CART_INFO item) {

        if(JavaUtil.isDoubleClick()) return;

        // Data Transfer
        EventBus.getDefault().postSticky(item);

        Intent i = new Intent(getActivity(), AddCartActivity_.class);
        i.putExtra("cart_id", item.getCart_id());
        startActivityForResult(i, AppConst.REQUEST_CODE_ACT_MODIFY_CART);

    }

    @Override
    public void itemLongClicked(int position , T_CART_INFO item) {
//        toast("onItemLongClick " + position);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_menu, menu);
//        boolean isGridLayout = PreferUtil.getPreferenceBoolean(AppConst.PREFERENCE_MAIN_LIST_TYPE,getActivity());
//        if(isGridLayout){
//            menu.getItem(0).setIcon(R.drawable.ic_list_white_48dp);
//        } else {
//            menu.getItem(0).setIcon(R.drawable.ic_view_module_white_48dp);
//        }
//        MenuItem menuItem = menu.getItem(0);
//        button = (ImageView) menuItem.getActionView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.menu_mode_delete:
////                Toast.makeText(getActivity().getApplicationContext(), "menu_mode_delete", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_align:
////                Toast.makeText(getActivity().getApplicationContext(), "menu_align", Toast.LENGTH_SHORT).show();
//                reloadActionButton(item);
//                break;
//        }
        return true;
    }

    private void reloadActionButton(MenuItem item){
        if (isGridLayout){
            isGridLayout = false;
            item.setIcon(R.drawable.ic_view_module_white_48dp);
            PreferUtil.setSharedPreference(AppConst.PREFERENCE_MAIN_LIST_TYPE , false , getActivity());
        } else {
            isGridLayout = true;
            item.setIcon(R.drawable.ic_list_white_48dp);
            PreferUtil.setSharedPreference(AppConst.PREFERENCE_MAIN_LIST_TYPE, true, getActivity());
        }
        afterViews();
    }

    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
//        refreshLayout.setOnRefreshListener(getActivity());
//        refreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_green_light,
//                android.R.color.holo_red_light);
    }

//    @Override
//    public void onRefresh() {
//        Log.d("OptionsMenu", "onRefresh");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                // ListView Item Update
//                // Get the last king position
//                int lastKingIndex = mAdapter.getItemCount() - 1;
//                // If there is a king
//                if (lastKingIndex > -1) {
//                    // Remove him
//                    mAdapter.remove();
//                    swipeRefreshLayout.setRefreshing(false);  // Refresh Finished
//                    Log.d("OptionsMenu", "onRefresh mAdapter.remove");
//                } else {
//                    // No-one there, add new ones
//                    mAdapter.addAll();
////                    mEmptyViewContainer.setRefreshing(false);
//                    Log.d("OptionsMenu", "onRefresh mAdapter.addAll");
//                }
//                // Notify adapters about the kings
//                mAdapter.notifyDataSetChanged();
//            }
//        }, 6000);
//    }

    private void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setOnFAMSelectedListener (OnFAMSelectedListener listener ){
        famSelectedListener = listener;
    }

    public interface OnFAMSelectedListener{
         void setOnFloatingActionMenuSelectedListener (com.flask.floatingactionmenu.FloatingActionButton floatingActionButton);
    }

}
