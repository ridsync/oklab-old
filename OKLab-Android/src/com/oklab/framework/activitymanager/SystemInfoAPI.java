package com.oklab.framework.activitymanager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.oklab.BaseActivity;
import com.oklab.R;

public class SystemInfoAPI extends BaseActivity{

	private static final String TAG = SystemInfoAPI.class.getSimpleName();
	
	public static final int REQ_CODE_DATA_EXPRESS = 100;
	public static final String EXTRA_NAME_DATA1 = "ActivityA_EXTRA_NAME_DATA1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a);

		getMemoryInfo();
		
		getResolution();
		
		getDensity();
		
		getBuildInfos();
		
		getNetworkInfos();
		
		getTelephonyManagerInfos();
		
		getAccountOnDevice();
		
		getDeviceUniqUUID();
		
		
		findViewById(R.id.title).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 *  앱의 프로세스 종료
				 *  http://blog.naver.com/huewu/110120532880
				 */
//				finish(); // Activitiy만 종료
				// System 명령어를 통한 종료는 비정상종료로 인식. 
				// Activitiy가 남아있거나 서비스가 동작중이라면 다시 실행됨.
//				System.exit(0); 
//				android.os.Process.killProcess(android.os.Process.myPid());
				// 앱이 백그라운드 이하의 중요도를 갖고 있어야지만 정상적으로 동작
//				ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
//				am.killBackgroundProcesses(getPackageName());
			}
		});
	}

	private void getResolution() {
		Log.i(TAG, "++++++++++++++ [ DisplayInfos ] ++++++++++++++ :  ");
		DisplayMetrics displayMetrics = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int pxWidth  = displayMetrics.widthPixels;
		int pxHeight = displayMetrics.heightPixels;
		Log.d(TAG, String.format("Display width : %d , Display height : %d",
				pxWidth, pxHeight));
		Toast.makeText(this,String.format("width:%d , height: %d",
				pxWidth, pxHeight), Toast.LENGTH_LONG).show();
		//--- displayMetrics.density : density / 160, 0.75 (ldpi), 1.0 (mdpi), 1.5 (hdpi)
//		int dipWidth  = displayMetrics.widthPixels  / displayMetrics.density;
//		int dipHeight = displayMetrics.heightPixels / displayMetrics.density;	
	}

	private void getMemoryInfo() {
		// 프로세스 메모리 limit 보기
		Log.i(TAG, "++++++++++++++ [ MemoryInfos ] ++++++++++++++ :  ");
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo meminfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(meminfo);
		Log.d(TAG, String.format("System Available:%d,LowMemory Threshold: %d %s",
				meminfo.availMem, meminfo.threshold, meminfo.lowMemory?" (low memory)":""));
		int maxMemSize = am.getMemoryClass(); // 프로세스 메모리 Limit 메모리사이즈 MB
		int largeMemSize = am.getLargeMemoryClass(); // Dalvk Heap 사이즈 늘리기  (HoneyComb이후 가능)
		 // 또는 AndroidManifest.xml에 android:largeHeap="true"추가한다. (Android ICS부터 default 속성)
		Log.d(TAG, String.format("Dalvk Heap Limit : maxMemSize:%d MB largeMemSize:%d MB", maxMemSize, largeMemSize));
		Log.d(TAG, String.format("Dalvk Heap Limit after largeHeap : maxMemSize:%d ", am.getMemoryClass()));
		
		// Dalvik Heap 영역 메모리 보기
		 long maxMem = Runtime.getRuntime().maxMemory();  // Dalvik heap 영역 최대 크기(프로세스당 메모리 한계)
		 long totalMem = Runtime.getRuntime().totalMemory(); // Dalvik heap 영역 크기(footprint):
		 long freeMem = Runtime.getRuntime().freeMemory(); // Dalvik heap free 크기
		// 사용중인 메모리는 Dalvik heap allocated 크기
		// long usedMem = totalMemory() - freeMemory(); 
		 Log.d(TAG, String.format("maxMemSize:%d totalMemSize:%d freeMemSize:%d", maxMem, totalMem, freeMem));
		Debug.MemoryInfo debugMemInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(debugMemInfo);
	}
	
	private void getDensity(){
		DisplayMetrics dm = new DisplayMetrics();
	     getWindowManager().getDefaultDisplay().getMetrics(dm);
	     
	     Log.d(TAG, "density : " + dm.density);
	     Log.d(TAG, "heigth : " + dm.heightPixels);
	     Log.d(TAG, "width : " + dm.widthPixels);
	}
	
	public void getBuildInfos() {
		// Mac Address by Wifi
	  Log.i(TAG, "++++++++++++++ [ BuildInfos ] ++++++++++++++ :  ");
	  Log.d(TAG,"BOARD : " + Build.BOARD);
	  Log.d(TAG,"BRAND :  : " + Build.BRAND);
	  Log.d(TAG,"CPU_ABI : " + Build.CPU_ABI);
	  Log.d(TAG,"DEVICE : " + Build.DEVICE);
	  Log.d(TAG,"DISPLAY : " + Build.DISPLAY);
	  Log.d(TAG,"FINGERPRINT : " + Build.FINGERPRINT);
	  Log.d(TAG,"HOST : " + Build.HOST);
	  Log.d(TAG,"ID : " + Build.ID);
	  Log.d(TAG,"MANUFACTURER : " + Build.MANUFACTURER);
	  Log.d(TAG,"MODEL : " + Build.MODEL);
	  Log.d(TAG,"PRODUCT : " + Build.PRODUCT);
	  Log.d(TAG,"TAGS : " + Build.TAGS);
	  Log.d(TAG,"TYPE : " + Build.TYPE);
	  Log.d(TAG,"USER : " + Build.USER);
	  
	}
	
	public void getNetworkInfos() {
		// Mac Address by Wifi
		Log.i(TAG, "++++++++++++++ [ NetworkInfos ] ++++++++++++++ :  ");
		Log.d(TAG, String.format("MacAddress : %s", getMacAddress(this) ) ) ;
		Log.d(TAG, String.format("getMacAddressByWifi : %s", getMacAddressByWifi(this) ) ) ;
		Log.d(TAG, String.format("MacAddress 2 : %s", getLocalIpAddress() ) ) ;
	}
	
	private String getMacAddress(Context context) {
	    WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    String macAddress = wimanager.getConnectionInfo().getMacAddress();
	    if (macAddress == null) {
	        macAddress = "와이파이가 안되거나 맥어드레스가 없는경우";//device has no macaddress or wifi is disabled
	    }
	    return macAddress;
	}
	
	/**
	 * Mac Address 주소 반환 . WiFi가 꺼져있어도 가져오도록... 
	 * 단. Wifi가 없는기기는 안됨.
	 * @param context
	 * @return
	 * @author ojungwon
	 */
	public static String getMacAddressByWifi(Context context){
		
		String macAddress="";  
	    boolean bIsWifiOff=false;  
	          
	    WifiManager wfManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
	    if(!wfManager.isWifiEnabled()){  
	        wfManager.setWifiEnabled(true);  
	        bIsWifiOff = true;  
	    }  
	          
	    WifiInfo wfInfo = wfManager.getConnectionInfo();  
	    macAddress = wfInfo.getMacAddress();  
	          
	    if(bIsWifiOff){  
	        wfManager.setWifiEnabled(false);  
	        bIsWifiOff = false;  
	    }  
	    Log.d(TAG, "WifiManager : getMacAddress = " +  macAddress);
		return macAddress;
	}
	
	private String getLocalIpAddress() { //MAC 어드레스 ?? IP V6인거같음
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }

                }
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
	// 폰기능이 있어야 나옴....
	private void getTelephonyManagerInfos(){

	    TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	    Log.i(TAG, "++++++++++++++ [ TelephonyManagerInfos ] ++++++++++++++ :  ");
	    Log.d(TAG, "getCallState : "+manager.getCallState());
	    Log.d(TAG, "getDataActivity : "+manager.getDataActivity());
	    Log.d(TAG, "getDataState : "+manager.getDataState());
	    Log.d(TAG, "getDeviceId : "+manager.getDeviceId()); // IMEI - 디바이스 아이디
	    Log.d(TAG, "getSubscriberId : "+manager.getSubscriberId()); //  IMSI - 통신사 가입 아이디
	    Log.d(TAG, "getLine1Number : "+manager.getLine1Number());   // MSISDN - 전화 번호
	    Log.d(TAG, "getDeviceSoftwareVersion : "+manager.getDeviceSoftwareVersion());
	    Log.d(TAG, "getNetworkCountryIso : "+manager.getNetworkCountryIso());
	    Log.d(TAG, "getNetworkOperator : "+manager.getNetworkOperator());
	    Log.d(TAG, "getNetworkOperatorName : "+manager.getNetworkOperatorName());
	    Log.d(TAG, "getNetworkType : "+manager.getNetworkType());
	    Log.d(TAG, "getPhoneType : "+manager.getPhoneType());
	    Log.d(TAG, "getSimCountryIso : "+manager.getSimCountryIso()); // SIM 국가 코드
	    Log.d(TAG, "getSimSerialNumber : "+manager.getSimSerialNumber()); // SIM 일련 번호
	    Log.d(TAG, "getSimState : "+manager.getSimState()); //SIM 상태 (통신 가능 한가, PIN 잠겨 있는지 등)
	    Log.d(TAG, "getVoiceMailAlphaTAG : "+manager.getVoiceMailAlphaTag());
	    Log.d(TAG, "getVoiceMailNumber : "+manager.getVoiceMailNumber());
	    Log.d(TAG, "isNetworkRoaming : "+manager.isNetworkRoaming());
	    Log.d(TAG, "hasIccCard : "+manager.hasIccCard());
	    Log.d(TAG, "hashCode : "+manager.hashCode());
	    Log.d(TAG, "toString : "+manager.toString());
	}

	private void getAccountOnDevice(){
		// device 계정 정보 가져오기
		Log.i(TAG, "++++++++++++++ [ AccountInfos OnDevice ] ++++++++++++++ :  ");
	    Account[] accounts =  AccountManager.get(getApplicationContext()).getAccounts();
	    Account account = null;

	    for(int i=0;i<accounts.length;i++) {
	          account = accounts[i];

	          Log.d(TAG, "Account - name: " + account.name + ", type :" + account.type);
	          if(account.type.equals("com.google")){  
	        	  //이러면 구글 계정 구분 가능
	          }

	    }
	}
	
	/**
	 * TODO UUID 생성은 프로그램 재설치시 전에값이 안나오고 변경된다????
	 *  http://huewu.blog.me/110107222113 
	 *  http://theeye.pe.kr/archives/1408?commentId=60136
	 */
	private void getDeviceUniqUUID() {
		Log.i(TAG, "++++++++++++++ [ DeviceUniqUUID OnDevice ] ++++++++++++++ :  ");
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 
		final String tmDevice, tmSerial, androidId;
		// ※ ANDROID_ID 는 빈값이 나올 경우가 많다고함. 제조사에따라
		// 64bit 고유한값, 프로요 2.2 버젼부터 확인 가능 , 공장초기화시 값이 바뀌기도함
		androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		// ※ TelephonyManager 도 전화기능이 있어야 가능하다...
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		Log.d(TAG, String.format("Uniq DeviceId (UUID Combine) =  %s", deviceId)
				+"\n( SecureAndroidId = "+ androidId
				+" / getTmDeviceId = "+ tmDevice
				+" / getTmSimSerialNumber = "+ tmSerial +" )");
	}
	
}
