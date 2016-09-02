package com.example.test.oktest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPatternMatches {
	static String left = "^[";
	static String right = "]*$";
 public static void main(String[] args) {
	 	
		
        String [] mails = { "okjsd22fsdfdf@okjsp.com",
                            "df232.dd@okjsp.com",
                            "he-llo@kr.com",
                            "he-llo23@naver.co.kr",
                            "",
                            "happ_okjsp.pe.kr",
                            "dfdkf@happ_okjsp.pe.kr.",
                            "@happ_okjsp.pe.kr",
                            "happokjspfgigkdfjlkgjdklgjlgjflslklsfkldklsdlr"};
                           
        for(int i=0; i<mails.length; i++) {
            System.out.println(mails[i] + " : " + checkEmailTest(mails[i]));
        } // end for
        
//        if ( checkEmailTest("Df2dfsaf@sdfsfaf4favddaffd ") ){;
//        	System.out.println("true");
//        }else {
//        	System.out.println("false");
//        }
 }
    
	public static boolean checkEmailTest(String checkStr)
	{
		if(checkStr==null) return false;
//		Pattern checkPattern = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z0-9]+$  or  ^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$");
//		Pattern checkPattern = Pattern.compile("^[a-zA-Z0-9]+[@][a-zA-Z0-9]+[\\.]+\\w+[a-zA-Z0-9]+[\\.]*$");
		Pattern checkPattern2 = Pattern.compile("^\\D.+@.+\\.[a-z]+");
		Matcher checkMatcher = checkPattern2.matcher(checkStr);
		if (checkMatcher.matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	   
    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches(
        		"^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$",
            email.trim());
        return b;
    }
}