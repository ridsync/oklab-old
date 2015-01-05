package com.oklab.oktwitter;

import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oklab.R;

public class OKTwitterActivity extends Activity {

	 ListView mList;
	 ArrayList<Status> mData;
	 ArrayList<Status> tmpData;
	 TimelineAdapter adapter;
	 User user;
	 Button tB_newTwit;
	 Button tB_timeline;
	 Button tB_mention;
	 Button tB_msg;
	 View twit_btns_layout;
	 Button bB_latest;
	 Button bB_ago;
	 View send_btns_layout;
	 Button bB_send;
	 Button bB_cancel;
	 EditText newTwEdit;
	 Boolean visuable = false;
	 
	 TwitterApi twitter_api;
	 String token;
	 String tokenSecret;
	 
	 ProgressBar probar;
	 long since_id;
	 long max_id;
	 String value;
	 
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       // �˶� ��������
       alarmCancel();
       // ��Ÿ �� �� ������ �ʱ�ȭ 
       setContentView(R.layout.activity_twitter);
       mData = new ArrayList<Status>();
       tmpData = new ArrayList<Status>();
       mList = (ListView)findViewById(R.id.list);
       twit_btns_layout = (View)findViewById(R.id.twit_layout);
       send_btns_layout = (View)findViewById(R.id.send_layout);
       newTwEdit = (EditText)findViewById(R.id.newTwEdit);
       probar = (ProgressBar) findViewById(R.id.progress);
       
       //1. ��ܺ��� MyID�� MyImage����
       Intent i = getIntent();
       user = new User();
       user.screen_name = (String)i.getSerializableExtra("screen_name");
       user.profile_image_url = (Bitmap)i.getParcelableExtra("profile_image_url");
       Log.d("myID","Screen_name : " + user.screen_name);
       Log.d("myImg","profile_image_url : " + user.profile_image_url);
       TextView myId = (TextView)findViewById(R.id.MyId);
       myId.setText(user.screen_name);
       
       ImageView myImage = (ImageView)findViewById(R.id.MyImage);
       myImage.setImageBitmap(user.profile_image_url);
       
       //2. HomeTimeLine�� ������ mData�ϼ��Ͽ�  �߾� HomeTimeLine����Ʈ��(mList)�� �����ֱ�
       //Ʈ����Api SharedPreferences�� ����� token�� �̿��� Ŭ���� �ʱ�ȭ
       SharedPreferences pref = getSharedPreferences("twitt", 0);
	   	token = pref.getString("token", null);
	   	tokenSecret = pref.getString("tokenSecret", null);
	   	twitter_api = new TwitterApi(token, tokenSecret);
	   	// AsyncTask�̿��� ������� 'get_HomeTimeLine()'�޼ҵ��� �۾��� ����
	   	new DownloadFileTask().execute("0","0","0");
       //get_HomeTimeLine();
 
       //3. Button ���� ����
       
       // 1)ȭ�鿡 homeTileline �����ֱ� - ��ܹ�ư 
       tB_timeline = (Button) findViewById(R.id.tB_time);
       tB_timeline.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// AsyncTask�̿��� ������� 'getNew_HomeTimeLine()'�޼ҵ��� �۾��� ����
			tmpData = new ArrayList<Status>();
			value = String.valueOf(mData.get(0).id);
			new DownloadFileTask().execute("2",value,"0");
			//getNew_HomeTimeLine();
		}
	});
       // 2)ȭ�鿡 ��� �����ֱ� - ��ܹ�ư 
       tB_mention = (Button)findViewById(R.id.tB_ment);
       tB_mention.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			tmpData = new ArrayList<Status>();
			new DownloadFileTask().execute("1","0","0");
			
//			ArrayList<Status> tmpData = new ArrayList<Status>();
//	
//			try {
//				tmpData = twitter_api.getMentionTimeLine(0 , 0);
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//			
//			adapter = new TimelineAdapter(OKTwitterActivity.this,  tmpData);
//			mList.setAdapter(adapter);

		}
	});
       
       // 3) ���ο� Ʈ�� �ۼ��ϱ� - ��ܹ�ư

       tB_newTwit = (Button) findViewById(R.id.tB_newTwit);
       tB_newTwit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(visuable == false){
					twit_btns_layout.setVisibility(View.GONE);
					send_btns_layout.setVisibility(View.VISIBLE);
					newTwEdit.setVisibility(View.VISIBLE);
					newTwEdit.setText("");
				}
				visuable = true;
				
			}
		});
       
       // 4)ȭ�鿡 ���ο�Ʈ�� �����ֱ� - �ϴܹ�ư
       bB_latest = (Button) findViewById(R.id.bB_latest);
       bB_latest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tmpData = new ArrayList<Status>();
				value = String.valueOf(mData.get(0).id);
				new DownloadFileTask().execute("2",value,"0");
				//getNew_HomeTimeLine();
				
			}
		});
       
       // 5)ȭ�鿡 ����Ʈ�� �����ֱ� - �ϴܹ�ư 
       bB_ago = (Button) findViewById(R.id.bB_ago);
       bB_ago.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tmpData = new ArrayList<Status>();
				value = String.valueOf(mData.get(mData.size()-1).id);
				new DownloadFileTask().execute("3","0",value);

