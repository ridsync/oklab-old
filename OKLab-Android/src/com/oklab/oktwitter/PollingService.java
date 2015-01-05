package com.oklab.oktwitter;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class PollingService extends Service {
	
//	public static void schedule(Context context){
//		
//		AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//		Intent intent = new Intent(context,PollingService.class);
//		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
//		
//		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, 
//								System.currentTimeMillis(), 
//								AlarmManager.INTERVAL_FIFTEEN_MINUTES, 
//								pi);
//	}
	
//	@Override
//	public void onStart(Intent intent, int startId) {
//		super.onStart(intent, startId);
//		//Ʈ���Ϳ� ���ο� Ʈ���� �ִ��� Ȯ��. AsyncTask���
//		//���ο� Ʈ���� ������ Notification���� �˷��ֱ�
//	}

	@Override
	public void onCreate() {
		Log.d("Polling", "Service Created");
		super.onCreate();
	}
	int count;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("Polling", "Service Started");
		//try{Thread.sleep(5000);}catch (Exception e){}; // <=���� ��� . Main Thread���� ����Ǳ⶧��
		//...
		count = getHomeTimeLineCount();
		if (count > 0) {
		sendNotification();
		stopSelf();
		Log.d("Polling", "New Twit Notification !!!");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d("Polling", "Service Destroyed");
		super.onDestroy();
	}

	final static int NOTIFY_ID = 1;
	public void sendNotification(){
		
		
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		Notification noti = new Notification(android.R.drawable.stat_notify_error, 
				count+"���� ���ο� Ʈ���� �ֽ��ϴ�", System.currentTimeMillis());
		
		//PendingIntent : �ý��ۻ󿡼� ������� ����Ʈ�� ����, ���� App���ο��� �ٷ� �����ϴ°��� �ƴ�.
		Intent intent = new Intent(this, LoginActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0); 
		noti.setLatestEventInfo(this, "OKTwitter Ʈ�� �˸�", count+"���� ���ο� Ʈ���� �ֽ��ϴ�", pi);
		nm.notify(NOTIFY_ID,noti);
		
	}
	
	void CancelNotification(){
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(NOTIFY_ID);
	}
		
	   public int getHomeTimeLineCount(){

		   //(Preference�� ����� ������id
		   int count;
		   SharedPreferences pref = getSharedPreferences("twitt", 0);
		   long last_id = pref.getLong("last_id", 0);
		   
			ArrayList<Status> newData = new ArrayList<Status>();
			String token = pref.getString("token", null);
			String tokenSecret = pref.getString("tokenSecret", null);
			TwitterApi twitter_api = new TwitterApi(token, tokenSecret);
			
			newData = twitter_api.getHomeTimeLine(last_id,0 );
			count = newData.size();
			
		   Log.d("OKTwitter", "newTwit Count : " + count);
			return count;
	   }
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	
	
}
