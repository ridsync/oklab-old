package com.okitoki.checklist.okperm;

import android.os.Handler;
import android.os.Looper;
import de.greenrobot.event.EventBus;

/**
 * Created by andman on 2016-04-20.
 */
public class EventBusProvider{


    private static EventBusProvider instance;

    public static EventBusProvider getInstance() {

        if(instance==null)
            instance = new EventBusProvider();

        return instance;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public void post(final Object event) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            EventBus.getDefault().post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(event);
                }
            });
        }

    }
}
