package com.oklab.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ViewLog extends View {
	public static final String TAG = "ViewLog";
	
	Paint pnt;
		
	public ViewLog(Context context) {
		this(context, null, 0);
	}
	
	public ViewLog(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ViewLog(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		pnt = new Paint();
		pnt.setColor(Color.YELLOW);
		pnt.setTextSize(20);
		Log.i(TAG, "Con() -> getMeasuredWidth() : " + getMeasuredWidth() + " , getMeasuredHeight() : "+ getMeasuredHeight());
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		super.onDraw(canvas);
		
		canvas.drawText("Hello, World!", 20, 20, pnt);
		Log.i(TAG, "onDraw()");
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		Log.i(TAG, "onMeasure()1 -> widthMeasureSpec : " + widthMeasureSpec + " , heightMeasureSpec : "+ heightMeasureSpec);
		Log.i(TAG, "onMeasure()2 -> getMeasuredWidth() : " + getMeasuredWidth() + " , getMeasuredHeight() : "+ getMeasuredHeight());
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		Log.i(TAG, "onLayout() -> changed: " + changed + ", left: " + left + ", top: " + top + ", right: " + right + ", bottom:" + bottom);
		Log.i(TAG, "onLayout() -> getMeasuredWidth() : " + getMeasuredWidth() + " , getMeasuredHeight() : "+ getMeasuredHeight());
	}
	
	public void Log(String where){
		Log.i(TAG, where + " is Invoke -> getMeasuredWidth() : " + getMeasuredWidth() + " , getMeasuredHeight() : "+ getMeasuredHeight());
	}
	
}