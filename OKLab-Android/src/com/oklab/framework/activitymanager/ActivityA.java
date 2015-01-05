package com.oklab.framework.activitymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.oklab.BaseActivity;
import com.oklab.R;

public class ActivityA extends BaseActivity {
		private static final String TAG = ActivityA.class.getSimpleName();
		
		public static final int REQ_CODE_DATA_EXPRESS = 100;
		public static final String EXTRA_NAME_DATA1 = "ActivityA_EXTRA_NAME_DATA1";
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_a);

			((TextView)findViewById(R.id.title)).setText("Activity A");
			((TextView)findViewById(R.id.title)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ActivityA.this, ActivityB.class);
					intent.putExtra(EXTRA_NAME_DATA1, "Data A-123");
					startActivityForResult(intent, REQ_CODE_DATA_EXPRESS);
					overridePendingTransition(android.R.anim.fade_in,0);
				}
			});
			
//			LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
//			layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setGravity(Gravity.CENTER);
//
//			EditText child = new EditText(this);
//			child.setText("테스트2번화면");
//			child.setInputType(InputType.TYPE_CLASS_PHONE);
//			child.setTextColor(Color.RED);
//			child.setTextSize(20);
//			child.setBackgroundColor(Color.GREEN);
//			child.setFocusable(true);
//			child.requestFocus();
//
//			LayoutParams params = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.WRAP_CONTENT,
//					LinearLayout.LayoutParams.WRAP_CONTENT);
//			layout.addView(child, params);
			
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.d(TAG, "onActivityResult called !! resultCode = " + resultCode);
			if (resultCode == Activity.RESULT_OK) {
				if (data != null){
					Toast.makeText(this, "result data = " + data.getStringExtra(EXTRA_NAME_DATA1), Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(this, "result data = null ", Toast.LENGTH_SHORT).show();
				}
				
			} else {
				Toast.makeText(this, "result not RESULT_OK ", Toast.LENGTH_SHORT).show();
			}
		}
}
