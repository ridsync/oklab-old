package com.oklab.oktwitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;


// Http�����Ͽ� xml������ ������� API ��� (Twitter�� �� xml������, ������ ����)
public class TwitterApi {
	
	int statusCode;
	public static final String CONSUMER_KEY = "1kuClSeLE3vUceRoVzwdA";
	public static final String CONSUMER_SECRET = "bN7MVLuFnHcJbdCUyGBSRuKD2xoxk1f1NXh2DBxdA";
	public static final String CALLBACK_URL = "twit://twitlecture.com";
	
//	String mToken;
//	String mTokenSecret;
	CommonsHttpOAuthConsumer mConsumer;

	
	public TwitterApi(String token, String tokenSecret){
		mConsumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		mConsumer.setTokenWithSecret(token, tokenSecret);
	}
	//OAuth �����κ�
	public User verifyCredentials() {
		
		User user = new User();
		Log.d("TwitterApi", "verifyCredentials Start");
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://api.twitter.com/1/account/verify_credentials.xml");
			mConsumer.sign(httpGet); //����
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			
			if( statusCode != 200){
				Log.d("TwitterApi", "Status Code Error : " + statusCode);
				
					if (entity != null){
					InputStreamReader reader = new InputStreamReader(entity.getContent());
					BufferedReader bReader = new BufferedReader(reader);
					String s;
					
					while((s = bReader.readLine()) != null ){
						Log.e("TwitterApi",s);
					}
					bReader.close();
					}
				 return null;
				
				} else {
					if (entity != null){
						Log.d("TwitterApi","Status Code : " + statusCode);
						InputStream instream = entity.getContent();
						//Parse User
						XmlPullParserFactory factory;
						factory = XmlPullParserFactory.newInstance();
						XmlPullParser xpp = factory.newPullParser();
						xpp.setInput(instream, "UTF-8");
						TwitterParser tp = new TwitterParser();
						user = tp.parseUser(xpp);
						Log.d("TwitterApi","User : " + user.screen_name  +"/"+ user.profile_image_url);
						instream.close();
					}
				}
		} catch (Exception e) {
			Log.d("TwitterApi", "verifyCredentials Error : " + e);
			// ���ͳ������� �ȵǼ� ��������� Exceptionó�� �ʿ� (ex ��������ȳ�?)
		}
		return user;
		
	}

	//Hometime���� ��������
	public ArrayList<Status> getHomeTimeLine(long sinceId, long maxId) {
		ArrayList<Status> mData = null;
		//Parser parser = new Parser();

		
		HttpClient httpClient = new DefaultHttpClient();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		if(sinceId != 0){
			params.add(new BasicNameValuePair("since_id", String.valueOf(sinceId)));
		}
		if(maxId != 0){
			params.add(new BasicNameValuePair("max_id",String.valueOf(maxId)));
		}
		
//		URLEncodedUtils.format(parameters, encoding);
		String query = URLEncodedUtils.format(params, "UTF-8");
		
		try {
			URI u = URIUtils.createURI("http", "api.twitter.com", -1, 
					"/1/statuses/home_timeline.xml", query, null);
			
			HttpGet httpGet = new HttpGet(u);
			//HttpGet httpGet = new HttpGet("http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=" + screenName);
			
			mConsumer.sign(httpGet); //HomeTilmeLine������ �����ʿ� userTimeLine�� ���ʿ�
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(statusCode != 200) {
				// �������� �����ڵ� ����
				Log.e("TwitterApi","Status Code : " + statusCode);
				if(entity != null){
					InputStreamReader reader = new InputStreamReader(entity.getContent());
					BufferedReader bReader = new BufferedReader(reader);
					String s;
					while((s = bReader.readLine()) != null ){
						Log.e("TwitterApi",s);
					}
					bReader.close();
				}
				return null;
			} else {
				if(entity != null){
					InputStream instream = entity.getContent();
					//Parsing timeline
					TwitterParser tp = new TwitterParser();
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					XmlPullParser xpp = factory.newPullParser();
					
					xpp.setInput(instream, "UTF-8");
					mData = tp.parseTimeLine(xpp); //TwitterParser�κ��� ������ �Ľ��� �����͸� mData�� ����
					instream.close();

				}
			}
		} catch (Exception e){
			Log.d("TwitterApi", "getHomeTimeLine Error : " + e);
		}
		
		return mData;
	}
	
	//UserTime���� ��������
	public ArrayList<Status> getUserTimeLine(String screenName , long sinceId, long maxId) throws XmlPullParserException {
		ArrayList<Status> mData = null;

		
		HttpClient httpClient = new DefaultHttpClient();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("screen_name", screenName));
		
		if(sinceId != 0){
			params.add(new BasicNameValuePair("since_id", String.valueOf(sinceId)));
		}
		if(maxId != 0){
			params.add(new BasicNameValuePair("max_id",String.valueOf(maxId)));
		}
		
