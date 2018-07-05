package com.okitoki.checklist.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.ui.MainActivity;
import com.okitoki.checklist.ui.base.BaseFragment;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.PreferUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainCartFragment extends BaseFragment {
    /***************************
     * Static Member Variable
     ***************************/
    private static volatile MainCartFragment sMainCartFragment;
    public static MainCartFragment getInstance() {
        return sMainCartFragment;
    }

    private void setInstance(MainCartFragment instance) {
        if (instance != null) {
            sMainCartFragment = instance;
        }
    }

    /***************************
     * Android's Variable
     ***************************/
    @Bind(R.id.VP_MAIN_VIEWPAGER)
    ViewPager vpMainViewPager;
    MainFragmentPagerAdapter mAdapter;

    BaseFragment cartListFragment = new MainCartListViewFragment_();
    MartHolidayMainFavListFragment favMartListFragment = new MartHolidayMainFavListFragment();

    /***************************
     * Logical Member Variable
     ***************************/

    private boolean mIsLoading = false;
    private android.support.design.widget.CoordinatorLayout coordinatorLayout;
    private android.support.design.widget.AppBarLayout appBar;
    public MainCartFragment() {
    }


    /***************************
     * Android LifeCycle
     ***************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d(this, " ++ onCreateView ++" + this.toString());
        if (mRootView == null) {
            setInstance(this);
            mRootView = inflater.inflate(R.layout.layout_main_fragment, container, false);
            ButterKnife.bind(this, mRootView);
            initFirst();
//            coordinatorLayout = (CoordinatorLayout) mRootView.findViewById(R.id.coordinatorLayout);
//            appBar = (AppBarLayout) mRootView.findViewById(R.id.appbar);
        }
        else {
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//            behavior.onRestoreInstanceState(coordinatorLayout, appBar, toolbarState);
        }
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSelectedListener");
        }
    }

    @Override
    public void onResume() {
        Logger.d(this, " ++ onResume ++");
        super.onResume();
         sendScreenToGA("메인리스트");
    }

    int axe = 0 ;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        toolbarState = behavior.onSaveInstanceState(coordinatorLayout, appBar );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = mAdapter.getItem(tabLayout.getSelectedTabPosition());
        if (fragment instanceof MainCartListViewFragment_ && cartListFragment != null){
            cartListFragment.onActivityResult(requestCode, resultCode, data);
            ((MainActivity)getActivity()).setToolbarEnabled(true);
        } else if(fragment instanceof MartHolidayMainFavListFragment){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***************************
     * Member Methods or Others
     ***************************/
    private void initFirst() {

        setupTablayout();
        setupMainViewPager();

        int mainPosi = PreferUtil.getPreferenceInteger(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,mContext);
        if (vpMainViewPager != null){
            vpMainViewPager.setCurrentItem(mainPosi);
        }
    }

    private void setupTablayout() {

        if (tabLayout == null) {
            tabLayout = (TabLayout) mRootView.findViewById(R.id.tabLayout);
        }
        tabLayout.removeAllTabs();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_white_color));
        tabLayout.setTabTextColors(getResources().getColor(R.color.facebook_bg), getResources().getColor(R.color.tabayout_font_selected));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_txt_popular)));

    }

    private void setupMainViewPager() {
        if (tabLayout == null || mRootView == null) return;

        FragmentManager manager ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            manager =  getChildFragmentManager();
        } else {
            manager =  getFragmentManager();
        }
//        vpMainViewPager = (ViewPager) mRootView.findViewById(R.id.VP_MAIN_USER_LIST);
        mAdapter = new MainFragmentPagerAdapter(manager, tabLayout.getTabCount());
        vpMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        vpMainViewPager.setAdapter(mAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (vpMainViewPager == null) return;
                vpMainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if ( position == 2 ) {
//                            AUtil.showIME(getActivity(),vpMainViewPager);
//                        } else {
//                            AUtil.hideIME(getActivity(),vpMainViewPager);
//                        }
//                    }
//                },500);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_PAGE_ITEMS = 0;

        public MainFragmentPagerAdapter(FragmentManager fragmentManager, int tabItemCount) {
            super(fragmentManager);
            NUM_PAGE_ITEMS = tabItemCount;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_PAGE_ITEMS;
        }

        // Returns the fragment to display for that page
        // Adapter내부 인스턴스가 필요한경우만 호출됨. Destroy를 안하도록했기에 최초1회만 호출.
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = cartListFragment;
                    break;
                case 1:
                    fragment = favMartListFragment;
                    break;
                case 2:
                    fragment = new ProductInfoSearchFragment_();
                    break;
            }
            Logger.d("MainFragmentPagerAdapter", "getItem position = " + position + "fragment = " + fragment);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            if ( object instanceof  MartHolidayMainFavListFragment) {
                Log.d("MainCart", "FirebaseAuth:getItemPosition: instanceof");
                return POSITION_NONE;
            } else {
                return super.getItemPosition(object);
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            Logger.d("MainFragmentPagerAdapter", "destroyItem position = " + position);
        }
    }

    @Override
    public void onDestroy() {
        // very important:
        Logger.d(this, "Destroying helper.");
        super.onDestroy();
    }

    private void setLoading(boolean bLoading) {
        mIsLoading = bLoading;
        if (mRootView != null) {
            mRootView.findViewById(R.id.RL_USERLIST_MAIN_PROGRESS).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            mRootView.findViewById(R.id.RL_USERLIST_MAIN_PROGRESS).setVisibility(bLoading ? View.VISIBLE : View.GONE);
            mRootView.findViewById(R.id.PB_USERLIST_MAIN_PROGRESS).setVisibility(bLoading ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isLoading() {
        return mIsLoading;
    }

}
