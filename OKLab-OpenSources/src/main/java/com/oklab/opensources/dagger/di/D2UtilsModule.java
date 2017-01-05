package com.oklab.opensources.dagger.di;

import android.app.Application;

import com.oklab.opensources.dagger.utils.CollectionUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andman on 2017-01-05.
 */
@Module
public class D2UtilsModule {

    private Application mApplication;

    public D2UtilsModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    CollectionUtils provideCollectionUtils() {
        return new CollectionUtils();
    }
}