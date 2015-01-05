package com.oklab.framework.fragment;

import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oklab.BaseActivity;
import com.oklab.R;

public class MainFragmentActivity extends BaseActivity implements OnClickListener {

	final String TAG = "MainFragmentActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ONE = 0;
	public final static int FRAGMENT_TWO = 1;
	public final static int FRAGMENT_THREE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		Button bt_oneFragment = (Button) findViewById(R.id.bt_oneFragment);
		bt_oneFragment.setOnClickListener(this);
		Button bt_twoFragment = (Button) findViewById(R.id.bt_twoFragment);
		bt_twoFragment.setOnClickListener(this);
		Button bt_threeFragment = (Button) findViewById(R.id.bt_threeFragment);
		bt_threeFragment.setOnClickListener(this);

		mCurrentFragmentIndex = FRAGMENT_ONE;

		fragmentAdd(mCurrentFragmentIndex);
		
		// android.app.fragment Package Using
		
		getFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {

			@Override
			public void onBackStackChanged() {
				Log.d(TAG, "onBackStackChanged " );
			}
		});
		//android support library v4 using
		// getSuppportFragmentManager();
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_oneFragment:
//			mCurrentFragmentIndex = FRAGMENT_ONE;
//			fragmentReplace(mCurrentFragmentIndex);
			
			// FragmentDialog 테스트
			TestDlgFragment dlg = TestDlgFragment.getInstance();
			dlg.show(getFragmentManager(), "tagtest");
			break;
		case R.id.bt_twoFragment:
			mCurrentFragmentIndex = FRAGMENT_TWO;
			fragmentAdd(mCurrentFragmentIndex);
			break;
		case R.id.bt_threeFragment:
			mCurrentFragmentIndex = FRAGMENT_THREE;
			fragmentAdd(mCurrentFragmentIndex);
			break;

		}

	}
	
	public void fragmentAdd(int reqNewFragmentIndex) {
		
		android.app.Fragment newFragment = null;

		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

		newFragment = getFragment(reqNewFragmentIndex);

		// add fragment
		android.app.FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.add(R.id.ll_fragment, newFragment);
		// Commit the transaction
		transaction.commit();

	}
	
	public void fragmentReplace(int reqNewFragmentIndex) {

		android.app.Fragment newFragment = null;

		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

		newFragment = getFragment(reqNewFragmentIndex);

		// replace fragment
		android.app.FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.ll_fragment, newFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();

	}

	@Override
	public void onBackPressed() {
		int cnt = getFragmentManager().getBackStackEntryCount();
		if (cnt > 0){
		getFragmentManager().popBackStack();
			Log.d(TAG, "popBackStack() cnt = " + cnt);
		}else {
			super.onBackPressed();
			Log.d(TAG, "onBackPressed() cnt = " + cnt);
		}
	}
	
	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_ONE:
			newFragment = new OneFragment();
			break;
		case FRAGMENT_TWO:
			newFragment = new TwoFragment();
			break;
		case FRAGMENT_THREE:
			newFragment = new ThreeFragment();
			break;

		default:
			Log.d(TAG, "Unhandle case");
			break;
		}

		return newFragment;
	}
	
}
