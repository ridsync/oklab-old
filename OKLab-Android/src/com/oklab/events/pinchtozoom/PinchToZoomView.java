package com.oklab.events.pinchtozoom;

	import com.oklab.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
	 
/**
 * 터치하여 움직이고, 핀치투줌 이 되는 이미지뷰 구현.
 * @author P042209
 *
 */
public class PinchToZoomView  extends View{
	    int X,Y,Height,Width;
	    private PinchToZoomUnit m_ptzUnit;
	     
	    public PinchToZoomView(Context context) {
	        super(context);
	        // TODO Auto-generated constructor stub
	    }
	    public void setSelectedImage(String ImagePath){
//	        Bitmap image = BitmapFactory.decodeFile(ImagePath);
	        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.okt);
	        
	        m_ptzUnit=new PinchToZoomUnit(image);
	        invalidate();
	    }
	 
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        // TODO Auto-generated method stub
	        m_ptzUnit.TouchProcess(event);
	        invalidate();
	        return (true);
	    }
	     
	    @Override
	     
	    public void draw(Canvas canvas) {
	        // TODO Auto-generated method stub
	        canvas.drawBitmap(m_ptzUnit.getImage(), null,  m_ptzUnit.getRect(), null);
	        super.draw(canvas);
	    }
}
