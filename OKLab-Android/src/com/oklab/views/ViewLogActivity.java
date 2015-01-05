package com.oklab.views;

import android.os.Bundle;
import android.util.Log;

import com.oklab.BaseActivity;
import com.oklab.R;

public class ViewLogActivity extends BaseActivity{
    /** Called when the activity is first created. */
	public static String TAG;
	
    ViewLog myview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	TAG = getClass().getSimpleName();
    	
    	Log.i(TAG, "onCreate() START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        myview  = (ViewLog)findViewById(R.id.myv);
//        myview = new MyView(this);
//        setContentView(myview);
        myview.Log("onCreate()");
        Log.i(TAG, "onCreate() END");
    }
        
    @Override
	protected void onStart() {
    	// TODO Auto-generated method stub
    	Log.i(TAG, "onSTART() START");
    	super.onStart();
    	Log.i(TAG, "onSTART() END");
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	Log.i(TAG, "onResume() START");
    	super.onResume();
    	
    	myview.Log("onResume()");
    	Log.i(TAG, "onResume() END");
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	Log.i(TAG, "onWindowFocusChanged() START");
    	super.onWindowFocusChanged(hasFocus);
    	myview.Log("onWindowFocusChanged()");
    	Log.i(TAG, "onWindowFocusChanged() END");
    }
    
}