//		URLEncodedUtils.format(parameters, encoding);
		String query = URLEncodedUtils.format(params, "UTF-8");
		
		try {
			URI u = URIUtils.createURI("http", "api.twitter.com", -1, 
					"/1/statuses/user_timeline.xml", query, null);
			
			HttpGet httpGet = new HttpGet(u);
			//HttpGet httpGet = new HttpGet("http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=" + screenName);
			
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if(statusCode != 200) {
				// �������� �����ڵ� ����
				Log.e("TwitterApi","Status Code : " + statusCode);
				if(entity != null){
					InputStreamReader reader = new InputStreamReader(entity.getContent());
					BufferedReader bReader = new BufferedReader(reader);
					String s;
					while((s = bReader.readLine()) != null ){
						Log.e("Twitter",s);
					}
					bReader.close();
				}
				return null;
			} else {
				if(entity != null){
					InputStream instream = entity.getContent();
					//Parsing timeline
					//Parser parser = new Parser();
					TwitterParser tp = new TwitterParser();
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
					XmlPullParser xpp = factory.newPullParser();
					
					xpp.setInput(instream, "UTF-8");
					mData = tp.parseTimeLine(xpp); //TwitterParser�κ��� ������ �Ľ��� �����͸� mData�� ����
					instream.close();
					//return list;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return mData;
	}
	
	//Ʈ���� Mention ��������
	public ArrayList<Status> getMentionTimeLine(long sinceId, long maxId) throws URISyntaxException{
		
		HttpClient httpClient = new DefaultHttpClient();

		ArrayList<Status> mData = null;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		if(sinceId != 0){
			params.add(new BasicNameValuePair("since_id", String.valueOf(sinceId)));
		}
		if(maxId != 0){
			params.add(new BasicNameValuePair("max_id", String.valueOf(maxId)));
		}
		
		String query = URLEncodedUtils.format(params, "UTF-8");
		Log.d("TwitterApi", "params : " + params);
		
		try{
			URI u = URIUtils.createURI("http", "api.twitter.com",-1,"/1/statuses/mentions.xml",query,null);
			Log.d("TwitterApi", "uri : " + u);
			HttpGet httpGet = new HttpGet(u);
			
			mConsumer.sign(httpGet);

			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			
			if(statusCode !=200){
			//�������� �����ڵ� ����
			Log.e("TwitterApi", "Status Code" + statusCode);
			
				if(entity != null){
					InputStreamReader reader = new InputStreamReader(entity.getContent());
					BufferedReader bReader = new BufferedReader(reader);
//				String s;
//				while((s = bReader.readLine()) != null){
//					Log.e("TwitterApi",s);
//				}
//				bReader.close();
					
					while (true){
						String s = bReader.readLine();
						if(s==null)
							break;
						Log.d("TwitterApi",s);
					}
					bReader.close();
			}
			return null;
		}else{
			if(entity != null){
				InputStream instream = entity.getContent();
				//parsing timeline (�Ľ��ϴ� �۾�)
				XmlPullParserFactory factory;
				factory = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(instream, "UTF-8");
				TwitterParser tp = new TwitterParser();
				mData = tp.parseTimeLine(xpp);   //TwitterParser�κ��� ������ �Ľ��� �����͸� mData�� ����
				instream.close();
				//return mData
				}
			}
		}catch (Exception e){
			Log.d("TwitterApi", "getMentionTimeLine Error : " + e);
		}
		return mData;
		
	}
	
	//Ʈ���� status ����
	public boolean statusUpdate(String status){
	HttpClient httpClient = new DefaultHttpClient();
	HttpPost post = new HttpPost("http://api.twitter.com/1/statuses/update.xml");
	
	HttpParams hp = new BasicHttpParams();
	HttpProtocolParams.setUseExpectContinue(hp,false);
	post.setParams(hp);
	
	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("status", status));
	
	try{
		do{
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"UTF-8");
		post.setEntity(formEntity);
		mConsumer.sign(post);
		
		HttpResponse res = httpClient.execute(post);
		statusCode = res.getStatusLine().getStatusCode();
		
		if(statusCode == 200)
			break;
		} while(statusCode != 200);
	}catch(Exception e){}

	return true;
	
	//����ó���� getó����  ����.
	
	}
	
	
}
