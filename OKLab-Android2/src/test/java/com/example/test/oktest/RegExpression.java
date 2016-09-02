package com.example.test.oktest;

import java.util.regex.*;

public class RegExpression {

   public static void main(String[] args) throws Exception {

       //한개이상의연속된‘,’와공백문자를의미하는패턴을만든다.

       Pattern p =Pattern.compile("[,{space}]+");

       //패턴에따라입력문자열을쪼갠다.

       String[] result =

                p.split("one,two, three  four , five");

       for (int i=0;i<result.length;i++)

           System.out.println(result[i]);

   }

}