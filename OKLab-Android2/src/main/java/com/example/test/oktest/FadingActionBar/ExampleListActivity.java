package com.example.test.oktest.FadingActionBar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test.oktest.R;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class ExampleListActivity extends Activity {
	private ListView mListView;
	private String[] items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.header).parallax(false)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		items = new String[31];
		for (int index = 0; index <= 30; index++) {
			items[index] = "����Ʈ ������ " + index;
		}

		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
