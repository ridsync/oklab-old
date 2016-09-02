package com.oklab.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;

import org.apache.commons.codec.binary.Base64;

//import android.util.Base64;

/**
 * AES암호화 , BASE64인코딩  - String & File
 * 안드로이드 라이브러리에서 제공되는 Base64 나 Apache의 Base64 를 각 사용 구분 (android.util.Base64)
 * 문자 및 파일 암(복)호화 관리 클래스
 * 1. 키관리  키존재여부 체크,키 가져오기 , 키설정
 * 2. 암(복)호화 메소드 (String , is 암(복)호화?)
 */
public class CryptoManager{

	private final static int DECRYPT_KEY_LENGTH = 16; // 암호화 키 자리수
	
	private final static byte[] ivBytes = { 0x30, 0x41, 0x41, 0x30, 0x43, 0x31, 0x33, 0x31, 0x37, 0x36, 0x44, 0x39, 0x38, 0x44, 0x39, 0x37 };

	private final static int BUFFSIZE = 4096; //버퍼의 크기
	
	private final static boolean isUseBase64ForFile = false ; // 파일복호화 Base64유무
	
	/**
	 * 복호화 키 가져오기 - 초기화DATA파일용(ZIP파일)
	 * @param context
	 * @return
	 */
	public static String getSecurityKeyForFile(Context context){
		return "sl25gmfp3d9da16d";
	}
	
	/**
	 * 복호화 키 가져오기 - 개인정보용(String) 
	 * @return key
	 */
	private static String getSecurityKey(){
//		return EagleTalkCoreApplication.getSecurityKey();
		return "sl25gmfp3d9da16d";
	}
	
	/**
	 * 문자 암호화
	 * 
	 * @History <br/>
	 *          modify date ----- author ----- contents
	 * 
	 * @param key
	 * @param str
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 *
	 */
	public static String encodeStringByAES(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException {
		String key = getSecurityKey();
		if ( key == null || key.length() < DECRYPT_KEY_LENGTH ) return null;
		
		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		     SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		     Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		
		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}

	/**
	 * 문자 복호화
	 * 
	 * @History <br/>
	 *          modify date ----- author ----- contents
	 * 
	 * @param key
	 * @param str
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 *
	 */
	public static String decodeStringByAES(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String key = getSecurityKey();
		if ( key == null || key.length() < DECRYPT_KEY_LENGTH ) return null;
		
		byte[] textBytes;
		
		textBytes =Base64.decodeBase64(str); // Base64 encode일때
//		textBytes = str.getBytes("UTF-8");
		
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}
	
	/**
	 * 문자 암복호화 ( 모드로 구분 )
	 * 
	 * @History <br/>
	 *          modify date ----- author ----- contents
	 * 
	 * @param key
	 * @param str
	 * @see Cipher , Apache의 Base64 사용
	 * @return
	 */
	public static String cryptoStringByAES(int opmode, String str) {
		String result = null;
		String key = getSecurityKey();
		if ( key == null || key.length() < DECRYPT_KEY_LENGTH ) return null;
		try {
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		    SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(opmode, newKey, ivSpec);
			
			byte[] textBytes = null ;
			if (opmode == Cipher.ENCRYPT_MODE){
				textBytes = str.getBytes("UTF-8");
				result = Base64.encodeBase64String(cipher.doFinal(textBytes));
			}else if (opmode == Cipher.DECRYPT_MODE){
				textBytes = Base64.decodeBase64(str);
				result = new String (cipher.doFinal(textBytes), "UTF-8");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 모드로 구분 가능하다.. String암복호화처럼  한 메소드로 가도 되듯 ??
	 */
	
	/**
	 * File 암호화 파일저장
	 * @param src 암호화할 파일
	 * @param dest 저장대상 파일
	 * @return file 암호화된 파일</br>
	 * 키관련오류나 기타 인코딩 오류시 false 반환됨
	 */
	public static boolean encodeFileByAES(String key,File src, File dest) {
		boolean isResult = false;
		if ( key == null || key.length() < DECRYPT_KEY_LENGTH ) return isResult;
		
		BufferedInputStream in = null;
		CipherOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src));
			
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			     SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			     Cipher cipher = null;
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
			
			out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(dest)), cipher, isUseBase64ForFile);
			
			byte buffer[] = new byte[BUFFSIZE];
			int length = 0;
			
			while ((length = in.read(buffer)) >= 0) { //파일을 끝까지 읽는다.
				out.write(buffer, 0, length);
			}
			out.flush();
			isResult = true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isResult;
	}
	
	/**
	 * File 복호화 파일저장
	 * @param src 암호화된 파일
	 * @param dest 저장대상 파일
	 * @return file 복호화된 파일</br>
	 * 키관련오류나 기타 인코딩 오류시 false 반환됨
	 */
	public static boolean decodeFilebyAES(String key,File src, File dest){
		boolean isResult = false;
		if ( key == null || key.length() < DECRYPT_KEY_LENGTH ) return isResult;
		
		CipherInputStream in = null;
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(dest));
			
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			
			in = new CipherInputStream(new BufferedInputStream(new FileInputStream(src)), cipher);

			byte buffer[] = new byte[BUFFSIZE];
			int length = 0;
			
			while ((length = in.read(buffer)) >= 0) { //파일을 끝까지 읽는다.
//				if (isUseBase64ForFile){
//					buffer = Base64.decode(buffer, Base64.DEFAULT);
//				}
				out.write(buffer, 0, length);
			}
			out.flush();
			isResult = true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return isResult;
	}
	
}
