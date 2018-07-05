package com.okitoki.checklist.network.retrofit;

import com.okitoki.checklist.BuildConfig;
import com.okitoki.checklist.network.NetManager;
import com.okitoki.checklist.utils.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-19.
 */
public class RestAPIService {

    private static final int NETWORK_CONNECT_TIME_OUT = 7;
    private static final int NETWORK_READ_TIME_OUT = 60;

    private static OkHttpClient okClient = new OkHttpClient();
    private static Retrofit retrofit = null;

    public static void initRetrofit(){
        okClient.setConnectTimeout(NETWORK_CONNECT_TIME_OUT, TimeUnit.SECONDS);
        okClient.setReadTimeout(NETWORK_READ_TIME_OUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            okClient.interceptors().add(logging);
            okClient.networkInterceptors().add(new Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Interceptor.Chain chain) throws IOException {

                    Request request = chain.request().newBuilder()
                            .build();

                    String strRequset = chain.request().urlString();
                    Logger.d(NetManager.class, "[netInterceptor] => " + strRequset);
                    long startTime = System.nanoTime();
                    com.squareup.okhttp.Response response = chain.proceed(request);
                    long endTime = System.nanoTime();
//                    String bodyString = response.body().string();
                    Logger.d(NetManager.class, "[netInterceptor] => " + "elapsedTime:( " + ((endTime - startTime) / 1000000) + " ms) " + strRequset);
//                    Logger.d(NetManager.class, "[netInterceptor] => " + "body:( "+ bodyString + " )");
//                    response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                    return response;
                }
            });
        }
//        mExecutorService = Executors.newCachedThreadPool(); Retrofit retrofit = new Retrofit.Builder()
        retrofit = new Retrofit.Builder()
                .client(okClient)
                .baseUrl(NetManager.API_NAVER__URL)
//                .callbackExecutor(new MainThreadExecutor())
                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }



    public boolean isGoodNetworkStatus(Context context) {
        boolean isNetworkConnected = false;

        ConnectivityManager ctvMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (ctvMgr != null) {
            NetworkInfo info = ctvMgr.getActiveNetworkInfo();
            if (info != null) {
                NetworkInfo.State state = info.getState();
                if (state == NetworkInfo.State.CONNECTED) {
                    int nType = info.getType();
                    String strTypeName = info.getTypeName();
                    if (ConnectivityManager.TYPE_WIFI == nType || "Ethernet".equals(strTypeName)) {
                        isNetworkConnected = true;
                        //return isNetworkConnected;
                    }
                    if (ConnectivityManager.TYPE_MOBILE == nType && isWifiEnabled(context)) {
                        isNetworkConnected = true;
                        //return isNetworkConnected;
                    }
                }
            }
        }
        return isNetworkConnected;
    }

    public boolean isWifiEnabled(Context context) {
        Object result = false;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Method[] wmMethods = wifi.getClass().getDeclaredMethods();

        for (Method method : wmMethods) {
            if ("isWifiApEnabled".equals(method.getName())) {
                try {
                    result = method.invoke(wifi);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (((Object) true).equals(result)) {
            return true;
        }

        return false;
    }

}
