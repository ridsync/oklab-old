package com.oklab.opensources.dagger.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.oklab.opensources.dagger.utils.NetworkStateManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andman on 2017-01-05.
 */
@Module
public class D2SystemServicesModule {

    private final Application application;

    public D2SystemServicesModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
        //Method parameter injected by Dagger2
    NetworkStateManager provideNetworkStateManager(ConnectivityManager connectivityManagerCompat) {
        return new NetworkStateManager(connectivityManagerCompat);
    }
}
