package com.okitoki.checklist.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class CartItemRecvAdapter extends RecyclerView.Adapter<CartItemRecvAdapter.ViewHolder> {
    private final Context mContext;
    private int mLayoutResId;
    private ArrayList<String> mCountNumset = null;
    private List<T_CART_ITEM> mDataset;
    private ItemClickListener itemClickListener;
    private ItemCheckedListener itemCheckedListener;

    public CartItemRecvAdapter(Context context, int layoutResId, List<T_CART_ITEM> dataset, @NonNull ItemClickListener itemClickListener) {
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
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {
        if (mDataset ==null || mDataset.size() <=0 ) return;
        String countryName =  mDataset.get(position).getItem_name();
//        int flagResId = mContext.getResources().getIdentifier(values[1], "drawable", mContext.getPackageName());

        viewHolder.mSpinnerCount.setSelection( mDataset.get(position).getQty() - 1);
        viewHolder.chkView.setChecked(mDataset.get(position).isCheckView_fl());
//        viewHolder.chkView.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
////                llFg.setSelected(isChecked);
//                mDataset.get(position).setCheck_fl(isChecked);
//                notifyItemChanged(position);
//
//                if (itemCheckedListener != null)
//                    itemCheckedListener.itemChecked(position, isChecked);
//            }
//        });
        viewHolder.titleView.setText(countryName);
//        viewHolder.mTextView.setCompoundDrawablesWithIntrinsicBounds(flagResId, 0, 0, 0);
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
        public LinearLayout llBg;
        public com.rey.material.widget.CheckBox chkView;
        public EditText titleView;
        public Spinner mSpinnerCount;

        public ViewHolder(View v) {
            super(v);
            chkView = (com.rey.material.widget.CheckBox) v.findViewById(R.id.cb_item_checkbox);
            chkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDataset.get(getLayoutPosition()).setCheckView_fl(isChecked);

                    if (itemCheckedListener!=null)
                        itemCheckedListener.itemChecked(getLayoutPosition(),isChecked);
                }
            });
            titleView = (EditText) v.findViewById(R.id.et_item_title);
            mSpinnerCount = (Spinner) v.findViewById(R.id.sp_item_count_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.row_spn, mCountNumset);
            adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
            mSpinnerCount.setAdapter(adapter);
            mSpinnerCount.setSelection(0);
            mSpinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mDataset.get(getLayoutPosition()).setQty((int)id +1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            titleView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mDataset.get(getLayoutPosition()).setItem_name(s.toString());
                }
            });
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("test", "onClick " + getLayoutPosition() + " ");
            Logger.d(this,"Logger Click " + getLayoutPosition() + " ");
            itemClickListener.itemClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()), view);
//            view.findViewById(R.id.cb_item_checkbox).performClick();
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
        void itemChecked(int position,boolean isChecked);
    }
}