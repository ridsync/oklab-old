package com.okitoki.checklist.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.okitoki.checklist.network.task.NetworkTask;
import com.okitoki.checklist.utils.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-16.
 */
public class NetManager {

    public static final String API_BASE_URL = "http://210.116.118.236:1377";
    public static final String API_NAVER__URL = "https://openapi.naver.com";

    static HashMap<NetworkTask<?>, NetworkTask<?>> mSet = new HashMap<NetworkTask<?>, NetworkTask<?>>();

    public NetManager() {
    }

    public static void startTask(NetworkTask<?> task){
        cancelTask(task);

        task.setResultListener(stateListener);
        mSet.put(task, task);

        if(checkNetStat(task.getContext(), task)==true){
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    public static void cancelTask(NetworkTask<?> item){
        NetworkTask<?> task = mSet.remove(item);
        if(null != task)
        {
            Logger.i(NetManager.class, "--------cancel------------");
            switch(task.getStatus()){
                case RUNNING:
                case PENDING:
                    task.cancel(true);
                    task.destroyProgressBar();
                    break;
            }
        }
//		mSet.remove(item);
    }

    public static void cancelAllTask(){
        if(!mSet.isEmpty()){
            synchronized(mSet){
                Set<NetworkTask<?>> taskSet = mSet.keySet();
                if(null != taskSet){
                    Iterator<NetworkTask<?>> iter = taskSet.iterator();
                    if(null != iter)
                    {
                        NetworkTask<?> task;
                        while(iter.hasNext()){
                            task = iter.next();
                            if(null != task && task.getStatus()== AsyncTask.Status.RUNNING){
                                task.cancel(true);
                                Logger.d(NetManager.class, "cancelAll _task="+task);
                            }
                        }
                    }
                }
                mSet.clear();
            }
        }
    }

    //TODO AbstractFragment 상에서 처리.
    static OnNetworkStateListener stateListener = new OnNetworkStateListener() {

        @Override
        public void onStateSucess(int requestType, NetworkTask<?> task) {
            mSet.remove(task); // task remove
            // NotifySucess(); // TODO 네트워크상태 알림바 Off
        }
        @Override
        public void onStateFail(int resultCode, NetworkTask<?> task) {
//            if(resultCode==ResultCode.SESSION_ERROR){
//                cancelAll();
//            }else{
            mSet.remove(task); // task remove
//            }
            // NotifyFail(); // TODO 네트워크상태 알림바 On
        }
    };

    private static boolean checkNetStat(Context context, final NetworkTask<?> task){

        // TODO 온라인여부 체크 로직
        if(isOnline(context)){
            if(false == wifiAvail(context)){
                Logger.d(NetManager.class, "3G연결");
            }
            return true;
        }
        if(task!=null){
            task.notifyConnectFail();
        }
        return false;
    }

    /**
     *
     * TODO 네트워크 상태체크 유효성 ??
     * TODO  -> 체크유틸 로 이동
     *
     **/

    public static boolean wifiAvail(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiAvail = ni.isAvailable();
        return isWifiAvail;
    }
    public static boolean mobileAvail(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvail = ni.isAvailable();
        return isMobileAvail;
    }
    /**
     * 3G or WiFi 사용 가능 여부
     * @param context
     * @return true : 3G or WiFi 사용 가능
     */
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }

        return false;
    }
}
