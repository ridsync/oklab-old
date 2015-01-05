package com.oklab.menutabs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oklab.R;
import com.oklab.menutabs.TabMenuScrollView.OnScrollListener;
import com.oklab.util.Logger;

public class GameTabView extends LinearLayout implements OnScrollListener , OnClickListener, OnTouchListener {

	private int currentGameGroup = 1;
    private int gameGroupCnt = 0;
    private int mScrollOffset = 0;
    
	private Context m_context = null;
	private TabMenuScrollView mTabScrollView;
	private LinearLayout mTabContainer;
	private ImageView mLeftArrow;
	private ImageView mRightArrow;
	
	public GameTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		initialize();
	}
	
	public GameTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		initialize();
	}
		
	public GameTabView(Context context) {
		super(context);
		m_context = context;
		initialize();
	}
	
	private void initialize()
	{
		LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 inflater.inflate(R.layout.game_tab_view, this, true);
		
		mTabScrollView = (TabMenuScrollView)findViewById(R.id.GAME_TAB_SCROLLVIEW);
		mTabContainer = (LinearLayout)findViewById(R.id.GAME_TAB_CONTAINER);

		if(mTabScrollView != null){
			mTabScrollView.setOnScrollListener(this);
			mTabScrollView.setOnTouchListener(this);
		}
		
		mLeftArrow = (ImageView)findViewById(R.id.GAME_TAB_LEFT_ARROW);	
		mLeftArrow.setOnClickListener(this);
		mRightArrow = (ImageView)findViewById(R.id.GAME_TAB_RIGHT_ARROW);
		mRightArrow.setOnClickListener(this);
		
	}

	public void setData(int gameGroupCnt)
	{
		this.gameGroupCnt = gameGroupCnt;
		if (this.gameGroupCnt <= 0) this.gameGroupCnt = 1;
		
		drawTabLayout(); // 1.초기세팅 2.게임탭클릭 3.한게임더 클릭 탭 추가
	}
	
	private void drawTabLayout() {
		if (mTabContainer == null) return;
		
		mTabContainer.removeAllViews();
		
		// 게임수 만큼 View 추가. click리스너 등록 Tag등록
		// GAME_TAB_ITEM
		for(int i= 0; i < gameGroupCnt; i++)
		{
			GameTab view = new GameTab(m_context);
			FrameLayout tab  = (FrameLayout) view.findViewById(R.id.FL_GAME_TAB_ITEM);
			tab.setTag( i + 1 );
			tab.findViewById(R.id.FL_GAME_TAB_ITEM).setOnClickListener(this);
//			tab.setLayoutParams(new LayoutParams(123, 430 ,1));  //TODO dimen 에서 적당히 정의해야함?
			tab.setSelected( getCurrentGameGroup() == (i+1) );
			view.setData( "Game" + (i + 1) );
			mTabContainer.addView(view, i);
		}	
		
		// 2. 좌우화살표 설정
		setVisiableArrow(measureDispWidth());
	}

	public int getCurrentGameGroup() {
		return currentGameGroup;
	}

	public void setCurrentGameGroup(int currentGameGroup) {
		this.currentGameGroup = currentGameGroup;
	}

	public int getGameGroupCnt() {
		return gameGroupCnt;
	}

	public void setGameGroupCnt(int gameGroupCnt) {
		this.gameGroupCnt = gameGroupCnt;
	}
	
	public void addOneMoreGame()
	{
		// 1. 한겜 더 추가한 다음엔 바로 오른쪽끝으로 스크롤뷰 이동하기
		gameGroupCnt++;
		drawTabLayout();
		setScrollPositionToRight();
		
		// 2. 게임 그룹데이터 갱신 콜백  // TODO
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
				case R.id.FL_GAME_TAB_ITEM:
					Log.d("onClick = v.getTag() = ", v.getTag() + "");
				// 1. Tag를 확인해서  setCurrentGameGroup ( getTag() ) 현재 게임그룹확인
					setCurrentGameGroup ( (Integer)v.getTag() );
					Log.d("getCurrentGameGroup = ", getCurrentGameGroup() +"" );
					drawTabLayout();
					
				// 2. 게임 그룹데이터 갱신 콜백  // TODO
					
				break;
				case R.id.GAME_TAB_LEFT_ARROW:
					// 1. 스크롤뷰 왼쪽 이동 
					mTabScrollView.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							mScrollOffset -= mLeftArrow.getWidth();
							if (mScrollOffset <= 0) mScrollOffset = 0;
							
							mTabScrollView.scrollTo(mScrollOffset, 0);
							
							setVisiableArrow(measureDispWidth());
						}
					}, 100);
					
					
				break;
				case R.id.GAME_TAB_RIGHT_ARROW:
					mTabScrollView.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							int arrWidth = mLeftArrow.getWidth() *2;
							int scrollViewWidth = measureDispWidth() - arrWidth;
							int tabContainerWidth = mTabContainer.getWidth(); // 고정값
							
							mScrollOffset += mLeftArrow.getWidth();
							if(mScrollOffset > 0 && mScrollOffset + scrollViewWidth > tabContainerWidth ){
								mScrollOffset -= mLeftArrow.getWidth();
							}
							
							mTabScrollView.scrollTo(mScrollOffset, 0);
							
							setVisiableArrow(measureDispWidth());
						}
					}, 100);
					
				break;
		}
	}

	public void setScrollPositionToRight() {
		mTabScrollView.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
					mTabScrollView.fullScroll(View.FOCUS_RIGHT);
//					int width = mTabContainer.getWidth();
//					mTabScrollView.setScrollX(width);
					
//					mTabScrollView.postDelayed(new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							mScrollOffset = mTabScrollView.getScrollX(); // ScrollOffSet 저장
//							Logger.d(this.getClass().getSimpleName() +  "setScrollPositionToRight() mScrollOffset = " + mScrollOffset);
//						}
//					}, 700);
			}
		}, 400); // 오른쪽 이동위해 딜레이 필요
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if ( event.getActionMasked() == MotionEvent.ACTION_OUTSIDE ) 
			Logger.d( "MotionEvent.ACTION_OUTSIDE called !!! ");
		
		Logger.d( "MotionEvent.ACTION_OUTSIDE called = " + event.getX() + " / "+ event.getY());
		
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_SCROLL
				|| event.getAction() == MotionEvent.ACTION_UP){
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void onScrollListener(int l, int t, int oldl, int oldt) {
		
		// 스크롤 위치 저장
		mScrollOffset = mTabScrollView.getScrollX();
		Logger.d(this.getClass().getSimpleName() +  "onScrollListener() mScrollOffset = " + mScrollOffset);
		Logger.d(this.getClass().getSimpleName() +  "onScrollListener() mScrollOffset : l = " + l + " t = " + t);
	}
	
	/**
	 * 게임수가 4개 이상이면 화살표 표시 !!
	 * 위치이동에 따른 화살표 좌우 표시 여부 계산 !!
	 * @param dispWidth
	 */
	private void setVisiableArrow(final int dispWidth){
		if(mTabContainer == null)
			return;
		mTabContainer.post(new Runnable()
			{
				@Override
				public void run()
				{
					int tabContainerWidth = mTabContainer.getWidth();
					int arrWidth = mLeftArrow.getWidth() *2;
					int scrollViewWidth = dispWidth - arrWidth;
					
					// scrollViewWidth보다 메뉴길이가 더 크면 offset에 포지션 따라 처리
					if (tabContainerWidth > scrollViewWidth){
							checkArrowVisibleAtScroll();
							Logger.d( "tabContainerWidth > scrollViewWidth = " + scrollViewWidth + " / tabContainerWidth = " + tabContainerWidth + " / arrWidth = " + arrWidth);
					}else {// scrollViewWidth 길이보다 메뉴레이아웃이 작으면 모두 INVISIABLE
						mRightArrow.setVisibility(View.GONE);
						mLeftArrow.setVisibility(View.GONE);
						Logger.d( "tabContainerWidth =< scrollViewWidth = " + scrollViewWidth + " / tabContainerWidth = " + tabContainerWidth + " / arrWidth = " + arrWidth);
					}
				}
		});
		
	}
	
	private void checkArrowVisibleAtScroll(){
		if(mTabScrollView == null)
			return;
		
		int arrWidth = mLeftArrow.getWidth() *2;
		int scrollViewWidth = measureDispWidth() - arrWidth;
		int tabContainerWidth = mTabContainer.getWidth(); // 고정값
		
		if (mScrollOffset == 0) {
			mLeftArrow.setVisibility(View.GONE);
			mRightArrow.setVisibility(View.VISIBLE);
		} else if(mScrollOffset > 0 && mScrollOffset + scrollViewWidth + 10 < tabContainerWidth ){ // 10 px 오차 있어서 더해줌.
			mLeftArrow.setVisibility(View.VISIBLE);
			mRightArrow.setVisibility(View.VISIBLE);
		} else if (mScrollOffset > 0 && mScrollOffset + scrollViewWidth + 10 >= tabContainerWidth ) {
			mLeftArrow.setVisibility(View.VISIBLE);
			mRightArrow.setVisibility(View.GONE);
		} 
		Logger.d( "checkArrowVisibleAtScroll : tabContainerWidth = " + tabContainerWidth  + " / scrollViewWidth = " +  scrollViewWidth+ " / mScrollOffset = " + mScrollOffset);
	}
	
	//  TODO 게임탭뷰 부모크기로 가져와야함.
	private int measureDispWidth(){
		WindowManager wm = (WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE);
		Display dp = wm.getDefaultDisplay();
		return dp.getWidth();
	}

	
}
