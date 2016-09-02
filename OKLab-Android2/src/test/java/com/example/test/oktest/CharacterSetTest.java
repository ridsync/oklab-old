package com.example.test.oktest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CharacterSetTest {

	public static void main( String[] agrs ){
        
		String d = "안녕 親9"; // 자바는 내부 문자열을 모두 유니코드 처리한다
		byte[] ar = {20, -107, -120, -21, -123, -107, 32, -24, -90, -86, 57};

		//유니코드 문자열을 UTF-8 캐릭터 바이트배열로 변환하여 반환
		byte[] utf8;
		byte[] def;
		try {
			
			def = d.getBytes();
			utf8 = d.getBytes("UTF-8");
			
			//유니코드 문자열을 EUC-KR 캐릭터 바이트배열로 변환하여 반환
			byte [] euckr = d.getBytes("EUC-KR");

			FileWriter filewriter;
			try {
				filewriter = new FileWriter("out");
				String encname = filewriter.getEncoding();
				filewriter.close();
				System.out.println("default charset is: " + encname);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//당연히 다른 바이트 배열이므로 사이즈가 다르다.
			System.out.println("byte length > " + def.length); // byte length > 11
			System.out.println("byte length > " + utf8.length); // byte length > 11
			System.out.println("byte length > " + euckr.length); // byte length > 8

			System.out.println(new String(def));
			System.out.println(new String(utf8, "UTF-8"));
			System.out.println(new String(euckr, "EUC-KR"));
			
			//실수 코드.
			//UTF-8 캐릭터셋으로 배열된 바이트배열을 EUC-KR로 해석할 수 없다.
			System.out.println(new String(utf8, "EUC-KR"));
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
    }
	
}
