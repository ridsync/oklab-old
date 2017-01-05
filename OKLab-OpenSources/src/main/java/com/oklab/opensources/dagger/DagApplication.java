package com.oklab.opensources.dagger;

import android.app.Application;
import android.content.Context;

import com.oklab.opensources.dagger.di.DaggerApplicationComponent;
import com.oklab.opensources.dagger.di.ApplicationComponent;
import com.oklab.opensources.dagger.di.D2ApplicationModule;
import com.oklab.opensources.dagger.di.D2SystemServicesModule;
import com.oklab.opensources.dagger.di.D2UtilsModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by andman on 2017-01-04.
 */
public class DagApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // realm 설정
        initRealmConfiguration();
        // dagger application component 설정
        initApplicationComponent();
    }

    private void initRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initApplicationComponent() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .d2ApplicationModule(new D2ApplicationModule(this))
                .d2SystemServicesModule(new D2SystemServicesModule(this))
                .d2UtilsModule(new D2UtilsModule(this))
                .build();
        this.applicationComponent.inject(this);
    }

    public static ApplicationComponent component(Context context) {
        return ((DagApplication) context.getApplicationContext()).applicationComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
