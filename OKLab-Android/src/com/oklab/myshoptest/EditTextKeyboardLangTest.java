package com.oklab.myshoptest;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.oklab.BaseActivity;
import com.oklab.R;
import com.oklab.menutabs.GameTabView;

public class EditTextKeyboardLangTest extends BaseActivity {

		private static final String TAG = EditTextKeyboardLangTest.class.getSimpleName();
		
		public static final int REQ_CODE_DATA_EXPRESS = 100;
		public static final String EXTRA_NAME_DATA1 = "LangTest_EXTRA_NAME_DATA1";
		
		EditText deviceNum1 ;
		EditText deviceNum2 ;
		EditText deviceNum3 ;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_et_keyboard_lang_test);
			
			deviceNum1 = (EditText) findViewById(R.id.ET_DEVICE_NUM);
			deviceNum2 = (EditText) findViewById(R.id.ET_DEVICE_NUM_2);
			deviceNum3 = (EditText) findViewById(R.id.ET_DEVICE_NUM_3);
			
			deviceNum1.setPrivateImeOptions("defaultinputmode=english;");
			deviceNum2.setPrivateImeOptions("defaultinputmode=korean;");
			deviceNum3.setPrivateImeOptions("defaultinputmode=number;");
			
//			deviceNum2.setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
////					InputMethodManager mgr  = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
////					mgr.showSoftInput(deviceNum2, InputMethodManager.SHOW_FORCED);
//					deviceNum2.setPrivateImeOptions("defaultInputmode=korean;");
//					Log.d("OnTouchListener  ", "OnTouchListener");
//					return false;
//				}
//			});
			deviceNum2.setOnKeyListener(onKeyListener);
			deviceNum2.setOnEditorActionListener(editorActioListener);
//			deviceNum2.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					v.requestFocus();
//					showSoftKeyboard(v);
//				}
//			});
			
			
			GameTabView gtv = (GameTabView)findViewById(R.id.gtv);
			int gameGroupCnt = 10;
			gtv.setData(gameGroupCnt);
//			LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
//			layout.addView(gtv);
			
			Log.d(TAG, "onCreate() Success ~ ");
		}
		
		public void showSoftKeyboard(View view) {
		    if (view.requestFocus()) {
		        InputMethodManager imm = (InputMethodManager)
		                getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		        Log.d(TAG, "showSoftKeyboard() Success ~ ");
		    }
		}
		

		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			
			Log.d(TAG, "dispatchKeyEvent() event.getKeyCode = " + event.getKeyCode());
		return super.dispatchKeyEvent(event);
		}

		OnKeyListener onKeyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				Log.d(TAG, "onKey() Success keyCode = " + arg2.getKeyCode());
				return false;
			}
			
		};
		
		
		OnEditorActionListener editorActioListener = new OnEditorActionListener() {
	         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        	 Log.d(TAG, "onEditorAction() actionId / event.getKeyCode = " + actionId + " / " + event.getKeyCode());
	          if (actionId == EditorInfo.IME_ACTION_DONE) {
	        
	          }
	          return false;
	         }
	        };
}
