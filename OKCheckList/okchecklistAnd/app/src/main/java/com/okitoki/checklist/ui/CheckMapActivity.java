package com.okitoki.checklist.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.okitoki.checklist.R;
import com.okitoki.checklist.android.GpsInfo;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.ui.mapapi.Item;
import com.okitoki.checklist.utils.Logger;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by okc on 2016-07-24.
 */
public class CheckMapActivity extends CoreActivity implements MapView.MapViewEventListener {

    private static final String LOG_TAG = "CheckMapActivity";

    private Toolbar toolbar_actionbar;
    private MapView mMapView;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

    double latitude =0; // 위도
    double longitude = 0; // 경도
    int radius = 6000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
    int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
    String apikey = AppConst.API_KEY;

    boolean isInitialized = true;

    // GPSTracker class
    private GpsInfo gps;
    private DBManager dbMgr = null;
    private int mCartInfoId = -1 ;
    T_CART_INFO cartInfo = new T_CART_INFO();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gps_mart_search);

        toolbar_actionbar = ((Toolbar)findViewById(R.id.toolbar_actionbar));
        toolbar_actionbar.setTitle(R.string.menu_mode_region_select);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        spin_mart_filter = (Spinner)findViewById(R.id.spin_mart_filter);

        dbMgr = new DBManager();
        mCartInfoId = getIntent().getIntExtra("cart_id", 0);
        if(mCartInfoId > 0) {
            cartInfo = dbMgr.getCartInfo(mCartInfoId);
            Log.d("CheckMap", "Checkmap CartId = " + mCartInfoId);
        }
        cartInfo.setCart_title( getIntent().getStringExtra("title") );
        cartInfo.setAddress_latitude( getIntent().getDoubleExtra("lati", 0) );
        cartInfo.setAddress_longitude( getIntent().getDoubleExtra("long", 0) );

        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(AppConst.API_KEY);
        mMapView.setMapViewEventListener(this);
//        mMapView.setPOIItemEventListener(this);
//        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        hideSoftKeyboard(); // 키보드 숨김

//        setSpinner(spin_mart_filter, R.array.mart_gps_list, mOnMartItemSelectedListener);

        findViewById(R.id.btn_current_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // 현재 위치 표시
                gps = new GpsInfo(com.okitoki.checklist.ui.CheckMapActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 0, true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMarker(latitude, longitude);
                        }
                    },500);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });
        findViewById(R.id.btn_save_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( latitude > 0 && longitude > 0 ){
                    Intent i = new Intent();
                    i.putExtra("lati",latitude);
                    i.putExtra("long",longitude);
                    setResult(RESULT_OK,i);
                    finish();
                } else {
                    Toast.makeText(com.okitoki.checklist.ui.CheckMapActivity.this, "위치를 못찾았다 다시해", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(com.okitoki.checklist.ui.CheckMapActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMarker(double latitude, double longitude) {

        mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제

        String title ;
        if(cartInfo.getCart_title() != null &&
            cartInfo.getCart_title().length() > 0 ){
            title = cartInfo.getCart_title();
        } else {
            title = "현재위치";
        }

        Log.d("CheckMap","ShowMarker lat long = " + latitude + " / " +longitude);
        // 저장된 위치 표시
        MapPointBounds mapPointBounds = new MapPointBounds();

        // TODO 이마트 , 홈플러스 , 롯데마트 , 코스트코
        // 이외 다른 검색된것들 제외.  클릭할대 체크하여 넘길수있는 지점만 디테일화면 이동.

        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName(title);
        poiItem.setTag(0);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        poiItem.setMapPoint(mapPoint);
        mapPointBounds.add(mapPoint);
        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        poiItem.setCustomImageAutoscale(true);
        poiItem.setCustomImageAnchor(0.5f, 1.0f);

        mMapView.addPOIItem(poiItem);

//            mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        if(isInitialized){
            isInitialized = false;
            MapPOIItem[] poiItems = mMapView.getPOIItems();
            if (poiItems.length > 0) {
                mMapView.selectPOIItem(poiItems[0], true);
            }
        }
    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        gps = new GpsInfo(com.okitoki.checklist.ui.CheckMapActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            if(cartInfo.getAddress_longitude() ==0 || cartInfo.getAddress_latitude() ==0){
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 0, true);
                showMarker(latitude, longitude);
            } else {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(cartInfo.getAddress_latitude(),cartInfo.getAddress_longitude()), 0, false);
                showMarker(cartInfo.getAddress_latitude(), cartInfo.getAddress_longitude());
            }

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

//        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        Logger.d("MartGPSSearch", "onMapViewCenterPointMoved " + mapCenterPoint);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewLongPressed " + mapPoint);

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewSingleTapped " + mapPoint.getMapPointGeoCoord().latitude);
        this.latitude = mapPoint.getMapPointGeoCoord().latitude;
        this.longitude = mapPoint.getMapPointGeoCoord().longitude;
        showMarker(latitude, longitude);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewDragEnded " + mapPoint);
//        latitude = mapPoint.getMapPointGeoCoord().latitude;
//        longitude =  mapPoint.getMapPointGeoCoord().longitude;
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewMoveFinished " + mapPoint);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        Logger.d("MartGPSSearch", "onMapViewZoomLevelChanged zoomLevel " + zoomLevel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
