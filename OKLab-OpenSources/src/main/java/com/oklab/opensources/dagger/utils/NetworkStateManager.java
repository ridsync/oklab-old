package com.oklab.opensources.dagger.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andman on 2017-01-05.
 */
public class NetworkStateManager {

    private final ConnectivityManager connectivityManager;

    public NetworkStateManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnectedOrConnecting(){
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo.isConnectedOrConnecting();
    }
}
