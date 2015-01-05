package com.oklab.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.oklab.R;

public class ProgressBarCustomViewTest extends Activity implements OnClickListener{

	SeatTableGameProgressView  stgp ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_progresstest);
		
		((Button)findViewById(R.id.BT_INPUT_BUTTON)).setOnClickListener(this);
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		stgp =  (SeatTableGameProgressView)findViewById(R.id.TV_GAME_PROGRESS_BAR);
		((SeatTableGameProgressView)findViewById(R.id.TV_GAME_PROGRESS_BAR)).setProgressBar(0, 18);
	}
	
	@Override
	public void onClick(View arg0) {
		String value = ((EditText)findViewById(R.id.ET_INPUT_DATA)).getText().toString();
		String total = ((EditText)findViewById(R.id.ET_INPUT_DATA_TOTAL)).getText().toString();
		stgp.setProgressBar( Integer.valueOf(value), Integer.valueOf(total) );
		
	}
	
}
