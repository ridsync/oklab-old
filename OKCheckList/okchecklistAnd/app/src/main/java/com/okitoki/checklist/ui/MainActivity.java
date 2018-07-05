package com.okitoki.checklist.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.ui.components.NavigationDrawerCallbacks;
import com.okitoki.checklist.ui.fragment.MainCartFragment;
import com.okitoki.checklist.ui.fragment.MainCartListViewFragment;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.MarketVersionChecker;


public class MainActivity extends CoreActivity implements NavigationDrawerCallbacks {

    public static final String TAG = MainCartListViewFragment.class.getSimpleName();

    private Toolbar mToolbar;
    private RelativeLayout fbgV ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        fbgV = (RelativeLayout) findViewById(R.id.fadingBgActionBar);
        fbgV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO
//                currentFragment.fab_toggle.toggleOff();
//                setToolbarEnabled(true);
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MainCartFragment()).commit();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.cart);
        mToolbar.setNavigationIcon(R.mipmap.okcheck);

    }
    public void replaceFragment(Fragment fragment){

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final android.app.Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment).commit();

    }
    public void startActivityWithAnim(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.anim_webview_scale_in,R.anim.anim_scale_stay);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OKCartApplication.isShowFullAd_search = 1;
        OKCartApplication.isShowFullAd_search_2 = 1;
        OKCartApplication.martRestNearByClickCount = 0;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
//        if (mNavigationDrawerFragment.isDrawerOpen())
//            mNavigationDrawerFragment.closeDrawer();
//        else // TODO FABMenu Toggle Check

        final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if(currentFragment instanceof MainCartListViewFragment){
            MainCartListViewFragment mainFragment = ((MainCartListViewFragment)currentFragment);
            if (mainFragment.fab_toggle.isToggleOn()){
                mainFragment.fab_toggle.toggleOff();
                setToolbarEnabled(true);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    public void setToolbarEnabled(boolean isEnabled){
        if (isEnabled){
            fbgV.setVisibility(View.GONE);
        }else {
            fbgV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    Handler verHandler = new Handler();

    private void startVersionCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String store_version = MarketVersionChecker.getMarketVersion(getPackageName());
                    Logger.d("Version","[VerChecker] store_version = " + store_version);
                    String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    Logger.d("Version","[VerChecker] device_version = " + device_version);
                    if (store_version !=null && store_version.compareTo(device_version) > 0) {
                        // 업데이트 필요
                        Logger.d("Version","[VerChecker] Need App Update !!! ");
                        if(verHandler!=null)
                            verHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!isFinishing()){
                                        showDialogVerUpdater();
                                    }
                                }
                            });
                    } else {

                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void showDialogVerUpdater(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle(R.string.alert_title_ver);
        dialog.setMessage(R.string.dialog_msg_ask_update_app_version);
        dialog.setPositiveButton(R.string.btn_move, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse( "market://details?id=" + "com.okitoki.okcart");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
//        dialog.setNegativeButton(R.string.btn_later, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
