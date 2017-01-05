package com.oklab.opensources.dagger.di;

import com.oklab.opensources.dagger.utils.CollectionUtils;
import com.oklab.opensources.dagger.DagApplication;
import com.oklab.opensources.dagger.DaggerActivity;

/**
 * Created by andman on 2017-01-05.
 */
public interface BaseComponent {

    void inject(DagApplication app);

    void inject(DaggerActivity daggerActivity);

}