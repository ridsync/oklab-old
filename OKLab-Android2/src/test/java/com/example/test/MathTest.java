package com.example.test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(round(3.1415, 1));
		
		System.out.println(round(0.78367, 100, 2) + "\n");
		
		roundByDecimalFormat();
	}
	
	/**
	 * 소수 반올림 처리 round 함수
	 * @param d  숫자
	 * @param n 자리수
	 */
	static double round(double d, int n) {
	      return Math.round(d * Math.pow(10, n)) / Math.pow(10, n);
	}
	
	static double round(double d, double l, int n) {
		return Math.round(d * l * Math.pow(10, n)) / Math.pow(10, n);
	}
	
	/**
	 * DecimalFormat 과 RoundingMode 를 이용한 올림 소수점 반올림 처리. 
	 */
	static void roundByDecimalFormat(){
		
		DecimalFormat df = new DecimalFormat("0.###");
        double num[] = {1.9779, 1.9752, 1.9724};
        RoundingMode[] modes = {RoundingMode.UP, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.FLOOR, RoundingMode.HALF_UP, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN};
         
        for(int n=0; n<3; n++){
            System.out.println("number : "+num[n]);
             
            for(int i=0; i<7; i++){
                df.setRoundingMode(modes[i]);
                System.out.println(modes[i]+" : "+df.format(num[n]));
            }
             
            System.out.println();
        }
	}
}
