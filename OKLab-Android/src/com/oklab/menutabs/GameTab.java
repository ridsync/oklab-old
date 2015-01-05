package com.oklab.menutabs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oklab.R;

public class GameTab extends LinearLayout{

	public static interface IMenuSizeViewBtnListener {
		public abstract void onClickMenuSize(int index);
	}
	
	private Context m_context = null;
	private IMenuSizeViewBtnListener m_listener = null;
	private DPdt m_pdt = null;
	private int index = 0;
	
	public GameTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		initialize();
	}
	
	public GameTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		initialize();
	}
		
	public GameTab(Context context) {
		super(context);
		m_context = context;
		initialize();
	}
	
	private void initialize()
	{
		LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_sale_order_v_game_tab, this, true);
		
	}

	public void setData(String text)
	{
		
		TextView menuName = (TextView)findViewById(R.id.TV_NAME);
		menuName.setText( text );

	}
}
