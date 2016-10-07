package com.example.test.oktest.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.oktest.R;

import java.util.Arrays;
import java.util.Vector;

/**
 * 1) SwipeRefreshLayout https://github.com/AndroidExamples/SwipeRefreshLayout-ListViewExample
 * Created by ojungwon on 2014-10-01.
 */
public class SwipeRereshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mListViewContainer;
    private SwipeRefreshLayout mEmptyViewContainer;
    private ArrayAdapter<String> mAdapter;

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
        Log.d("Navigation", SwipeRereshFragment.class.getSimpleName() + "=> onCreateOptionsMenu");
        inflater.inflate(R.menu.swipe_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Navigation", SwipeRereshFragment.class.getSimpleName() + "=> onOptionsItemSelected");
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "AutoScrollTest", Toast.LENGTH_SHORT).show();
            mAdapter.add("AutoScrollTest" + (mAdapter.getCount() + 1) );
            mEmptyViewContainer.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setSubtitle(SwipeRereshFragment.class.getSimpleName());

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

        View view = inflater.inflate(R.layout.activity_swiperefreshview, null);

        // SwipeRefreshLayout
        mListViewContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_listView);
        mEmptyViewContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_emptyView);
        // Configure SwipeRefreshLayout
        onCreateSwipeToRefresh(mListViewContainer);
        onCreateSwipeToRefresh(mEmptyViewContainer);
        // Adapter
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
        // ListView
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setEmptyView(mEmptyViewContainer);
        listView.setAdapter(mAdapter);
        return view;
    }

    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get the last king position
                int lastKingIndex = mAdapter.getCount() - 1;
                // If there is a king
                if(lastKingIndex > -1) {
                    // Remove him
                    mAdapter.remove(mAdapter.getItem(lastKingIndex));
                    mListViewContainer.setRefreshing(false);
                }else {
                    // No-one there, add new ones
                    mAdapter.addAll(new Vector(Arrays.asList(getResources().getStringArray(R.array.kings))));
                    mEmptyViewContainer.setRefreshing(false);
                }
                // Notify adapters about the kings
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}
