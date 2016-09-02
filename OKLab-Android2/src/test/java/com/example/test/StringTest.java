package com.example.test;

import java.math.BigDecimal;
import java.text.NumberFormat;


public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. index contains Test
		String str = " 먹고잡아고구마를 먹고잡아";
		
		int index = str.indexOf("고구마");
		
		boolean value = str.contains("마담");
		
		System.out.print("index = " + index + " / " + "value = " + value);
		
		// 2. Java Call by Value (Reference) Test
		VRTest vrTest = new VRTest("sam");
		// 기본 데이터형은 모두 Call by Value로 처리되고, 
		// 클래스의 객체는 Call by Reference로 처리된다.
		doMethod(index, vrTest); 
		System.out.print("\n vrTest :: index = " + index + " / values :: " + vrTest.toString());
		
		// String -> Double 
		String stringValue = "100.00";
		stringValue = stringValue.replace(",", "");
		stringValue = stringValue.replace("\\", "");
//		Double values = Double.parseDouble(stringValue);
		Double values = currencyStringToDouble("21.00", true);
		System.out.print("\n stringValue = " + values.toString());
		System.out.print("\n stringValue = " + longDouble2String(2, values));
		
		// 3. BigDecimal 연산 Test
		BigDecimal su1 = new BigDecimal("12345678901234567899");
        BigDecimal su2 = new BigDecimal("12345678901234567890");
        BigDecimal p_add = su1.add(su2);   // 더하기  
	
		BigDecimal p_sub = su1.subtract(su2);   // 빼기  
		
		BigDecimal p_mul = su1.multiply(su2);   // 곱하기  
		
		BigDecimal p_div1 = su1.divide(su2, BigDecimal.ROUND_UP);   // 나누기 - 무조건 반올림  
		
		BigDecimal p_div2 = su1.divide(su2, 4, BigDecimal.ROUND_UP);  // 나누기 - 소수점 4번째 자리에서 반올림.
		        System.out.println("덧셈 : " + p_add);
		
		System.out.println("뺄셈 : " + p_sub);
		
		System.out.println("곱셈 : " + p_mul);
		
		System.out.println("나눗셈1 : " + p_div1);
		
		System.out.println("나눗셈2 : " + p_div2);
		String test = "01234";
		System.out.println("doMaskingEachDigitString : " +maskingEachDigitString(test, 1, 2));
		
		// 4. Obj vs Primitive 비교
		int prim = 2;
		Integer wrapVar = new Integer(2);
		boolean result = prim == wrapVar; 
		System.out.println("Integer : " + result );
		System.out.println("IntegerTest : " + getDoubleData("3") );
		
		String d = "1";
		System.out.println("SubstringTest : " + d.substring(1) );
	}
	
	public static String longDouble2String(int size, double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();

        nf.setMaximumFractionDigits(size);

        nf.setGroupingUsed(false);

        return nf.format(value);

    }
	
	private static Integer getIntData(String qty){
		Integer result = new Integer(0);
		if (qty != null){
			result = new Integer(qty);
		}
		return result; 
	}
	
	private static Double getDoubleData(String qty){
		Double result = new Double(0);
		if (qty != null){
			result = new Double(qty);
		}
		return result; 
	}
	
	/**
	 * String 지정 자리수별 Masking(*) 처리 하는 메소드<p>
	 * 1) targetStr의 자리수 값은 왼쪽부터 1로 입력<p>
	 * 2) startDigit이 더 큰수를 넣으면 masking 처리 안하고 그대로 return
	 * @param targetStr
	 * @param startDigit
	 * @param lastDigit
	 * @return
	 * @author ojungwon
	 */
	 public static String maskingEachDigitString(String targetStr ,int startDigit,int lastDigit) {
		 String maskedStr = "";
		 if (targetStr == null) return maskedStr;
		 
		 startDigit = startDigit -1;
		 lastDigit = lastDigit -1;
		 for(int i=0; i < targetStr.length() ;i++) {
			 Character digitText = targetStr.charAt(i);
			 if(startDigit <= i  && i <= lastDigit ) {
				 maskedStr = maskedStr.concat("*");
			 }else {
				 maskedStr = maskedStr.concat(digitText.toString());
			 }
		 }

		 return maskedStr;
	 }
	 
    /* 통화표기 스트링을 Double로 변경
    * @param currencyString
    * @return Double
    */
   public static Double currencyStringToDouble(String currencyString, boolean withoutSymbol) {
       Double retValue;

       String stringValue = "";
       Boolean isDouble = currencyString.contains(".");
       if (withoutSymbol) {
           stringValue = currencyString.replace(",", "");
       }
       else {
           stringValue = currencyString.substring(1).replace(",", "");            
       }
       stringValue = stringValue.replace(".", "");

       int index = 0;
       for (index = 0; index < stringValue.length(); index++) {
           if (currencyString.charAt(index) != '0') {
               break;
           }
       }

       if (index > 0) {
           if (withoutSymbol) {
               stringValue = stringValue.substring(index);
           }
           else {
               stringValue = stringValue.substring(index + 1);
           }
       }

       retValue = Double.valueOf(stringValue);
       if (isDouble) {
           retValue /= 100;
       }

       return retValue;
   }
	private static void doMethod(int index , VRTest test){
		index = 1000;
//		test = new VRTest("newbob");
		test.strVal = "bob";
		test.value = 55;
		
		System.out.print("\n doMethod local variable :: index = " + index + " / values :: " + test.toString());
	}
	
	public static class VRTest { 
		int value = 10;
		String strVal = "okVRE";
		
		public VRTest(String val) {
			strVal = val;
		}
		
		@Override
		 public String toString() {
			return "strVal = " + strVal + " / " + "value = " + value;
		 }
	}
}
