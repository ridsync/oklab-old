package com.oklab.myshoptest;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;

public class SoftKeyBoard_old extends InputMethodService {

	
	 @Override
	public View onCreateInputView() {
		
		 Log.d("InputMethodervice ","onCreateInputView ");
		 return super.onCreateInputView();
	}

	@Override
	protected void onCurrentInputMethodSubtypeChanged(
			InputMethodSubtype newSubtype) {
		 Log.d("InputMethodervice ","onCurrentInputMethodSubtypeChanged ");
		 
		super.onCurrentInputMethodSubtypeChanged(newSubtype);
		
	}

	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		 
		 info.privateImeOptions = "defaultinputmode=korean;";
		 Log.d("InputMethodervice ","onStartInputView ");
		super.onStartInputView(info, restarting);
	        
	}
}
