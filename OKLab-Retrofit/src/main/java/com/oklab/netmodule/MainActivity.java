package com.oklab.netmodule;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oklab.netmodule.concurrent.OKTaskManager;
import com.oklab.netmodule.concurrent.UPloadCallable;
import com.oklab.netmodule.model.BaseModel;
import com.oklab.netmodule.model.Repo;
import com.oklab.netmodule.retrofit.ApiInterface;
import com.oklab.netmodule.retrofit.ProgressRequestBody;
import com.oklab.netmodule.retrofit.R;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.concurrent.Future;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse("image/jpg"), file);
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse("image/jpg"), s);
    }

    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.tem)
    TextView tem;
    @Bind(R.id.upload)
    TextView upload;
    @Bind(R.id.uploadBtn)
    Button getWeatherBtn;
    @Bind(R.id.tvLatitude)
    TextView tvLatitude;
    @Bind(R.id.tvLongtitude)
    TextView tvLongtitude;
    @Bind(R.id.lat)
    EditText mlat;
    @Bind(R.id.lon)
    EditText mlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        if( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double longitude = location.getLongitude();
                mlon.setText(longitude+"");
                double latitude = location.getLatitude();
                mlat.setText(latitude+"");
            }
        } else {
            mlon.setText("no GPS Permission");
            mlat.setText("no GPS Permission");
        }
    }

    @OnClick(R.id.getWeatherBtn)
    public void setGetWeatherBtn(View view){

        String lat= mlat.getText().toString();
        String lot = mlon.getText().toString();

        Retrofit client = new Retrofit.Builder().baseUrl("http://api.openweathermap.org").addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface service = client.create(ApiInterface.class);
        Call<Repo> call = service.repo("684b98e21b4f35b7d52abe9ff6279349", "metric" , Double.valueOf(lat), Double.valueOf(lot));
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if (response.isSuccessful()) {
                    Repo repo = response.body();
                    tem.setText(String.valueOf(repo.getMain().getTemp()) + "  ºC");
                } else {

                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
            }
        });
    }

    @OnClick(R.id.uploadBtn)
    public void onUpload(View view){

        execBackgroundWork();

////          // 압축 테스트.
////        try {
////            ZipUtil.zip(path, getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + File.separator +"한글.zip");
////
////            ZipUtil.unzip(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + File.separator +"한글.zip",
////                    getExternalFilesDir(Environment.DIRECTORY_ALARMS).getPath(), false );
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
    }

    /**
     *  ※ Concurrent TaskManager를 이용한 백그라운드 작업 ※
     *     - Retrofit,DB,기타 I/O 처리 모듈등에 사용
     */
    protected void execBackgroundWork(){
        OKTaskManager.getInstance().getBgTasks().execute(new Runnable() {
            @Override
            public void run() {
                uploadFileTask();
            }
        });
    }

    protected void submitBackgroundWork(){
        final Future future = OKTaskManager.getInstance().getBgTasks().submit(new UPloadCallable(getApplicationContext()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                future.cancel(true);
            }
        }, 3000);
    }

    protected void doMainThreadWork(Runnable runnable){
        OKTaskManager.getInstance().getMainThreadTasks().execute(runnable);
    }


    private void uploadFileTask() {

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
//        File file1 = new File(path+File.separator+"test1.jpg");
//        File file2 = new File(path+File.separator+"test2.jpg");
        File file1 = new File(path+File.separator+"test22.zip");
        File file2 = new File(path+File.separator+"test11.zip");
        if(file1.exists()) {
            Log.d("Retrofit","file1 exist = "+file1.getName());
        }

        final OkHttpClient.Builder okClient = new OkHttpClient().newBuilder();
        Retrofit client = new Retrofit.Builder().baseUrl("http://10.23.51.41:3960").client(okClient.build()).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface service = client.create(ApiInterface.class);

//        // 1) 여러 파일 업로드 MultipartBody.Builder
//        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        requestBodyBuilder.addFormDataPart("userfile", file1.getName(),
//                RequestBody.create(MediaType.parse("image/*"), file1));
//        requestBodyBuilder.addFormDataPart("userfile", file2.getName(),
//                RequestBody.create(MediaType.parse("image/*"), file2));
//        Call<BaseModel> call = service.reqUploadMultipleImage(requestBodyBuilder.build());

        // 2)  파일 업로드  MultipartBody.Part
//        RequestBody fileBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("userfile", file1.getName(), fileBody);
//        RequestBody fileBody2 =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file2);
//        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("userfile", file2.getName(), fileBody2);
//        Call<BaseModel> call = service.reqUploadMultipleImage(filePart, filePart2);

        // 3) MultipartBody.PartMap   <= 테스트안됨.
//        Map<String, RequestBody> params = new HashMap<>();
//        params.put("userfile1", createRequestBody("userfile1"));
//        params.put("userfile2", createRequestBody("userfile2"));
//
//        Map<String, RequestBody> files = new HashMap<>();
//        for (int pos = 0; pos < 2; pos++) {
//            RequestBody requestBody = createRequestBody(pos == 0?file1 : file2);
//            files.put("userfile" + ( pos == 0 ? 1:2 ), requestBody);
//        }
//        Call<BaseModel> call = service.reqUploadMultipleImageMap(params, files);

        // 4)  Progress 단일파일 업로드 여러개 파일은 아니다.
        ProgressRequestBody fileBody = new ProgressRequestBody(file1, uploadCallback);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("userfile", file1.getName(), fileBody);
        Call<BaseModel> call = service.reqUploadImageProgress(filePart);

        try {
            Response<BaseModel> result = call.execute();
            if(result.isSuccessful() && result.body().retVal == 1){
                doMainThreadWork(new Runnable() {
                    @Override
                    public void run() {
                        upload.setText("UPload Completed");
                        Log.d("Retrofit"," Thread test - "+ "UPload Completed");
                    }
                });
            } else {
                doMainThreadWork(new Runnable() {
                    @Override
                    public void run() {
                        upload.setText("UPload Failure");
                        Log.d("Retrofit"," Thread test - "+ "UPload Failure");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ProgressRequestBody.UploadCallbacks uploadCallback = new ProgressRequestBody.UploadCallbacks() {
        @Override
        public void onProgressUpdate(int percentage) {
            progressBar.setProgress(percentage);
        }

        @Override
        public void onError() {

        }

        @Override
        public void onFinish() {
            progressBar.setProgress(100);
        }
    };
}
