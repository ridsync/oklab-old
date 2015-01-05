package com.oklab.player.network;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.os.AsyncTask;

import com.oklab.R;
import com.oklab.player.model.Download;
import com.oklab.player.model.PlayInfo;
import com.oklab.util.Logger;

public class DownloadContentManager {
	public interface OnDownloadListener{
		public void onShowDialog();
		
		public void onProgressDialog(int per);
		
		public void onDismissDialog();
		
		public void onSucessDownload();
		
		public void onFailDownload(int msg);
	}
	
	private Context context;
	private OnDownloadListener listener;
	
	private int res_count = 0;
	private float curr_down_per = 0;
	
	private HashMap<Download, DownloadContentsHandler> map;
	
	public DownloadContentManager() {
		// TODO Auto-generated constructor stub
		this.map = new HashMap<Download, DownloadContentManager.DownloadContentsHandler>();
	}
	
	public void start(Context context, String type, Download dn, OnDownloadListener listener) {
		ArrayList<Download> downList = new ArrayList<Download>();
		downList.add(dn);
		
		start(context, type, downList, listener);
	}
	
	public void start(Context context, String type, ArrayList<Download> downList, OnDownloadListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listener = listener;
		
		this.res_count = downList.size();
		
		for(Download dn : downList){
			startDownload(type, dn);
		}
	}
	
	private Object sync_obj = new Object();
	
	private void startDownload(String type, Download dn){
		synchronized (sync_obj) {
			DownloadContentsHandler hanlder = new DownloadContentsHandler(type);
			
			map.put(dn, hanlder);
			
			hanlder.execute(dn);
		}
	}
	
	private void endDownload(Download dn){
		synchronized (sync_obj) {
			map.remove(dn);
			
			Log("Download File cntResource : " + map.size());
			
			if (map.size() == 0) {
				if(listener!=null){
					listener.onDismissDialog();
//					if(pDialog_down!=null && pDialog_down.isShowing()) dismissDialog(progress_bar_down);
					
					if(error_msg > 0){
						listener.onFailDownload(error_msg);
//						new AlertDialog.Builder(this)
//						.setTitle(R.string.app_name)
//						.setMessage(error_msg)
//						.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								finish();
//							}
//						})
//						.show();
					}else{
						listener.onSucessDownload();
					}
					
				}
			}
		}
	}
	
	public void cancelAll(){
		synchronized (sync_obj) {
			Set<Download> keySet = map.keySet();
			Iterator<Download> iter = keySet.iterator();
			
			while(iter.hasNext()){
				Download dn = iter.next();
				
				DownloadContentsHandler handler = map.remove(dn);
				handler.cancel(true);
			}
		}
	}

	int error_msg = 0;
	
	private synchronized void setErrorMsg(int msg){
		if(error_msg == 0){
			error_msg = msg;
		}
	}

	/**
	 * Background Async Task to download file
	 * */
	class DownloadContentsHandler extends
			AsyncTask<Download, Integer, Boolean> {
		private final String ReadWrite = "rw";
		
		private String type;
		private Download dn;
		
		public DownloadContentsHandler(String type) {
			// TODO Auto-generated constructor stub
			this.type = type;
		}
		
		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(listener!=null){
				listener.onShowDialog();
			}
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected Boolean doInBackground(Download... args) {
			dn = args[0];
			
			int count;
			boolean result = false;

			
			String urlstr = null;
			if(type.equals(PlayInfo.TYPE_VIDEO)){
				urlstr = dn.getUrlPath();
			}else{
				String name = dn.getFileNm().substring(dn.getFileNm().lastIndexOf("/")+1, dn.getFileNm().length());
				urlstr = dn.getUrlPath().substring(0, dn.getUrlPath().lastIndexOf("/")+1) + name;
			}
			
			String file_path = dn.getFileNm();
			
//			Log("Download file name="+name);
			Log("Download url="+urlstr);
			Log("Download file path="+file_path);
			
			InputStream input=null;
			RandomAccessFile output=null;
			try {
				File file = new File(file_path);
				if(!file.exists()){
					file.createNewFile();
				}
				
				long file_length = file.length();
				Log(">> file length (byte) = "+file_length);
				
				URL url = new URL(urlstr);
				URLConnection conection = url.openConnection();
				conection.setRequestProperty("Range", "bytes=" + String.valueOf(file_length) + '-');
				conection.connect();
				
				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				long remain = conection.getContentLength();
				float lenghtOfFile = file_length+remain;
				
				if(remain <= 0) return true;
				
				if(ContentsHandler.isFreeSpaceExternal((int) remain)){
					// download the file
//					input = new BufferedInputStream(url.openStream(), 8192);
					input = conection.getInputStream();
					
					// Output stream
//					output = new FileOutputStream(file_path);
					output = new RandomAccessFile(file, ReadWrite);
					output.seek(file_length);

					float byte_per = file_length / lenghtOfFile;
					float file_per = 100f / res_count;
					curr_down_per += (file_per*byte_per);
										
					byte data[] = new byte[1024];

					while ((count = input.read(data)) != -1) {
						// publishing the progress....
						// After this onProgressUpdate will be called
						byte_per = count / lenghtOfFile;
						file_per = 100f / res_count;
						curr_down_per += (file_per*byte_per);
						publishProgress((int) curr_down_per);

						// writing data to file
						output.write(data, 0, count);
					}

					// flushing output
//					output.flush();

					result = true;
				}else{
//					setErrorMsg(R.string.er_free_space_empty);
				}
			} catch (Exception e) {
				Logger.e("Error: " + e.getMessage());
				
//				setErrorMsg(R.string.er_content_download);
				result = false;
			} finally {
				// closing streams
				if(output!=null){
					try{
						output.close();
					}catch (Exception e) {
						// TODO: handle exception
						Logger.e("Error: " +e.getMessage());
					}
				}
				if(input!=null){
					try{
						input.close();
					}catch (Exception e) {
						// TODO: handle exception
						Logger.e("Error: " + e.getMessage());
					}
				}
			}
			return result;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(Integer... progress) {
			// setting progress percentage
			if(listener!=null){
				listener.onProgressDialog(progress[0]);
			}
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(Boolean result) {
			// dismiss the dialog after the file was downloaded
			onResultDown(result, dn);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
		}
	}
	
	private void onResultDown(Boolean result, Download dn){		
//		if (result) {
//			SmartWeSQLite.regDownInfo(context, dn);
//			Log("Download File Insert! : Key is " + dn);
//		}else{
//			if(dn.getFileNm()!=null) ContentsHandler.deleteDownFile(dn.getFileNm());
//		}

		endDownload(dn);
	}
	
	private void Log(String msg){
		Logger.d("EHRD " + getClass().getSimpleName() +" >> "+ msg);
	}
}
