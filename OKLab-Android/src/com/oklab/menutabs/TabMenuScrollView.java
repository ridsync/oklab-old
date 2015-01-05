package com.oklab.menutabs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class TabMenuScrollView extends HorizontalScrollView{
	private OnScrollListener mlistener;
	
	public interface OnScrollListener {
		void onScrollListener(int l, int t, int oldl, int oldt);
	}

	public TabMenuScrollView(Context context) {
		super(context);
	}
	
	public TabMenuScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TabMenuScrollView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnScrollListener(OnScrollListener listener) {
		this.mlistener = listener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
		if(null != mlistener){
			mlistener.onScrollListener(l, t, oldl, oldt);
		}
	}
}
