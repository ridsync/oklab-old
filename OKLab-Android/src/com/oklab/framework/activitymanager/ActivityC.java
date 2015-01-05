package com.oklab.framework.activitymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.oklab.BaseActivity;
import com.oklab.R;

public class ActivityC extends BaseActivity {
		private static final String TAG = ActivityC.class.getSimpleName();
		private static final int REQ_CODE_DATA_EXPRESS = 100;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_a);

			((TextView)findViewById(R.id.title)).setText("Activity C");
			((TextView)findViewById(R.id.title)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(ActivityC.this, ActivityA.class);
//					intent.putExtra(EXTRA_NAME_DATA1, "Data-123");
//					startActivityForResult(intent, REQ_CODE_DATA_EXPRESS);
//					overridePendingTransition(android.R.anim.fade_in,0);
				}
			});
			
			Intent intent = new Intent();
			intent.putExtra(ActivityA.EXTRA_NAME_DATA1, "Data-C-ok");
			setResult(RESULT_OK, intent);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == Activity.RESULT_OK) {
				}
		}
}
