package com.oklab.opensources.dagger.di;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by andman on 2017-01-04.
 */
@Module
public class D2ApplicationModule {

    private Application mApplication;

    public D2ApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus(ThreadEnforcer.MAIN);
    }
}