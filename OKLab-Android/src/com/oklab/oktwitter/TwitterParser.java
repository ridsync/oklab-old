package com.oklab.oktwitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Log;

public class TwitterParser {
	Status s;
	User u;
	
	ArrayList<Status> parseTimeLine(XmlPullParser xpp) throws XmlPullParserException, IOException {
		Log.d("Parser", "parseTimeLine Start");
		ArrayList<Status> mList = new ArrayList<Status>();	

		while(true)	{
			int eventType = xpp.nextTag();
			if(eventType == XmlPullParser.START_TAG) {
				String tag = xpp.getName();
				Log.d("Parser", "parseTimeLine tag : " + tag);
				if("status".equals(tag)) {
					parseStatus(xpp);
					mList.add(new Status(s.createdAt, s.text, u, s.id));
					
				}
			}else if(eventType == XmlPullParser.END_TAG) {
				break;
			}
		}
		Log.d("Parser", "parseTimeLine Finished");
		return mList;
	}
	
	Status parseStatus(XmlPullParser xpp) throws XmlPullParserException, IOException {
		Log.d("Parser", "parseStatus Start");
		s = new Status();
		
		while (true) {
			int eventType = xpp.nextTag();
			if (eventType == XmlPullParser.START_TAG) {
				String tag = xpp.getName();
				Log.d("Parser", "parseStatus tag : " + tag);
				if("status".equals(tag)){
					
				} else if ("created_at".equals(tag)) {
					s.setCreatedAt(getCreatedAtRelative(xpp.nextText())); //created_at Set�κ�
					//s.createdAt = getCreatedAtRelative(xpp.nextText()); ���� ����
				} else if ("id".equals(tag)) {
					s.id = Long.parseLong(xpp.nextText()); //id Set�κ�
					Log.d("Parser", "s.id : " + s.id);
				}else if ("text".equals(tag)) {
					s.setText(xpp.nextText()); //text Set�κ�
					Log.d("Parser", "s.text : " + s.text);
				} else if ("user".equals(tag)) {
					s.setUser(parseUser(xpp)); //user��ü Set�κ�
				} else if ("retweeted_status".equals(tag)) {
					xpp.nextToken();	
				} 
				else{
					xpp.nextText();
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				String tag = xpp.getName();
				if ("retweeted_status".equals(tag)) {
					xpp.nextToken();	
				}else{
					break;					
				}
				
			}
		}
		Log.d("Parser", "parseStatus Finished");
		return s;
	}

	
	User parseUser(XmlPullParser xpp) throws XmlPullParserException, IOException {
		Log.d("Parser", "parseUser Start");
		u = new User();
		while(true) {
			int eventType = xpp.nextTag();
			if(eventType == XmlPullParser.START_TAG) {
				String tag = xpp.getName();
				Log.d("Parser", "parseUser tag : " + tag);
				if("user".equals(tag)){
					// doo nothing
				} else if("screen_name".equals(tag)) {
					u.setScreen_name(xpp.nextText());//screen_name Set�κ�
					Log.d("Parser", "u.screen_name : " + u.screen_name);
				} 

				else if("profile_image_url".equals(tag)) {
					URL url;
					try{
						url = new URL (xpp.nextText());
						HttpURLConnection conn = (HttpURLConnection)url.openConnection();
						InputStream is = conn.getInputStream();
						u.setProfile_image_url(BitmapFactory.decodeStream(is));//image Set�κ�
						is.close();
						conn.disconnect();
						Log.d("Parser", "u.profile_image_url : " + u.profile_image_url);
					} catch(MalformedURLException e){
						Log.e("Parser", e.getMessage());
					} catch(IOException e){
						Log.e("Parser", e.getMessage());
					}
				} else if ("status".equals(tag)) {
					xpp.nextToken();	
				} else{
					//parseOthers(xpp);
					xpp.nextText();
				}
			} else if(eventType == XmlPullParser.END_TAG) {
				break;
			}
		}
		Log.d("Parser", "parseUser Finished");
		return u;
	}
	
//	public Bitmap downloadImage(String profile_image_url){
//	URL url = null;
//	Bitmap bmp = null;
//	try{
//	url = new URL(profile_image_url);
//	
//	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//	InputStream is = conn.getInputStream();
//	bmp = BitmapFactory.decodeStream(is);
//	is.close();
//	}catch(MalformedURLException e){
//		Log.e("Parser",e.getMessage());
//	}catch(IOException e){
//		Log.e("Parser",e.getMessage());
//	}
//	return bmp;
//}

	//createAT - date��� ���ð����� ���� �ϴ� �޼ҵ�
	String getCreatedAtRelative(String time) {		

		Date date = new Date(time);
		long timeMillis = date.getTime();
		return DateUtils.getRelativeTimeSpanString(timeMillis, System.currentTimeMillis(), 0, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
	}

	
}
