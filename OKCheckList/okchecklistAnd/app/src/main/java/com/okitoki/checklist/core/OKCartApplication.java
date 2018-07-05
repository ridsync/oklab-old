package com.okitoki.checklist.core;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.okitoki.checklist.database.dao.DaoFactory;
import com.okitoki.checklist.database.dao.DaoFactoryOrmLite;
import com.okitoki.checklist.network.retrofit.RestAPIService;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.PreferUtil;
import com.squareup.otto.Bus;


/**
 * Created by ojungwon on 2015-04-07.
 * @author ojungwon
 */
public class OKCartApplication extends android.support.multidex.MultiDexApplication {

    private static OKCartApplication instance;
    private static Bus BUS = new Bus();

    protected static Context sContext;

    public static int isShowFullAd_search = 1;
    public static int isShowFullAd_search_2 = 1;
    public static int martRestNearByClickCount = 0;
    public static int martRestAddCount = 1;

    public OKCartApplication() {
        super();
        sContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        instance = this;

//        Session.initialize(this);
//        SystemInfo.initialize();

        // ORMLite Initialization
        DaoFactory daoFactory = DaoFactoryOrmLite.getInstance();
        daoFactory.setContext(getApplicationContext());

        RestAPIService.initRetrofit();

        boolean isDefaultSet = PreferUtil.getPreferenceBoolean(AppConst.PREF_HOLLIDAY_ALARM_IS_SET_DEFAULT,getApplicationContext());
        if(!isDefaultSet){
            PreferUtil.setSharedPreference(AppConst.PREF_DEFAULT_MAINSCREEN_SETTING,1,getApplicationContext());

            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_IS_SET_DEFAULT,true,getApplicationContext());

            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_NOTIFY,true,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_DAY_AGO,6,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_HOUR,12,getApplicationContext());
            PreferUtil.setSharedPreference(AppConst.PREF_HOLLIDAY_ALARM_SET_MINUTE,0,getApplicationContext());

            AUtil.setAlarmManagerForPRnoty(getApplicationContext());
        }

    }

    public static Bus getBus() {
        if (BUS == null){
            BUS = new Bus();
        }
        return BUS;
    }

    public static synchronized FirebaseAnalytics getDefaultFBAnalytics() {
        return null;
    }

}
