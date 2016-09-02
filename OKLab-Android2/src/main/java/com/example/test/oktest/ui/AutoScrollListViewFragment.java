package com.example.test.oktest.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.oktest.R;

import java.util.Arrays;
import java.util.Vector;

/**
 * AutoScroll ListView  https://gist.github.com/jpardogo/70143625771697844896
 * 겔러리뷰 AutoScroll 잘만든것 참고 https://github.com/jpardogo/ListBuddies
 *
 * Created by ojungwon on 2014-10-01.
 */
public class AutoScrollListViewFragment extends BaseFragment implements View.OnTouchListener, AbsListView.OnScrollListener {

    private ListView mListView;
    private ListViewAutoScrollHelper  mScrollHelper ;
    private ArrayAdapter<String> mAdapter;
    private int TOTAL_ITEMS = 200;
    private boolean mActionDown;
    private boolean isListViewScrolling = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_listview, null);

        mListView = (ListView) view.findViewById(android.R.id.list);
        // Adapter
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        // ListView
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mListView.setOnTouchListener(this);
//        mListView.setSelection(TOTAL_ITEMS / 2);

        initScrollHelper();
        startAutoScroll();

//        final ListViewAutoScrollHelper scrollHelper = new ListViewAutoScrollHelper(mListView);
//        scrollHelper.setEnabled(true);
//        mListView.setOnTouchListener(scrollHelper);

        return view;
    }

    private void initScrollHelper() {
        mScrollHelper = new ListViewAutoScrollHelper (mListView) {
            @Override
            public void scrollTargetBy(int deltaX, int deltaY) {
                mListView.smoothScrollBy(2, 0);
            }
        };
        mScrollHelper.setEnabled(true);
        mScrollHelper.setEdgeType(AutoScrollHelper.EDGE_TYPE_OUTSIDE); // Option Type 3
    }


    private void startAutoScroll() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                forceScroll();
            }
        });
    }

    private void forceScroll() {
        MotionEvent event = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_MOVE, mListView.getWidth()/2, -1, 0);
        mScrollHelper.onTouch(mListView, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionDown=true;
                break;
            case MotionEvent.ACTION_UP:
                mActionDown=false;
                break;
        }
        return mScrollHelper.onTouch(v, event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                Log.d( AutoScrollListViewFragment.class.getSimpleName(),"onScrollStateChanged" + "=> SCROLL_STATE_IDLE");
                if(!mActionDown){
                    forceScroll();
                }
                isListViewScrolling = false;
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                isListViewScrolling = true;
                Log.d(AutoScrollListViewFragment.class.getSimpleName(), "onScrollStateChanged" + "=> SCROLL_STATE_TOUCH_SCROLL");
                break;
            case SCROLL_STATE_FLING:
                isListViewScrolling = true;
                Log.d(AutoScrollListViewFragment.class.getSimpleName(), "onScrollStateChanged" + "=> SCROLL_STATE_FLING");
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("Navigation", AutoScrollListViewFragment.class.getSimpleName() + "=> onOptionsItemSelected");
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
            menu.clear();
            inflater.inflate(R.menu.autoscroll_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Navigation", AutoScrollListViewFragment.class.getSimpleName() + "=> onOptionsItemSelected");

        if (item.getItemId() == R.id.action_autoscroll) {
            Toast.makeText(getActivity(), " action_autoscroll. on AutoScrollListViewFragment", Toast.LENGTH_SHORT).show();
            if(isListViewScrolling){
                mScrollHelper.setEnabled(false);
            }else {
                mScrollHelper.setEnabled(true);
                mListView.smoothScrollBy(1, 0);
                forceScroll();
            }

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
