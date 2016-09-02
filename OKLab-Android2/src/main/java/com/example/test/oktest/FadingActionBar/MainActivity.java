package com.example.test.oktest.FadingActionBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test.oktest.R;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class MainActivity extends Activity {
	private ListView mListView;
	private String[] items = { "ScrollView", "ListView", "WebView",
			"No Parallax" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.header)
				.contentLayout(R.layout.activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View mView,
					int position, long ld) {
				switch (position) {
				case 1:
					startActivity(new Intent(MainActivity.this,
							ExampleScrollViewActivity.class));
					break;
				case 2:
					startActivity(new Intent(MainActivity.this,
							ExampleListActivity.class));
					break;
				case 3:
					startActivity(new Intent(MainActivity.this,
							ExampleWebViewActivity.class));
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
