package com.example.test.oktest.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.oktest.R;

import java.util.logging.Logger;

/**
 * 1) NestedScrollFragment
 * Created by ojungwon on 2015-09-19
 */
public class NestedScrollFragment extends BaseFragment implements NestedScrollView.NestedScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private NestedScrollView mScrollView;
    private TextView mTitleView;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if this is set true,
        // Activity.onCreateOptionsMenu will call Fragment.onCreateOptionsMenu
        // Activity.onOptionsItemSelected will call Fragment.onOptionsItemSelected
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        Log.d("Navigation", NestedScrollFragment.class.getSimpleName() + "=> onCreateOptionsMenu");
        inflater.inflate(R.menu.swipe_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Navigation", NestedScrollFragment.class.getSimpleName() + "=> onOptionsItemSelected");
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "NestedScrollFragment", Toast.LENGTH_SHORT).show();
//            mAdapter.add("NestedScrollFragment" + (mAdapter.getCount() + 1) );
//            mEmptyViewContainer.setRefreshing(false);
//            mAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {
        actionBar.hide();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setSubtitle(NestedScrollFragment.class.getSimpleName());

        String[] DropdownName = {getString(R.string.title_section1),
                getString(R.string.title_section2), getString(R.string.title_section3), getString(R.string.title_section4)};

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, DropdownName), new ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        return false;
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < 4; i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText("title" + i)
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                        }

                        @Override
                        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                        }

                        @Override
                        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                        }
                    }));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nestedscrollview, null);

        // Defatul Setting
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        // 시스템상 실제 액션바사이즈를 가져와야한다...;
        mActionBarSize = getResources().getDimensionPixelSize(R.dimen.flexible_space_actionbar);

        mScrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        mImageView = (ImageView) view.findViewById(R.id.image);
        mOverlayView = (View) view.findViewById(R.id.overlay);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = (TextView) view.findViewById(R.id.title);
        mTitleView.setText("NestedScrolling");

        ScrollViewUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                //onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                //mScrollView.scrollTo(0, 1);
                //mScrollView.scrollTo(0, 0);
            }
        });

        return view;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mImageView.getHeight();
//        mOverlayView.setTranslationY( ScrollViewUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        mImageView.setTranslationY(ScrollViewUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
//        mOverlayView.setAlpha( ScrollViewUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollViewUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        mTitleView.setPivotX(0);
        mTitleView.setPivotY(0);
        mTitleView.setScaleX(scale);
        mTitleView.setScaleY(scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        mTitleView.setTranslationY(titleTranslationY);

        Log.d("transitTollbar", "scrollY = " + scrollY + "  maxTitleTranslationY = " + maxTitleTranslationY + " mActionBarSize = " + mActionBarSize
         + " titleTranslationY = " + titleTranslationY);
        // Translate FAB
//        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
//        float fabTranslationY = ScrollViewUtils.getFloat(
//                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
//                mActionBarSize - mFab.getHeight() / 2,
//                maxFabTranslationY);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
//            // which causes FAB's OnClickListener not working.
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
//            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
//            lp.topMargin = (int) fabTranslationY;
//            mFab.requestLayout();
//        } else {
//            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
//            ViewHelper.setTranslationY(mFab, fabTranslationY);
//        }

//        // Show/hide FAB
//        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
//            hideFab();
//        } else {
//            showFab();
//        }
    }
}
