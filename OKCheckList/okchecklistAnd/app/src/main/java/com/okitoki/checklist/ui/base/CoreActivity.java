package com.okitoki.checklist.ui.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by okc on 2015-06-21.
 */
public class CoreActivity extends AppCompatActivity {

    /***************************
     * Static Member Variable
     ***************************/

    /***************************
     * Logical Member Variable
     ***************************/

    /***************************
     * Android's Variable
     ***************************/
    protected Bundle mBundle;
    protected View mRootView;
    protected TabLayout tabLayout;
    protected Context mContext;

    /***************************
     * Android LifeCycle
     ***************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    // LG MENU Button Bug Fix
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_MENU) &&
                (Build.VERSION.SDK_INT == 16) &&
                (Build.MANUFACTURER.compareTo("LGE") == 0)) {
            if(getSupportActionBar()!=null)
                getSupportActionBar().openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected void sendScreenToGA(String screenName){
//        if(mTracker==null){
//            mTracker = ((OKCartApplication)(getApplication())).getDefaultTracker();
//        }
//        mTracker.setScreenName(screenName);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
