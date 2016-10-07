package com.example.test.oktest.oklab.crypto;
import com.example.test.oktest.oklab.iofile.Timer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.crypto.Cipher;

/**
 * @author ojungwon
 *
 */
public class CryptoTest {

	private static final String SRC_FILE_NAME = "filechannel.log";
	private static final String DST_FILE_NAME = "copy_database.db";
	private static final String ENCRYPT_FILE_NAME = "encrypt_database.db";
	private static final String DECRYPT_FILE_NAME = "decrypt_database.db";

	private static String FILE_PATH = null;

	public static void log(String str) {
		System.out.println(str);
	}

	Timer timer = new Timer(Timer.Resolution.MILLISECOND);
	
	public static void main(String[] args) {

		/**
		 * 머신 루트 디렉토리 경로의 파일 복사
		 */
		File[] roots = File.listRoots();
		FILE_PATH = roots[1] + "OKDownload\\"; // roots[1] 두번째 드라이브

		File src = new File(FILE_PATH, SRC_FILE_NAME);
		File dst = new File(FILE_PATH, DST_FILE_NAME);
		File enc = new File(FILE_PATH, ENCRYPT_FILE_NAME);
		File dec = new File(FILE_PATH, DECRYPT_FILE_NAME);

		/**
		 * 파일 복사시 암호화 복호화 하기. CryptoManager 사용
		 */
//		 boolean isCrypt =
//		 CryptoManager.encodeFileByAES(CryptoManager.getSecurityKeyForFile(null), src, enc);
//		 System.out.println("isCrypt = " + isCrypt);
//
//		 boolean isdeCrypt =
//		 CryptoManager.decodeFilebyAES(CryptoManager.getSecurityKeyForFile(null), enc, dec);
//		 System.out.println("isdeCrypt = " + isdeCrypt);

		// Test
		String path = System.getProperty("user.dir");
		System.out.println("user.dir Path = " + path);
		System.out.println(new File(".").getAbsolutePath());
		System.out.println(roots[0].getAbsolutePath());
		System.out.println(roots[0].getName());
		
		
		/**
		 * String 암호화 복호화 하기. Cipher + Base64 사용
		 */
		 String plaintext = "Encrypt test ok";
		 System.out.println("plaintext = " + plaintext);
		 String strEnc = CryptoManager.cryptoStringByAES(Cipher.ENCRYPT_MODE, plaintext);
		 System.out.println("strEnc = " + strEnc);

		 String strDec =CryptoManager.cryptoStringByAES(Cipher.DECRYPT_MODE, strEnc);
		 System.out.println("strDec = " + strDec);
	}

}
