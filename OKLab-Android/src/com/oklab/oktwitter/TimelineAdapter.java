package com.oklab.oktwitter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oklab.R;

//xml �Ľ��ؼ� object ���ϴ� Ŭ���� 
public class TimelineAdapter extends ArrayAdapter<Status> {
	List<Status> objects;
	public TimelineAdapter(Context context, List<Status> objects) {
		super(context, 0, 0, objects);
		this.objects = objects;
	}

	public void addAll(Collection collection){
		objects.addAll(collection);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//1./ view ����� 
		View v;
		ViewHolder holder;
		
		//convertView�� ���ο��� ��� View
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.timeline, null);
			
			holder = new ViewHolder();
			holder.pImage = (ImageView)v.findViewById(R.id.profile_img);
			holder.screenName = (TextView)v.findViewById(R.id.twit_name);
			holder.createdAt = (TextView)v.findViewById(R.id.createdAt);
			holder.text = (TextView)v.findViewById(R.id.twit_text);
			
			v.setTag(holder);
		}else {
			v = convertView;
			holder = (ViewHolder)v.getTag();
		}
		//2. ���õ� �������� �Ѱ���
		Status data = getItem(position);
		
		//3. data�� �����Ͽ� View�� ����
		//holder�� ����Ͽ� �ݺ������� findViewById �۾��� ���ϰ� ����. 
		holder.pImage.setImageBitmap(data.user.profile_image_url);
		holder.screenName.setText(data.user.screen_name);
		holder.createdAt.setText(data.createdAt);
		holder.text.setText(data.text);
		
		return v;
	}

	static class ViewHolder{
		
		ImageView pImage;
		TextView screenName;
		TextView createdAt;
		TextView text;
		
	}

}
