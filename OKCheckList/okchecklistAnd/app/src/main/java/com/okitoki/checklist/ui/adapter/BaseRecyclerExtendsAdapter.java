package com.okitoki.checklist.ui.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by andman on 2015-09-07.
 */
public abstract class BaseRecyclerExtendsAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int LOAD_MORE_LIST_LIMIT = 20;

    public static class VIEW_TYPES {
        public static final int NORMAL = 1;
        public static final int HEADER = 2;
        public static final int FIRST_VIEW = 3;
        public static final int FOOTER = 4;
    }

    public abstract void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, BaseRecyclerExtendsAdapter<T> adapter, int i);

    public abstract RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, BaseRecyclerExtendsAdapter<T> adapter, int i);

    public interface OnItemClickEvent {
        /**
         * Event triggered when you click on a item of the adapter
         *
         * @param v        view
         * @param position position on the array
         */
        void onClick(View v, int position);
    }

    protected RecyclerView mRecyclerView;
    protected List<T> mData;
    protected HeaderViewWrapper mHeader;
    protected View mFoterView;
    protected RecyclerView.ViewHolder footerViewHolder;
    protected OnItemClickEvent mOnItemClickEvent;

    public BaseRecyclerExtendsAdapter(List<T> data) {
        mData = data ;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
//        Logger.d("Adapter", "onBindViewHolder position = " + i);
        if ( ! isHeader(i) && ! isFooter(i) ){// header, footer will not binding
            onBindViewHolderImpl(viewHolder, this, i - (hasHeader() ? 1 : 0));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
//        Logger.d("Adapter", "onCreateViewHolder viewType = " + viewType);
        if (viewType == VIEW_TYPES.HEADER && mHeader != null){
            return new RecyclerView.ViewHolder(mHeader) {
            };
        }
        if (viewType == VIEW_TYPES.FOOTER && footerViewHolder !=null) {
            return footerViewHolder;
        }
        final RecyclerView.ViewHolder holder = onCreateViewHolderImpl(viewGroup, this, viewType);
        if (mOnItemClickEvent != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickEvent.onClick(v, holder.getAdapterPosition() - (mHeader == null ? 0 : 1));
                }
            });
        }
        return holder;
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 1; // Header만이라도 그리도록 ?
        return mData.size() + (mHeader == null ? 0 : 1) + (hasFooter() ? 1: 0);
    }

    @Override
    public int getItemViewType(int position) {
//        Logger.d("Adapter", "getItemViewType position = " + position);
        if (isFooter(position)) {
            return VIEW_TYPES.FOOTER;
        } else if(isHeader(position)){
            return VIEW_TYPES.HEADER;
//        } else if( position == 1){
//            return VIEW_TYPES.FIRST_VIEW; // Parallax에나 필요함
        } else {
            return VIEW_TYPES.NORMAL;
        }
    }

    /**
     * Set the view as Real header Behind of RecyclerView.
     *
     * @param header The Behind Recycler Real header
     */
    public HeaderViewWrapper setNormalHeader(View header) {
        if (header != null) {
            mHeader = new HeaderViewWrapper(header.getContext(), false);
            mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mHeader.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return mHeader;
        }
        return null;
    }

    public void setFooterView(View foot){
        mFoterView = foot;
    }

    public void setVisibleFooterView(boolean isShow){
        if (isShow){
            if (mFoterView != null && footerViewHolder ==null)
                footerViewHolder = new FooterViewHolder(mFoterView);
            notifyItemInserted(getItemCount());
        } else {
            removeFooter();
        }
    }

    public void removeFooter(){
        if (footerViewHolder != null){
            footerViewHolder = null;
            notifyDataSetChanged();
//            notifyItemRemoved(getItemCount());
        }
    }

    public boolean isFooter(int position){
        return hasFooter() && position == mData.size() + (hasHeader() ? 1 : 0);
    }

    public boolean isHeader(int position){
        return hasHeader() && position == 0;
    }

    /**
     * @return true if there is a header on this adapter, false otherwise
     */
    public boolean hasHeader() {
        return mHeader != null;
    }

    public boolean hasFooter(){
        return footerViewHolder != null;
    }

    public void setOnItemClickEvent(OnItemClickEvent onItemClickEvent) {
        mOnItemClickEvent = onItemClickEvent;
    }

    /**
     * Data Getter Setter & Notification Implement
     * @see
     */
    public void setPageItemCountByLoadMore(int count){
        if (count > 0){
            LOAD_MORE_LIST_LIMIT = count;
        }
    }

    public int getPageItemCountByLoadMore(){
        return LOAD_MORE_LIST_LIMIT;
    }

    public int getPosition(final T item) {
        return mData.indexOf(item);
    }

//    public int getTotalItemCount() {
//        return getItemCount();
//    }

    // 리스트 상단 더보기용
    @Nullable
    public T getLatestItemData() {
        return mData !=null ? mData.get(0) : null;
    }

    // 리스트 하단 더보기용
    @Nullable
    public T getFitstItemData() {
        return (mData !=null && mData.size()>0) ? mData.get(mData.size()-1) : null;
    }

    public List<T> getData() {
        return mData;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setData(List<T> items) {
        if (items ==null ) return;
//        final int size = getItemCount();
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
//        notifyItemRangeChanged(0, size);
        setVisibleFooterView(items.size() >= LOAD_MORE_LIST_LIMIT ? true : false);
    }

    public void addItemAll(List<T> items) {
        if (mData!=null && items!=null&& items.size()>0){
            final int size = getItemCount();
            mData.addAll(items);
            notifyItemRangeInserted(size, size + items.size());
            setVisibleFooterView(items.size() >= LOAD_MORE_LIST_LIMIT ? true : false);
        }
    }

    public void insertItem(T item, int position) {
        if (mData !=null){
            mData.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void addItemAtLast(T item) {
        if (mData !=null){
            mData.add(item);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    //TODO 특정아이템변경
    public void changeItem(int position) {
        notifyItemChanged(position + (hasHeader() ? 1 : 0));
    }

    public void removeItem(T item) {
        final int position = getPosition(item);
        if (position < 0)
            return;
        mData.remove(item);
        notifyItemRemoved(position);
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mData, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }

    static class HeaderViewWrapper extends RelativeLayout {

        private int mOffset;
        private boolean mShouldClip;

        public HeaderViewWrapper(Context context, boolean shouldClick) {
            super(context);
            mShouldClip = shouldClick;
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (mShouldClip) {
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            }
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
