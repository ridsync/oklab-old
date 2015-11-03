package com.oklab.framework.activitymanager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.oklab.BaseActivity;
import com.oklab.R;
import com.oklab.oktwitter.LoginActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ojungwon on 2015-06-23.
 */
public class FullScreenModeTest extends BaseActivity{

        private static final String TAG = FullScreenModeTest.class.getSimpleName();
        private static final int LOCK_CHECK = 0;
        private static Thread th ;
        private Handler mHandler ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            Log.i(TAG, "onCreate() invoked");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.taskmgr);

            LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);

            EditText child = new EditText(this);
            child.setText("테스트");
            child.setInputType(InputType.TYPE_CLASS_PHONE);
            child.setTextColor(Color.RED);
            child.setTextSize(20);
            child.setBackgroundColor(Color.GREEN);
            child.setFocusable(true);
            child.requestFocus();

            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(child, params);

            Button child2 = new Button(this);
            child2.setText("다음화면");
            child2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent inte = new Intent(FullScreenModeTest.this,PickMediaTestActivity.class);
                    inte.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(inte);
                }
            });

            layout.addView(child2, params);

            // 처음에 키보드를 나오지 않게 해준다.
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		checkProcess();
//		appLockthread();
            getIsPlayingVideo(this);

            Button btn = (Button)findViewById(R.id.button1);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSystemBar();
                }
            });

            checkInstallDate();

            /**
             *
             */
            hideSystemBar();

            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {

                    System.out.println("focus  onSystemUiVisibilityChange " + visibility);

                    if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {

                        hideSystemBar();

                    }
                }

            });

        }

    public void onWindowFocusChanged(boolean hasFocus) {

        //  super.onWindowFocusChanged(hasFocus);
        System.out.println("focus : " +  hasFocus);
        if(hasFocus)
        {
            hideSystemBar();
        }
    }


    void hideSystemBar()

    {

        if(Build.VERSION.SDK_INT >= 19)
        {

            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN );

        }
        else
        {

            getWindow().getDecorView().setSystemUiVisibility(View.GONE); getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_FULLSCREEN |

                        View.SYSTEM_UI_FLAG_LOW_PROFILE |

                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );

        }

    }



    // 현재 화면에 보이고있는 엑티비티 클래스명 구하기
        public boolean getIsPlayingVideo(Context context){
            boolean isVideoPlaying = false;

            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = am.getRunningTasks(1);

            String topActvt = null;
            if (runningTaskInfo != null) {
                ActivityManager.RunningTaskInfo runInfo = runningTaskInfo.get(0);
                topActvt = runInfo.topActivity.toString();
            }


            if (topActvt!= null && topActvt.length() > 0 ){
                isVideoPlaying = topActvt.contains("TaskManagement");
                Log.d("oklab", "topActvt = " + topActvt + " / " + isVideoPlaying);
            }
            return isVideoPlaying;
        }

        @Override
        protected void onRestart() {
            // TODO Auto-generated method stub
            Log.i(TAG, "onRestart() ");
//		checkProcess();
            if (mHandler != null) {
                if ( mHandler.hasMessages(LOCK_CHECK) ){
                    mHandler.removeMessages(LOCK_CHECK);
                    Log.i(TAG, "removeMessages(LOCK_CHECK) ");
                }
            }
            super.onRestart();
        }
        @Override
        public void onBackPressed() {
            // TODO Auto-generated method stub
            super.onBackPressed();
        }
        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            Log.i(TAG, "onDestroy() ");
//		th.interrupt();
            super.onDestroy();
        }
        @Override
        public void onUserLeaveHint() {
            Log.i(TAG, "onUserLeaveHint() ");
            super.onUserLeaveHint();
//		appLockthread();
            goToLockScreen();
        }


        @Override
        public void onPause()
        {
            super.onPause();

	    /*
	     * 홈버튼을 눌렀을 때 byebye를 날린다
	     */
            if (!isForegrdound())
                Log.e(TAG, "onPause() : by Home Button Pressed?");
        }

        /**
         * 프로세스가 최상위로 실행중인지 검사한다.
         * @return true=최상위
         */
        public boolean isForegrdound()
        {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            ComponentName cn = list.get(0).topActivity;
            String name = cn.getPackageName();

            return name.indexOf(getPackageName()) > -1;
        }

        //	@Override
