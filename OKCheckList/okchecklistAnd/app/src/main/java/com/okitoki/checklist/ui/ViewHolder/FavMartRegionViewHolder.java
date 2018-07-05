package com.okitoki.checklist.ui.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.holiday.RestMartRegion;

public class FavMartRegionViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout itemBgLayout;
    public TextView martNameView;

    public FavMartRegionViewHolder(View itemView) {
        super(itemView);

        itemBgLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_foreground);
        martNameView = (TextView) itemView.findViewById(R.id.tv_item_title);
    }

    public void bindToPost(RestMartRegion restEmart, View.OnClickListener clickListener) {
//        martNameLogo.setText(restEmart.getPointName());
        martNameView.setText(restEmart.getRegionName());
        itemBgLayout.setOnClickListener(clickListener);
    }
}
