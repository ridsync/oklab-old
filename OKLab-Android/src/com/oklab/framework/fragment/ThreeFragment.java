package com.oklab.framework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.oklab.R;
import com.oklab.framework.activitymanager.ActivityA;

public class ThreeFragment  extends BaseFragment implements OnClickListener{

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        Bundle args = getArguments();
        if (args != null) {
//            mIdx = args.getInt("index", 0);
        }
         
    }
     
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(this.getClass().getSimpleName(), "onCreateView()");
		View v = inflater.inflate(R.layout.fragment_three, container, false);
		
		Button button = (Button)v.findViewById(R.id.bt_ok);
		button.setOnClickListener(this);
		Button button2 = (Button)v.findViewById(R.id.bt_start_activity);
		button2.setOnClickListener(this);
		
		return v;
	}
     
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.bt_ok:
			Toast.makeText(getActivity(), "ThreeFragment", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_start_activity:
			Intent intent = new Intent(getActivity(), ActivityA.class);
			startActivity(intent);
			break;
		
		}
		
	}
	
}
