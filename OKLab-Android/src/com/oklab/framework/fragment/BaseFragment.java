package com.oklab.framework.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BaseFragment extends Fragment{
	
	/**
	 * *****************************
	 * Fragment Life Cycle
	 * *****************************
	 */
	
	@Override
    public void onAttach(Activity activity) {
        Log.i(this.getClass().getSimpleName(), "onAttach()");
         
        super.onAttach(activity);
    }
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onCreate()");
         
        super.onCreate(savedInstanceState);
         
        Bundle args = getArguments();
        if (args != null) {
//            mIdx = args.getInt("index", 0);
        }
    }
     
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		Log.i(this.getClass().getSimpleName(), "onCreateView()");
//		return null;
//	}
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    Log.i(this.getClass().getSimpleName(), "onActivityCreated()");
	    super.onActivityCreated(savedInstanceState);
	}
	 
    @Override
    public void onStart() {
        Log.i(this.getClass().getSimpleName(), "onStart()");
        super.onStart();
    }
 
    @Override
    public void onResume() {
        Log.i(this.getClass().getSimpleName(), "onResume()");
        super.onResume();
    }
     
    /**
	 * Called when the hidden state
	 * - FragmentTransaction Show/Hide Mehod
	 */
    @Override
    public void onHiddenChanged(boolean hidden) {
    	Log.i(this.getClass().getSimpleName(), "onHiddenChanged() hidden = " + hidden);
    	super.onHiddenChanged(hidden);
    }
    
    @Override
    public void onPause() {
        Log.i(this.getClass().getSimpleName(), "onPause()");
        super.onPause();
    }
     
    @Override
    public void onStop() {
        Log.i(this.getClass().getSimpleName(), "onStop()");
        super.onStop();
    }
     
    @Override
    public void onDestroyView() {
        Log.i(this.getClass().getSimpleName(), "onDestroyView()");
        super.onDestroyView();
    }
     
    @Override
    public void onDestroy() {
        Log.i(this.getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
 
    @Override
    public void onDetach() {
        Log.i(this.getClass().getSimpleName(), "onDetach()");
        super.onDetach();
    }
    
    @Override
    public void onInflate(Activity activity, AttributeSet attrs,
            Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onInflate()");
        super.onInflate(activity, attrs, savedInstanceState);
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.getClass().getSimpleName(), "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }
    
}
