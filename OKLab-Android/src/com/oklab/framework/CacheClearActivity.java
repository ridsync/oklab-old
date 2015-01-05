package com.oklab.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.oklab.BaseActivity;

/**
 * 기기내의 캐쉬 모두 삭제 구현  -- 테스트 안됨...
 * ※ CachePackageDataObserver  AIDL 구현 및 Permission 등록한다.
 * 1. CACHE 일괄 삭제 방법  ??
 * 2. 기타 앱들의 기본정보(아이콘,이름,패키지,캐쉬용량등등) 알아내기 PackageManager 겟지 ?
 * @author okok
 *
 * http://stackoverflow.com/questions/17313721/how-to-delete-other-applications-cache-from-our-android-app
 */
public class CacheClearActivity extends BaseActivity{

	private static final String TAG = CacheClearActivity.class.getSimpleName();
	
	private static final long CACHE_APP = Long.MAX_VALUE;
	private CachePackageDataObserver mClearCacheObserver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
//		btnCache.setOnClickListener(new View.OnClickListener() {
//
//	        @Override
//	        public void onClick(View v) {
//	            clearCache();
//	        }
//	    });//End of btnCache Anonymous class

	}

	void clearCache() 
	{
	    if (mClearCacheObserver == null) 
	    {
	      mClearCacheObserver=new CachePackageDataObserver();
	    }

	    PackageManager mPM=getPackageManager();

	    @SuppressWarnings("rawtypes")
	    final Class[] classes= { Long.TYPE, IPackageDataObserver.class };

	    Long localLong=Long.valueOf(CACHE_APP);

	    try 
	    {
	      Method localMethod=
	          mPM.getClass().getMethod("freeStorageAndNotify", classes);

	      /*
	       * Start of inner try-catch block
	       */
	      try 
	      {
	        localMethod.invoke(mPM, localLong, mClearCacheObserver);
	      }
	      catch (IllegalArgumentException e) 
	      {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	      catch (IllegalAccessException e) 
	      {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	      catch (InvocationTargetException e)
	      {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	      /*
	       * End of inner try-catch block
	       */
	    }
	    catch (NoSuchMethodException e1)
	    {
	      // TODO Auto-generated catch block
	      e1.printStackTrace();
	    }
	}//End of clearCache() method

	private class CachePackageDataObserver extends IPackageDataObserver.Stub 
	{
	    public void onRemoveCompleted(String packageName, boolean succeeded) 
	    {

	    }//End of onRemoveCompleted() method
	}//End of CachePackageDataObserver instance inner class
	
}
