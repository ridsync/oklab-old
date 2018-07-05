package com.okitoki.checklist.ui;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.MarketVersionChecker;
import com.okitoki.checklist.utils.PreferUtil;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by okc on 2016-09-25.
 */
public class SettingActivity extends CoreActivity implements TimePickerDialog.OnTimeSetListener{

    public SettingActivity(){}
    /***************************
     * Static Member Variable
     ***************************/
    private boolean mIsAlarmNotify;
    private int nDayAgo;
    private int nSetHour;
    private int nSetMinute;


    /***************************
     * Logical Member Variable
     ***************************/


    /***************************
     * Android's Variable
     ***************************/
    Toolbar toolbar_actionbar;

    @Bind(R.id.TV_CURRENT_VERSION)
    TextView tvCurrentVersion;
    @Bind(R.id.TV_NEW_VERSION)
    TextView tvNewVersion;
    @Bind(R.id.BTN_SETTING_VER_UPDATE)
    Button btnSettingUpdate;
    @Bind(R.id.SWC_SETTING_NOTIFY_TOGGLE)
    SwitchCompat swcNotifyToggle;

    @Bind(R.id.RL_SETTING_PUSH_ALERT_group2)
    LinearLayout llAlertGrpoup2;
    @Bind(R.id.LL_SETTING_PUSH_ALERT_SAVE)
    LinearLayout llAlertsave;

    @Bind(R.id.BTN_SETTING_MAIN_CART_LISTVIEW) Button btnDefMainCartList;
    @Bind(R.id.BTN_SETTING_MAIN_MART_HOLIDAY) Button btnDefMartHoliday;
    @Bind(R.id.BTN_SETTING_MAIN_SEARCH) Button btnDefSearch;

    @Bind(R.id.BTN_SETTING_PUSH_ALERT_1_DAY) Button btnAlert1Day;
    @Bind(R.id.BTN_SETTING_PUSH_ALERT_3_DAY) Button btnAlert3Day;
    @Bind(R.id.BTN_SETTING_PUSH_ALERT_7_DAY) Button btnAlert7Day;
    @Bind(R.id.BTN_SETTING_PUSH_ALERT_TIME) Button tvsetAlertTime;
    @Bind(R.id.BTN_SETTING_PUSH_ALERT_SAVE) Button tvalertSave;

    @Bind(R.id.RL_SETTING_OPEN_LICENSE) RelativeLayout RlOpenLicense;
    @Bind(R.id.RL_SETTING_SEND_EMAIL) RelativeLayout RlTermofUse;
    @Bind(R.id.RL_SETTING_RATE) RelativeLayout RlPrivacy;
    @Bind(R.id.RL_SETTING_DONATE_PURCHASE) RelativeLayout RlYouth;
    @Bind(R.id.RL_SETTING_LOGOUT) RelativeLayout Rllogout;
    @Bind(R.id.RL_SETTING_DELETE_ACCOUNT) RelativeLayout RldeleteAccount;


