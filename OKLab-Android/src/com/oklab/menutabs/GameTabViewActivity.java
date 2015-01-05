package com.oklab.menutabs;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.oklab.BaseActivity;
import com.oklab.listview.OrderInfoView;

public class GameTabViewActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GameTabView gtv = new GameTabView(this);
		int gameGroupCnt = 20;
		gtv.setData(gameGroupCnt);
		
		OrderInfoView oiv = new OrderInfoView(this);
		
		setContentView( gtv );
		
		gtv.setScrollPositionToRight();
		// for each 문  참조 변수 변경 테스트 ----
		ArrayList<DOdr> gameOrderList = new ArrayList<DOdr>();
		ArrayList<DOdr> orderList2 = new ArrayList<DOdr>();
		
		
		for (int i = 0; i <  5 ; i++) {
			gameOrderList.add( DOdr.createMainOdr(new DPdt(), i, i , i, i) );
		}
		
		int index = 0;
		for (DOdr odr : gameOrderList) {
			odr.set_game_num( index );
			odr.set_game_type( index + 10 );
			index++;
			orderList2.add(odr) ;
		}
		
		for (DOdr odr : gameOrderList) {
			Log.d("Result 1 = ", odr.get_game_num() + " / "+ odr.get_game_type());
		}
		
		for (int i = 0; i >  gameOrderList.size() ; i++) {
			gameOrderList.get(i).set_game_num( i+1 );
			gameOrderList.get(i).set_game_type( i + 20 );
		}
		
		for (DOdr odr : gameOrderList) {
			Log.d("Result 2 = ", odr.get_game_num() + " / "+ odr.get_game_type());
		}
		
		for (DOdr odr : orderList2) {
			odr.set_game_num( 100 );
			odr.set_game_type( 20 );
			Log.d("copy orderList = ", odr.get_game_num() + " / "+ odr.get_game_type());
		}
		
		for (DOdr odr : gameOrderList) {
			Log.d("copy orderList 2 = ", odr.get_game_num() + " / "+ odr.get_game_type());
		}
	}
	
	
}
