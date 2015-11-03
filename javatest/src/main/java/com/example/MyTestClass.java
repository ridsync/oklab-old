package com.example;

import java.util.Calendar;

public class MyTestClass {

    public static void main(String[] args){

        Calendar calendar = Calendar.getInstance();
        System.out.print("Alarm" + "setNotificationAlarm " + calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
//        calendar.set(Calendar.SECOND, 0);
        System.out.print("\nAlarm" + "setNotificationAlarm " + calendar.getTime());
    }

}