    /***************************
     * Android LifeCycle
     ***************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        ButterKnife.bind(this);
        setActionBar();
        initFirst();
    }

    @Override
    public void onResume() {
        super.onResume();
        sendScreenToGA("설정");
    }

    @OnClick({R.id.BTN_SETTING_VER_UPDATE, R.id.RL_SETTING_RATE})
    public void OnClickAppUpdate(){

        Uri uri = Uri.parse( "market://details?id=" + "com.okitoki.okcart");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void OnClickNotifyToggle(boolean isChecked){

        if( isChecked ) {
            startAlarmInit();
            llAlertGrpoup2.setVisibility(View.VISIBLE);
            llAlertsave.setVisibility(View.VISIBLE);
        } else {
            llAlertGrpoup2.setVisibility(View.GONE);
            llAlertsave.setVisibility(View.GONE);

            // TODO 알림설정 삭제
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_NOTIFY,false,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_DAY_AGO,nDayAgo,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_HOUR,nSetHour,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_MINUTE,nSetMinute,getApplicationContext());

            AUtil.setAlarmManagerForPRnoty(getApplicationContext());
        }

    }

    @OnClick(R.id.BTN_SETTING_MAIN_CART_LISTVIEW)
    public void OnClickDefCartListView(){
        btnDefMainCartList.setSelected(true);
        btnDefMartHoliday.setSelected(false);
        btnDefSearch.setSelected(false);
        PreferUtil.setSharedPreference(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,0,getApplicationContext());
    }
    @OnClick(R.id.BTN_SETTING_MAIN_MART_HOLIDAY)
    public void OnClickDefMArtHoliday(){
        btnDefMainCartList.setSelected(false);
        btnDefMartHoliday.setSelected(true);
        btnDefSearch.setSelected(false);
        PreferUtil.setSharedPreference(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,1,getApplicationContext());
    }
    @OnClick(R.id.BTN_SETTING_MAIN_SEARCH)
    public void OnClickDefSearch(){
        btnDefMainCartList.setSelected(false);
        btnDefMartHoliday.setSelected(false);
        btnDefSearch.setSelected(true);
        PreferUtil.setSharedPreference(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,2,getApplicationContext());
    }

    @OnClick(R.id.BTN_SETTING_PUSH_ALERT_1_DAY)
    public void OnClickAlert1Day(){

        if(! btnAlert1Day.isSelected()){
            nDayAgo = 1;
            btnAlert1Day.setSelected(true);
            btnAlert3Day.setSelected(false);
            btnAlert7Day.setSelected(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    llAlertsave.setVisibility(View.VISIBLE);
                    llAlertsave.setEnabled(true);
                }
            },300);
        }
    }
    @OnClick(R.id.BTN_SETTING_PUSH_ALERT_3_DAY)
    public void OnClickAlert3Day(){

        if(! btnAlert3Day.isSelected()){
            nDayAgo = 3;
            btnAlert1Day.setSelected(false);
            btnAlert3Day.setSelected(true);
            btnAlert7Day.setSelected(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    llAlertsave.setVisibility(View.VISIBLE);
                    llAlertsave.setEnabled(true);
                }
            },300);
        }
    }
    @OnClick(R.id.BTN_SETTING_PUSH_ALERT_7_DAY)
    public void OnClickAlert7Day(){

        if(! btnAlert7Day.isSelected()){
            nDayAgo = 6;
            btnAlert1Day.setSelected(false);
            btnAlert3Day.setSelected(false);
            btnAlert7Day.setSelected(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    llAlertsave.setVisibility(View.VISIBLE);
                    llAlertsave.setEnabled(true);
                }
            },300);
        }

    }
    @OnClick(R.id.BTN_SETTING_PUSH_ALERT_TIME)
    public void OnClickAlertTime(){
        if(JavaUtil.isDoubleClick()) return;

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, R.style.PickerDialogTheme, this, nSetHour, nSetMinute, false);
        timePickerDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llAlertsave.setVisibility(View.VISIBLE);
                llAlertsave.setEnabled(true);
            }
        },300);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Logger.d(this,"hourOfDay = "+ hourOfDay + "minute = " + minute );
        nSetHour = hourOfDay;
        nSetMinute = minute;
        String strHour = String.format("%02d", nSetHour);
        String strMinute =  String.format("%02d", nSetMinute);
        tvsetAlertTime.setText("알림시각    "+strHour+" : "+strMinute+" ");
    }

    @OnClick(R.id.BTN_SETTING_PUSH_ALERT_SAVE)
    public void OnClickAlertSave(){
        if(JavaUtil.isDoubleClick()) return;

        PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_NOTIFY,mIsAlarmNotify,mContext);
        PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_DAY_AGO,nDayAgo,mContext);
        PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_HOUR,nSetHour,mContext);
        PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_MINUTE,nSetMinute,mContext);

        AUtil.setAlarmManagerForPRnoty(getApplicationContext());

        llAlertsave.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llAlertsave.setVisibility(View.GONE);
            }
        },300);

        Toast.makeText(mContext,"알림설정을 저장했습니다.",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.RL_SETTING_OPEN_LICENSE)
    public void OnClickOpenLicense(){
        if(JavaUtil.isDoubleClick()) return;
        new LicensesDialog.Builder(SettingActivity.this).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show();
    }

    @OnClick(R.id.RL_SETTING_SEND_EMAIL)
    public void OnClickSendEmail(){
        if(JavaUtil.isDoubleClick()) return;
        writeEmail();
    }

    /***************************
     * Member Methods or Others
     ***************************/
    private void setActionBar() {
        toolbar_actionbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        toolbar_actionbar.setTitle("설정");
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFirst() {

        // 버젼 초기화
        startVersionCheck();

        // 메인화면 초기화.
        int defMain = PreferUtil.getPreferenceInteger(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,mContext);
        if(defMain == 0 ){
            btnDefMainCartList.setSelected(true);
        } else if(defMain == 1 ){
            btnDefMartHoliday.setSelected(true);
        } else {
            btnDefSearch.setSelected(true);
        }

        // 휴무일 알림설정 초기화.
        mIsAlarmNotify = PreferUtil.getPreferenceBoolean(AppConst.PREF_HOLLIDAY_ALARM_NOTIFY,mContext);
        nDayAgo = PreferUtil.getPreferenceInteger(AppConst.PREF_HOLLIDAY_ALARM_DAY_AGO,mContext);
        nSetHour = PreferUtil.getPreferenceInteger(AppConst.PREF_HOLLIDAY_ALARM_SET_HOUR,mContext);
        nSetMinute = PreferUtil.getPreferenceInteger(AppConst.PREF_HOLLIDAY_ALARM_SET_MINUTE,mContext);
        startAlarmInit();
    }

    private void startAlarmInit() {

        swcNotifyToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsAlarmNotify = isChecked;
                OnClickNotifyToggle(isChecked);
            }
        });

        if(mIsAlarmNotify){
            if(nDayAgo == 1 ){
                btnAlert1Day.setSelected(true);
            } else if(nDayAgo == 3 ){
                btnAlert3Day.setSelected(true);
            } else {
                nDayAgo = 6;
                btnAlert7Day.setSelected(true);
            }
            String strHour = String.format("%02d", nSetHour);
            String strMinute =  String.format("%02d", nSetMinute);
            tvsetAlertTime.setText("알림시각    "+strHour+" : "+strMinute+" ");
            llAlertGrpoup2.setVisibility(View.VISIBLE);
        }

        // view
        swcNotifyToggle.setChecked(mIsAlarmNotify);
        llAlertsave.setVisibility(View.GONE);

        tvsetAlertTime.setSelected(true);
        tvalertSave.setSelected(true);
    }

    Handler verHandler = new Handler();

    private void startVersionCheck() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String store_version = MarketVersionChecker.getMarketVersion(getPackageName());
                    Logger.d("Version","[VerChecker] store_version = " + store_version);
                    final String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    Logger.d("Version","[VerChecker] device_version = " + device_version);
                    verHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!isFinishing()){
                                tvNewVersion.setText("최신 버젼 : V"+store_version);
                                tvCurrentVersion.setText("현재 버젼 : V"+ device_version);
                            }
                        }
                    });

                    if (store_version !=null && store_version.compareTo(device_version) > 0) {
                        // 업데이트 필요
                        Logger.d("Version","[VerChecker] Need App Update !!! ");
                        if(verHandler!=null)
                            verHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!isFinishing()){
                                        btnSettingUpdate.setSelected(true);
                                        btnSettingUpdate.setEnabled(true);
                                    }
                                }
                            });
                    } else {
                        verHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(!isFinishing()){
                                    btnSettingUpdate.setSelected(false);
                                    btnSettingUpdate.setEnabled(false);
                                }
                            }
                        });
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void writeEmail() {
        Intent it = new Intent(Intent.ACTION_SEND);
        String email = "ridsync@naver.com";
        String[] mailaddr = {email};
        it.setType("plain/text");
        it.putExtra(Intent.EXTRA_EMAIL, mailaddr);
        it.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        try {
            it.putExtra(Intent.EXTRA_TEXT,
                    "Device Model : " + AUtil.getDeviceName() + "\n"
                            + "Android Version : " + AUtil.getOsVersion() + "\n"
                            + "AppVersion : " + AUtil.getVersion(mContext) + "\n"
                            + "Device ID : " + AUtil.getDeviceId(mContext) + "\n"
                            + "OS Sdk : " + AUtil.getOsSdkInt() + "\n"
                            + "===================================================\n\n");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        startActivity(it);
    }
}
