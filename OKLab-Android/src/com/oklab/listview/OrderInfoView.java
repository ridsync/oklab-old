package com.oklab.listview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oklab.R;
import com.oklab.menutabs.DPdt;

/**
 * 리스트뷰내에서 특정포지션에 해당하는 아이템 뷰를 안보이게 처리 하는 테스트...
 * @author P042209
 *
 */
public class OrderInfoView extends LinearLayout implements OnItemClickListener{

	private Context m_context = null;
	private ListView m_TableListView;
	private OrderInfoAdapter m_adapter;
    private ArrayList<DPdt> m_orderList = new ArrayList<DPdt>();
	public OrderInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		initialize();
	}

	public OrderInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		initialize();
	}

	public OrderInfoView(Context context) {
		super(context);
		m_context = context;
		initialize();
	}

	private void initialize() {
		LayoutInflater inflater = (LayoutInflater) m_context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_sale_order_list_sale, this,
				true);
		
		setData();
		
		m_TableListView = (ListView) findViewById(R.id.LV_ORDER_LIST);
		m_TableListView.setDivider(null);
		m_TableListView.setOnItemClickListener(this);
		m_adapter = new OrderInfoAdapter(m_context, R.layout.item_sale_order_list ,m_orderList); 
		m_TableListView.setAdapter(m_adapter);
		
		LinearLayout listViewbg = (LinearLayout) findViewById(R.id.LL_ORDER_LIST_LINE_BG);
		listViewbg.removeAllViews();
		for (int i = 0 ; i < 9 ; i++){
			LinearLayout view = (LinearLayout) inflater.inflate(R.layout.item_sale_order_list, null);
			listViewbg.addView(view, i);
		}
		
	}
	
	public void setData(){
		
		int size = 3;
		
		for( int i = 0; i < size; i++ ) {
            
			DPdt dpt = DPdt.createEmptyPdt();
			dpt.m_lTip = i;
            m_orderList.add( dpt );
        }
	}

  	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
  	{
  		// TableView의 스크롤이 되는 것을 막는다.
//  		if(ev.getAction()==MotionEvent.ACTION_MOVE)
//	  	      return true;
  		
  		return super.dispatchTouchEvent(ev);
  	}
  	
  	private class OrderInfoAdapter extends ArrayAdapter<DPdt>
	{

		private ArrayList<DPdt> m_arr = null;
		private Context m_con = null;
		private int m_resId = 0;
		
		public OrderInfoAdapter(Context context, int textViewResourceId,
				ArrayList<DPdt> objects) {
			super(context, textViewResourceId, objects);
			
			m_con = context;
			m_arr = objects;
			m_resId = textViewResourceId;
			
		}

		@Override
		public int getCount() {
			return m_arr.size();
		}

		@Override
		public DPdt getItem(int position) {
			return m_arr.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null){
				LayoutInflater inflater = (LayoutInflater) m_con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(m_resId, null);
			}
			
//			if (position == 3 || position == 6 || position == 11){
			if (true){
//				LayoutInflater inflater = (LayoutInflater) m_con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(m_resId, null);
//				convertView.setVisibility(View.VISIBLE);
				
				DPdt order = m_arr.get(position);

				String tip = String.valueOf( order.getTip() );
				TextView listCntTV =(TextView) convertView.findViewById(R.id.label);
				listCntTV.setText( tip );
				
				if (6 == position){
					ImageView ivListLine =(ImageView) convertView.findViewById(R.id.IV_LIST_LINE);
					ivListLine.setBackgroundColor(Color.RED);
				}
			}else {
//				LayoutInflater inflater = (LayoutInflater) m_con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(R.layout.item_sale_order_empty_view, null);
//				convertView.setVisibility(View.GONE);
				((ViewGroup)convertView).removeAllViews();
			}

			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Toast toast = Toast.makeText(m_context, "position = " + arg2, Toast.LENGTH_SHORT); // this:context , msg : 표시할 메시지
		toast.setGravity(Gravity.CENTER, 0, 0); //좌표값 설정
		toast.show();
		
		DPdt dptd = DPdt.createEmptyPdt();
		dptd.m_lTip = m_orderList.size() + 1;
        m_orderList.add( dptd );
        
        m_adapter.notifyDataSetChanged();
		
	}
  	
	
	private class CustomAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}

