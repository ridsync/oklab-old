package com.okitoki.checklist.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-01.
 */
public class AUtil {
    private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;

    public static String getVersion(Context context) {
        String strVersion = null;

        PackageInfo pkg = null;

        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pkg != null) {
            strVersion = pkg.versionName;
        } else {
            strVersion = "-";
        }

        return strVersion;
    }

    public static void setViewFavRestMartIcon(TextView view , int martCode){
        if (RestUtil.MART_CODE_EMART == martCode ){
            view.setBackgroundResource(R.drawable.ic_emart);
        } else if (RestUtil.MART_CODE_HOMEPLUS == martCode){
            view.setBackgroundResource(R.drawable.ic_homeplus);
        }else if (RestUtil.MART_CODE_LOTTE == martCode){
            view.setBackgroundResource(R.drawable.ic_lotte);
        }else if (RestUtil.MART_CODE_COSTCO == martCode){
            view.setBackgroundResource(R.drawable.ic_costco);
        }
    }

    public static void setViewMartIcon(TextView view , String iconCode){
        if (AppConst.SYMBOL_ICON_ID_EMART.equals(iconCode) ){
            view.setBackgroundResource(R.drawable.circular_textview_bg_emart);
            view.setText(AppConst.SYMBOL_ICON_ID_EMART);
        } else if (AppConst.SYMBOL_ICON_ID_HOMEPLUS.equals(iconCode)){
            view.setBackgroundResource(R.drawable.circular_textview_bg_hom);
            view.setText(AppConst.SYMBOL_ICON_ID_HOMEPLUS);
        }else if (AppConst.SYMBOL_ICON_ID_LOTTEMART.equals(iconCode)){
            view.setBackgroundResource(R.drawable.circular_textview_bg_lotte);
            view.setText(AppConst.SYMBOL_ICON_ID_LOTTEMART);
        }else if (AppConst.SYMBOL_ICON_ID_GSHOME.equals(iconCode)){
            view.setBackgroundResource(R.drawable.circular_textview_bg_gs);
            view.setText(AppConst.SYMBOL_ICON_ID_GSHOME);
        }else if (AppConst.SYMBOL_ICON_ID_COSTCO.equals(iconCode)){
            view.setBackgroundResource(R.drawable.circular_textview_bg_red_costco);
            view.setText(AppConst.SYMBOL_ICON_ID_COSTCO);
        }else if (AppConst.SYMBOL_ICON_ID_GENERAL_MART.equals(iconCode)){
            view.setBackgroundResource(R.drawable.circular_textview_bg_normal);
            view.setText(AppConst.SYMBOL_ICON_ID_GENERAL_MART);
        } else {
            view.setBackgroundResource(R.drawable.circular_textview_bg_normal);
            view.setText(AppConst.SYMBOL_ICON_ID_GENERAL_MART);
        }
    }

    public static final String[] week = { "일", "월", "화", "수", "목", "금", "토" };

    public static boolean isHolidayMartToday(RestMartInfo restMart, Calendar calendar) {
        boolean result = false;
        Date date = calendar.getTime();

        try{

            // TODO 휴무일을 숫자날짜가 아닌 매주 둘째 넷째 문자를 파싱하는것으로 해야 업데이트 ㅍ불필요.
            // 특이한 특정일 업체 예외처리 는 필수.
            if(RestUtil.MART_CODE_EMART == restMart.getMartCode()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd일");
                String today = dateFormat.format(date);
                // 이마트 00일,00일
                String restDays = restMart.getRestDateInfo();
                result = restDays.contains(today);
                // String 분석 format 나무위키
                // 1) 둘째, 넷째주 일요일 2) 둘째 수요일, 넷째 일요일
//                String restDays = restMart.getRestDateInfo();
//                boolean isSameRest = restDays.contains("둘째, 넷째");
//                int countWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);// 몇번째요일
//                int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
//                String todayWeek = week[dayofWeek - 1] + "요일";
//                if(isSameRest){
//                    String weekday = restDays.substring(restDays.indexOf("둘째, 넷째") + 8, restDays.indexOf("둘째, 넷째") + 11);
//                    if (todayWeek.equals(weekday)
//                            && (countWeek == 2  || countWeek == 4)){
//                        result = true;
//                    }
//                } else {
//                    String weekday = restDays.substring(restDays.indexOf("둘째") + 3, restDays.indexOf("둘째") + 6);
//                    String weekday2 = restDays.substring(restDays.indexOf(", 넷째") + 5, restDays.indexOf(", 넷째") + 8);
//                    if (todayWeek.equals(weekday) && countWeek == 2
//                            || ( todayWeek.equals(weekday2) && countWeek == 4)){
//                        result = true;
//                    }
//                }
            } else if (RestUtil.MART_CODE_HOMEPLUS == restMart.getMartCode() ){
                // 홈펄러스 망할 숫자날짜좀 기재하지...
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("매월 2,4번째");
                int countWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);// 몇번째요일
                int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
                String todayWeek = week[dayofWeek - 1] + "요일";

                // 날짜 비교
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd일");
                String today = dateFormat.format(date);
                result = restDays.contains(today);
                if(result){
                    return result;
                }
                // 주차 요일 비교
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 2,4번째") + 9, restDays.indexOf("매월 2,4번째") + 12);
                    if (todayWeek.equals(weekday)
                            && (countWeek == 2  || countWeek == 4)){
                        result = true;
                    }
                } else {
                    String weekday = restDays.substring(restDays.indexOf("매월 2번째") + 7, restDays.indexOf("매월 2번째") + 10);
                    String weekday2 = restDays.substring(restDays.indexOf(",4번째") + 5, restDays.indexOf(",4번째") + 8);
                    if (todayWeek.equals(weekday) && countWeek == 2
                            || ( todayWeek.equals(weekday2) && countWeek == 4)){
                        result = true;
                    }
                }
            } else if (RestUtil.MART_CODE_LOTTE == restMart.getMartCode()){
                // 롯데마트 망할 중간에 0이없는것도있네.
    //            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    //            SimpleDateFormat dateFormat2 = new SimpleDateFormat("M/d");
    //            SimpleDateFormat dateFormat3 = new SimpleDateFormat("M월 d일");
    //            String today = dateFormat.format(date);
    //            String today2 = dateFormat2.format(date);
    //            String today3 = dateFormat3.format(date);
    //            String restDays = restMart.getRestDateInfo();
    //            result = restDays.contains(today) || restDays.contains(today2) || restDays.contains(today3);

                // String 분석 format 나무위키
                // 1) 둘째, 넷째주 일요일 2) 둘째 수요일, 넷째 일요일
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("둘째, 넷째");
                int countWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);// 몇번째요일
                int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
                String todayWeek = week[dayofWeek - 1] + "요일";

                // 주차 요일 비교
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 둘째, 넷째") + 11, restDays.indexOf("매월 둘째, 넷째") + 14);
                    if (todayWeek.equals(weekday)
                            && (countWeek == 2  || countWeek == 4)){
                        result = true;
                        return result;
                    }
                } else if( restDays.contains("2째주") || restDays.contains("4째주")){
                    String weekday = restDays.substring(restDays.indexOf("2째주") + 4, restDays.indexOf("2째주") + 7);
                    String weekday2 = restDays.substring(restDays.indexOf("4째주") + 4, restDays.indexOf("4째주") + 7);
                    if ((todayWeek.equals(weekday) && countWeek == 2)
                            || ( todayWeek.equals(weekday2) && countWeek == 4)){
                        result = true;
                        return result;
                    }
                } else {
                    String weekday = restDays.substring(restDays.indexOf("둘째") + 3, restDays.indexOf("둘째") + 6);
                    String weekday2 = restDays.substring(restDays.indexOf(", 넷째") + 5, restDays.indexOf(", 넷째") + 8);
                    if (todayWeek.equals(weekday) && countWeek == 2
                            || ( todayWeek.equals(weekday2) && countWeek == 4)){
                        result = true;
                        return result;
                    }
                }

                // 특정날짜이므로 날짜로 비교
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM월dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("M월dd");
                SimpleDateFormat dateFormat3 = new SimpleDateFormat("M/dd");
                SimpleDateFormat dateFormat4 = new SimpleDateFormat(",dd(");
                String today = dateFormat.format(date);
                String today2 = dateFormat2.format(date);
                String today3 = dateFormat3.format(date);
                String today4 = dateFormat4.format(date);
                boolean bToday = restDays.contains(today);
                boolean bToday2 = restDays.contains(today2);
                boolean bToday3 = restDays.contains(today3);
                boolean bToday4 = restDays.contains(today4);
                if(bToday || bToday2 || bToday3 || bToday4){
                    result = true;
                }

            } else if (RestUtil.MART_CODE_COSTCO == restMart.getMartCode()){
                // 코스트코
                // String 분석 format 나무위키
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("매월 둘째, 넷째");
                int countWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);// 몇번째요일
                int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
                String todayWeek = week[dayofWeek - 1] + "요일";
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 둘째, 넷째") + 10, restDays.indexOf("매월 둘째, 넷째") + 13);
                    if (todayWeek.equals(weekday)
                            && (countWeek == 2  || countWeek == 4)){
                        result = true;
                    }
                } else {
                    String weekday = restDays.substring(restDays.indexOf("매월 둘째") + 6, restDays.indexOf("매월 둘째") + 9);
                    String weekday2 = restDays.substring(restDays.indexOf(", 넷째") + 5, restDays.indexOf(", 넷째") + 8);
                    if (todayWeek.equals(weekday) && countWeek == 2
                            || ( todayWeek.equals(weekday2) && countWeek == 4)){
                        result = true;
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }


    public static ArrayList<Integer> getHolidayofMonth(RestMartInfo restMart) {
        boolean result = false;
        ArrayList<Integer> holidays = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();

        try{

            if(RestUtil.MART_CODE_EMART == restMart.getMartCode()){
                try {
                    // 이마트 00일,00일
                    String holidayInfo = restMart.getRestDateInfo();
                    String firstDay = holidayInfo.substring(holidayInfo.indexOf("일")-2,holidayInfo.indexOf("일"));
                    String secondDay = holidayInfo.substring(holidayInfo.lastIndexOf("일")-2,holidayInfo.lastIndexOf("일"));

                    holidays.add(Integer.parseInt(firstDay));
                    holidays.add(Integer.parseInt(secondDay));
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (RestUtil.MART_CODE_HOMEPLUS == restMart.getMartCode() ){
//                // 홈펄러스 망할 숫자날짜좀 기재하지...
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("매월 2,4번째");
//
//                // 날짜 비교
                String holidayInfo = restMart.getRestDateInfo();

                try {
                    String firstDay = holidayInfo.substring(holidayInfo.indexOf("일")-2,holidayInfo.indexOf("일"));
                    holidays.add(Integer.parseInt(firstDay));

                    String secondDay = holidayInfo.substring(holidayInfo.lastIndexOf("일")-2,holidayInfo.lastIndexOf("일"));
                    holidays.add(Integer.parseInt(secondDay));
                } catch (Exception e){
                    e.printStackTrace();
                }

//                // 주차 요일 비교
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 2,4번째") + 9, restDays.indexOf("매월 2,4번째") + 12);
                    for (int j = 0; j < 2; j++) {
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, j == 0 ? 2 : 4);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }
                } else {
                    String weekday2 = restDays.substring(restDays.indexOf("매월 2번째") + 7, restDays.indexOf("매월 2번째") + 10);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
                    for (int i = 0; i < week.length; i++) {
                        String strWeek = week[i];
                        String wday = weekday2.substring(0,1);
                        if(strWeek.equals(wday)){
                            calendar.set(Calendar.DAY_OF_WEEK, i+1);
                            holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                    String weekday4= restDays.substring(restDays.indexOf(",4번째") + 5, restDays.indexOf(",4번째") + 8);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
                    for (int i = 0; i < week.length; i++) {
                        String strWeek = week[i];
                        String wday = weekday4.substring(0,1);
                        if(strWeek.equals(wday)){
                            calendar.set(Calendar.DAY_OF_WEEK, i+1);
                            holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                }
            } else if (RestUtil.MART_CODE_LOTTE == restMart.getMartCode()){
//                // 롯데마트 망할 중간에 0이없는것도있네.
//                //            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
//                //            SimpleDateFormat dateFormat2 = new SimpleDateFormat("M/d");
//                //            SimpleDateFormat dateFormat3 = new SimpleDateFormat("M월 d일");
//                //            String today = dateFormat.format(date);
//                //            String today2 = dateFormat2.format(date);
//                //            String today3 = dateFormat3.format(date);
//                //            String restDays = restMart.getRestDateInfo();
//                //            result = restDays.contains(today) || restDays.contains(today2) || restDays.contains(today3);
//
//                // String 분석 format 나무위키
//                // 1) 둘째, 넷째주 일요일 2) 둘째 수요일, 넷째 일요일
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("둘째, 넷째");
//
//                // 주차 요일 비교
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 둘째, 넷째") + 11, restDays.indexOf("매월 둘째, 넷째") + 14);
                    for (int j = 0; j < 2; j++) {
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, j == 0 ? 2 : 4);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }
                } else if( restDays.contains("2째주") || restDays.contains("4째주") ){
                    try {
                        String weekday2 = restDays.substring(restDays.indexOf("2째주") + 4, restDays.indexOf("2째주") + 7);
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday2.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        String weekday4 = restDays.substring(restDays.indexOf("4째주") + 4, restDays.indexOf("4째주") + 7);
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday4.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if( restDays.contains("둘째") || restDays.contains(", 넷째") ){
                    try{
                        String weekday2 = restDays.substring(restDays.indexOf("둘째") + 3, restDays.indexOf("둘째") + 6);
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday2.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    try{
                        String weekday4 = restDays.substring(restDays.indexOf(", 넷째") + 5, restDays.indexOf(", 넷째") + 8);
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday4.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String strRests = restDays;
                    if(restDays.contains("매월 10일")){
                        strRests = restDays.replaceAll("매월 10일,", "");
                    }
                    String[] arDates = strRests.split(",");
                    Date holiday;
                    for (int i=0;i< arDates.length;i++) {
                        String strDate = arDates[i];
                        try{
                            if (strRests.contains("/")) {
                                String res = strDate.replaceAll("[^\\d/]", "");
                                SimpleDateFormat dateFormat4 = new SimpleDateFormat("M/dd");
                                holiday = dateFormat4.parse(res);
                            } else {
                                SimpleDateFormat dateFormat4 = new SimpleDateFormat("M월dd일");
                                holiday = dateFormat4.parse(strDate);
                            }
                            calendar.setTime(holiday);
                            holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                if(restDays.contains("매월 10일")){
                    calendar.set(Calendar.DAY_OF_MONTH, 10);
                    holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                }

            } else if (RestUtil.MART_CODE_COSTCO == restMart.getMartCode()){
//                // 코스트코
//                // String 분석 format 나무위키
                String restDays = restMart.getRestDateInfo();
                boolean isSameRest = restDays.contains("매월 둘째, 넷째");
                int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일
                if(isSameRest){
                    String weekday = restDays.substring(restDays.indexOf("매월 둘째, 넷째") + 10, restDays.indexOf("매월 둘째, 넷째") + 13);
                    for (int j = 0; j < 2; j++) {
                        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, j == 0 ? 2 : 4);
                        for (int i = 0; i < week.length; i++) {
                            String strWeek = week[i];
                            String wday = weekday.substring(0,1);
                            if(strWeek.equals(wday)){
                                calendar.set(Calendar.DAY_OF_WEEK, i+1);
                                holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                            }
                        }
                    }
                } else {
                    String weekday2 = restDays.substring(restDays.indexOf("매월 둘째") + 6, restDays.indexOf("매월 둘째") + 9);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
                    for (int i = 0; i < week.length; i++) {
                        String strWeek = week[i];
                        String wday = weekday2.substring(0,1);
                        if(strWeek.equals(wday)){
                            calendar.set(Calendar.DAY_OF_WEEK, i+1);
                            holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                    String weekday4 = restDays.substring(restDays.indexOf(", 넷째") + 5, restDays.indexOf(", 넷째") + 8);
                    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
                    for (int i = 0; i < week.length; i++) {
                        String strWeek = week[i];
                        String wday = weekday4.substring(0,1);
                        if(strWeek.equals(wday)){
                            calendar.set(Calendar.DAY_OF_WEEK, i+1);
                            holidays.add(calendar.get(Calendar.DAY_OF_MONTH));
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return holidays;
    }

    public static String getMartNameByCode(int martCode){
        String result = "emart";
        if(martCode == RestUtil.MART_CODE_EMART){
            result = "emart";
        } else if (martCode == RestUtil.MART_CODE_HOMEPLUS) {
            result = "homeplus";
        } else if (martCode == RestUtil.MART_CODE_LOTTE) {
            result = "lottemart";
        } else if (martCode == RestUtil.MART_CODE_COSTCO) {
            result = "costco";
        }
        return result;
    }

    public static String getDateExp(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }

    public static String getDateExpKor(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        return format.format(date);
    }

    public static boolean hideIME(Context context, View view) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showIME(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.showSoftInputFromInputMethod (view.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED);
    }


    /**
     *  App이 현재 실행중(사용자인터렉티브가능상태)인지 판단하는 메소드
     *  -> 스크린 ON > App 프로세스 상태 ForeGround  + 푸시팝업이 상단에 있지 않을때
     * @param context
     * @return
     */
    public static boolean isAppInForeground(Context context) {
        boolean isInForeround = false;
        boolean isScreenon = PMWakeLock.isScreenOnDevice(context);
        if (isScreenon) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInForeround = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                if(taskInfo!=null && taskInfo.size() > 0){
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (componentInfo.getPackageName().equals(context.getPackageName())) {
                        isInForeround = true;
                    }
                }
            }

        }
        return isInForeround;
    }

    /**
     * App이 현재 실행중(사용자인터렉티브가능상태)인지 판단하는 메소드
     *  -> 프로세스 상태에 따른 App Status
     * @param context
     * @return
     */
    public static boolean isContextForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            Logger.d("testApp", "appProcess/Id" + appProcess.processName + " / "+ appProcess.pid);
            if (appProcess.pid == pid) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     *  App 상단에 topActivity 체크 로직
     * @param context
     * @return
     */
    public static boolean isTopAlertActivity(Context context){
        boolean isTopAlertActivity = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // topActivity API23 M 이상부터가능
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            for (ActivityManager.AppTask task: tasks){
                ComponentName componentName = task.getTaskInfo().topActivity;
                if(componentName !=null
                        && componentName.getShortClassName() !=null
                        && componentName.getShortClassName().contains("AlertAcitivty")){
                    isTopAlertActivity = true;
                    break;
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1); // 21에서 Deprecated되었음.
            if(Info!=null && Info.size() > 0){
                ComponentName topActivity = Info.get(0).topActivity;
                String topactivityname = topActivity.getShortClassName();
                if (topactivityname != null && topactivityname.contains("AlertAcitivty")){
                    isTopAlertActivity = true;
                }
            }
        }

        return isTopAlertActivity;
    }

    /**
     * 매일 정해진 시각으로 알람 등록.
     * @param context
     */
    public static void setAlarmManagerForPRnoty(Context context){

        int nSetHour = PreferUtil.getPreferenceInteger(AppConst.PREF_HOLLIDAY_ALARM_SET_HOUR,context);
        int nSetMinute = PreferUtil.getPreferenceInteger(AppConst.PREF_HOLLIDAY_ALARM_SET_MINUTE,context);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, nSetHour);
        calendar.set(Calendar.MINUTE, nSetMinute);

        // 2) 기존 알람 취소.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        int pId = 1111;
        PendingIntent pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), pId, intent, PendingIntent.FLAG_ONE_SHOT);
        if(pIntent != null){
            am.cancel(pIntent);
            pIntent.cancel();
            Logger.d("Alarm", "Cancel !! pId " + pId  + " / " + pIntent.toString());
        }
//        intent.replaceExtras(bundle);
        // 3) 신규 알람 등록.
        boolean isAlarmNotify = PreferUtil.getPreferenceBoolean(AppConst.PREF_HOLLIDAY_ALARM_NOTIFY,context);
        if( ! isAlarmNotify) return;

        PendingIntent newPi = PendingIntent.getBroadcast(context.getApplicationContext(), pId, intent
                , PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newPi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), newPi);
            am.setAlarmClock(alarmClockInfo, newPi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newPi);
        } else {
            am.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newPi);
        }
//        am.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newPi);

        Logger.d("Alarm", "New Alarm Setting !! pId " + pId  + " / Alarmtime = " + calendar.getTime());
//        LoggerF.getLogger(SQApp.class).debug("[Alarm] New Alarm Setting !! pId " + pId  + " / " + calendar.getTime() + " / " + intent.getExtras().toString());

    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getOsSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (JavaUtil.isEmptyString(android_id)) {
            return android.os.Build.SERIAL;
        }
        try {
            return URLEncoder.encode(android_id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return android_id;
        }
    }

    public static String getDeviceName() throws UnsupportedEncodingException {
        return URLEncoder.encode(android.os.Build.MODEL, "utf-8");
    }


    public static File getExternalCacheDir(Context context ) {
        if (context != null) {
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            return null;
        }
    }

    public static int getScreenSizeInch(Activity activity) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels/dm.xdpi,2);
            double y = Math.pow(dm.heightPixels/dm.ydpi,2);
            double screenInches = Math.sqrt(x+y);
            Double d = new Double(screenInches);
            return d.intValue();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * ************************************
     *  Bitmap Processing Methods
     */
    public static void saveBitmapToFile(Bitmap bitmap, String path) {

        // 디바이스로부터 이미지 가져올때 이미 Resize 되어서 필요는없으나 범용 저장Resize
        Bitmap resized = resizeBitmapImage(bitmap, AppConst.IMAGE_MAX_SIZE);

        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            resized.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            // 에러처리
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Uri getImageUri(String path) {

        return Uri.fromFile(new File(path));
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees) throws Exception{
        if(degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if(bitmap != converted) {
                bitmap.recycle();
                bitmap = converted;
            }
        }
        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        if(options ==null || options.outWidth == -1 || options.outHeight ==-1)
            return 2;

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    // Get Image From URL
    public static Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;

        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            e.printStackTrace();
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }

        return imgBitmap;
    }

    public static Bitmap getBitmapWithResize(Context context, String path) {
        return getBitmapWithResize(context,getImageUri(path));
    }

    public static Bitmap getBitmapWithResize(Context context, Uri uri) {
//        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            // file:///storage/emulated/0/tmp_1419830825226.jpg
            // file:///external/images/media/519

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
//            if(o.outHeight > AppConst.IMAGE_MAX_SIZE || o.outWidth > AppConst.IMAGE_MAX_SIZE) {
//                scale = (int) Math.pow(2, (int) Math.round(Math.log(AppConst.IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
//            }
            scale = calculateInSampleSize(o,AppConst.IMAGE_MAX_SIZE, AppConst.IMAGE_MAX_SIZE);

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = context.getContentResolver().openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();
            return b;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public static int dpToPx(Context context, int dp) {
        if(context==null || dp == 0) return 0;

        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /*
     *  Bitmap Processing Methods Ends
     * ************************************
     */
}
