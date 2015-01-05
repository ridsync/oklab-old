package com.oklab.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oklab.R;

public class SeatTableGameProgressView extends LinearLayout {

	protected Context m_context = null; 
	protected View           m_view;
	private LinearLayout m_llProgressBg;
	private TextView m_llProgressBar;
	
	public SeatTableGameProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		initialize();
	}
	
	public SeatTableGameProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		
		if(this.isInEditMode()) {
			return;
		}
		
		initialize();
	}
		
	public SeatTableGameProgressView(Context context) {
		super(context);
		m_context = context;
		
		if(this.isInEditMode()) {
			return;
		}
		
		initialize();
        }

	private void initialize()
	{
		LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = inflater.inflate(R.layout.comp_sale_table_game_progress, this, true);

		m_llProgressBg			= (LinearLayout)m_view.findViewById(R.id.LL_PROGRESS_BG);
		m_llProgressBar	= (TextView)m_view.findViewById(R.id.LL_PROGRESS_BAR);
	}
	
	
	public void setProgressBar(final int currStat , final int total){
		
		if (currStat < 0 || total <= 0) return ;
		
		m_llProgressBar.post(new Runnable() {
			
			@Override
			public void run() {
				int size = (int) calculateSize(currStat , total );
				Log.d("", "++ calculateSize size() = " + size);
				
//				m_llProgressBar.setWidth(size);
				
				ViewGroup.LayoutParams param = m_llProgressBar.getLayoutParams();
				param.width = size;
//				param.height = 높이;
				m_llProgressBar.setLayoutParams(param);
				
//				m_llProgressBar.setLayoutParams(new LayoutParams( size,LayoutParams.FILL_PARENT ));
			}
		});
	}
	
	private float calculateSize(int currStat, int total) {
//		m_llProgressBg.measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY); 
//		int width =  m_llProgressBg.getMeasuredWidth();
//		((View) m_llProgressBg.getParent()).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED); 
//		int width = ((View) m_llProgressBg.getParent()).getMeasuredWidth();
		int width = m_llProgressBg.getWidth();
		Log.d("", "++ m_llProgressBg.getWidth() = " + width);
		
		 if(  currStat > total ) return width;
		 
		float percent = (100*currStat)/total;
		Log.d("", "++ m_llProgressBg.percent() = " + percent);
		float result = ((percent)*width/100);
		
		Log.d("", "++ m_llProgressBg result(width) = " + result);
		return result; 
	}
	
    public void finalize()
    {
    	Log.d("","++ SeatTableView.finalize()" );
        m_view = null;
    }
}
