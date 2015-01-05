package com.oklab.myshoptest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.oklab.BaseActivity;

/**
 * 0) DB파일 Export to SDCARD 메소드
 * 1) Myshop ContentProvider 의 URI 를 Resolver DB 사용 테스트
 * 2) 타 App Activity 런칭 테스트
 * @author P042209
 *
 */
public class ContentResolverNotherAppLauch extends BaseActivity implements OnClickListener{

	public static final String AUTHORITY = "com.skt.gzmyshop.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    
    public static final String GETTER_COMMAND_NAME = "DB_GETTER";
    public static final String SETTER_COMMAND_NAME = "DB_SETTER";
    public static final Uri GETTER_URI = Uri.withAppendedPath(CONTENT_URI, GETTER_COMMAND_NAME);
    
  	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView text = new TextView(this);
		 text.setOnClickListener(this);
	     text.setText("ResolverTest");
	     text.setTextColor(Color.GREEN);
	     text.setTextSize(20);
	     setContentView(text);
	     
	     Log.d("GETTER_URI is ", GETTER_URI +"") ;
	     
		ContentResolver cr = getContentResolver();
		long startTime = System.currentTimeMillis();
		Log.d("TimeCheck Started >>> ", startTime +"") ;
        Cursor cursor = cr.query(GETTER_URI
				, null
				, "get_csh_his_last"
				, null
				, null);
        
        long finishTime = System.currentTimeMillis();
        Log.d("TimeCheck Finished Elapsed Time => ", finishTime - startTime +"") ;
        DCshHis lastCashIn = null;
        if(cursor != null) {
            try {
                if(cursor.moveToFirst()) {
                	lastCashIn = InDCshHis.createInstance(cursor);
                }
            } finally {
            	Log.d("test", "cursor.getCount()" + cursor.getCount()
            			+ " getUserName = "+ lastCashIn.getUserName());
            	cursor.close();
            	cursor = null;
            }
        }

	}

	@Override
	public void onClick(View v) {
				// App 런칭
		String strPackageName = "com.skt.gzmyshop";
		String strActivityName = "MainPage";
		String strActionName = "com.skt.myshop.page.MainPage";
		 
		executeApp(getApplicationContext(), strPackageName);
//		executeApp2(getApplicationContext(), strPackageName, strActivityName);
//		executeApp3(getApplicationContext(), strActionName);
		
		// 안됨 
//		Intent intent = new Intent(Intent.ACTION_MAIN);
//		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		 intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		 intent.setComponent(new ComponentName("com.skt.gzmyshop.home.page", "com.skt.gzmyshop.home.page.IndexPage"));
//		 startActivity(intent);
	}
    
	/**
     * 해당 앱을 실행 시킨다.
     * @param strPackageName 패키지명
     */
    public static boolean executeApp(Context oContext, String strPackageName)
    {
        try {
            Intent intent = oContext.getPackageManager()
                    .getLaunchIntentForPackage(strPackageName);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            oContext.startActivity(intent);
            return true;
        } catch (Exception e) {
        	Log.e("executeApp " , "e = " + e);
        }
        return false;
    }

    /**
     * 해당 앱을 실행 시킨다.
     * @param strPackageName 패키지명
     * @param strActivityName 엑티비티명
     */
    public static boolean executeApp2(Context oContext, String strPackageName, String strActivityName)
    {
        try {
        	ComponentName cn = new ComponentName(strPackageName,strPackageName+ "." + strActivityName);
        	//ex) ("com.android.music" ,"com.android.music.MusicBrowserActivity") 
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setComponent(cn);
	        oContext.startActivity(intent);
            return true;
        } catch (Exception e) {
        	Log.e("executeApp2 " , "e = " + e);
        }
        return false;
    }
    
    /**
     * 해당 앱을 실행 시킨다.
     * @param strPackageName 액션명
     */
    public static boolean executeApp3(Context oContext, String strActionName)
    {
        try {
        	  //브로드 캐스트
            Intent intent = new Intent(strActionName); // 액션명
            oContext.sendBroadcast(intent);
            Log.e("executeApp3 " , "sendBroadcast . strActionName = " + strActionName);
            return true;
        } catch (Exception e) {
        	Log.e("executeApp3 " , "e = " + e);
        }
        return false;
    }

    /**
     *  DB File Export to Sdcard
     * @return
     */
    
    public boolean exportDBFile()
  	{
    	// App's Sqlㅑte DB 파일 위치
  		File dbFile = new File(Environment.getDataDirectory() + "/data/com.skt.gzmyshop/databases/"+"gzmyshop.db");
  		// 복사할 SDCARD 위치
  		File exportDir = new File(Environment.getExternalStorageDirectory(), "gzmyshop.db");
  		if (!exportDir.exists()) {
  			exportDir.mkdirs();
  		}
  		File file = new File(exportDir, dbFile.getName());

  		try {
  			file.createNewFile();
  			this.copyFile(dbFile, file);
  			return true;
  		} catch (IOException e) {
  			Log.e("mypck", e.getMessage(), e);
  			return false;
  		}
  	}
  	
    private void copyFile(File src, File dst) throws IOException {
  		FileChannel inChannel = new FileInputStream(src).getChannel();
  		FileChannel outChannel = new FileOutputStream(dst).getChannel();
  		try {
  			inChannel.transferTo(0, inChannel.size(), outChannel);
  		} finally {
  			if (inChannel != null)
  				inChannel.close();
  			if (outChannel != null)
  				outChannel.close();
  		}
  	}
    
    
    
}