//				ArrayList<Status> oldData = new ArrayList<Status>();
//				
//				try {
//					oldData = twitter_api.getHomeTimeLine(0, mData.get(mData.size()-1).id);
//				} catch (XmlPullParserException e) {
//					e.printStackTrace();
//				}
//				oldData.remove(0);
//				mData.addAll(oldData);
//				
//				adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
//				mList.setAdapter(adapter);		
//				adapter.notifyDataSetChanged();
			}
		});
		
	    // 6) Ʈ�� ����ϱ� - �ϴܹ�ư
	       
       bB_send = (Button) findViewById(R.id.bB_send);
       bB_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String status = newTwEdit.getText().toString();
				if(twitter_api.statusUpdate(status) == true){
					Toast.makeText(v.getContext(), "Ʈ�� ��� �Ϸ�", Toast.LENGTH_LONG).show();
					if(visuable == true){
						newTwEdit.setVisibility(View.GONE);
						send_btns_layout.setVisibility(View.GONE);
						twit_btns_layout.setVisibility(View.VISIBLE);
						hide_SoftKeyboard(); //Ű���� ����
						getNew_HomeTimeLine(); //�� Ʈ�� ����
					}
					visuable = false;
				}else{
					hide_SoftKeyboard();
					Toast.makeText(v.getContext(), "Ʈ�� ��� ����", Toast.LENGTH_LONG).show();
					
				}				
				
			}
		});      
       
	    // 7) Ʈ����� ����ϱ� - �ϴܹ�ư
       bB_cancel = (Button) findViewById(R.id.bB_cancel);
       bB_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide_SoftKeyboard();
				send_btns_layout.setVisibility(View.GONE);
				twit_btns_layout.setVisibility(View.VISIBLE);
				visuable = false;				
			}
		});
       // Button ���� ��

		
   }

   
   public void get_HomeTimeLine(){
      
//	    HomeTimeLine ����Ʈ�� ������ mData���� ���  3���� 
//        #(1)buildData()�� ���Ƿ� ���� �����͸�  mData�� ����
//       mData = buildData();
//       
//        #(2)public_timeline.xml ������ data�� Parsing�Ͽ�  mData�� ����
//       Parser parser = new Parser();
//	    
//	   	try {
//			InputStream is = getResources().openRawResource(R.raw.public_timeline);
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			XmlPullParser xpp = factory.newPullParser();
//			
//			xpp.setInput(is, "UTF-8");
//			
//			mData = parser.parseTimeLine(xpp);
//
//		}catch(XmlPullParserException e) {
//			Log.e("TA", "XmlPullParserException : "+ e.getMessage());
//				e.printStackTrace();
//		}catch(IOException e) {
//			Log.e("TA", "IOException : "+ e.getMessage());
//			e.printStackTrace();
//		}
		
		//#(3)TwitterApi�� ���� �����κ��� xml������ �ٿ�޾� Parsing�� data�� mData�� ����

       //����Ǿ��ִ� ��ū �ҷ�����
	SharedPreferences pref = getSharedPreferences("twitt", 0);
	token = pref.getString("token", null);
	tokenSecret = pref.getString("tokenSecret", null);
	twitter_api = new TwitterApi(token, tokenSecret);
	mData = twitter_api.getHomeTimeLine(0, 0);
		
	if(adapter==null){
		adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
	}else{
		adapter.notifyDataSetChanged();
	}
		
       // ListView�� Adapter ����
       //adapter = new TimelineAdapter(this, mData);
       mList.setAdapter(adapter);
   }
   
   // ���ο� Ʈ�� ���� �޼ҵ�
   public void getNew_HomeTimeLine() {
		ArrayList<Status> newData = new ArrayList<Status>();
		
		newData = twitter_api.getHomeTimeLine(mData.get(0).id, 0);
		mData.addAll(0,newData);
		adapter = new TimelineAdapter(OKTwitterActivity.this , mData);		
		mList.setAdapter(adapter);
   }
   
   //����ƮŰ���� ���� �޼ҵ�
   public void hide_SoftKeyboard(){
	   InputMethodManager imm = (InputMethodManager)getSystemService(OKTwitterActivity.INPUT_METHOD_SERVICE); 
	   imm.hideSoftInputFromWindow(newTwEdit.getWindowToken(), 0);
   }
   

   //�˶� ���� ���� �޼ҵ�
	void alarmCancel(){
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this,PollingService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		am.cancel(pi);
	}
	
	//�˶� ���� ���� (��Ƽ��Ƽ �����)
	@Override
	protected void onDestroy() {
	   super.onDestroy();
	   	// ������ Ʈ��id SharePreference�� ����
		SharedPreferences pref = getSharedPreferences("twitt", 0);
		SharedPreferences.Editor edit = pref.edit();
		
		edit.putLong("last_id", mData.get(0).id);
		edit.commit();
	    // �˶�����
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this,PollingService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
		
   }
	
	//AsyncTask ������ �۾� Ŭ����
	private class DownloadFileTask extends AsyncTask<String, Integer, Long>{
		
		//execute�� ������ Thread���� ���� ����
		@Override
		protected void onPreExecute() {
			//���׶� ProgressBar�� �����ְ�.
			//��ٶ� ProgressBar�� 0���� ����.
			probar.setVisibility(View.VISIBLE);

		}
		
		//�������� Thread �۾�����
		@Override
		protected Long doInBackground(String... params) {
			//Background Thread (��׶��忡�� �����)
			long result = 0;

//			for(int i=0; i<10;i++){
//				publishProgress(i);
//			}
			int mode = Integer.valueOf(params[0] );
			
			switch(mode){
			case 0: //home
				since_id = Long.valueOf(params[1]);
				max_id = Long.valueOf(params[2]);
				Log.d("Twitter", "sinc_id/max_id : " +since_id + "/" + max_id);
				mData = twitter_api.getHomeTimeLine(since_id, max_id);
				result = 0L;
				break;
			case 1: //mention
				since_id = Long.valueOf(params[1]);
				max_id = Long.valueOf(params[2]);
				try {
					tmpData = twitter_api.getMentionTimeLine(since_id , max_id);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				result = 1L;
				break;
			case 2: //new
				since_id = Long.valueOf(params[1]);
				max_id = Long.valueOf(params[2]);
				tmpData = twitter_api.getHomeTimeLine(since_id, max_id);
				Log.d("OK", "tmpData.size() : "+tmpData.size());
				
				mData.addAll(0,tmpData);
				result = 2L;
				break;
			case 3: //old
				since_id = Long.valueOf(params[1]);
				max_id = Long.valueOf(params[2]);
				tmpData = twitter_api.getHomeTimeLine(since_id,max_id);
				
				tmpData.remove(0);
				mData.addAll(tmpData);
				result = 3L;	
				break;
				
			};
						
			return result;
		}


		@Override
		// �Ķ������ result�� ���� doInBackground���� ������ ���� return���� ����.
		protected void onPostExecute(Long result) {
			//Main Thread (���� �����)
			//���׶� ProgressBar ���߰�.
			//Ʈ���� setAdapter Ȥ�� notifyDataSetChanged
			if(result == 0L){ //home
				probar.setVisibility(View.GONE);
				adapter = new TimelineAdapter(OKTwitterActivity.this,  mData);
				mList.setAdapter(adapter);
			} else if(result == 1L){ //mention
				probar.setVisibility(View.GONE);
				adapter = new TimelineAdapter(OKTwitterActivity.this,  tmpData);
				mList.setAdapter(adapter);
			} else if(result == 2L){ //new
				probar.setVisibility(View.GONE);
				adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
				mList.setAdapter(adapter);
			} else if(result == 3L){ //old
				probar.setVisibility(View.GONE);
//				if(adapter==null){
//				adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
//				}else{
//				}
				adapter.notifyDataSetChanged();
				Log.d("OK", "3L : notifyDataSetChanged() !!");

				mList.setAdapter(adapter);
				
			} else {
				probar.setVisibility(View.GONE);
			}
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			//Main Thread (���� �����)
			//��ٶ� ProgressBar�� ä����
		}
		
		
	}
	
	
}
