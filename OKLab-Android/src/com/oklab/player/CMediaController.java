package com.oklab.player;
//com.ehrd.app.android.resource.CMediaController

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.oklab.R;
import com.oklab.util.Logger;

public class CMediaController extends FrameLayout {
	public static int ID_PREV_BUTTON = R.id.prev;
	public static int ID_NEXT_BUTTON = R.id.next;
	public static int ID_PLAY_PAUSE_BUTTON = R.id.pause;
//	public static int ID_PREV_BUTTON;
	
	private CMediaPlayerControl  mPlayer;
	private Context             mContext;
	private View                mAnchor;
	private View                mRoot;
//	private WindowManager       mWindowManager;
//	private Window              mWindow;
//	private View                mDecor;
	private ProgressBar         mProgress;
	private TextView            mEndTime, mCurrentTime;
//	private boolean             mShowing;
	private boolean             mDragging;
	private static final int    sDefaultTimeout = 3000;
	private static final int    FADE_OUT = 1;
	private static final int    SHOW_PROGRESS = 2;
	private boolean             mUseFastForward;
	private boolean             mFromXml;
	private boolean             mListenersSet;
	private View.OnClickListener mNextListener, mPrevListener;
	StringBuilder               mFormatBuilder;
	Formatter                   mFormatter;
	private ImageButton         mPauseButton;
	private ImageButton         mFfwdButton;
	private ImageButton         mRewButton;
	private ImageButton         mNextButton;
	private ImageButton         mPrevButton;
	
