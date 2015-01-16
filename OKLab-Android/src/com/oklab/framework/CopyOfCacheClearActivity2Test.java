package com.oklab.framework;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.oklab.BaseActivity;
import com.oklab.R;

import java.io.File;
import java.util.List;

/**
 * 기기내의 캐쉬 모두 삭제 구현 -- 테스트 안됨... 1. Dir 관련 오류 있
 * 
 * @author okok
 * 
 *         참고하럿
 *         http://stackoverflow.com/questions/8474444/how-can-i-clear-the-android-app-cache
 */
public class CopyOfCacheClearActivity2Test extends BaseActivity {

	private static final String TAG = CopyOfCacheClearActivity2Test.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        Button btnCache = (Button)findViewById(R.id.bt_oneFragment) ;

        btnCache.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearCache();
            }
        });//End of btnCache Anonymous class
	}

	void clearCache() 
	{
		 Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);

		 mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		 List<ResolveInfo> mainList = getPackageManager().queryIntentActivities(mainIntent, 0);

		 Context context;

		 for(ResolveInfo rInfo : mainList)
		 {

		 String packageName = rInfo.activityInfo.applicationInfo.packageName;

		 try {
			 
			context = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
			 clearApplicationData(context);
			 
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 }
	}

	public void clearApplicationData(Context context) {

		if (context.getCacheDir() != null) {

			File cache = context.getCacheDir();

			File appDir = new File(cache.getParent());
			if (appDir.exists()) {
                Log.i("TAG", "appDir exists = "+ appDir.getName() + " / " + appDir.exists());
				String[] children = appDir.list();
                if (children != null){
                    for (String s : children) {
                        Log.i("TAG", "appDir s = "  + s);
                        if (!s.equals("lib")) {
                            deleteDir(new File(appDir, s));
                            Log.i("TAG", "File /data/data/APP_PACKAGE/" + s
                                    + " DELETED");
                        }
                    }
                }
			}
		}
	}
	
	private void deleteDir(File dir){
	    if (dir != null){
	        if (dir.listFiles() != null && dir.listFiles().length > 0){
	            // RECURSIVELY DELETE FILES IN DIRECTORY
	            for (File file : dir.listFiles()){
	            	deleteDir(file);
	            }
	        } else {
	            // JUST DELETE FILE
	            dir.delete();
	        }
	    }
	}
}
