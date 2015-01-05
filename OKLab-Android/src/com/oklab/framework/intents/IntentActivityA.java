package com.oklab.framework.intents;

import java.util.ArrayList;

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

/**
 * Intent로 Object나 ArrayList<? extends Parcelable>한 리스트를 번들로 다른 엑티비티에 전달.
 * IntentData(Object)안에 Pracelable한 Object(Download)를 담은 ArrayList객체도  전달.
 * @author P042209
 *
 */
public class IntentActivityA extends BaseActivity {
		private static final String TAG = IntentActivityA.class.getSimpleName();
		
		public static final int REQ_CODE_DATA_EXPRESS = 100;
		public static final String EXTRA_NAME_DATA1 = "IntentActivityA_EXTRA_NAME_DATA1";
		
		public ArrayList<IntentData> aLtest = new ArrayList<IntentData>();
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_a);

			for (int i=0 ; i < 3 ; i++){
				IntentData data = new IntentData();
				data.setIndex(i);
					ArrayList<Download> downlist = new  ArrayList<Download>();
					downlist.add(new Download("keys" + i, "contentpoolNo", "urlPath", "fileNm", 10 + i));
				data.setDownList( downlist );
				
				aLtest.add( data );
			}
			
			((TextView)findViewById(R.id.title)).setText("IntentActivity A");
			((TextView)findViewById(R.id.title)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(IntentActivityA.this, IntentActivityB.class);
//					intent.putExtra(EXTRA_NAME_DATA1, "Data A-123");
//					intent.putParcelableArrayListExtra(EXTRA_NAME_DATA1, aLtest);

					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList(EXTRA_NAME_DATA1, aLtest);
					intent.putExtras(bundle);
					startActivityForResult(intent, REQ_CODE_DATA_EXPRESS);
					overridePendingTransition(android.R.anim.fade_in,0);
				}
			});
			
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