//	protected void onPause() {
//		Log.i(TAG, "onPause() ");
////	    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
////	    if (keyguardManager.inKeyguardRestrictedInputMode()) {
////	        // lock screen
////	    	startActivity(new Intent(TaskManagement.this,LoginActivity.class));
////	    } else {
////	        // lock screen 이 아님
////	    	Log.i(TAG, "lock screen 이 아님");
////	    }
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		if ( pm.isScreenOn() ){
//			Log.i(TAG, "스크린온상태");
//		}else{
//			Log.i(TAG, "스크린온 아님");
//			goToLockScreen();
//		}
//		super.onPause();
//	}
        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            Log.i(TAG, "onStart() ");
            super.onStart();
        }
        @Override
        public void onResume() {
            // TODO Auto-generated method stub
            Log.i(TAG, "onResume() ");
            super.onStart();
        }

        @Override
        public void onStop() {
            // TODO Auto-generated method stub
            Log.i(TAG, "onStop() ");
            super.onStop();
        }

        private void goToLockScreen(){
            Log.i(TAG, "goToLockScreen ()!!");
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case LOCK_CHECK :
                            Log.i(TAG, "goToLockScreenActivity ()!!");
                            startActivity(new Intent(FullScreenModeTest.this,LoginActivity.class));
                            break;
                    }
                }
            };
            mHandler.sendEmptyMessageDelayed(LOCK_CHECK, 5000);

        }

        private void checkProcess() {
            // 1. 최근테스크목록 비교
            Context mContext = getApplicationContext();
            // ActivityManager am =
            // (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
            // // context는 이왕이면 application의 context쓰기 2. 실행중인 태스크 목록 가져와서 현재 어플태스크와
            // 비교
            //
            // List<RunningTaskInfo> task = am.getRunningTasks(1);
            //
            // if(!task.isEmpty()) {
            //
            // ComponentName topActivity = task.get(0).topActivity; // 현재 태스크 정보
            // Log.i(TAG, "topActivity.getClassName = "+
            // topActivity.getClassName());
            // if(!topActivity.getPackageName().equals(mContext.getPackageName())) {
            // // 현재 태스크와 내 어플을 비교
            //
            // Log.i(TAG, "Background !!");
            // Intent inte = new Intent(TaskManagement.this, LoginActivity.class);
            // startActivity(inte);
            //
            // }else {
            // Log.i(TAG, "foreground !!");
            // }
            //
            // }
            ActivityManager mgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RecentTaskInfo> info = mgr.getRecentTasks(1,
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            if (info != null) {
                ActivityManager.RecentTaskInfo recent = info.get(0);
                Intent intent = recent.baseIntent;
                ComponentName cn = intent.getComponent();
                if (cn.getPackageName().compareTo("com.academy.coupling") == 0) {
                    Log.i(TAG,
                            "cmp=" + cn.getPackageName() + "/" + cn.getClassName());
                } else {
                    Log.i(TAG,
                            "else - cmp=" + cn.getPackageName() + "/"
                                    + cn.getClassName());
                    // Intent inte = new Intent(CouplingMain.this,
                    // EventHelper.class);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    // Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    // startActivity(inte);
                    Log.i(TAG, "else - FLAG_ACTIVITY_MULTIPLE_TASK");
                }

            }
            // 2. 프로세스확인 역시 쓰레드로확인
            // ActivityManager am = (ActivityManager)
            // getSystemService(Context.ACTIVITY_SERVICE);
            // List<ActivityManager.RunningAppProcessInfo>appList =
            // am.getRunningAppProcesses();
            // for (ActivityManager.RunningAppProcessInfo app : appList)
            // {
            // if ("com.academy.coupling".equals(app.processName))
            // {
            // // [Bug_052] [결함내용]: 스마트폰지킴이 비밀번호 설정한 상태에서 Home키로 나갔다가 스마트폰 지킴이 재 실행시
            // 바로 app 실행되는 문제
            // if ( app.importance ==
            // ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ) // 다른
            // 작업때문에 위해 resume를 기다리는 경우는 제외(ex. 패키지 인스톨러 호출시)
            // {
            // // 지킴이 어플 Process importance를 모니터 하여, 지킴이 어플 백그라운드 동작시 모든 Activity
            // 종료시키고 모든 서비스 재구동 하도록 처리
            // Log.i(TAG,
            // "com.academy.coupling goes with a IMPORTANCE_FOREGROUND !");
            // }
            // else {
            // Log.i(TAG,
            // "com.academy.coupling goes with a IMPORTANCE_Background !");
            // }
            // break;
            // }
        }
        // 3. 쓰레드
        private void appLockthread() {
            Log.i(TAG, "appLockthread() ");
            th = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.interrupted()) {
                        ActivityManager mgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RecentTaskInfo> info = mgr.getRecentTasks(1, Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (info != null) {
                            ActivityManager.RecentTaskInfo recent = info.get(0);
                            Intent intent = recent.baseIntent;
                            ComponentName cn = intent.getComponent();
                            if (cn.getPackageName().compareTo(getPackageName()) != 0) {
                                Log.i(TAG,"cmp=" + cn.getPackageName() + "/"
                                        + cn.getClassName());
                                startActivity(new Intent(FullScreenModeTest.this,LoginActivity.class));
                                Thread.interrupted();
                            }
                        }
                    }
                }
            });
            th.start();
        }

        public boolean checkInstallDate() {
            boolean result = false;
            // Android 2.3 이상에서 사용 가능한 방식.
            long installed;
            try {
                PackageManager packageManager = this.getPackageManager();
                installed = packageManager.getPackageInfo(getPackageName(), 0)
                        .firstInstallTime;
                long a = 0;
                //종료날짜와 시작날짜의 차를 구합니다.
                //두날짜간의 차를 얻으려면 getTimeMills()를 이용하여 천분의 1초 단위로 변환하여야 합니다.
                long b = (Calendar.getInstance().getTimeInMillis() - installed) /1000;

                a = b/(60*60*24);
                Date today = new Date (installed);
                System.out.println ( today );
                Log.d("Trial Check", "a = " + a);
//	        if (a >= 30){
                result = true;
//	        }else {
//	        	result = false;
//	    	}
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return result;
        }
        /**
         * Android 설치된 어플 정보리스트 가져오기
         */
        private void showShareAvailableApps() {
            final PackageManager packageManager = this.getApplicationContext().getPackageManager();

            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo info : list) {
                String appActivity      = info.activityInfo.name;
                String appPackageName   = info.activityInfo.packageName;
                String appName          = info.loadLabel(packageManager).toString();

                Drawable drawable = info.activityInfo.loadIcon(packageManager);

                Log.d(TAG, "appName : " + appName + ", appActivity : " + appActivity
                        + ", appPackageName : " + appPackageName);
            }
        }
}
