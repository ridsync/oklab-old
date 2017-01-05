package com.oklab.opensources.dagger.di;

import com.oklab.opensources.dagger.BaseActivity;
import com.oklab.opensources.dagger.utils.CollectionUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lee on 2016. 8. 16..
 */
@Singleton
@Component(modules = {
        D2ApplicationModule.class,
        D2SystemServicesModule.class,
        D2UtilsModule.class
})
public interface ApplicationComponent extends BaseComponent {

    void inject(BaseActivity baseActivity);

    CollectionUtils getD2CollectionUtil();

}
