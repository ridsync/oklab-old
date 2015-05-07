package com.oklab.framework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.oklab.BaseActivity;
import com.oklab.R;
import com.oklab.util.SntpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NTP서버 를 통해서 시간정보를 얻어오는 SntpClient 클래스 테스트 화면
 * adb shell root유저에선 date 명령이 먹지만... 앱내에선 su 사용이 되지않으면 date 명령어로 시간 못바꿈...
 * 기타 AlaramManger , SystemClock API 역시 권한 문제로 안됨 ㅡㅡ;
 * 시스템 App등록되지거나 , shell상의 커맨드 su 일반App pid에서도 쓸수있게 제공되야함...
 * http://prasans.info/2014/02/utc-time-android-device-ntp-server-sync/
 * @author ojungwon
 *
 */
public class NetworkTimeSetActivity extends BaseActivity {
	private static final String TAG = NetworkTimeSetActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a);

		((TextView)findViewById(R.id.title)).setText("Activity A");
		((TextView)findViewById(R.id.title)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getNTPServerTime();
				
//				// 날짜 & 시간 설정을 자동으로...  권한문제로 안된다이젠..
//				Settings.Global.putInt(getContentResolver(), Settings.Global.AUTO_TIME, 1);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_DATE_CHANGED);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		registerReceiver(mIntentReceiver, filter, null, null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mIntentReceiver);
	}
	
	public void getNTPServerTime(){
        long nowAsPerDeviceTimeZone = 0;
        SntpClient sntpClient = new SntpClient();

        if (sntpClient.requestTime("time.bora.net", 30000)) {
            nowAsPerDeviceTimeZone = sntpClient.getNtpTime();
//            Calendar cal = Calendar.getInstance(); // Offset으로 UTC 시간을 구하기 윈한건가 ?
//            TimeZone timeZoneInDevice = cal.getTimeZone();
//            int differentialOfTimeZones = timeZoneInDevice.getOffset(System.currentTimeMillis());
//            nowAsPerDeviceTimeZone -= differentialOfTimeZones;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
        String date = sdf.format(new Date(nowAsPerDeviceTimeZone)) ;
        Log.d("getNTPServerTime", "get date = " + date);
        ((TextView)findViewById(R.id.title)).setText( date );
        
        // TODO 시스템 시간을 바꾼다 !!
        setSystemTime(date);
        setSystemTime2(nowAsPerDeviceTimeZone);
        
		 // 설정화면 이동하기.
		 startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS)); 
    }
	
	
	private void setSystemTime2(long time) {
		
//		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//		am.setTime(time);
		
//		boolean result = SystemClock.setCurrentTimeMillis( time );
//		 Log.d("getNTPServerTime", " SystemClock result = " + result);
       
	}

	private void setSystemTime (String date){
		
		// SU가 필요하다 !!! 안되네..
//		 Runtime runtime = Runtime.getRuntime();
//	        try{
//	        	Process p = runtime.exec("adb shell su -c \"date -s " + date + "\"");
//	        	Process p = runtime.exec("adb shell date -s " + date );
//	        	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//	        	String line = null;
//	        	while ((line = br.readLine())!=null){
//	        		Log.d("setSystemTime",line);
//	        	}
//	        	br.close();
//	        	
//	        }catch(Exception e){
//	        	Log.d("setSystemTime Exception",e.toString());
//	        }
	        
//		try {
//            Process process = Runtime.getRuntime().exec("su");
//        DataOutputStream os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes("date -s + " + date + "; \n");
//        } catch (Exception e) {
//            Log.d(TAG,"error=="+e.toString());
//            e.printStackTrace();
//        }
		
	}
	
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 처리 안함
		}
	};

}
