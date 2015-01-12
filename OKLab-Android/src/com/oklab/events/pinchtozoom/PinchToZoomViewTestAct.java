package com.oklab.events.pinchtozoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.oklab.R;
import com.oklab.views.MultiZoomImageView;

public class PinchToZoomViewTestAct extends Activity {
	
	private static final String TAG = "TouchEventsTest";

	private static final String m_imgPath = "//";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//1. 핀치 투 줌 (+뷰 드래그 이동)   // http://aroundck.tistory.com/839 샘플
		PinchToZoomView ptzView =new PinchToZoomView(this);
		ptzView.setSelectedImage(m_imgPath);
		
		//2. 줌인 아웃만 되는 이미지뷰 ??? 이동안됨 왜 ? // http://blog.flysky.kr/blog/4547416 샘플
//		ZoomImageView img = new ZoomImageView(this);
		MultiZoomImageView imgView = new MultiZoomImageView(this);
		imgView.setScaleType(ScaleType.MATRIX);
		imgView.setImageResource( R.drawable.okt );
		imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		LinearLayout ll = new LinearLayout(this);
//		ll.addView(ptzView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll.addView(imgView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		setContentView(ll);
	}
	
}
