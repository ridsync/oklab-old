package com.oklab.events;

import android.R;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TouchEventsTest extends Activity {
	// http://aroundck.tistory.com/839 샘플
	private static final String TAG = "TouchEventsTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		LinearLayout root = new LinearLayout(this) {

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev) {

				Log.e(TAG, "root's onInterceptTouchEvent " + getActionName(ev));

				return super.onInterceptTouchEvent(ev);

			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {

				Log.e(TAG, "root's onTouchEvent " + getActionName(event));

				return super.onTouchEvent(event);

			}

			@Override
			public boolean dispatchTouchEvent(MotionEvent ev) {

				Log.e(TAG, "root's dispatchTouchEvent " + getActionName(ev));

				return super.dispatchTouchEvent(ev);
			}

		};

		LinearLayout ll = new LinearLayout(this) {

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev) {

				Log.e(TAG, "ll's onInterceptTouchEvent " + getActionName(ev));

//				if (ev.getAction() == MotionEvent.ACTION_MOVE)
//
//					return true;
//
//				else

					return super.onInterceptTouchEvent(ev);

			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {

				Log.e(TAG, "ll's onTouchEvent " + getActionName(event));

				return super.onTouchEvent(event);

			}

			@Override
			public boolean dispatchTouchEvent(MotionEvent ev) {

				Log.e(TAG, "ll's dispatchTouchEvent " + getActionName(ev));

				return super.dispatchTouchEvent(ev);

			}

		};

		final TextView textView = new TextView(this) {

			@Override
			public boolean onTouchEvent(MotionEvent event) {

				Log.e(TAG, "textView' onTouchEvent " + getActionName(event));

				return super.onTouchEvent(event);

			}

			@Override
			public boolean dispatchTouchEvent(MotionEvent event) {

				Log.e(TAG, "textView' dispatchTouchEvent " + getActionName(event));

				return super.dispatchTouchEvent(event);

			}

		};

		textView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				Log.e(TAG, "textView onTouch " + getActionName(event));
				Log.e(TAG, "textView onTouch chkTouchInside - " + chkTouchInside(v, event.getRawX(), event.getRawY()));
				return false;
			}

		});
		final Rect rect = new Rect();
		textView.setText("Gamza!!");
		textView.setBackgroundResource(R.color.holo_purple);
		textView.setWidth(200);
		textView.setHeight(200);
		textView.setGravity(Gravity.CENTER);
		
		textView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int x = (int) event.getRawX(); // 화면상 절대 좌표
                int y = (int) event.getRawY();// 화면상 절대 좌표
                v.getGlobalVisibleRect(rect); //RootView 레이아웃을 기준으로한 상대 좌표.
//                textView.getHitRect(rect);
                Log.d("TouchEventsTest = ", "rect.left = " + rect.left + " / rect.right = " +  rect.right);
                Log.d("TouchEventsTest = ","rect.top = " +  rect.top + " / rect.bottom = " +  rect.bottom);
                if (rect.contains(x, y) == true){
                	Log.d("TouchEventsTest : ","Hit x=" + x +" y=" + y);
                }else
                	Log.d("TouchEventsTest : ","Miss x=" + x +" y=" + y);
				return true;
			}
		});

		ll.setLayoutParams(new LayoutParams(500, 500));
		ll.setGravity(Gravity.CENTER);
		ll.addView(textView);
		
		final Button btn = new Button(this);
		btn.setText("TestBtn");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TouchEventsTest.this, "TestBtn Click",Toast.LENGTH_SHORT).show();
			}
		});
		btn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				Log.d(TAG, "++ btn onTouch " + getActionName(event));
				return false;
			}
		});
		btn.setOnDragListener(new OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				Log.d(TAG, "++ btn onDrag " + getActionName(event));
				return false;
			}
		});
		ll.addView(btn);
		
		root.addView(ll);
		root.setEnabled(false);
		setContentView(root);

	}
	
	private float initX = 0;
	private float initY = 0;
	private static final int MAX_DISTANCE_MOVE = 60;
//	
//	@Override
//	public boolean dispatchTouchEvent( MotionEvent ev )
//	{
//		Log.d(TAG, "++ dispatchTouchEvent " + getActionName(ev));
//		// TableView의 스크롤이 되는 것을 막는다.
//		if (ev.getAction() == MotionEvent.ACTION_DOWN){
//			initX = ev.getX();
//			initY = ev.getY();
//		}else if( ev.getAction() == MotionEvent.ACTION_MOVE ){
//			float moveX = Math.abs(initX - ev.getX());
//			float moveY = Math.abs(initY - ev.getY());
//			if (moveX > MAX_DISTANCE_MOVE || moveY > MAX_DISTANCE_MOVE){
//				ev.setAction(MotionEvent.ACTION_CANCEL);
//				return super.dispatchTouchEvent( ev );
//			}
//			return true;
//		}
//		return super.dispatchTouchEvent( ev );
//	}
	
	public boolean chkTouchInside(View view, float x, float y) {

		 int[] location = new int[2];

		 view.getLocationOnScreen(location);

		 if (x >= location[0]) {

		  if (x <= location[0] + view.getWidth()) {

		   if (y >= location[1]) {

		    if (y <= location[1] + view.getHeight()) {

		     return true;

		    }

		   }

		  }

		 }

		 return false;

	}

	/** Private Method **/
	
	private String getActionName(MotionEvent event){
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			return "ACTION_DOWN";
		case MotionEvent.ACTION_UP:
			return "ACTION_UP";
		case MotionEvent.ACTION_MOVE:
			return "ACTION_MOVE";
		case MotionEvent.ACTION_OUTSIDE:
			return "ACTION_OUTSIDE";
		case MotionEvent.ACTION_CANCEL:
			return "ACTION_CANCEL";
		default:
			return "ACTION_Other";
		}
	}
	
	private String getActionName(DragEvent event){
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			return "ACTION_DOWN";
		case MotionEvent.ACTION_UP:
			return "ACTION_UP";
		case MotionEvent.ACTION_MOVE:
			return "ACTION_MOVE";
		case MotionEvent.ACTION_OUTSIDE:
			return "ACTION_OUTSIDE";
		case MotionEvent.ACTION_CANCEL:
			return "ACTION_CANCEL";
		default:
			return "ACTION_Other";
		}
	}
	
}
