package com.oklab.events;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

	public class GestureDetectorTest extends Activity implements OnGestureListener , OnDoubleTapListener{
		//  참고사이트 http://ukzzang.tistory.com/45
	  private GestureDetector gd;
	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	     // TODO Auto-generated method stub
	     super.onCreate(savedInstanceState);
	     gd = new GestureDetector(this);
	     TextView text=new TextView(this);
	     text.setText("제스쳐테스트");
	     text.setTextColor(Color.GREEN);
	     text.setTextSize(20);
	     setContentView(text);
	   }
	 
	 
	   // Activity의 onTouchEvent가 발생되면 이것을 GestureDetector의 onTouchEvent로
	   // 등록해줌으로써 액티비티의 이벤트를 제스쳐로 처리되도록 한다.
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
	       // TODO Auto-generated method stub
	       return gd.onTouchEvent(event);
	   }

	 

	   // 터치하려고 손을 대기만 해도 발생되는 이벤트, 모든 제스쳐의 시작
	   @Override
	   public boolean onDown(MotionEvent arg0) {
	      // TODO Auto-generated method stub
	      Toast.makeText(this, "onDown 이벤트 발생", Toast.LENGTH_SHORT).show();
	      Log.i("GestureTestActivity", "onDown called");
	      return true;
	   }

	    // 드래그하다 띄면 호출됨.
	    // onScroll 이벤트와 비슷하지만 끝에 살짝 튕기는 동작에서 발생되는 이벤트
	   @Override
	   public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	          // TODO Auto-generated method stub
	          Toast.makeText(this, "onFling 이벤트 발생", Toast.LENGTH_SHORT).show();
	          Log.i("GestureTestActivity", "onFling called");
	          return true;
	    }

	 

	   // 길게 누르면 호출됨(약 590~600ms 정도 누를때 호출됨)
	   @Override
	   public void onLongPress(MotionEvent e) {
	         // TODO Auto-generated method stub
	         Toast.makeText(this, "onFling 이벤트 발생", Toast.LENGTH_SHORT).show();
	         Log.i("GestureTestActivity", "onFling called");
	    }
	 
	    // 터치하면 호출됨. 100ms 정도 누를때 호출됨
	    @Override
	     public void onShowPress(MotionEvent e) {
	           // TODO Auto-generated method stub
	          Toast.makeText(this, "onShowPress 이벤트 발생", Toast.LENGTH_SHORT).show();

	     }
	 
	     // 드래그시 호출. 최소 30ms 이후부터는 onScroll 이벤트가 발생할 수 있으며,
	     // 플링시키지 않고 살며시 손을 떼면 끝까지 onScroll 이벤트만 연속으로 발생
	    @Override
	     public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
	           // TODO Auto-generated method stub
	           Toast.makeText(this, "onScroll 이벤트 발생", Toast.LENGTH_SHORT).show();
	           return true;
	     }

	 

	     // 한번 터치하고 다음에 다시 터치 이벤트가 들어오지 않았을때,
	     // 한번 터치가 확실하다고 확인 시켜주는 이벤트
	     @Override
	     public boolean onSingleTapUp(MotionEvent e) {
	           // TODO Auto-generated method stub
	          Toast.makeText(this, "onSingleTapUp 이벤트 발생", Toast.LENGTH_SHORT).show();
	          Log.i("GestureTestActivity", "onSingleTapUp called");
	          return true;
	     }

	     /**
	      * onDoubleTapListener Callback
	      */

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.i("GestureTestActivity", "onDoubleTap called");
			return false;
		}


		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			Log.i("GestureTestActivity", "onDoubleTapEvent called");
			return false;
		}


		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.i("GestureTestActivity", "onSingleTapConfirmed called");
			return false;
		}

	}
