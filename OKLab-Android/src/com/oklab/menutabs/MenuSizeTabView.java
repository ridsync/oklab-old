package com.oklab.menutabs;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.oklab.R;
import com.oklab.menutabs.GameTab.IMenuSizeViewBtnListener;

public class MenuSizeTabView extends LinearLayout implements OnClickListener, IMenuSizeViewBtnListener {

	public static interface IMenuSizeTabViewBtnListener {
		public abstract void onClickMenu(DPdt pdt);
	}

	private final int MAXCNT = 5;
	private final int VISIBLECNT = 3;

	private Context m_context = null;
	private IMenuSizeTabViewBtnListener m_listener = null;
	private ArrayList<DPdt> m_pdtList = null;
	
	private int m_firstIdx = 0;
	private FrameLayout m_leftArrow;
	private FrameLayout m_rightArrow;
	private LinearLayout m_container;
	
	public MenuSizeTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		initialize();
	}
	
	public MenuSizeTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		initialize();
	}
		
	public MenuSizeTabView(Context context) {
		super(context);
		m_context = context;
		initialize();
	}
	
	private void initialize()
	{
//		LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		inflater.inflate(R.layout.comp_sale_game_group_tab, this, true);
//		
//		m_leftArrow = (FrameLayout)findViewById(R.id.FL_MENUSIZE_LEFT_ARROW);
//		m_leftArrow.setOnClickListener(this);
//
//		m_rightArrow = (FrameLayout)findViewById(R.id.FL_MENUSIZE_RIGHT_ARROW);
//		m_rightArrow.setOnClickListener(this);
//		
//		m_leftArrow.setVisibility(View.GONE);
//		m_rightArrow.setVisibility(View.GONE);
//		
//		m_container = (LinearLayout)findViewById(R.id.LL_MENUSIZE_CONTAINER);
	}
//
//	public void setData(ArrayList<DPdt> pdtList , IMenuSizeTabViewBtnListener listener)
//	{
//		m_listener = listener;
//		m_pdtList = pdtList;
//		
//		m_firstIdx = 0;
//		
//		if(m_pdtList != null && m_pdtList.size() > MAXCNT)
//		{
//			m_leftArrow.setVisibility(View.VISIBLE);
//			m_rightArrow.setVisibility(View.VISIBLE);
//			setView(m_firstIdx , VISIBLECNT);
//		}
//		else
//		{
//			m_leftArrow.setVisibility(View.GONE);
//			m_rightArrow.setVisibility(View.GONE);
//			setView(m_firstIdx , m_pdtList.size());
//		}
//	}
//
//	private void init()
//	{
//		int cnt = m_container.getChildCount();
//		
//		for(int i= cnt - 1; i >= 0  ; i--)
//		{
//			View view = m_container.getChildAt(i);
//			if((view.getId() == R.id.FL_MENUSIZE_LEFT_ARROW) || 
//					(view.getId() == R.id.FL_MENUSIZE_RIGHT_ARROW))
//				continue;
//
//			m_container.removeViewAt(i);
//		}
//	}
//	
//	private void setView(int startIdx, int count )
//	{
//		init();
//		int index = 1;		//부모뷰안에서 leftArrow가 0번 , rightArrow 1번 인덱스임.
//		for(int i= startIdx; i < startIdx + count; i++)
//		{
//			DPdt pdt = m_pdtList.get(i);
//			MenuSizeView view = new MenuSizeView(m_context);
//			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
//			view.setData(pdt, this);
//			m_container.addView(view, index);
//			index++;				
//		}	
//	}
//	
	@Override
	public void onClick(View v) {
//		switch(v.getId())
//		{
//			case R.id.FL_MENUSIZE_LEFT_ARROW:
//				if((m_firstIdx - 1) >= 0)
//				{
//					m_firstIdx -= 1;
//					setView(m_firstIdx , VISIBLECNT);
//				}
//				break;
//			case R.id.FL_MENUSIZE_RIGHT_ARROW:
//				if((m_firstIdx + VISIBLECNT ) < m_pdtList.size())
//				{
//					m_firstIdx += 1;
//					setView(m_firstIdx , VISIBLECNT);
//				}
//				break;
//		}
//		
	}

	@Override
	public void onClickMenuSize(int index) {
		
	}

//	@Override
//	public void onClickMenuSize(DPdt pdt) {
//		if(pdt != null)
//		{
//			if(m_listener != null)
//			{
//				m_listener.onClickMenu(pdt);
//			}
//		}
//		
//	}
}