	public CMediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mRoot = this;
		mContext = context;
		mUseFastForward = true;
		mFromXml = true;
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		if(mRoot!=null){
			initControllerView(mRoot);
		}
	}
	
	public void setMediaPlayer(CMediaPlayerControl player) {
		mPlayer = player;
		updatePausePlay();
	}
	
	public void setAnchorView(View view) {
		mAnchor = view;

		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT
		);

		removeAllViews();
		View v = makeControllerView();
		addView(v, frameParams);
	}

	protected View makeControllerView() {
		LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRoot = inflate.inflate(R.layout.media_controller, null);

		initControllerView(mRoot);
		return mRoot;
	}
	
	private void initControllerView(View v) {
		mPauseButton = (ImageButton) v.findViewById(ID_PLAY_PAUSE_BUTTON);
		if (mPauseButton != null) {
			mPauseButton.requestFocus();
			mPauseButton.setOnClickListener(mPauseListener);
		}

//		mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
//		if (mFfwdButton != null) {
//			mFfwdButton.setOnClickListener(mFfwdListener);
//			if (!mFromXml) {
//				mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
//			}
//		}
//		mRewButton = (ImageButton) v.findViewById(R.id.rew);
//		if (mRewButton != null) {
//			mRewButton.setOnClickListener(mRewListener);
//			if (!mFromXml) {
//				mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
//			}
//		}

		// By default these are hidden. They will be enabled when setPrevNextListeners() is called
		mNextButton = (ImageButton) v.findViewById(ID_NEXT_BUTTON);
		if (mNextButton != null && !mFromXml && !mListenersSet) {
			mNextButton.setVisibility(View.GONE);
		}
		mPrevButton = (ImageButton) v.findViewById(ID_PREV_BUTTON);
		if (mPrevButton != null && !mFromXml && !mListenersSet) {
			mPrevButton.setVisibility(View.GONE);
		}

		mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
		if (mProgress != null) {
			if (mProgress instanceof SeekBar) {
				SeekBar seeker = (SeekBar) mProgress;
				seeker.setOnSeekBarChangeListener(mSeekListener);
			}
			mProgress.setMax(1000);
		}
		
		mEndTime = (TextView) v.findViewById(R.id.time);
		mCurrentTime = (TextView) v.findViewById(R.id.time_current);
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

		installPrevNextListeners();
	}

	public void show() {
		show(sDefaultTimeout);
	}
	
	private void disableUnsupportedButtons() {
		try {
			if (mPauseButton != null && !mPlayer.canPause()) {
				mPauseButton.setEnabled(false);
			}

			if (mRewButton != null && !mPlayer.canSeekBackward()) {
				mRewButton.setEnabled(false);
			}

			if (mFfwdButton != null && !mPlayer.canSeekForward()) {
				mFfwdButton.setEnabled(false);
				}
			} catch (IncompatibleClassChangeError ex) {
				// We were given an old version of the interface, that doesn't have
				// the canPause/canSeekXYZ methods. This is OK, it just means we
				// assume the media can be paused and seeked, and so we don't disable
				// the buttons.
			}
	}
	
	public void show(int timeout) {
		if (!isShowing() ){//&& mAnchor != null) {
			setProgress();
			if (mPauseButton != null) {
				mPauseButton.requestFocus();
			}
			disableUnsupportedButtons();

//			int [] anchorpos = new int[2];
//			mAnchor.getLocationOnScreen(anchorpos);
//
//			WindowManager.LayoutParams p = new WindowManager.LayoutParams();
//			p.gravity = Gravity.TOP;
//			p.width = mAnchor.getWidth();
//			p.height = LayoutParams.WRAP_CONTENT;
//			p.x = 0;
//			p.y = anchorpos[1] + mAnchor.getHeight() - p.height;
//			p.format = PixelFormat.TRANSLUCENT;
//			p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//			p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//			p.token = null;
//			p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
//			mWindowManager.addView(mDecor, p);
//			mShowing = true;
			this.setVisibility(View.VISIBLE);
		}
		updatePausePlay();
		// cause the progress bar to be updated even if mShowing
		// was already true.  This happens, for example, if we're
		// paused with the progress bar showing the user hits play.
//		mHandler.sendEmptyMessage(SHOW_PROGRESS);

		Message msg = mHandler.obtainMessage(FADE_OUT);
		if (timeout != 0) {
			mHandler.removeMessages(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}
	
	public boolean isShowing() {
		return isShown();
	}
	
	public void hide() {
		if (mAnchor == null) return;

		if (isShowing()) {
			try {
//				mHandler.removeMessages(SHOW_PROGRESS);
//				mWindowManager.removeView(mDecor);
				this.setVisibility(View.INVISIBLE);
			} catch (IllegalArgumentException ex) {
				Logger.d("MediaController " + "already removed");
			}
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FADE_OUT:
				hide();
				break;
			case SHOW_PROGRESS:
				msg = obtainMessage(SHOW_PROGRESS);
				sendMessageDelayed(msg, 1000);
				
				if(mPlayer!=null){
					mPlayer.onUpdateSec();
					
					if(mPlayer.isPlaying()){
						if (!mDragging && isShowing() ) {
							setProgress();
						}
					}
				}
			break;
			}
		}
	};
	
	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours   = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		
		return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
	}
	
	private int setProgress() {
		if (mPlayer == null || mDragging) {
			return 0;
		}
		int position = mPlayer.getCurrentPosition();
		int duration = mPlayer.getDuration();
		if (mProgress != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = 1000L * position / duration;
				mProgress.setProgress( (int) pos);
			}
			int percent = mPlayer.getBufferPercentage();
			mProgress.setSecondaryProgress(percent * 10);
		}
		
		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mCurrentTime != null)
			mCurrentTime.setText(stringForTime(position));
		return position;
	}
	
	private View.OnClickListener mPauseListener = new View.OnClickListener() {
		public void onClick(View v) {
			doPauseResume();
			show(sDefaultTimeout);
		}
	};
	
	public void initPlay(){
		if(!mHandler.hasMessages(SHOW_PROGRESS)) mHandler.sendEmptyMessage(SHOW_PROGRESS);
	}
	
	private void updatePausePlay() {
		if (mRoot == null || mPauseButton == null)
			return;

		if (mPlayer.isPlaying()) {
			mPauseButton.setBackgroundResource(R.drawable.btn_pause);
		} else {
			mPauseButton.setBackgroundResource(R.drawable.btn_play);
		}
	}

	private void doPauseResume() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
			mHandler.removeMessages(SHOW_PROGRESS);
		} else {
			mPlayer.start();
			mHandler.removeMessages(SHOW_PROGRESS);
			mHandler.sendEmptyMessage(SHOW_PROGRESS);
		}
		
		updatePausePlay();
	}
	
	private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
		public void onStartTrackingTouch(SeekBar bar) {
			show(3600000);

			mDragging = true;

			// By removing these pending progress messages we make sure
			// that a) we won't update the progress while the user adjusts
			// the seekbar and b) once the user is done dragging the thumb
			// we will post one of these messages to the queue again and
			// this ensures that there will be exactly one message queued up.
			mHandler.removeMessages(SHOW_PROGRESS);
		}

		public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
			if (!fromuser) {
				// We're not interested in programmatically generated changes to
				// the progress bar's position.
				return;
			}

			long duration = mPlayer.getDuration();
			long newposition = (duration * progress) / 1000L;
			mPlayer.seekTo( (int) newposition);
			if (mCurrentTime != null)
				mCurrentTime.setText(stringForTime( (int) newposition));
		}

		public void onStopTrackingTouch(SeekBar bar) {
			mDragging = false;
			setProgress();
			updatePausePlay();
			show(sDefaultTimeout);
			
			// Ensure that progress is properly updated in the future,
			// the call to show() does not guarantee this because it is a
			// no-op if we are already showing.
			mHandler.sendEmptyMessage(SHOW_PROGRESS);
		}
	};

	View.OnClickListener mRewListener = new View.OnClickListener() {
		public void onClick(View v) {
			int pos = mPlayer.getCurrentPosition();
			pos -= 5000; // milliseconds
			mPlayer.seekTo(pos);
			setProgress();
			show(sDefaultTimeout);
		}
	};

	private View.OnClickListener mFfwdListener = new View.OnClickListener() {
		public void onClick(View v) {
			int pos = mPlayer.getCurrentPosition();
			pos += 15000; // milliseconds
			mPlayer.seekTo(pos);
			setProgress();
			
			show(sDefaultTimeout);
		}
	};
	
	public void setSeekBarEnable(boolean enable){
		if(mProgress!=null){
			mProgress.setEnabled(enable);
		}
		
		if(mRewButton!=null){
//			mRewButton.setEnabled(enable);
			mRewButton.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
		}
		
		if(mFfwdButton!=null){
//			mFfwdButton.setEnabled(enable);
			mFfwdButton.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
		}
	}

	private void installPrevNextListeners() {
		if (mNextButton != null) {
			mNextButton.setOnClickListener(mNextListener);
			mNextButton.setEnabled(mNextListener != null);
		}

		if (mPrevButton != null) {
			mPrevButton.setOnClickListener(mPrevListener);
			mPrevButton.setEnabled(mPrevListener != null);
		}
	}

	public void setPrevNextListeners(View.OnClickListener prev, View.OnClickListener next) {
		mNextListener = next;
		mPrevListener = prev;
		mListenersSet = true;
		
		if (mRoot != null) {
			installPrevNextListeners();

			if (mNextButton != null){// && !mFromXml) {
				mNextButton.setVisibility(mNextListener!=null ? View.VISIBLE : View.INVISIBLE);
			}
			
			if (mPrevButton != null){// && !mFromXml) {
				mPrevButton.setVisibility(mPrevListener!=null ? View.VISIBLE : View.INVISIBLE);
			}
		}
	}
	
	public void close(){
		mHandler.removeMessages(FADE_OUT);
		mHandler.removeMessages(SHOW_PROGRESS);
	}
	
	public interface CMediaPlayerControl extends MediaPlayerControl{
		/**
		 * player가 플레이되는동안 1초단위로 호출된다.
		 */
		void onUpdateSec();
		
	}
}
