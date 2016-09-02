//package com.example.test.oktest.oklab.network;
//
//import java.util.Date;
//import java.util.Properties;
//import java.util.regex.Pattern;
//
//import javax.activation.CommandMap;
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.activation.MailcapCommandMap;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
///**
// * Gmail SMTP 서버를 통한 JAVA Mail Sender
// * TLS , SSL 인증  및 파일 첨부 지원
// * @author ojungwon
// *
// */
//public class GMailSender extends javax.mail.Authenticator{
//
//	public static final int MODE_SECURITY_NONE = 0;
//	public static final int MODE_SECURITY_SSL = 1;
//	public static final int MODE_SECURITY_TLS = 2;
//
//	// Mail SMTP Server Properties Setting via Gmail
//	private final String username = "cnysdir@gmail.com";
//	private final String password = "%tgbnhy^";
//
//	private String[] mToArr;
//	private String mFrom;
//	private String mSubject;
//	private String mBody;
//	private String mText;
//	private int modeSecType;
//
//	private Multipart mMultipart;
//
//	GMailSender(int modeSecType){
//		this.modeSecType = modeSecType;
//
//		mMultipart = new MimeMultipart();
//
//		// There is something wrong with MailCap, javamail can not find a
//		// handler for the multipart/mixed part, so this bit needs to be added.
//		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
//				.getDefaultCommandMap();
//		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
//		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
//		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
//		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
//		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
//		CommandMap.setDefaultCommandMap(mc);
//	}
//
//	GMailSender(int modeSecType, String host , String protocol){
//		this(modeSecType);
//	}
//
//	// TLS Backgroud 처리 해야함.
//	public void sendMail(){
//		System.out.println("SMTP Email Sending");
//
//		// Param Validation Check
////		if (! isValidMailAddress()){
////			System.out.println("ValidMailAddress is Not Valid");
////			return;
////		}
//		// Attatchment File Validation Check
////		if (! isValidMailAddress()){
////			System.out.println("Attatched File is Not Exist");
////			return;
////		}
//
//		Properties props = getProperties(modeSecType);
//
//		Session session = Session.getInstance(props,
//		  new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username, password);
//			}
//		  });
//
//		// Transport Message Send
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setSentDate(new Date());
//			message.setHeader("Content-TYpe", "text/html; charset=UTF-8");
//			message.setFrom(new InternetAddress(mFrom));
//			InternetAddress[] addressTo = new InternetAddress[mToArr.length];
//			for (int i = 0; i < mToArr.length; i++) {
//				addressTo[i] = new InternetAddress(mToArr[i]);
//			}
//			message.setRecipients(MimeMessage.RecipientType.TO, addressTo);
////			message.setTo("ridsync@gmail.com");
//			message.setSubject(mSubject);
//			if (mText !=null){
//				message.setText(mText); // type Plain Text
//			}
//			if (mBody !=null){
//				// setup message body  // type HTML
//				BodyPart messageBodyPart = new MimeBodyPart();
//				messageBodyPart.setContent(mBody, "text/html; charset=UTF-8");
//				mMultipart.addBodyPart(messageBodyPart);
//				// Put parts in message
//				message.setContent(mMultipart);
//			}
//			Transport.send(message);
//
//			System.out.println("Email Send Finished");
//
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	// From To 모두 이메일 형식이어야 발송됨 ...
//	private boolean isValidMailAddress() {
//		boolean result = false;
//
//		if (mToArr != null){
////			if (isEmail(mFrom)){
////				 result = true;
////			}
//			for (int i = 0; i < mToArr.length; i++) {
//				if (isEmail(mToArr[i])){
//					result = true;
//					break;
//				}
//			}
//		}
//
//		return result;
//	}
//
//	private boolean isEmail(String email) {
//        if (email==null) return false;
//        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
//        return b;
//    }
//
//	// Mail SMTP Server Properties Setting
//	private Properties getProperties(int modeSecType) {
//		Properties result =new Properties();
//
//		switch (modeSecType) {
//		case MODE_SECURITY_SSL: // SSL
//			result.put("mail.smtp.host", "smtp.gmail.com");
//			result.put("mail.smtp.port", "465");
//			result.put("mail.smtp.socketFactory.port", "465");
//			result.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			result.put("mail.smtp.auth", "true");
//			break;
//		case MODE_SECURITY_TLS:
//			result.put("mail.smtp.host", "smtp.gmail.com");
//			result.put("mail.smtp.port", "587"); // TLS
//			result.put("mail.smtp.auth", "true");
//			result.put("mail.smtp.starttls.enable", "true");
//		default:
//			result.put("mail.smtp.host", "smtp.gmail.com");
//			result.put("mail.smtp.port", "25"); // SMTP 25
//			result.put("mail.smtp.auth", "true");
//			result.put("mail.smtp.starttls.enable", "true");
//			break;
//		}
//		return result;
//	}
//
//	/**
//	 * 파일 첨부
//	 * "/data/data/com.upsolution.upsalon/email_receipt.html"
//	 * @param filePath
//	 * @throws Exception
//	 */
//	public void addAttachment(String filePath) {
//		BodyPart messageBodyPart = new MimeBodyPart();
//		DataSource source = new FileDataSource(filePath);
//		try {
//			messageBodyPart.setDataHandler(new DataHandler(source));
//			messageBodyPart.setFileName(source.getName());
//			mMultipart.addBodyPart(messageBodyPart);
//		} catch (MessagingException e) {
//			//TODO 파일 예외처리 별도 처리 필요함 Callback or Notify
//			e.printStackTrace();
//		}
//	}
//
//	// the getters and setters
//	public void setTo(String toAddr) {
//		this.mToArr = new String[]{toAddr};
//	}
//
//	public void setTo(String[] toAddr) {
//	this.mToArr = toAddr;
//	}
//
//	public void setFrom(String string) {
//	this.mFrom = string;
//	}
//
//	public void setSubject(String string) {
//	this.mSubject = string;
//	}
//
//	public String getBody() {
//		return mBody;
//	}
//
//	public void setBody(String _body) {
//		this.mBody = _body;
//	}
//
//	public String getText() {
//		return mText;
//	}
//
//	public void setText(String text) {
//		this.mText = text;
//	}
//}
