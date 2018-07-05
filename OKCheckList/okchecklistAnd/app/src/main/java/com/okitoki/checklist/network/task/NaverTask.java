package com.okitoki.checklist.network.task;

import android.content.Context;

import com.google.gson.JsonElement;
import com.okitoki.checklist.network.OnNetworkListener;
import com.okitoki.checklist.network.model.Rss;
import com.okitoki.checklist.network.protocol.ReqType;
import com.okitoki.checklist.network.retrofit.MainInterface;
import com.okitoki.checklist.network.retrofit.RestAPIService;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Call;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-20.
 */
public class NaverTask extends NetworkTask<Rss> {

    HashMap<String,Object> params = new HashMap<>();

    public NaverTask(Context mContext, int requestType, OnNetworkListener<Rss> onNetworkListener) {
        super(mContext, requestType, onNetworkListener);
    }

    public void setParam(HashMap<String,Object> params) {
        this.params = params;
    }

    // 상속 구현 ResInterfce API 생성
    protected Call<? extends Rss> getRetroCallable(){
        MainInterface service = RestAPIService.createService(MainInterface.class);
        // reqType별 API WindowsTopViewService 별도 반환
        if (mRequestType == ReqType.REQ_GET_SEARCH_PRODUCT){
            return service.getProductInfo(params);
        } else {
            return null;
        }
    }

    // Retrofit JsonElement로부터 직접파싱?
    @Override
    protected Rss onParse(JsonElement json) throws JSONException, IOException {
        return null;
    }
}
