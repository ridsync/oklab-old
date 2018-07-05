package com.okitoki.checklist.ui.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.holiday.RestMartInfo;

public class MartDetailRestInfoListViewHolder extends RecyclerView.ViewHolder {

    public TextView martNameLogo;
    public TextView martNameView;
    public ImageView ivFavMark;
    public TextView tvMartstatus;
    public TextView tvmartrestInfo;

    public MartDetailRestInfoListViewHolder(View itemView) {
        super(itemView);

        martNameLogo = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_logo);
        ivFavMark = (ImageView) itemView.findViewById(R.id.iv_fav_item_mart_fav);
        martNameView = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_name);
        tvMartstatus = (TextView) itemView.findViewById(R.id.iv_fav_item_mart_status);
        tvmartrestInfo = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_rest_date_info);
    }

    public void bindToPost(RestMartInfo restEmart, View.OnClickListener starClickListener) {
//        martNameLogo.setText(restEmart.getPointName());
        martNameView.setText(restEmart.getPointName());
        tvmartrestInfo.setText(restEmart.getRestDateInfo());
        ivFavMark.setOnClickListener(starClickListener);
    }
}
