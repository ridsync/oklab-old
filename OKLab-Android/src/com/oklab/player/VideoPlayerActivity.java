package com.oklab.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.oklab.R;
import com.oklab.player.CMediaController.CMediaPlayerControl;
import com.oklab.player.model.PlayInfo;
import com.oklab.player.network.ContentsHandler;
import com.oklab.util.Logger;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback {
	  public static final int MODE_VIDEO_PLAYER = 202;

	public static final String EXTRA_TYPE = "extra_type";
	public static final String EXTRA_IS_FINISH = "extra_is_finish";
	public static final String EXTRA_PLAYINFO = "extra_playinfo";

	public static void start(Context context, Boolean isDown, PlayInfo data){
		if(data!=null){
			Intent i = new Intent(context, VideoPlayerActivity.class);
			data.isDown = isDown;
			i.putExtra(EXTRA_PLAYINFO, data);

			((Activity)context).startActivityForResult(i, MODE_VIDEO_PLAYER);
		}
	}
	
	private Context mContext;
	
	private SurfaceView mPreview;
	private SurfaceHolder mHolder;

	private MediaPlayer mPlayer;
	private CMediaController mController;
	
	private View mProgress;
	
	// 영상정보
	private PlayInfo playInfo = null;
	// 학습정보 종류
	private String type = null;
	// 학습시간
	private int playTime;
	// 총시간
	private int time;
	// 학습완료 시점의 학습시간
	private int currentTime;
	// 다운로드한 파일 재생여부
	private boolean isDown = false;
	// 비디오 재생 완료 여부
	private boolean isVideoEnd = false;
	// 플레이어액테비티의 onPause여부
	private boolean isPlayerStatePause = false;
	// 플레이어 탐색 가능 여부
	private boolean seekEnable = false;
	
	private final int Control_Visible_Time = 10*1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_video_player);
		
		this.mContext = this;
        playInfo = getIntent().getParcelableExtra(EXTRA_PLAYINFO);
        if (playInfo!= null){    
        	this.type = playInfo.type; 		// 학습(동영상 컨텐츠) 종류
        	this.isDown = playInfo.isDown;
        	this.playTime = playInfo.playTime;
//        	this.time = playInfo.time;
        	this.seekEnable = playInfo.seekEnable;// 동영상 탐색 가능여부
        	
        	initViews();
        	
        	setControlSetting();
        	
        	Log( playInfo.toString() );
        }else{
        	Log("No DATA Finish");
        	
        	finish();
        }

        mHandler = new Handler();
        
	}
	
	private Handler mHandler;
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			mHandler.postDelayed(mRunnable, 1000);
			cnt++;
			if (PlayInfo.TYPE_CONTENTS_CYBERLEARN.equals(type)){
				updateLearnInfosForTC(false);
			}else {
				updateLearnInfosForKC(false);
			}
			Logger.d("VD3Player updateLearnInfos mRunnable run (cnt:" + cnt +")");
		}
	};
	
	private void initViews() {
		mProgress = findViewById(R.id.progressbar);

		mPreview = (SurfaceView)findViewById(R.id.surfaceView1);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        mController = (CMediaController) findViewById(R.id.mediaController1);
        mController.setMediaPlayer(mPlayerControler);
        mController.setAnchorView(findViewById(R.id.mediaController1));		
	}

	private void setControlSetting(){
		mController.setSeekBarEnable(seekEnable);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(mController!=null){
				if(mController.isShowing()){
					mController.hide();
				}else{
					mController.show(Control_Visible_Time);
				}
			}
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		EagleTalkAppImpl.setIsPlayingVideo(true); // 동영상엑티비티 시작됨
		if(mPlayer!=null) mController.show(Control_Visible_Time);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mHandler.removeCallbacks(mRunnable);
		
		if(mPlayer!=null){
			try{
				playTime = mPlayer.getCurrentPosition() / 1000;
				mPlayer.pause();
				isPlayerStatePause = true;
			}catch (Exception e) {
				// TODO: handle exception
				LogE(e.getMessage());
			}
			
			Log( "mPlayer.pause - offset :" + playTime );
		}
		
//		if (PlayInfo.TYPE_CONTENTS_CYBERLEARN.equals(type)){
//			updateLearnInfosForTC(false);
//		}else {
//			updateLearnInfosForKC(false);
//		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log("VideoPlayer onDestroy");
		if(mController!=null) mController.close();
		
		if (mHandler!= null)	
			mHandler.removeCallbacks(mRunnable);
		
		if(mPlayer!=null){
            mPlayer.release();
        }	
		
//		PlayerPrepare.finish(); // 동영상 플레이어 준비클래스도 초기화
//		EagleTalkAppImpl.setIsPlayingVideo(false); // 동영상엑티비티 종료알림
	}
	
	@Override
	public void onBackPressed() {
		onBackPressed(true);
	}
	
	public void onBackPressed(boolean isReload) {
		// TODO Auto-generated method stub
		Log("VideoStart onBackPressed");
//		setResult(RESULT_OK, getResultData());
		if(mController!=null) mController.close();
		
		if (isReload){
			
			if (currentTime > playInfo.lastTime){ 
				isVideoEnd  = true;
				Logger.d("Study currentTime = " + currentTime + " / lastTime = " + playInfo.lastTime);
			}
			
			Intent data = new Intent();
			data.putExtra(EXTRA_TYPE, type);
			data.putExtra(EXTRA_IS_FINISH, isVideoEnd);
			data.putExtra(EXTRA_PLAYINFO, playInfo);
			setResult(RESULT_OK, data);
		}
			
		super.onBackPressed();		
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub		
		super.finish();
	}
	
	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		resizeSurfaceView();
	};
	
	private void resizeSurfaceView(){
		if(mPreview!=null && mPlayer!=null){
			int videoWidth = mPlayer.getVideoWidth();
	        int videoHeight = mPlayer.getVideoHeight();
	        Logger.d1("resizeSurfaceView", "video :" + videoWidth + "/" + videoHeight);
	        
	        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
	        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
	        Logger.d1("resizeSurfaceView", "screen : " +  screenWidth + "/" + screenHeight);
			
	        android.view.ViewGroup.LayoutParams lp = mPreview.getLayoutParams();
	        
	        //portrat
	        if(screenWidth < screenHeight) {
	            lp.width = screenWidth;
	            lp.height = (int)(((float)videoHeight / (float)videoWidth) * (float)screenWidth);
	        }
	        else {
	            lp.width = (int)(((float)videoWidth / (float)videoHeight) * (float)screenHeight);
	            lp.height = screenHeight;
	        }
	        Logger.d1("resizeSurfaceView", lp.width + "/" + lp.height);
	        mPreview.setLayoutParams(lp);
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log("surfaceCreated surfaceCreated");
		if(mPlayer==null){
			mPlayer=new MediaPlayer();
	        
	        mPlayer.setOnPreparedListener(preparedListener);
	        mPlayer.setOnCompletionListener(completListener);
	        mPlayer.setOnErrorListener(errorListener);
	        mPlayer.setOnVideoSizeChangedListener(sizeChangedListener);
	        
	        try{
	        	String path = playInfo.streamPath;
	        	
	        	String pathResource = null;
	        	if(isDown){
	        		String keys = ContentsHandler.getKeys(playInfo.grpCd, playInfo.lecCd, playInfo.lecSeq);
	        		
	        		pathResource = ContentsHandler.getTarget(keys) + "/resource"+path.substring(path.lastIndexOf("/"), path.length());
	        		
//	        		File file = new File(pathResource);
//	        		Log("== exists"+file.exists());
//	        		
//	        		mPlayer.setDataSource(new FileInputStream(file).getFD());
	        	}else{
	        		pathResource = path;
	        	}

        		mPlayer.setDataSource(pathResource);
	        	
	        }catch (Exception e) {
				// TODO: handle exception
	        	LogE(e.getMessage());
			}
        }
//		else{
//            mPlayer.reset();
//        }
		
        mPlayer.setDisplay(holder);
        mPlayer.setScreenOnWhilePlaying(true);
        
        // ※ player.prepare 시점에 마이너 오류발생 이슈 존재함 player Process주의
        new VideoAsync().execute(playInfo.streamPath);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log("surfaceChanged "+format+"width/height="+width+"/"+height);
		
		if(mPlayer!=null) {
			mPlayer.setDisplay(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log("surfaceDestroyed surfaceDestroyed");
		
	}
	
//	private int getPlayTime(){
//		int result = 0;
//		if(mPlayer!=null){
//			try{
//				int curr_sec = mPlayer.getCurrentPosition()/1000;
//				
//				if(curr_sec!=0){
//					result = curr_sec - start_sec;
//				}
//			}catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
	
	OnPreparedListener preparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			Log("onPrepared duration="+mp.getDuration());
			resizeSurfaceView();

			try{
				if (isPlayerStatePause){
					mPlayer.seekTo(playTime * 1000);
				}else {
					if ("Y".equals(playInfo.finishYn))
						mPlayer.seekTo(0);
					else
						mPlayer.seekTo(playTime * 1000);
				}
				
		        
				Log("onPrepared startTime="+mp.getCurrentPosition());
	            mPlayer.start();
	            mHandler.post(mRunnable);
	            
	            // 파일 총 시간을 저장한다.
            	 time = mPlayer.getDuration() / 1000;
            	 Log("mPlayer.getDuration() / time = " + time);
			}catch (Exception e) {
				// TODO: handle exception
				LogE(e.getMessage());
			}
			            
            mController.initPlay();
            mController.show(Control_Visible_Time);
		}
	};

	OnBufferingUpdateListener bufferingListener = new OnBufferingUpdateListener() {
		
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	OnVideoSizeChangedListener sizeChangedListener =new OnVideoSizeChangedListener() {
		
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			// TODO Auto-generated method stub
			Log("onVideoSizeChanged width/height="+width+"/"+height);
		}
	};
	
	OnErrorListener errorListener = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
//			what	the type of error that has occurred:
//				MEDIA_ERROR_UNKNOWN
//				MEDIA_ERROR_SERVER_DIED
//				
//			extra	an extra code, specific to the error. Typically implementation dependent.
//				MEDIA_ERROR_IO
//				MEDIA_ERROR_MALFORMED
//				MEDIA_ERROR_UNSUPPORTED
//				MEDIA_ERROR_TIMED_OUT
			
			int msg = 0;
			switch(what){
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				msg = R.string.er_video_play;
				break;
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				msg = R.string.er_video_play;
				break;
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				msg = R.string.er_video_play;
				break;
			}
			
			if(msg>0){
				showErrorDialog(msg);
			}
			
			return true;
		}
	};
	
	OnCompletionListener completListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			mPlayer.pause();
			mHandler.removeCallbacks(mRunnable);
			isVideoEnd = true;
			
			if (PlayInfo.TYPE_CONTENTS_CYBERLEARN.equals(type)){
				updateLearnInfosForTC(isVideoEnd);
			}else {
				updateLearnInfosForKC(isVideoEnd);
			}
			
			Intent data = new Intent();
			data.putExtra(EXTRA_TYPE, type);
			data.putExtra(EXTRA_IS_FINISH, isVideoEnd);
			data.putExtra(EXTRA_PLAYINFO, playInfo);
			
			setResult(RESULT_OK , data);
			finish();
		}
	};
	
	private void showErrorDialog(int msg){
		new AlertDialog.Builder(mContext)
		.setTitle(R.string.app_name)
		.setMessage(msg)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				onBackPressed(false);
			}
		}).show();
	}
		
	
	private void showProgress(){
		if(mProgress!=null && mProgress.getVisibility() != View.VISIBLE){
			mProgress.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideProgress(){
		if(mProgress!=null && mProgress.getVisibility() == View.VISIBLE){
			mProgress.setVisibility(View.INVISIBLE);
		}
	}
	
	CMediaPlayerControl mPlayerControler = new CMediaPlayerControl() {
		
		@Override
		public void start() {
			// TODO Auto-generated method stub
			if(mPlayer!=null){
				try{
					mPlayer.start();
					mHandler.post(mRunnable);
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
		}
		
		@Override
		public void seekTo(int pos) {
			// TODO Auto-generated method stub
			if(mPlayer!=null){
				try{
					mPlayer.seekTo(pos);
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
		}
		
		@Override
		public void pause() {
			// TODO Auto-generated method stub
			if(mPlayer!=null){
				try{
					mPlayer.pause();
					mHandler.removeCallbacks(mRunnable);
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
		}
		
		@Override
		public boolean isPlaying() {
			// TODO Auto-generated method stub
			boolean result = false;
			if(mPlayer!=null) {
				try{
					result = mPlayer.isPlaying();
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
			return result;
		}
		
		@Override
		public int getDuration() {
			// TODO Auto-generated method stub
			int result = 0;
			if(mPlayer!=null){
				try{
					result = mPlayer.getDuration();
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
			return result;
		}
		
		@Override
		public int getCurrentPosition() {
			// TODO Auto-generated method stub
			int result = 0;
			if(mPlayer!=null){
				try{
					result = mPlayer.getCurrentPosition();
				}catch (Exception e) {
					// TODO: handle exception
		        	LogE(e.getMessage());
				}
			}
			return result;
		}
		
		@Override
		public void onUpdateSec() {
			if(mPlayer!=null) {
			}
		};
		
		@Override
		public int getBufferPercentage() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public boolean canSeekForward() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean canSeekBackward() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean canPause() {
			// TODO Auto-generated method stub
			return true;
		}
	};
	
	
	class VideoAsync extends AsyncTask<String, Integer, Boolean>{
		
		@Override
		protected void onPreExecute() {
			showProgress();
		};
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean result = false;
			if(mPlayer!=null && params[0]!=null){
				try{
		            mPlayer.prepare();
		            
		            result = true;
		        }catch(Exception e){
		        	LogE(e.getMessage());
		        }
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			hideProgress();
			
			if(result){
				
			}
		};
	}
	
	private int cnt; // TODO
	
	/**
	 * 학습 진도 전송 (사이버학습 팀카페)<br>
	 * DB Update or Server Update 
	 */
	public void updateLearnInfosForTC(boolean complete) {
		
		// 학습완료된 영상은 진도전송 안함.
		if ("Y".equals(playInfo.finishYn)) {
			return;
		}
		
		try {
//			
//			// Log.v("dddd", "Cnt : " + Cnt);
//			sqlsend = new SmartWeSQLite(this);
//			ContentValues values = new ContentValues();
//			
//			if (complete)
//				currentTime = time;
//			else
//				currentTime = mPlayer.getCurrentPosition() / 1000;
//			
//			if (currentTime > 1) {
//				long uCnt = 0;
//				StringBuffer sb = new StringBuffer();
//				sb.append("lecCd ='" + playInfo.lecCd + "'");
//				sb.append(" and lecSeq ='" + playInfo.lecSeq + "'");
//				sb.append(" and grpCd ='" + playInfo.grpCd + "'");
//				sb.append(" and userid ='" + playInfo.userId + "'");
//				sb.append(" and contentpoolNo ='" + playInfo.contentpoolNo + "'");
//
//				String where = sb.toString();
//				
//				values.put(SmartWeNaming.mobilePlaySecond, String.valueOf(currentTime));
//				values.put(SmartWeNaming.attendCnt, Integer.toString(cnt));
//				
//				uCnt = sqlsend.update(SmartWeSQLite.TABLE_SEND_CYBERLEARN, values,
//						where, null);
//				
//				if (uCnt == 0) {
//					values.put(SmartWeNaming.lecCd, playInfo.lecCd);
//					values.put(SmartWeNaming.lecSeq, playInfo.lecSeq);
//					values.put(SmartWeNaming.grpCd, playInfo.grpCd);
//					values.put(SmartWeNaming.userId, playInfo.userId);
//					values.put(SmartWeNaming.contentpoolNo,playInfo.contentpoolNo);
//					sqlsend.insert(SmartWeSQLite.TABLE_SEND_CYBERLEARN, values);
//				}
//			}
//			
////			if (serverUpdate 
////					&& SmartWeHttpUtil.checkNetwork( VideoPlayerActivity.this, false)){
////				
////				new Thread(new Runnable() {
////					@Override
////					public void run() {
////						StudyProgressNet.StudyProgress(VideoPlayerActivity.this);
////					}
////				}).start();
//				
////			}
//			
		}
		catch (Exception e) {
			e.getMessage();
		}
		finally {
//			sqlsend.close();
		}
	}
	
	/**
	 * 학습 진도 전송 (지식채널)<br>
	 * DB Update or Server Update 
	 */
	public void updateLearnInfosForKC(boolean complete) {
		
		// 학습완료된 영상은 진도전송 안함.
		if ("Y".equals(playInfo.finishYn)) {
			return;
		}
				
		try {
			
			// Log.v("dddd", "Cnt : " + Cnt);
//			sqlsend = new SmartWeSQLite(this);
//			ContentValues values = new ContentValues();
//			
//			if (complete)
//				currentTime = time;
//			else
//				currentTime = mPlayer.getCurrentPosition() / 1000;
//			
//			if (currentTime > 1) {
//				long uCnt = 0;
//				StringBuffer sb = new StringBuffer();
//				sb.append("sgrpCd ='" + playInfo.sgrpCd + "'");
//				sb.append(" and cosCd ='" + playInfo.cosCd + "'");
//				sb.append(" and userId ='" + playInfo.userId + "'");
//				sb.append(" and contents_idx ='" + playInfo.idx + "'");
//
//				String where = sb.toString();
//				
//				values.put(SmartWeNaming.mobilePlaySecond, String.valueOf(currentTime));
//				values.put(SmartWeNaming.studyTime, Integer.toString(cnt));
//				
//				uCnt = sqlsend.update(SmartWeSQLite.TABLE_SEND_KNOWCH, values,
//						where, null);
//				
//				if (uCnt == 0) {
//					values.put(SmartWeNaming.sgrpCd, playInfo.sgrpCd);
//					values.put(SmartWeNaming.cosCd, playInfo.cosCd);
//					values.put(SmartWeNaming.userId, playInfo.userId);
//					values.put(SmartWeNaming.contents_idx,playInfo.idx);
//					sqlsend.insert(SmartWeSQLite.TABLE_SEND_KNOWCH, values);
//				}
//			}
			
//			if (serverUpdate 
//					&& SmartWeHttpUtil.checkNetwork( VideoPlayerActivity.this, false)){
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						StudyProgressNet.StudyProgressKnowch(VideoPlayerActivity.this);
//					}
//				}).start();
//			}
			
		}
		catch (Exception e) {
			e.getMessage();
		}
		finally {
//			sqlsend.close();
		}
	}
	
	private void Log(String msg){
		Logger.d1(getApplicationInfo().name, getClass().getSimpleName()+" "+msg);
	}
	
	private void LogE(String msg){
		Logger.e1(getApplicationInfo().name, getClass().getSimpleName()+" "+msg);
	}
	
}
