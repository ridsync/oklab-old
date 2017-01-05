package com.oklab.opensources.dagger.di;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by andman on 2017-01-05.
 */
public class EventBus {
    private final Bus bus;

    @Inject
    public EventBus(Bus bus) {
        this.bus = bus;
    }

    public void post(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bus.post(event);
            }
        });
    }

    public void register(final Object object) {
        try {
            bus.register(object);
        } catch (IllegalArgumentException e) {
            Log.e(this.getClass().getSimpleName(),"Already registered object!");
        }
    }

    public void unregister(final Object object) {
        try {
            bus.unregister(object);
        } catch (IllegalArgumentException e) {
            Log.e(this.getClass().getSimpleName(),"You can not unregister no registered object!");
        }
    }
}
