package com.oklab.opensources.concurrent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.oklab.opensources.retrofit.ApiInterface;
import com.oklab.opensources.model.BaseModel;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andman on 2016-12-12.
 */
public class UPloadCallable implements Callable {
    private Context context;

    public UPloadCallable(Context context) {
        this.context = context;
    }

    public Integer call() {

        String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        // Retrofit 초기화
        final OkHttpClient.Builder okClient = new OkHttpClient().newBuilder();
        okClient.connectTimeout(1, TimeUnit.HOURS);
        okClient.readTimeout(1, TimeUnit.HOURS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClient.interceptors().add(logging);
        okClient.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .addHeader("test", "test")
                        .build();

                String strRequset = chain.request().url().toString();
                String strHeader = request.headers().toString();
                Log.i("NetManager", "[Request] => " + strRequset + "\n"+ strHeader);
                long startTime = System.nanoTime();
                okhttp3.Response response = chain.proceed(request);
                long endTime = System.nanoTime();
                String bodyString = response.body().string();
                Log.i("NetManager", "[Response] => " + "elapsedTime:( " + ((endTime - startTime) / 1000000) + " ms) " + strRequset);
                Log.i("NetManager", "[Response] => " + "body:( "+ bodyString + " )");
                response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                return response;
            }
        });

        // 멀티플 파일 업로드 http://bit.ly/2gjna5A
        File file1 = new File(path+File.separator+"file1.jpg");
        if(file1.exists() ) {
            Log.d("Retrofit","file1 exist = "+file1.getName() );
        }

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        requestBodyBuilder.addFormDataPart("userfile", file1.getName(),
                RequestBody.create(MediaType.parse("image/*"), file1));

        Retrofit client = new Retrofit.Builder().baseUrl("http://10.23.51.41:3960").client(okClient.build()).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface service = client.create(ApiInterface.class);
        Call<BaseModel> call = service.reqUploadMultipleImage(requestBodyBuilder.build());

        int result = -1;
        try {
            Response<BaseModel> res = call.execute();
            if (res.isSuccessful()) {
                result = res.body().retVal;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
