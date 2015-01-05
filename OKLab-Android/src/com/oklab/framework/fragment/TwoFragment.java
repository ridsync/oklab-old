package com.oklab.framework.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.oklab.R;

public class TwoFragment extends BaseFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(this.getClass().getSimpleName(), "onCreateView()");
		
		View v = inflater.inflate(R.layout.fragment_two, container, false);
		
		Button button = (Button)v.findViewById(R.id.bt_ok);
		button.setOnClickListener(this);
		
		return v;
	}
     
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_ok:
			Toast.makeText(getActivity(), "TwoFragment", Toast.LENGTH_SHORT)
					.show();
			break;

		}

	}

}
