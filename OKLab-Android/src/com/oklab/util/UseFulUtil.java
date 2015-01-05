package com.oklab.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

public class UseFulUtil extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/**
	 * 알아두면 유용한 Android 프레임웍 관련 메소드들~ 모음
	 *  
	 */
	

	/** 
	 * 홈키 무력화 
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	}
 

	/** 
	 * 뒤로 가기 무력화 :
	 */
	public boolean onKeyDown( int KeyCode, KeyEvent event )
	{
		if( event.getAction() == KeyEvent.ACTION_DOWN ){
			if( KeyCode == KeyEvent.KEYCODE_BACK ){
			//여기에 뒤로 버튼을 눌렀을때 해야할 행동을 지정한다
			return true;
			}
		}
		return super.onKeyDown( KeyCode, event );
	}

	
}
