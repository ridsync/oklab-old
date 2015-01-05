package com.oklab.framework.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.oklab.R;

public class TestDlgFragment extends DialogFragment {

    private final static String TAG = "TestDlgFragment Dlg";

    // create instance for singleton
    private volatile static TestDlgFragment singleton;

    public static TestDlgFragment getInstance() {
        if (singleton == null) {
            synchronized (TestDlgFragment.class) {
                if (singleton == null) {
                    Log.d(TAG, "create new instance");
                    singleton = new TestDlgFragment();
                }
            }
        }
        return singleton;
    }
 
    /**
	 * *****************************
	 * Fragment Life Cycle
	 * *****************************
	 */
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(this.getClass().getSimpleName(), "onCreate()");
		super.onCreate(savedInstanceState);
	}
	  
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Dialog dialog = new Dialog(getActivity());  
		  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		    WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		  dialog.setContentView(R.layout.fragment_one);  
		  dialog.getWindow().setBackgroundDrawable(  
		    new ColorDrawable(Color.TRANSPARENT));  
		  dialog.show(); 
		  Log.d(this.getClass().getSimpleName(), "onCreateDialog()");
		return dialog;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Log.d(this.getClass().getSimpleName(), "onCreateView()");
        return null;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(this.getClass().getSimpleName(), "onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(this.getClass().getSimpleName(), "onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d(this.getClass().getSimpleName(), "onStart()");
		super.onStart();
	}

    @Override
	public void onResume() {
    	Log.d(this.getClass().getSimpleName(), "onResume()");
		super.onResume();

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		Log.d(this.getClass().getSimpleName(), "onCancel()");
		super.onCancel(dialog);
	}

	@Override
    public void onDismiss(DialogInterface dialog) {
		Log.d(this.getClass().getSimpleName(), "onDismiss()");
    	super.onDismiss(dialog);
    }
  
	@Override
	public void onAttach(Activity activity) {
		Log.d(this.getClass().getSimpleName(), "onAttach()");
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		Log.d(this.getClass().getSimpleName(), "onDetach()");
		super.onDetach();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(this.getClass().getSimpleName(), "onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		Log.d(this.getClass().getSimpleName(), "onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(this.getClass().getSimpleName(), "onStop()");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		Log.d(this.getClass().getSimpleName(), "onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		Log.d(this.getClass().getSimpleName(), "onDestroy()");
		super.onDestroy();
	} 
	  
}
