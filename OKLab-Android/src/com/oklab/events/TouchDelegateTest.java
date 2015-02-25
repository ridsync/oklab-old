package com.oklab.events;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.oklab.BaseActivity;
import com.oklab.R;

/**
 * 뷰 자신의 터치 영역을 부모뷰에 위임한다.
 * Rect 값을 수정하여 실제 터치될 영역을 확장하여 위임할 수있다.
 * http://developer.android.com/reference/android/view/TouchDelegate.html
 * https://github.com/brendanw/Touch-Delegates
 * http://www.brendanweinstein.me/2012/06/26/touchdelegate-and-gethitrect-making-buttons-with-expanded-touch-areas/
 * @author okok
 *
 */
public class TouchDelegateTest extends BaseActivity {

	private static final String TAG = "TouchDelegateTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_delegate);

		View mParent = findViewById(R.id.FrameContainer);
		mParent.post(new Runnable() {
		    @Override
		    public void run() {
		        Rect delegateArea = new Rect();
		        ImageButton mTutorialButton = (ImageButton) findViewById(R.id.title);
		        mTutorialButton.setEnabled(true);
		        mTutorialButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		                Toast.makeText(TouchDelegateTest.this, "Test TouchDelegate", Toast.LENGTH_SHORT).show();
		            }
		        });
		 
		        mTutorialButton.getHitRect(delegateArea);
//		        bounds.top -= 200; // 뷰 영역 확장할 영역 및 SIZE
//		        bounds.bottom += 200; // 뷰 영역 확장할 영역 및 SIZE
//		        bounds.left -= 200; // 뷰 영역 확장할 영역 및 SIZE
                delegateArea.right += 200; // 뷰 영역 확장할 영역 및 SIZE
		        TouchDelegate touchDelegate = new TouchDelegate(delegateArea, mTutorialButton);
		 
		        if (View.class.isInstance(mTutorialButton.getParent())) {
		            ((View) mTutorialButton.getParent()).setTouchDelegate(touchDelegate);
		        }
                /**
                 * 한부모뷰 안에서 두개의 View가 동시에 Delegate는 안되는것같다 ?
                 */
                Log.d(TAG, "delegated top/bottom/left/right" + delegateArea.top + "/" + delegateArea.bottom + "/" + delegateArea.left + "/" + delegateArea.right);
		    }
		});
	
	}
}
