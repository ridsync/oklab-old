package com.okitoki.checklist.utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-02-09.
 */
public class JavaUtil {

    public static long	PREVIOUS_TIME = 0;
    public static long	BAK_KEY_PREVIOUS_TIME = 0;
    public static long	INTERVAL_PREVENT_DOUBLE_CLICK = 1000;

    public static boolean isEmptyString(String str) {
        boolean retVal = false;
        if (str != null) {
            if (str.isEmpty()) {
                retVal = true;
            } else if (str.equalsIgnoreCase("null")) {
                retVal = true;
            }
        } else {
            retVal = true;
        }
        return retVal;
    }

    /** 더블클릭 방지
     * @return
     */
    public static boolean isDoubleClick(){
        long curTime = System.currentTimeMillis();

        if (INTERVAL_PREVENT_DOUBLE_CLICK == 0) {
            PREVIOUS_TIME = curTime;
            INTERVAL_PREVENT_DOUBLE_CLICK = 1200;
            return false;
        }



        if (curTime - PREVIOUS_TIME < INTERVAL_PREVENT_DOUBLE_CLICK){
            return true;
        }

        PREVIOUS_TIME = curTime;
        return false;
    }

    public static boolean isDoubleClick(int interval){

        long curTime = System.currentTimeMillis();
        if (curTime - BAK_KEY_PREVIOUS_TIME < interval){
            return true;
        }

        BAK_KEY_PREVIOUS_TIME = curTime;
        return false;
    }

    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>)s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>)s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[])s).length == 0);
        }
        return false;
    }

//    Calendar c = Calendar.getInstance();
//    System.out.println("오늘은 이달의 몇주차인지 = " + c.get(Calendar.WEEK_OF_MONTH));
//    System.out.println("오늘요일은 이달의 몇번째나온 주차인지 = " + c.get(Calendar.DAY_OF_WEEK_IN_MONTH));
//
//    //2008년 12월 3주차 일요일(SUNDAY=1)은 몇일인지를 리턴합니다.
//    String day = getDate( 2008, 12, 3, 1 );
//    System.out.println("2008년 12월 3주차 일요일(SUNDAY=1)은 몇일인지 =" + day);

    public static String getDate( int year, int month, int week, int dayOfWeek )

    {

	   /*

	   Calendar.SUNDAY = 1

	   Calendar.MONDAY = 2

	   Calendar.TUESDAY = 3

	   Calendar.WEDNESDAY = 4

	   Calendar.THURSDAY = 5

	   Calendar.FRIDAY = 6

	   Calendar.SATURDAY = 7

	   */

        DecimalFormat df = new DecimalFormat("00");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year );

        calendar.set(Calendar.MONTH, month - 1);

        calendar.set(Calendar.WEEK_OF_MONTH, week );

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek );

        String strMonth = df.format(calendar.get(Calendar.MONTH) + 1);

        String strDay = df.format(calendar.get(Calendar.DAY_OF_MONTH));

        String date = strMonth + "월" + strDay +"일";

        return date;

    }
}
