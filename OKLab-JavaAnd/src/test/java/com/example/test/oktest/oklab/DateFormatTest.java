package com.example.test.oktest.oklab;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String sRegDate = "2013-12-31 13:27:26.000";
		
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
				Locale.getDefault());
		try {
			date = sdf.parse(sRegDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String dateString = sdf.format(date);
		System.out.println(dateString);
		
		Date  date2 = new Date();
		date2.setTime(1000);
		System.out.println( date2.toString() );
		
		NumberFormat numformat = new DecimalFormat("###,##0.00");
		 String result = numformat.format(1234.123);
		 System.out.println(result);
		 
		 System.out.println(getStringDecimalFormatted("3087879.080000000000000004") );
	}
	
	public static String getStringDecimalFormatted(String deciString){
		String result = null;
	try {
		 Double dob = Double.parseDouble(deciString);
		 result =  makeMoneyType(dob);
	} catch (Exception e) {
		return result = "0.00";
	}
	return result;
	
	}
	
	public static String makeMoneyType(Double dblMoneyString) 
	{ 
		if(dblMoneyString == null) {
			return "0.00";
		}
		String moneyString = dblMoneyString.toString(); 
		String format = "###,##0.00"; 
		DecimalFormat df = new DecimalFormat(format); 
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(); 

		dfs.setGroupingSeparator(',');
		df.setGroupingSize(3);
		df.setDecimalFormatSymbols(dfs); 
		return (df.format(Double.parseDouble(moneyString))).toString(); 
	}

}
