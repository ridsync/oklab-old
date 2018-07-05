package com.okitoki.checklist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;
import java.util.Date;

public class MainCartRecvAdapter extends RecyclerView.Adapter<MainCartRecvAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<T_CART_INFO> mDataset;
    private ItemClickListener itemClickListener;
    private DBManager dbMgr = null;
    private int layoutId;

    public MainCartRecvAdapter(Context context, int layoutId , ArrayList<T_CART_INFO> dataset, @NonNull ItemClickListener itemClickListener) {
        mContext = context;
        mDataset = dataset;
        this.layoutId = layoutId;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
        dbMgr = new DBManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (Build.VERSION_CODES.LOLLIPOP >= Build.VERSION.SDK_INT){
//            layoutId = R.layout.item_view_main_cart_over_v21;
//        } else {
//            layoutId = R.layout.item_view_main_cart;
//        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final T_CART_INFO cartInfo = mDataset.get(position);
        final String title = cartInfo.getCart_title();
        String storeIcon = cartInfo.getStore_id();
        AUtil.setViewMartIcon(viewHolder.mIconView, storeIcon);
        viewHolder.mTitleView.setText(title);
        viewHolder.mPriceView.setText( cartInfo.getPrices());
        viewHolder.mLocationView.setText("위치:" + cartInfo.getLocation());
        viewHolder.mAddressView.setText( "주소:" + cartInfo.getAddress() );
        viewHolder.mDateView.setText( "이사가능날짜:" + cartInfo.getMigrateDate() );
        viewHolder.mTotalScoreView.setText( "TOTAL : " + cartInfo.getTotalScore() );
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T_CART_INFO getItem(int index) {
        return mDataset.get(index);
    }

    public void addAll(ArrayList<T_CART_INFO> dataset) {
       mDataset = dataset;
    }

    public void remove(String obj) {
//        mDataset.
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        public android.support.v7.widget.CardView mItemCardView;
        public RelativeLayout mItemLayout;
        public TextView mIconView;
        public TextView mTotalScoreView;
        public TextView mTitleView;
        public TextView mPriceView;
        public TextView mLocationView;
        public TextView mAddressView;
        public TextView mDateView;
        public TextView mContentView;

        public ViewHolder(View v) {
            super(v);
            mItemCardView = (android.support.v7.widget.CardView) v.findViewById(R.id.cv_citem_layout);
            mItemLayout = (RelativeLayout) v.findViewById(R.id.rl_item_layout);
//            RelativeLayout ripple = (RelativeLayout) v.findViewById(R.id.rl_item_layout);
            mIconView = (TextView) v.findViewById(R.id.iv_mart_icon);
            mLocationView = (TextView) v.findViewById(R.id.tv_item_location);
            mAddressView = (TextView) v.findViewById(R.id.tv_item_address);
            mTitleView = (TextView) v.findViewById(R.id.tv_item_title);
            mTotalScoreView = (TextView) v.findViewById(R.id.tv_item_total_score);
            mPriceView = (TextView) v.findViewById(R.id.tv_item_price);
            mDateView = (TextView) v.findViewById(R.id.tv_item_date);
            mContentView = (TextView) v.findViewById(R.id.tv_item_content_list);
            mItemLayout.setOnClickListener(this);
            mItemLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("test", "onClick " + getLayoutPosition() + " ");
            Logger.d(this, "Logger Click " + getLayoutPosition() + " ");
            itemClickListener.itemClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.itemLongClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()));
            return true;
        }

        public View getTitleView(){
            return mTitleView;
        }

        public View getDateView(){
            return mDateView;
        }

        public View getContentView(){
            return mContentView;
        }

        public View getIvIconView(){
            return mIconView;
        }
    }

    public interface ItemClickListener {
        void itemClicked(int position, T_CART_INFO item);
        void itemLongClicked(int position, T_CART_INFO item);
    }
}