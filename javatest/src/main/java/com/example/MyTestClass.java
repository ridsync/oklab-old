package com.example;

import java.text.DecimalFormat;
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

        String vlaue = "\\8900";
        String strEachItemPerPrice = "#1 month for #2/month"; //TODO
        String strPerPrice = "0";
        String strsaveRatio = "0";
        try {
            String strCurrency = vlaue.replaceAll("[0-9]", "").replaceAll(",", "").replaceAll("\\.", "");
            String numPrice = vlaue.replaceAll("[^0-9|^.]", "").replaceAll(",", "");
            boolean hisPrimeNumber  = vlaue.indexOf(".") >= 0 ? true: false;

            Double totalPrice = Double.parseDouble(numPrice);
            Integer month = Integer.parseInt("1");
            Double monthPrice = totalPrice / month;
            strPerPrice = strCurrency + String.format("%.2f" , monthPrice); ;

        } catch (Exception e){
            e.printStackTrace();
        }
        strEachItemPerPrice = strEachItemPerPrice.replace("#1", "6");
        strEachItemPerPrice = strEachItemPerPrice.replace("#2", strPerPrice);
        // perPrice
        System.out.print("\n"+ strEachItemPerPrice);
        System.out.print("\n"+ strsaveRatio);
    }

}
