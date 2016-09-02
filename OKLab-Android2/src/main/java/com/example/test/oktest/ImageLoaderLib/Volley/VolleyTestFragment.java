/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.test.oktest.ImageLoaderLib.Volley;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.test.oktest.ImageLoaderLib.AUILTestFragment;
import com.example.test.oktest.ImageLoaderLib.AbsListViewBaseFragment;
import com.example.test.oktest.ImageLoaderLib.Constants;
import com.example.test.oktest.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class VolleyTestFragment extends AbsListViewBaseFragment {

	String[] imageUrls = Constants.IMAGES;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_auiltest_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		((ListView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AUILTestFragment());
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.commit();
			}
		});
		return rootView;
	}

    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {

    }

    @Override
	public void onDestroy() {
		super.onDestroy();
	}

	private static class ViewHolder {
		TextView text;
        FadeInNetworkImageView netImage;
        ImageView image;
	}

	class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.item_list_image_volley, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.netImage = (FadeInNetworkImageView) view.findViewById(R.id.imageVolley);
				holder.image = (ImageView) view.findViewById(R.id.imageView);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText("Item " + (position + 1));

            // Volley ImageLoader
            ImageLoader imageLoader = MyVolley.getImageLoader();
            imageLoader.get(imageUrls[position],
                    ImageLoader.getImageListener(holder.image,
                            R.drawable.ic_people,
                            R.drawable.ic_error));

            holder.netImage.setImageUrl(imageUrls[position], imageLoader);

			return view;
		}
	}

}