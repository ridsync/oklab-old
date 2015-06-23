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
            child.setText("�׽�Ʈ");
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
            child2.setText("����ȭ��");
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

            // ó���� Ű���带 ������ �ʰ� ���ش�.
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



    // ���� ȭ�鿡 ���̰��ִ� ��Ƽ��Ƽ Ŭ������ ���ϱ�
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
	     * Ȩ��ư�� ������ �� byebye�� ������
	     */
            if (!isForegrdound())
                Log.e(TAG, "onPause() : by Home Button Pressed?");
        }

        /**
         * ���μ����� �ֻ����� ���������� �˻��Ѵ�.
         * @return true=�ֻ���
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
////	        // lock screen �� �ƴ�
////	    	Log.i(TAG, "lock screen �� �ƴ�");
////	    }
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		if ( pm.isScreenOn() ){
//			Log.i(TAG, "��ũ���»���");
//		}else{
//			Log.i(TAG, "��ũ���� �ƴ�");
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
            // 1. �ֱ��׽�ũ��� ��
            Context mContext = getApplicationContext();
            // ActivityManager am =
            // (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
            // // context�� �̿��̸� application�� context���� 2. �������� �½�ũ ��� �����ͼ� ���� �����½�ũ��
            // ��
            //
            // List<RunningTaskInfo> task = am.getRunningTasks(1);
            //
            // if(!task.isEmpty()) {
            //
            // ComponentName topActivity = task.get(0).topActivity; // ���� �½�ũ ����
            // Log.i(TAG, "topActivity.getClassName = "+
            // topActivity.getClassName());
            // if(!topActivity.getPackageName().equals(mContext.getPackageName())) {
            // // ���� �½�ũ�� �� ������ ��
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
            // 2. ���μ���Ȯ�� ���� �������Ȯ��
            // ActivityManager am = (ActivityManager)
            // getSystemService(Context.ACTIVITY_SERVICE);
            // List<ActivityManager.RunningAppProcessInfo>appList =
            // am.getRunningAppProcesses();
            // for (ActivityManager.RunningAppProcessInfo app : appList)
            // {
            // if ("com.academy.coupling".equals(app.processName))
            // {
            // // [Bug_052] [���Գ���]: ����Ʈ����Ŵ�� ��й�ȣ ������ ���¿��� HomeŰ�� �����ٰ� ����Ʈ�� ��Ŵ�� �� �����
            // �ٷ� app ����Ǵ� ����
            // if ( app.importance ==
            // ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ) // �ٸ�
            // �۾������� ���� resume�� ��ٸ��� ���� ����(ex. ��Ű�� �ν��緯 ȣ���)
            // {
            // // ��Ŵ�� ���� Process importance�� ����� �Ͽ�, ��Ŵ�� ���� ��׶��� ���۽� ��� Activity
            // �����Ű�� ��� ���� �籸�� �ϵ��� ó��
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
        // 3. ������
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
            // Android 2.3 �̻󿡼� ��� ������ ���.
            long installed;
            try {
                PackageManager packageManager = this.getPackageManager();
                installed = packageManager.getPackageInfo(getPackageName(), 0)
                        .firstInstallTime;
                long a = 0;
                //���ᳯ¥�� ���۳�¥�� ���� ���մϴ�.
                //�γ�¥���� ���� �������� getTimeMills()�� �̿��Ͽ� õ���� 1�� ������ ��ȯ�Ͽ��� �մϴ�.
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
         * Android ��ġ�� ���� ��������Ʈ ��������
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
