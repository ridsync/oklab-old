package com.okitoki.checklist.network.retrofit;

import com.okitoki.checklist.network.model.ResProfile;
import com.okitoki.checklist.network.model.Rss;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-19.
 */
public interface MainInterface {

    @Headers({
            "Content-Type: application/xml",
            "X-Naver-Client-Id: wASKsyyrWrU72MQwqVmG",
            "X-Naver-Client-Secret: rgrOi3RJcH"
    })
    @GET("/v1/search/shop.xml")
    Call<Rss> getProductInfo(@QueryMap Map<String, Object> map);

    @GET("/profile/getUserInfo")
    Call<ResProfile> getprofile(
            @Query("uId") String uId
    );
}
