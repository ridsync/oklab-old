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

public class OneFragment extends BaseFragment implements OnClickListener {
	
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
		View view = inflater.inflate(R.layout.fragment_one, container, false);

		Button button = (Button) view.findViewById(R.id.bt_ok);
		button.setOnClickListener(this);

		return view;
	}
     
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_ok:
			Toast.makeText(getActivity(), "One Fragment", Toast.LENGTH_SHORT)
					.show();
			break;

		}

	}
	
}
