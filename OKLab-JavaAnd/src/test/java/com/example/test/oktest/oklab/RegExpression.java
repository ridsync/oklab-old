package com.example.test.oktest.oklab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;

public class RegExpression {

   public static void main(String[] args) throws Exception {

       //한개이상의연속된‘,’와공백문자를의미하는패턴을만든다.

//       Pattern p =Pattern.compile("[,{space}]+");
//
//       //패턴에따라입력문자열을쪼갠다.
//
//       String[] result =
//
//                p.split("one,two, three  four , five");
//
//       for (int i=0;i<result.length;i++)
//           System.out.println(result[i]);


       // Pattern Matcher
        String strDates = "9월15일,9월28일 ";

       String[] arDates = strDates.split(",");
       Date holiday = null;

       for (int i=0;i< arDates.length;i++) {
           String date = arDates[i];
           if (strDates.contains("/")) {
               String res = date.replaceAll("[^\\d/]", "");
               SimpleDateFormat dateFormat4 = new SimpleDateFormat("M/dd");
               holiday = dateFormat4.parse(res);
           } else {
               SimpleDateFormat dateFormat4 = new SimpleDateFormat("M월dd일");
               holiday = dateFormat4.parse(date);
           }
       }
       System.out.println(holiday.toString());

//       Pattern pattern = Pattern.compile("^[0-9]{1,2}/[0-9]{2}$");
//
////       Matcher m = pattern.matcher("9월15일,9월28일 ");
//       Matcher m = pattern.matcher("9/15(목),9/28(수) ");
//
//       boolean a = false;
//
//       while(a = m.find()){
//
//           System.out.println(m.start() + " " + m.group());
//
//       }

   }

}