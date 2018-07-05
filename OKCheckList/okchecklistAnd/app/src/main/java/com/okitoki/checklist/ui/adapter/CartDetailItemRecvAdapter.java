package com.okitoki.checklist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.ui.ProductInfoSearchActivity_;
import com.okitoki.checklist.ui.fragment.CartDetailViewFragment;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class CartDetailItemRecvAdapter extends RecyclerView.Adapter<CartDetailItemRecvAdapter.ViewHolder> {
    private final Context mContext;
    private int mLayoutResId;
    private ArrayList<String> mCountNumset = null;
    private List<T_CART_ITEM> mDataset;
    private ItemClickListener itemClickListener;
    private ItemCheckedListener itemCheckedListener;
    public CartDetailItemRecvAdapter(Context context, int layoutResId, List<T_CART_ITEM> dataset, @NonNull ItemClickListener itemClickListener) {
        mContext = context;
        mLayoutResId = layoutResId;
        mDataset = dataset;
//        mCountNumset = context.getResources().getStringArray(R.array.countnumbers);
        mCountNumset = new ArrayList<>();
        for (int i = 0; i < AppConst.TOTAL_COUNT_EACH_ITEM; i++) {
            mCountNumset.add(new String(i+1+""));
        }
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
    }

    public void setItemCheckedListener(@NonNull ItemCheckedListener itemCheckedListener){
        this.itemCheckedListener = itemCheckedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (mDataset ==null || mDataset.size() <=0 ) return;
        String countryName =  mDataset.get(position).getItem_name();
//        int flagResId = mContext.getResources().getIdentifier(values[1], "drawable", mContext.getPackageName());

        final LinearLayout llFg = viewHolder.llFg;
        final ImageView search = viewHolder.ivSearch;
        viewHolder.chkView.setOnCheckedChangeListener(null);
        viewHolder.llFg.setSelected(mDataset.get(position).isCheck_fl());
        viewHolder.chkView.setChecked(mDataset.get(position).isCheck_fl());
        viewHolder.chkView.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                llFg.setSelected(isChecked);
                search.setSelected(isChecked);
                mDataset.get(position).setCheck_fl(isChecked);
                notifyItemChanged(position);

                if (itemCheckedListener != null)
                    itemCheckedListener.itemChecked(position, isChecked);
            }
        });
        viewHolder.titleView.setText(countryName);
        String qty = mDataset.get(position).getQty() + "";
        viewHolder.tv_item_count_label.setText("수량  :  " + qty);
        viewHolder.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putString(AppConst.FA_PARAM_NORMAL, "normal");
                // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_CART_DETAIL_SEARCH, bundle2);

                    String name = mDataset.get(position).getItem_name();
                    Intent intent = new Intent(mContext, ProductInfoSearchActivity_.class);
                    String storId = CartDetailViewFragment.getInstance().getCartInfo().getStore_id();
                    intent.putExtra(AppConst.INTENT_EXTRA_SELECT_MART, storId);
                    intent.putExtra(AppConst.INTENT_EXTRA_PRODUCT_TITLE, name);
                    mContext.startActivity(intent);
                OKCartApplication.isShowFullAd_search++;
            }
        });

        // TouchDelegate
//        int pixel = mContext.getResources().getDimensionPixelSize(R.dimen.touchdelegate);
//        ViewGroup.LayoutParams params = llFg.getLayoutParams();
//        llFg.setTouchDelegate(new TouchDelegate(new Rect(params.width - pixel, 0, params.width, params.height), search));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T_CART_ITEM getItem(int index) {
        return mDataset.get(index);
    }

    public void addAll(List<T_CART_ITEM> dataset ) {
       mDataset = dataset;
    }

    public void remove(String obj) {
//        mDataset.
    }

    public class ViewHolder extends AnimateViewHolder implements View.OnClickListener , View.OnLongClickListener{
        public LinearLayout llFg;
        public com.rey.material.widget.CheckBox chkView;
        public ImageView ivSearch;
        public TextView titleView;
        public TextView tv_item_count_label;

        public ViewHolder(View v) {
            super(v);
            llFg = (LinearLayout) v.findViewById(R.id.ll_item_foreground);
            ivSearch = (ImageView) v.findViewById(R.id.iv_btn_search);
            titleView = (TextView) v.findViewById(R.id.tv_item_title);
            tv_item_count_label = (TextView) v.findViewById(R.id.tv_item_count_label);
            chkView = (com.rey.material.widget.CheckBox) v.findViewById(R.id.cb_item_checkbox);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("test", "onClick " + getLayoutPosition() + " ");
            Logger.d(this, "Logger Click " + getLayoutPosition() + " ");
            itemClickListener.itemClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()), view);
            view.findViewById(R.id.cb_item_checkbox).performClick();
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.itemLongClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()));
            return true;
        }

        public View getTitleView(){
            return chkView;
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(-itemView.getHeight() * 0.3f)
                    .alpha(0)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

        @Override
        public void preAnimateAddImpl() {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);
        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }
    }

    public interface ItemClickListener {
        void itemClicked(int position, T_CART_ITEM item, View view);
        void itemLongClicked(int position, T_CART_ITEM item);
    }

    public interface ItemCheckedListener {
        void itemChecked(int position, boolean isChecked);
    }
}