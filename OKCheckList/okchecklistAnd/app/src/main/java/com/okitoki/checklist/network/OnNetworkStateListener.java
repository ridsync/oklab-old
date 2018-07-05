package com.okitoki.checklist.network;

import com.okitoki.checklist.network.task.NetworkTask;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-20.
 */
public interface OnNetworkStateListener {
    public void onStateSucess(int requestType ,NetworkTask<?> task);
    public void onStateFail(int resultCode, NetworkTask<?> task);
}
