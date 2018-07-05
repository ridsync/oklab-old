package com.okitoki.checklist.network;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-19.
 */
public interface OnNetworkListener<T> {
    public void onNetSuccess(T data, int reqType);
    public void onNetFail(int retCode, String strError, int reqType);

    public void onLoadingDialog(int reqType);
    public void onLoadingDialogClose(int reqType);
}
