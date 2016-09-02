package com.oklab.util;

import android.R;
import android.util.Base64;

import com.oklab.BaseActivity;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 1) BASE64  인코딩
 *
 * 2) MD5, MD2, SHA-1, SHA-256, SHA-384, SHA-512 등  단방향 암호화 : 해시함수  복호화 불가.
 * => JAVA : MessageDigest Class
 * 
 * 3) AES, DES, DESede, SEED , 3DES (대칭키) / RSA(비대칭키) 양방향 암호화(블럭 암호화) : 복호화 가능.
 * => JAVA : Cipher Class
 * 
 * OKLab-Java : CryptoManager 참조.
 * http://blog.kangwoo.kr/44
 * 
 * @author ojungwon
 *
 */
public class EncryptionTest extends BaseActivity{

	@Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_item);

		// 1. Encode Base64 Test
		String base64Str = EncryptionTest.encodeBase64("안녕하세요");
        System.out.println(base64Str);

        System.out.println(EncryptionTest.decodeBase64(base64Str));

        // 2. Encdoe SHA-1
      		String sha = EncryptionTest.encodeMd("안녕하세요", "SHA-512");
              System.out.println(sha);

        // 3. Encdoe AES , DES
        String sha2 = EncryptionTest.encodeMd("안녕하세요", "SHA-512");
//        System.out.println(sha2);

    };


    /**

     * message를 Base64인코딩하여 반환한다.

     * @param message

     * @return

     */

    public static String encodeBase64(String message){


            return Base64.encodeToString( message.getBytes(), Base64.DEFAULT);

    }

	/**

     * BASE64 디코딩

     * @param message

     * @return

     */

    public static String decodeBase64(String mesdecodeMessage){

    		byte[] message = Base64.decode(mesdecodeMessage, Base64.DEFAULT);

            return new String(message);

    }

	/**

     * message를 SHA1으로 변환한뒤 Base64인코딩하여 반환한다.

     * @param message

     * @return

     */

    public static String encodeMd(String message, String algorithm){
        if(message ==null || algorithm == null ) return null;

        MessageDigest md;

        try {
            md = MessageDigest.getInstance(algorithm);
            byte[] bytes = message.getBytes(Charset.forName("UTF-8"));
            return Base64.encodeToString( md.digest(bytes), Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e){

            e.printStackTrace();

            return null;
        }

    }

}
