package com.example.test.oktest;

import java.util.Random;
import java.util.logging.Logger;

public class RandomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Logger log = Logger.getLogger("ok");
			log.info( "random = " + getRndNumByRanDomClass() );
			log.info( "random = " + getRndNum() );
//			log.info( "random = " + getAuthSmsKey() );
//			log.info(""+ (int)Math.random()*10);
	}
	
	/**
	 * 가장 심플한 난수 발생 메소드
	 */
	public static String getRndNumByRanDomClass(){
		Random rd = new Random(System.currentTimeMillis());
		int rnd = rd.nextInt(9999); // 0 부터 9999 까지 발생
//		int rnd = rd.nextInt(9999) + 1; // 1 부터 10000 까지 발생
		
		 // 앞자리 0 필요하면 길이에따라 붙이면 됨
		
		return String.valueOf( rnd );
	}
	
	public static String getRndNum(){
		
		int rndNum =(int)(Math.random()*10);
		if (rndNum==0) rndNum = 1; 
		String smsKey = ""+ rndNum;
		for (int i=0;i<3;i++){
			rndNum = (int)(Math.random()*10);
			smsKey = smsKey + rndNum;
		}
		return smsKey;
	}
	public static String getAuthSmsKey() {
		Random rd = new Random();
		int x = rd.nextInt();
		if (x < 0)
			x = x * -1;
		
		if (x > 10000)
			x = x % 10000;
		
		String authKey = Integer.toString(x);
		String prefix = "";
		switch (authKey.length()) {
		case 1:
			prefix = "0000";
			break;
		case 2:
			prefix = "000";
			break;
		case 3:
			prefix = "00";
			break;
		case 4:
			prefix = "0";
			break;
		default:
			break;
		}
		authKey = prefix + authKey;
		return authKey;
		
	}
	
	
}
