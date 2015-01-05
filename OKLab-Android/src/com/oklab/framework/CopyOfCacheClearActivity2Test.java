package com.oklab.framework;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.oklab.BaseActivity;

/**
 * 기기내의 캐쉬 모두 삭제 구현 -- 테스트 안됨... 1. CACHE 일괄 삭제 방법 중 하나 테스트필요 ?
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
				String[] children = appDir.list();
				for (String s : children) {
					if (!s.equals("lib")) {
						deleteDir(new File(appDir, s));
						Log.i("TAG", "File /data/data/APP_PACKAGE/" + s
								+ " DELETED");
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
