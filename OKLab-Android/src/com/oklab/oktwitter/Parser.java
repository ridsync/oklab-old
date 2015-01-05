package com.oklab.oktwitter;
//package com.academy.ok;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//
//public class Parser {
//	
//	ArrayList<Status> parseTimeLine(XmlPullParser xpp) throws XmlPullParserException, IOException {
//		Log.d("TA", "parserTimeline Start ");
//		ArrayList<Status> list = new ArrayList<Status>();
//		
//		// ����
//		int evtType = xpp.getEventType();
//
//		Status s = null;
//		String tag = "";
//		
//		while(evtType != XmlPullParser.END_DOCUMENT) {
//	
//			int eventType = xpp.nextTag();
//			tag = xpp.getName();
//			Log.d("TA", "parserTimeline while1");
//			if(eventType == XmlPullParser.START_TAG) {
//				
////				Log.d("TA", "2-1 tag : "+ tag);
//				// Root tag. 
//				if("statuses".equals(tag)) {
//					// do nothing
//				}else if("status".equals(tag)) {
//					s = new Status();
//					// do nothing
//					Log.d("TA", "status() Created");
//				}else if("created_at".equals(tag)) {
////						Log.d("TA", "5 tag : "+ tag);
//						s.setCreated_at(xpp.nextText());
//				}else if("text".equals(tag)) {
////						Log.d("TA", "6 tag : "+ tag);
//						s.setText(xpp.nextText());
//				}else if("screen_name".equals(tag)) {
//						s.set_ScreenName(xpp.nextText());
//				}else if("profile_image_url".equals(tag)) {
//						s.set_BitImage(downloadImage(xpp.nextText()));
//				}
////				if("statuses".equals(tag)) {
////					// ������ �ǳʶٰ�.
////				}else if("status".equals(tag)) {
////					Log.d("TA", "8 : "+ xpp.nextText());
////					s = parseStatus(xpp);
////					// do nothing.
////				}
//
//			}else if(eventType == XmlPullParser.END_TAG) {
//				if("status".equals(tag)) {
//					Log.d("TA", "end : "+ tag);
//					list.add(s); 
//					Log.d("TA", "list added");
//					//<status></status> ������ ���ϴ� ������ s�� �����ϰ� ����Ʈ�� ��´�.
//				}
//			}
//
//			evtType = xpp.next();
//		}
//		
////		Log.d("TA", "size : "+ list.size()+" : "+ cnt);
//		return list;
//		
//	}
//
//	public Bitmap downloadImage(String profile_image_url){
//		URL url = null;
//		Bitmap bmp = null;
//		try{
//		url = new URL(profile_image_url);
//		
//		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//		InputStream is = conn.getInputStream();
//		bmp = BitmapFactory.decodeStream(is);
//		is.close();
//		}catch(MalformedURLException e){
//			Log.e("TA",e.getMessage());
//		}catch(IOException e){
//			Log.e("TA",e.getMessage());
//		}
//		return bmp;
//	}
//	
//	User parseUser(XmlPullParser xpp) throws XmlPullParserException, IOException{
//		User u = new User();
//		
//		while(true) {
//			int eventType = xpp.nextTag();
//			
//			if(eventType == XmlPullParser.START_TAG) {
//				String tag = xpp.getName();
//				
//				if("screen_name".equals(tag)) {
//					u.setScreenName(xpp.nextText());
//				}else if("profile_image_url".equals(tag)){
//					u.set_BitImage(downloadImage(xpp.nextText()));
//				}
//				
//			}else if(eventType == XmlPullParser.END_TAG) {
//				break;
//			}
//		}
//		return u;
//		
//	}
//	
//	Status parseStatus(XmlPullParser xpp){
//		return null;
//		
//	}
//
//}
