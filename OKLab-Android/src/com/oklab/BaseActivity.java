package com.oklab;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class BaseActivity extends Activity {

public static final String TAG = "BaseActivity";

	/**
	 * *****************************
	 * Activity Life Cycle
	 * *****************************
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onCreate()");
		Log.i(TAG +"/"+ this.getClass().getSimpleName(),"" + getPackageName());
		super.onCreate(savedInstanceState);
	}
	// Standard implementation of onCreateView(View, String, Context, AttributeSet) used when inflating with the LayoutInflater returned by getSystemService(String).
	// This implementation handles tags to embed fragments inside of the activity.
//	@Override
//	public View onCreateView(View parent, String name, Context context,
//			AttributeSet attrs) {
//		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onCreateView() 1");
//		return super.onCreateView(parent, name, context, attrs);
//	}
//	
//	@Override
//	public View onCreateView(String name, Context context, AttributeSet attrs) {
//		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onCreateView() 2");
//		return super.onCreateView(name, context, attrs);
//	}
	// 
    
    @Override
	protected void onRestart() {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onRestart() ");
		super.onRestart();
	}
    
    @Override
    protected void onStart() {
        Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onStart()");
        super.onStart();
    }
    
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onPostCreate() ");
		super.onPostCreate(savedInstanceState);
	}
	  
    @Override
    protected void onResume() {
        Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onResume()");
        super.onResume();
    }
    
    @Override
	protected void onPostResume() {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onPostResume ");
		super.onPostResume();
	}
    
    @Override
    protected void onPause() {
        Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onPause()");
        super.onPause();
    }
     
    @Override
    protected void onStop() {
        Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onStop()");
        super.onStop();
    }
     
    @Override
    protected void onDestroy() {
        Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
    
    @Override
	public void onAttachedToWindow() {
		 Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onAttachedToWindow()");
		super.onAttachedToWindow();
	}
	
    @Override
	public void onDetachedFromWindow() {
		 Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onDetachedFromWindow()");
		super.onDetachedFromWindow();
	}
    

    
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onWindowFocusChanged() hasFocus = " + hasFocus);
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onAttachFragment()");
		super.onAttachFragment(fragment);
	}
    
	@Override
	public void onBackPressed() {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(), "onBackPressed() ");
		super.onBackPressed();
	}
	@Override
	public void onUserLeaveHint() {
		Log.i(TAG +"/" + this.getClass().getSimpleName(), "onUserLeaveHint() ");
		super.onUserLeaveHint();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG +"/" + this.getClass().getSimpleName(), "onActivityResult() ");
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG +"/" + this.getClass().getSimpleName(), "onConfigurationChanged() newConfig = " + newConfig.toString());
		super.onConfigurationChanged(newConfig);
	}
  
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(),"onSaveInstanceState() ");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG +"/"+ this.getClass().getSimpleName(),"onRestoreInstanceState() ");
		super.onRestoreInstanceState(savedInstanceState);
	}
}

