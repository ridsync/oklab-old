//package com.example.test.oktest.oklab.network;
//
//import java.io.File;
//
//public class MailSendTest {
//
//	private static final String SRC_FILE_NAME = "salonlog.txt";
//
//	private static String FILE_PATH = null;
//
//	private static String to = "ridsync@gmail.com";
//	private static String[] toArr = {"cnysdir@gmail.com", "ridsync@gmail.com"};
//	private static String from = "ridsync@naver.com";
//	private static String subject = "gdsgjdlgjdlgjkdlgj";
//	private static String body = "body fdksfafsdfsdjfs";
//	private static String text = "text ssssssssss fdksfafsdfsdjfs";
//
//	public static final void main(String[] srgs) {
//
//		File[] roots = File.listRoots();
//		FILE_PATH = roots[1] + "OKDownload\\"; // Win roots[1] 두번째 드라이브
//
//		File src = new File(FILE_PATH, SRC_FILE_NAME);
//		String filePath = src.getAbsolutePath();
//
//		// SMTP , TLS , SSL  3가지 방식으로 이메일 발송 옵션
//		GMailSender mail  = new GMailSender(GMailSender.MODE_SECURITY_SSL);
////		mail.setTo(toArr); // 여러명에게 동시에 보냄... 제한은 있을거임
//		mail.setTo(to);
//		mail.setFrom(from); // Gmail SMTP사용 Auth 계정으로만 보내지고, 의미없다 ?
//		mail.setSubject(subject);
//		mail.setText(text);
////		mail.setBody(body); // HTML등 다른형식 지원
////		mail.addAttachment(filePath); // 첨부파일
//		mail.sendMail();
//
//	}
//}
