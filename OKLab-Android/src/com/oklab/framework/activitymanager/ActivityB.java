package com.oklab.framework.activitymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.oklab.BaseActivity;
import com.oklab.R;

public class ActivityB extends BaseActivity {
		private static final String TAG = ActivityB.class.getSimpleName();
		private static final int REQ_CODE_DATA_EXPRESS = 100;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_a);

			((TextView)findViewById(R.id.title)).setText("Activity B");
			((TextView)findViewById(R.id.title)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ActivityB.this, ActivityC.class);
//					intent.putExtra(EXTRA_NAME_DATA1, "Data-123");
					startActivityForResult(intent, REQ_CODE_DATA_EXPRESS);
//					overridePendingTransition(android.R.anim.fade_in,0);
					finish();
				}
			});
			
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.d(TAG, "onActivityResult called !! ");
			if (resultCode == Activity.RESULT_OK) {
				
//				Intent intent = new Intent();
//				intent.putExtra(ActivityA.EXTRA_NAME_DATA1, data.getStringExtra(ActivityA.EXTRA_NAME_DATA1));
//				setResult(RESULT_OK, intent);
//				finish();
			}
		}
}
