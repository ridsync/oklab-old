package com.oklab.graphics;

import android.os.Bundle;

import com.oklab.BaseActivity;

/**
 * Created by ojungwon on 2015-01-08.
 */
public class CanvasTestActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GradiationView mainView =new GradiationView(this);
        MainView mainView =new MainView(this);
        setContentView(mainView);

    }


}
