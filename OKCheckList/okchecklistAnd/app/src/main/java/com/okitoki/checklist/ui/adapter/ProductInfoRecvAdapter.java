package com.okitoki.checklist.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.network.model.ProductItem;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class ProductInfoRecvAdapter extends RecyclerView.Adapter<ProductInfoRecvAdapter.ViewHolder> {
    private static final int VIEW_TYPE_AD_ADMOB = 10;
    private static final int VIEW_TYPE_CONTENTS = 11;
    private final Context mContext;
    private String mKeyWord;
    private int mLayoutResId;
    private ArrayList<String> mCountNumset = null;
    private List<ProductItem> mDataset = new ArrayList<>();
    private ItemClickListener itemClickListener;
    private ItemCheckedListener itemCheckedListener;

    public ProductInfoRecvAdapter(Context context, int layoutResId, List<ProductItem> dataset , @NonNull ItemClickListener itemClickListener) {
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

    public void setmKeyWord(String mKeyWord) {
        this.mKeyWord = mKeyWord;
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 20 == 0) // position > 20 &&
            return VIEW_TYPE_AD_ADMOB;
        return VIEW_TYPE_CONTENTS;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder =  null;
        if (VIEW_TYPE_AD_ADMOB == viewType){
//            com.google.android.gms.ads.AdView ad_view = new com.google.android.gms.ads.AdView(parent.getContext());
//            ad_view.setAdSize(AdSize.BANNER);
//            ad_view.setAdUnitId(mContext.getString(R.string.banner_ad_unit_id4));
//            int height = mContext.getResources().getDimensionPixelOffset(R.dimen.cart_product_itemvieW_image);
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
//            ad_view.setBackgroundColor( mContext.getResources().getColor(R.color.main_bg_color) );
//            ad_view.setLayoutParams(params);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            ad_view.loadAd(adRequest);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, null);
            holder = new ViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (mDataset ==null || mDataset.size() <=0 ) return;
        String mallName =  mDataset.get(position).getMallName();
        String title =  mDataset.get(position).getTitle();
        String imgUrl =  mDataset.get(position).getImage();
        int lPrice =  mDataset.get(position).getLprice();

        int viewType = getItemViewType(position);
        if(VIEW_TYPE_CONTENTS == viewType){
            final LinearLayout llFg = viewHolder.llFg;

    //        Picasso.with(mContext).load(imgUrl).resizeDimen(R.dimen.cart_product_itemvieW_image, R.dimen.cart_product_itemvieW_image)
    //                .centerCrop().into(viewHolder.imageView);

            int size = mContext.getResources().getDimensionPixelSize(R.dimen.cart_product_itemvieW_image);

            Glide.with(mContext)
                    .load(imgUrl)
    //                .thumbnail(0.1f)
                    .placeholder(R.drawable.cart_speed)
                    .priority(Priority.IMMEDIATE)
                    .override(size,size)
                    .centerCrop()
    //                .dontAnimate()
    //                .crossFade()
                    .into(viewHolder.imageView);

            int martResId = -1;
    //        int martResId = getMartIconResId(mallName);
            if(martResId>0){
                viewHolder.martLogo.setVisibility(View.VISIBLE);
                viewHolder.martLogo.setImageResource(martResId);
                viewHolder.martName.setVisibility(View.GONE);
            } else {
                viewHolder.martLogo.setVisibility(View.VISIBLE);
                viewHolder.martLogo.setImageResource(R.drawable.naver);
                viewHolder.martLogo.setAdjustViewBounds(true);
                viewHolder.martLogo.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.martName.setVisibility(View.VISIBLE);
                viewHolder.martName.setText(mallName);
            }
            String symbol = Currency.getInstance(Locale.KOREA).getSymbol();
            String strLowPrice= String.format("최저가\n "+symbol+"%,d", lPrice);
            SpannableString spbprice = new SpannableString(strLowPrice);
            spbprice.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_accent_color)), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            viewHolder.tv_item_low_price.setText(spbprice);

            if(mKeyWord!=null && mKeyWord.length() >0
                     && title != null && title.contains(mKeyWord) ){
                String itemName = title.replaceAll("<b>","");
                itemName = itemName.replaceAll("</b>","");
                    SpannableString spb = new SpannableString(itemName);
                    Logger.d("SpanTest", "itemName = " + itemName);
                    spb.setSpan(new BackgroundColorSpan(mContext.getResources().getColor(R.color.item_higglight_color)), itemName.indexOf(mKeyWord), itemName.indexOf(mKeyWord) + mKeyWord.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    spb.setSpan(new TextAppearanceSpan(null, 0, mContext.getResources().getDimensionPixelSize(R.dimen.highlight_text_size), null, null), itemName.indexOf(mKeyWord), itemName.indexOf(mKeyWord) + mKeyWord.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    viewHolder.item_title.setText(spb);
            } else {
                viewHolder.item_title.setText(Html.fromHtml(title));
            }

        } else if (VIEW_TYPE_AD_ADMOB == viewType){
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ProductItem getItem(int index) {
        return mDataset.get(index);
    }

    public void addAll(List<ProductItem> dataset ) {
       mDataset = dataset;
    }

    public void clearItem() {
        mDataset.clear();
    }

    public void remove(String obj) {
//        mDataset.
    }

    public class ViewHolder extends AnimateViewHolder implements View.OnClickListener , View.OnLongClickListener{
        public LinearLayout llFg;
        public ImageView imageView;
        public ImageView martLogo;
        public TextView martName;
        public TextView item_title;
        public TextView tv_item_low_price;

        public ViewHolder(View v) {
            super(v);
            llFg = (LinearLayout) v.findViewById(R.id.ll_item_foreground);
            imageView = (ImageView) v.findViewById(R.id.iv_product_image);
            martLogo = (ImageView) v.findViewById(R.id.iv_item_mart_logo);
            martName = (TextView) v.findViewById(R.id.tv_item_mart_name);
            item_title = (TextView) v.findViewById(R.id.tv_item_title);
            tv_item_low_price = (TextView) v.findViewById(R.id.tv_item_low_price);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("test", "onClick " + getLayoutPosition() + " ");
            Logger.d(this, "Logger Click " + getLayoutPosition() + " ");
            itemClickListener.itemClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()), view);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.itemLongClicked(getLayoutPosition(), mDataset.get(getLayoutPosition()));
            return true;
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
        void itemClicked(int position, ProductItem item, View view);
        void itemLongClicked(int position, ProductItem item);
    }

    public interface ItemCheckedListener {
        void itemChecked(int position, boolean isChecked);
    }

    private int getMartIconResId(String martName){
        if( "이마트".contains(martName) ){
            return R.drawable.emartmall;
        } else if("홈플러스".contains(martName)){
            return R.drawable.homeplus;
        } else if("롯데마트".contains(martName)){
            return R.drawable.lottemal;
        }  else if("롯데슈퍼".contains(martName)){
            return R.drawable.lottesuper;
        } else if("GSSHOP".contains(martName)){
            return R.drawable.gsshop;
        } else if("G마켓".contains(martName)){
            return R.drawable.gmarket;
        } else if("옥션".contains(martName)){
            return R.drawable.auction;
        } else if("SSG닷컴".contains(martName)){
            return R.drawable.ssg;
        } else {
            return -1;
        }
    }
}