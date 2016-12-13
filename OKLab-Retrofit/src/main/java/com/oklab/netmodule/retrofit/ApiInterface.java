package com.oklab.netmodule.retrofit;

import com.oklab.netmodule.model.BaseModel;
import com.oklab.netmodule.model.Repo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by hubert on 2016-02-02.
 */
public interface ApiInterface {
    //@GET("/data/2.5/weather?lat={lat}&lon={lon}&appid=684b98e21b4f35b7d52abe9ff6279349")
    //Call<Repo> repo(@Path("lat") String lat, @Path("lon") String lon);
    @GET("/data/2.5/weather")
    Call<Repo> repo(@Query("appid") String appid, @Query("units") String units, @Query("lat") double lat, @Query("lon") double lon);


    // 1) 업로드 MultipartBody Builder
    @POST("/file/upImageMulti")
        Call<BaseModel>  reqUploadMultipleImage(@Body MultipartBody filePart);

    // 2) 업로드 MultipartBody.Part
    @Multipart
    @POST("/file/upImageMulti")
    Call<BaseModel>  reqUploadImageProgress(@Part MultipartBody.Part file1);

    @Multipart
    @POST("/file/upImageMulti")
    Call<BaseModel>  reqUploadMultipleImage(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);

    // 3) 업로드 PartMap
    @Multipart
    @POST("/file/upImageMulti")
        Call<BaseModel>  reqUploadMultipleImageMap(@PartMap Map<String, RequestBody> params, @PartMap Map<String, RequestBody> files);

}

