package com.okitoki.checklist.ui.adapter;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-01-18.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ReloadRecyclerViewScrollListner extends RecyclerView.OnScrollListener{

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 1;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public ReloadRecyclerViewScrollListner() {
    }

    public ReloadRecyclerViewScrollListner(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setVisibleThreshold(int nVisibleThreshold) {
        visibleThreshold = nVisibleThreshold;
    }

    public ReloadRecyclerViewScrollListner(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    public void reset() {
        currentPage = 0;
        previousTotalItemCount = 0;
        totalItemCount = 0;
        firstVisibleItem = 0;
        loading = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            loading = true;
            // Do something
            currentPage++;

            onLoadMore(currentPage, totalItemCount);
        }

//        Logger.d("onScrolled", "loading = " + loading + " totalItemCount = " + totalItemCount + " previousTotalItemCount = "
//                + previousTotalItemCount + " visibleItemCount = " +visibleItemCount  + " firstVisibleItem = " + firstVisibleItem );
//        this.visibleItemCount = recyclerView.getChildCount();
//        this.totalItemCount = mLinearLayoutManager.getItemCount() - 2;
//        this.firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//
//        if (loading && (totalItemCount > previousTotalItemCount)) {
//            loading = false;
//            previousTotalItemCount = totalItemCount;
//            currentPage++;
//        }
//
//        if (loading) {
//            if ( (visibleItemCount + firstVisibleItem) >= totalItemCount) {
//                loading = false;
//                onLoadMore(currentPage + 1, totalItemCount);
//            }
//        }

        onScrolledExt(recyclerView,dx,dy);
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

    public abstract void onScrolledExt(RecyclerView recyclerView, int dx, int dy);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
    }

}
