package com.okitoki.checklist.ui.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by okc on 2015-06-21.
 */
public abstract class HolidayCoreActivity extends AppCompatActivity {

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
    protected TabLayout tabLayout;
    protected Context mContext;

    // [START define_database_reference]
    protected DatabaseReference mDatabase;
    // [END define_database_reference]

    protected RecyclerView mRecycler;
    protected LinearLayoutManager mManager;

    /***************************
     * Android LifeCycle
     ***************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);


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
