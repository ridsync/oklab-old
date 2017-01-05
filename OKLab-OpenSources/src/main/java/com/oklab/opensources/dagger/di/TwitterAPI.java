package com.oklab.opensources.dagger.di;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by andman on 2017-01-05.
 */
@Singleton
public class TwitterAPI {

    @Inject
    public TwitterAPI() {
    }

    public void tweet(Context context, String tweetBody){
        Toast.makeText(context, tweetBody, Toast.LENGTH_SHORT).show();
    }
}
