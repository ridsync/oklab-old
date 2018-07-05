package com.okitoki.checklist.network.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.okitoki.checklist.network.OnNetworkListener;
import com.okitoki.checklist.network.OnNetworkStateListener;
import com.okitoki.checklist.network.model.BaseModel;
import com.okitoki.checklist.network.protocol.ResultCode;
import com.okitoki.checklist.utils.Logger;

import org.json.JSONException;

import java.io.IOException;

import retrofit.Call;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-19.
 */
public abstract class NetworkTask<T> extends AsyncTask<Void, Void, T> {

    protected int mRequestType;
    private Context mContext;
    protected OnNetworkListener<T> mNetworkListener;
    private boolean isRetroParsing = true;
    private int mErrorCode = ResultCode.API_NO_ERROR;
    private String mServerErrorMsg = "NOERROR";

    protected OnNetworkStateListener stateListener;

    public NetworkTask() {
        super();
    }
    /**
     *
     * @param mContext
     * @param reqType : int mRequestType Field로 존재
     * @param onNetworkListener
     */
    public NetworkTask(Context mContext, int reqType, OnNetworkListener<T> onNetworkListener) {
        super();
        this.mContext = mContext;
        this.mRequestType = reqType;
        this.mNetworkListener = onNetworkListener;
    }

    public void setResultListener(OnNetworkStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    protected void onCancelled() {
        switch (getStatus()) {
            case RUNNING:
//                if(null != stateListener){
//                    stateListener.onStateFail(ResultCode.CANCELLED, this);
//                }
                break;
        }
        Logger.d(this, "onCancelled mRequestType = " + mRequestType);
        super.onCancelled();
    }
    @Override
    protected void onPreExecute() {
        if(isCancelled()){
            Logger.d(this, "NetTask -- Canceld");
        }
        else {
            initProgressBar();
        }
    }

    @Override
    protected void onPostExecute(T result) {
        if(isCancelled()){
            destroyProgressBar();
            return;
        }

        if(mErrorCode != ResultCode.API_NO_ERROR
                || result == null){
            if(null != stateListener){
                stateListener.onStateFail(mErrorCode, this);
            }
            if(null != mNetworkListener){
                mNetworkListener.onNetFail(mErrorCode, mServerErrorMsg, mRequestType);
            }
            destroyProgressBar();
            return ;
        }
        // server status check
//        BaseModel ret = ((BaseModel) result);
//        if (ret.retVal != ResultCode.API_SUCCESS ){
//            mNetworkListener.onNetFail(ret.retVal, ret.retMsg, mRequestType);
//            destroyProgressBar();
//            return;
//        }

        if(null != mNetworkListener){
            mNetworkListener.onNetSuccess(result, mRequestType);
        }
        if(null != stateListener){
            stateListener.onStateSucess(mRequestType, this);
        }
        destroyProgressBar();
        this.cancel(true);

    }

    @Override
    protected T doInBackground(Void... params) {

        if(isCancelled()){
            return null;
        }
        try {
            retrofit.Response response = getRetroCallable().execute();
            // http status check
            if (response == null || response.code() != ResultCode.HTTP_SUCCESS_OK){
                mErrorCode = ResultCode.SERVER_ERROR;
                mServerErrorMsg = "ErrorMsg.SERVER_ERROR" + response.errorBody().string();
                return null;
            }
            if (isRetroParsing){ // TODO
                return (T) response.body();
            } else {
                return (T) onParse((JsonElement)response.body());
            }

        } catch (IllegalStateException e) {
            mErrorCode = ResultCode.CLIENT_ERROR;
            mServerErrorMsg = "ErrorMsg.CLIENT_ERROR";
            e.printStackTrace();
        } catch (IOException e) {
            mErrorCode = ResultCode.CLIENT_ERROR;
            mServerErrorMsg = "ErrorMsg.CLIENT_ERROR";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            mErrorCode = ResultCode.JSON_PRASE_ERROR;
            mServerErrorMsg = "ErrorMsg.JSON_PRASE_ERROR";
            e.printStackTrace();
        } catch (Exception e) {
            mErrorCode = ResultCode.CLIENT_ERROR;
            mServerErrorMsg = "ErrorMsg.CLIENT_ERROR";
            e.printStackTrace();
//            Logger.w("Exception :" + mErrorString);
        } finally {
        }
        return null;
    }

    abstract protected Call<? extends BaseModel> getRetroCallable();

    abstract protected T onParse(JsonElement json)  throws JSONException, IOException;

    protected T onGsonParsing(String data,  Class<?> classOfT){
        Gson gson = new Gson();
        return (T) gson.fromJson(data, classOfT);
    };

    public void notifyConnectFail() {
        if(null != mNetworkListener){
            mNetworkListener.onNetFail(ResultCode.CONNECT_FAIL, null, mRequestType);
        }
        if(null != stateListener){
            stateListener.onStateFail(ResultCode.CONNECT_FAIL, this);
        }
    }

    public final void initProgressBar() {
        if (null != mNetworkListener) {
            mNetworkListener.onLoadingDialog(mRequestType);
        }
    }

    public final void destroyProgressBar() {

        if (null != mNetworkListener) {
            mNetworkListener.onLoadingDialogClose(mRequestType);
        }
    }

}
