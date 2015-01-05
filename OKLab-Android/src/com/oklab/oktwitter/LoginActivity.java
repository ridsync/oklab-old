package com.oklab.oktwitter;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.oklab.R;
import com.oklab.framework.activitymanager.TaskManagement;


//���� oauth-signpost ���� signpost-*.jar ���� 2�� �ʿ�
public class LoginActivity extends Activity {
	CommonsHttpOAuthConsumer mConsumer;
	CommonsHttpOAuthProvider mProvider;
	String token;
	String tokenSecret;
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Log.i("LoginActivity", "onCreate() ");
		
		Button btn = (Button)this.findViewById(R.id.btn);
		btn.setText("잠금해제");
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// �˶� ��������
//	       alarmCancel();
		//����Ǿ��ִ� ��ū �ҷ�����
//		SharedPreferences pref = getSharedPreferences("twitt", 0);
//
//		token = pref.getString("token", "");
//		tokenSecret = pref.getString("tokenSecret", "");
//		Log.d("Login-1", "token/tokenSecret " + token + "/"+ tokenSecret);
//		
//		
//		//Login ��ư ��
//		Button btn = (Button)findViewById(R.id.btn);
//		btn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				//OAuth ��������
//				
//				//����� Token �ҷ�����
//				TwitterApi api = new TwitterApi(token, tokenSecret);
//				
//				User user = api.verifyCredentials();
//				
//				if( user != null){
//					
//					Intent intent = new Intent(LoginActivity.this, OKTwitterActivity.class);
//					intent.putExtra("screen_name", user.screen_name);
//					intent.putExtra("profile_image_url",user.profile_image_url);
//					Log.d("Login-2", "user.screen_name : " + user.screen_name);
//					Log.d("Login-2", "user.profile_image_url : " + user.profile_image_url);
//					startActivity(intent);  //OKTwitterActivity ����
//					
//				}else {
//					mConsumer = new CommonsHttpOAuthConsumer(
//							TwitterApi.CONSUMER_KEY,
//							TwitterApi.CONSUMER_SECRET);
//					mProvider = new CommonsHttpOAuthProvider(
//							"https://api.twitter.com/oauth/request_token", 
//							"https://api.twitter.com/oauth/access_token", 
//							"https://api.twitter.com/oauth/authorize");
//					try {
//					// Twitter Activity ����
//					String authUrl = mProvider.retrieveRequestToken(mConsumer, TwitterApi.CALLBACK_URL);
//					startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(authUrl)));
//					
//					} catch (OAuthException e){
//						Log.e("Login", e.getMessage());
//					}
//				}
//			}
//		});
	}
	
//	//Token ��û�� �� SharedPreferences�� �����ϰ� OKTwitterActivity �����ϴ� Intent
//	@Override
//	protected void onNewIntent(Intent intent) {
//	
//		super.onNewIntent(intent);
//		Log.d("Login-3(onNew)", intent.getData().toString());
//		
//		Uri uri = intent.getData();
//		
//		if(uri != null){
//			try{
//				String verifier = uri.getQueryParameter("oauth_verifier");
//				mProvider.retrieveAccessToken(mConsumer, verifier);
//				
//				String token = mConsumer.getToken();
//				String tokenSecret = mConsumer.getTokenSecret();
//				
//				Log.d("Login-4(onNew)",token + "/" + tokenSecret);
//				
//				//SharedPreference�� token�� tokenSecret ����.
//				SharedPreferences pref = getSharedPreferences("twitt", 0);
//				SharedPreferences.Editor edit = pref.edit();
//				
//				edit.putString("token", token);
//				edit.putString("tokenSecret", tokenSecret);
//				edit.commit();
//				startActivity(new Intent(this, OKTwitterActivity.class));
//				
//			}catch(OAuthException e){
//				Log.e("Login", e.getMessage());
//			}
//			
//		}else {
//			
//		}
//	}
//	
//	   //�˶� ���� ���� �޼ҵ�
//	void alarmCancel(){
//		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//		Intent intent = new Intent(this,PollingService.class);
//		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
//		am.cancel(pi);
//	}
}
