package com.oklab.framework.activitymanager;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.oklab.BaseActivity;
import com.oklab.R;

public class PickMediaTestActivity extends BaseActivity {
	private static final String TAG = PickMediaTestActivity.class.getSimpleName();

	private static Thread th ;
	public static final int PICK_FROM_CAMERA = 11000;
	public static final int PICK_FROM_ALBUM  = 11001;
	public static final int PICK_FROM_VIDEO_REC  = 11002;
	public static final int CROP_IMAGE = 11003;
	public static final int PICK_VIDEO = 11004;
	
	MediaScannerConnection msc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskmgr);

		LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);

		EditText child = new EditText(this);
		child.setText("테스트2번화면");
		child.setInputType(InputType.TYPE_CLASS_PHONE);
		child.setTextColor(Color.RED);
		child.setTextSize(20);
		child.setBackgroundColor(Color.GREEN);
		child.setFocusable(true);
		child.requestFocus();

		LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(child, params);
		
		/**
		 *  사진가져오기 및 파일스캔 테스트
		 */
		
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				startActivityForResult(intent, PICK_FROM_ALBUM);
				
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				File file = new File("/mnt/sdcard/imgpicktest.jpg");
//				String mImageCaptureUrl = file.getPath();
//				Uri mImageCaptureUri = Uri.fromFile(file);
//				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//				//intent.putExtra("return-data", true);
//				startActivityForResult(intent, PICK_FROM_CAMERA);
			}
		});
		
		
	}
	
	Uri mImageCaptureUri;
	String path;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Cursor cursor = null;
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PICK_FROM_CAMERA:
//					cursor = getContentResolver().query(mImageCaptureUri, null, null, null, null);
//					path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
//					Uri mImageCaptureUri = Uri.fromFile(new File(path));
//					Log.d(TAG, "Image Path =  "+ path + " / mImageCaptureUri = "+ mImageCaptureUri);
					//스캔
					msc = new MediaScannerConnection(this, mScanClient);
					msc.connect();
				break;
				
			case PICK_FROM_ALBUM:
//				Log.d(TAG,"PICK_FROM_CAMERA data.getData "+data+"/"+data.getExtras()+"/"+data.getExtras().getParcelable("data"));
					Uri uri2 = data.getData();
					cursor = getContentResolver().query(uri2, null, null, null, null);
					if (cursor.moveToNext()) {
						path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
						Uri mImageCaptureUri2 = Uri.fromFile(new File(path));
						Log.d(TAG, "Image Path =  "+ path + " / mImageCaptureUri = "+ mImageCaptureUri2);
						
					}
					msc = new MediaScannerConnection(this, mScanClient);
					msc.connect();
				break;
				
			}
		}
	}
	
	private MediaScannerConnectionClient mScanClient = new MediaScannerConnectionClient(){

	      public void onMediaScannerConnected() {
	            Log.i(TAG, "onMediaScannerConnected");
	            msc.scanFile( new File(path).getAbsolutePath(), "image" );
	            Log.i(TAG, "scanFile Finished" + "path = "+ path);
	      }

	      public void onScanCompleted(String path, Uri uri) {
	            Log.i(TAG, "onScanCompleted(" + path + ", " + uri.toString() + ")");     // 스캐닝한 정보를 출력
	            msc.disconnect();
	      }
	};
}
