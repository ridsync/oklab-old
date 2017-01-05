package com.oklab.opensources.dagger.di;

import android.content.Context;

import javax.inject.Inject;

/**
 * Created by andman on 2017-01-05.
 */
public class Twitter {

    private Context appContextProvider;

    private TwitterAPI twitterAPI;

    @Inject
    public Twitter(Context appContextProvider, TwitterAPI twitterAPI) {
        this.appContextProvider = appContextProvider;
        this.twitterAPI = twitterAPI;
    }

    public void tweet(String tweetBody){
        twitterAPI.tweet(appContextProvider, tweetBody);
    }
}