package com.oklab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oklab.events.pinchtozoom.PinchToZoomViewTestAct;
import com.oklab.framework.activitymanager.ActivityA;
import com.oklab.framework.activitymanager.SystemInfoAPI;
import com.oklab.framework.activitymanager.TaskManagement;
import com.oklab.framework.fragment.MainFragmentActivity;
import com.oklab.menutabs.GameTabViewActivity;
import com.oklab.myshoptest.ContentResolverNotherAppLauch;
import com.oklab.oktwitter.LoginActivity;
import com.oklab.oktwitter.OKTwitterActivity;
import com.oklab.player.LearningViewActivity;
import com.oklab.webview.shouldOverrideUrlLoadingOnWebView;

public class MainMenuActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}
	
	/**
	 * 1. Activity LifeCycle
	 * @param v
	 */
	public void startActivityLifeCycle(View v){
		Intent intent = new Intent(MainMenuActivity.this, ActivityA.class);
        startActivity(intent);
	}

	/**
	 * 2. Fragment LifeCycle
	 * => FrameWork !!
	 * @param v
	 */
	public void startActivityFragment(View v){
		Intent intent = new Intent(MainMenuActivity.this, MainFragmentActivity.class);
		startActivity(intent);
	}

	/**
	 * 1. OK Twitter
	 * @param v
	 */
	public void startActivityTwitter(View v){
		Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
	}
	/**
	 * 2. Touch Event & Gesture 
	 * =>
	 * @param v
	 */
	public void startActivityEvents(View v){
		
//		Intent intent = new Intent(MainMenuActivity.this, EventMoveOnSurfaceView.class);
//		Intent intent = new Intent(MainMenuActivity.this, GestureDetectorTest.class);
		Intent intent = new Intent(MainMenuActivity.this, PinchToZoomViewTestAct.class); // 이미지뷰 자체 줌,이동 테스트
//		Intent intent = new Intent(MainMenuActivity.this, TouchEventsTest.class); // 터치이벤트 테스트  
        startActivity(intent);
	}
	/**
	 * 3. VIEWS  (VIEWS패키지)
	 * => 이미지뷰 Scale 테스트 &  멀티 줌이미지뷰
	 * @param v
	 */
	public void startActivityViews(View v){
		
		Intent intent = new Intent(MainMenuActivity.this, GameTabViewActivity.class); // MyShop게임탭뷰
//		Intent intent = new Intent(MainMenuActivity.this, ImageViewScaleTestActivity.class);
//		Intent intent = new Intent(MainMenuActivity.this, ViewLogActivity.class); // View Log Actiity
        startActivity(intent);
	}
	/**
	 * 4. Activity Task 관리 등
	 * => FrameWork !!
	 * @param v
	 */
	public void startActivityManagment(View v){
		Intent intent = new Intent(MainMenuActivity.this, TaskManagement.class);

//		Intent intent = new Intent(MainMenuActivity.this, EncodingTest.class);
//		Intent intent = new Intent(MainMenuActivity.this, SystemInfoAPI.class);
//		Intent intent = new Intent(MainMenuActivity.this, PickMediaTestActivity.class);
//		Intent intent = new Intent(MainMenuActivity.this, ProgressBarCustomViewTest.class);
        startActivity(intent);
	}
	/**
	 * 4. WebView 
	 * => 
	 * @param v
	 */
	public void startActivityWebView(View v){
		Intent intent = new Intent(MainMenuActivity.this, shouldOverrideUrlLoadingOnWebView.class);
        startActivity(intent);
	}
	/**
	 * 5. Video Player
	 * @param v
	 */
	public void startActivityPlayer(View v){
		Intent intent = new Intent(MainMenuActivity.this, LearningViewActivity.class);
//		Intent intent = new Intent(MainMenuActivity.this, VideoPlayerActivity.class);
        startActivity(intent);
	}
	
	/**
	 * 5. ContentResolver Test + Other Activity Launch Test
	 *  + 커스텀 소프트키보드 테스트
	 * @param v
	 */
	public void startActivityCR(View v){
		Intent intent = new Intent(MainMenuActivity.this, ContentResolverNotherAppLauch.class);
//		Intent intent = new Intent(MainMenuActivity.this, EditTextKeyboardLangTest.class);
        startActivity(intent);
	}
	
}
