package com.okitoki.checklist.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.okitoki.checklist.BuildConfig;
import com.okitoki.checklist.R;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.utils.AUtil;

public class SplashActivity extends CoreActivity{

    public static final String TAG = SplashActivity.class.getSimpleName();

    Handler startHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(BuildConfig.DEBUG){
            ((TextView) findViewById(R.id.TV_SPLASH_VERSION)).setText("Debug." + AUtil.getVersion(getApplicationContext()));
        } else {
            ((TextView) findViewById(R.id.TV_SPLASH_VERSION)).setText("Ver." + AUtil.getVersion(getApplicationContext()));
        }


        findViewById(R.id.iv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHandler.removeCallbacks(mRunnable);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(startHandler!=null)
            startHandler.postDelayed(mRunnable,400);

    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isFinishing()){
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
