package com.oklab.oktwitter;
//package com.academy.ok;
//
//import android.app.*;
//import android.content.SharedPreferences;
//import android.os.*;
//import android.view.*;
//import android.widget.*;
//
////	new DownloadFileTask().execute();
//	public class DoTimeLineTask extends AsyncTask<String, Integer, Long>{
//		ProgressBar progressBar;
//		ListView mList;
//		
//		int a=0;
//		@Override
//		protected Long doInBackground(String... params) {
//			  long result = 0;
//			   int mode = Integer.valueOf(params[0] );
//			//Background Thread (��׶��忡�� �����)
////			for(int i=0; i<10;i++){
////				publishProgress(i);
////			}
//			   switch(mode){
//			   
//			   case 0://ȨŸ�Ӷ���
//				    mData = twitter_api.getHomeTimeline(0,0);
//				    break;
//			   case 1://���
//			    try {
//			    	tmpData = twitter_api.getMentionTimeline(0 , 0);
//			     } catch (URISyntaxException e) {
//			      e.printStackTrace();
//			     }
//			     break;
//			   case 2://���ο�Ʈ��
//				    since_id = Long.valueOf(params[1]);
//				    newData = twitter_api.getHomeTimeline(since_id, 0);
//				    mData.addAll(0,newData);
//				    break;
//			   case 3: //����Ʈ��
//				    since_id = Long.valueOf(params[1]);
//				    max_id = Long.valueOf(params[2]);
//				    oldData = twitter_api.getHomeTimeline(0,max_id);
//				    oldData.remove(0);
//					mData.addAll(oldData);
//					break;  
//			   };
//			
//			//Ʈ���� �Ľ�..
//			return result;
//		}
//
//
//		@Override
//		// �Ķ������ result�� ���� doInBackground���� ������ ���� return���� ����.
//		protected void onPostExecute(Long result) {
//			//Main Thread (���� �����)
//			//���׶� ProgressBar ���߰�.
//			//Ʈ���� setAdapter Ȥ�� notifyDataSetChanged
//			//progressBar.setVisibility(View.GONE);
//			
//			if(stay == 0){
//				progressBar.setVisibility(View.GONE);
//			    adapter = new TimelineAdapter(OKTwitterActivity.this,  mData);
//			    mList.setAdapter(adapter);
//			   } else if(stay == 1){
//				   progressBar.setVisibility(View.GONE);
//			    adapter = new TimelineAdapter(OKTwitterActivity.this,  tmpData);
//			    mList.setAdapter(adapter);
//			   } else if(stay == 2){
//				   progressBar.setVisibility(View.GONE);
//			    adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
//			    mList.setAdapter(adapter);
//			   } else if(stay == 3){
//				   progressBar.setVisibility(View.GONE);
//			    adapter = new TimelineAdapter(OKTwitterActivity.this , mData);
//			    mList.setAdapter(adapter);
//			   } else {
//				   progressBar.setVisibility(View.GONE);
//			   }
//		}
//
//		@Override
//		protected void onPreExecute() {
//			//execute�� ������ Thread - ���� Main Thread
//			//���׶� ProgressBar�� �����ְ�.
//			//��ٶ� ProgressBar�� 0���� ����.
//			progressBar.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			//Main Thread (���� �����)
//			//��ٶ� ProgressBar�� ä���ִµ� 
//		}
//		
//		
//}

