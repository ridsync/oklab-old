package com.okitoki.checklist.ui.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;
import com.okitoki.checklist.utils.AUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FavMartRestInfoListViewHolder extends RecyclerView.ViewHolder {

    public TextView martNameLogo;
    public TextView martNameView;
    public ImageView ivFavMark;
    public TextView tvMartstatus;
    public TextView tvmartrestInfo;
    public LinearLayout llcalendarLayout;
    LayoutInflater inflater;
    Context context;
    int reIdGrayText;
    int reIdBlueOpen;
    int reIdRedClosed;
    int reIdBlueSaturday;
    int reIdRedSunday;

    public FavMartRestInfoListViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        reIdGrayText = context.getResources().getColor(R.color.mart_rest_text_gray_7);
        reIdRedClosed = context.getResources().getColor(R.color.red_color);
        reIdBlueOpen = context.getResources().getColor(R.color.fab_blue_color);
        reIdRedSunday = context.getResources().getColor(R.color.calendar_bg_red);
        reIdBlueSaturday = context.getResources().getColor(R.color.calendar_bg_blue);
        inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        martNameLogo = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_logo);
        ivFavMark = (ImageView) itemView.findViewById(R.id.iv_fav_item_mart_fav);
        martNameView = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_name);
        tvMartstatus = (TextView) itemView.findViewById(R.id.iv_fav_item_mart_status);
        tvmartrestInfo = (TextView) itemView.findViewById(R.id.tv_fav_item_mart_rest_date_info);
        llcalendarLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_mart_date_mini_calendar);
    }

    public void bindToPost(boolean isSetRestInfos, RestMartInfo restMart, View.OnClickListener starClickListener) {
        // 초기화
        if(isSetRestInfos) {
//            martNameLogo.setVisibility(View.VISIBLE);
        } else {
//            martNameLogo.setVisibility(View.INVISIBLE);
            martNameView.setText("");
        }
        AUtil.setViewFavRestMartIcon(martNameLogo, restMart.getMartCode());

        martNameView.setText(restMart.getPointName());
        if(restMart.getMartCode() == RestUtil.MART_CODE_COSTCO){
            String restInfo = restMart.getRestDateInfo();
            tvmartrestInfo.setText( restInfo.substring( restInfo.indexOf("매월")) );
        } else {
            tvmartrestInfo.setText(restMart.getRestDateInfo());
        }
        tvMartstatus.setText("");
        tvMartstatus.setTextColor( context.getResources().getColor(R.color.text_color_gray_59) );
        tvMartstatus.setTextColor( context.getResources().getColor(R.color.text_color_gray_59) );
        tvMartstatus.setBackgroundResource( R.drawable.selector_mart_open_status );

//        if( ! isSetRestInfos) return;

        ivFavMark.setOnClickListener(starClickListener);

        // 7일치 오픈 Status
        setHolidayMartRestTable(restMart);

        final Calendar calendar = Calendar.getInstance();
        // 오픈 Status
        if(AUtil.isHolidayMartToday(restMart,calendar)){
            tvMartstatus.setBackgroundResource(R.drawable.selector_purchase_completed);
            tvMartstatus.setTextColor(reIdRedClosed);
            tvMartstatus.setText( R.string.fav_mart_closed );
            tvMartstatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
//            int px = AUtil.getPixelFromDP(mContext,15);
//            tvMartstatus.setPadding(px,px,px,px);
        } else {
            tvMartstatus.setBackgroundResource(R.drawable.selector_mart_open_status);
            tvMartstatus.setTextColor(reIdBlueOpen);
            tvMartstatus.setText(R.string.fav_mart_open);
            tvMartstatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
//            int px = AUtil.getPixelFromDP(mContext,17);
//            tvMartstatus.setPadding(px, px, px, px);
        }


    }

    private void setHolidayMartRestTable(RestMartInfo restMart) {

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, i == 0 ? 0 : 1);
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
            String strToday = dateFormat.format(date);
            int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
            String todayWeek = AUtil.week[dayofWeek - 1] ;
            LinearLayout tableRow;
            LinearLayout layout = (LinearLayout)llcalendarLayout.findViewWithTag("item_layout"+i);
            if(layout == null){
                tableRow = (LinearLayout)inflater.inflate(R.layout.item_mart_rest_day_table,null);
            } else {
                tableRow = layout;
            }
            ((TextView)tableRow.findViewById(R.id.item_calendar_row_date)).setText(strToday +"\n"+todayWeek);
            if("토".equalsIgnoreCase(todayWeek)){
//                ((TextView)tableRow.findViewById(R.id.item_calendar_row_date)).setTextColor( reIdBlueOpen );
//                tableRow.findViewById(R.id.item_calendar_bg).setBackgroundColor( reIdBlueSaturday );
            } else if ( "일".equalsIgnoreCase(todayWeek) ){
//                ((TextView)tableRow.findViewById(R.id.item_calendar_row_date)).setTextColor( reIdRedClosed );
//                tableRow.findViewById(R.id.item_calendar_bg).setBackgroundColor( reIdRedSunday );
            }

            boolean isrestDay = AUtil.isHolidayMartToday(restMart,calendar);

            TextView tvStats= ((TextView)tableRow.findViewById(R.id.item_calendar_row_status));
            if(isrestDay){
                tvStats.setTextColor( reIdRedClosed );
                tvStats.setText(R.string.fav_mart_closed);
                tableRow.findViewById(R.id.item_calendar_bg).setBackgroundColor( reIdRedSunday );
            } else {
                tvStats.setTextColor( reIdGrayText );
                tvStats.setText(R.string.fav_mart_open_2);
                tableRow.findViewById(R.id.item_calendar_bg).setBackground( null );
            }

            if(layout ==null){
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                param.gravity = Gravity.CENTER_VERTICAL;
                tableRow.setTag("item_layout"+i);
                tableRow.setLayoutParams(param);
                llcalendarLayout.addView(tableRow);
            }

        }
    }

    public View getMartName(){
        return martNameView;
    }

    public View getDateView(){
        return tvMartstatus;
    }

    public View getIvIconView(){
        return martNameLogo;
    }
    public View getIvStarView(){
        return ivFavMark;
    }
}
