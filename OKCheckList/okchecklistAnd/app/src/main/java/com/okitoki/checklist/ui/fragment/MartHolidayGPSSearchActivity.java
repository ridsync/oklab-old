package com.okitoki.checklist.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.okitoki.checklist.R;
import com.okitoki.checklist.android.GpsInfo;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;
import com.okitoki.checklist.ui.base.HolidayCoreActivity;
import com.okitoki.checklist.ui.mapapi.Item;
import com.okitoki.checklist.ui.mapapi.OnFinishSearchListener;
import com.okitoki.checklist.ui.mapapi.Searcher;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by okc on 2016-07-24.
 */
public class MartHolidayGPSSearchActivity extends HolidayCoreActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    private static final String LOG_TAG = "SearchDemoActivity";

    private Toolbar toolbar_actionbar;
    private MapView mMapView;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

    String query = "이마트";
    int martCode = RestUtil.MART_CODE_EMART;
    double latitude =0; // 위도
    double longitude = 0; // 경도
    int radius = 6000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
    int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
    String apikey = AppConst.API_KEY;

    Spinner spin_mart_filter;

    boolean isInitialized = true;

    // GPSTracker class
    private GpsInfo gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gps_mart_search);

        toolbar_actionbar = ((Toolbar)findViewById(R.id.toolbar_actionbar));
        toolbar_actionbar.setTitle(R.string.menu_mode_mart_search);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        spin_mart_filter = (Spinner)findViewById(R.id.spin_mart_filter);

        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(AppConst.API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        hideSoftKeyboard(); // 키보드 숨김

//        setSpinner(spin_mart_filter, R.array.mart_gps_list, mOnMartItemSelectedListener);
    }

    private synchronized void startSearch() {

        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchKeyword(getApplicationContext(), "마트", latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                showResult(itemList); // 검색 결과 보여줌
            }

            @Override
            public void onFail() {
                showToast("마트검색을 실패했습니다.");
            }
        });

    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return null;
    }

    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

            private final View mCalloutBalloon;

            public CustomCalloutBalloonAdapter() {
                mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
            }

            @Override
            public View getCalloutBalloon(MapPOIItem poiItem) {
                if (poiItem == null) return null;
                Item item = mTagItemMap.get(poiItem.getTag());
                if (item == null) return null;
                ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
                TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
                textViewTitle.setText(item.title);
                TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
                textViewDesc.setText(item.address);
                //TODO 각 마트 아이콘 표시.
                if( item.title.contains("이마트") ){
                    imageViewBadge.setImageResource(R.drawable.ic_emart);
                } else if (item.title.contains("홈플러스")) {
                    imageViewBadge.setImageResource(R.drawable.ic_homeplus);
                } else if (item.title.contains("롯데마트")) {
                    imageViewBadge.setImageResource(R.drawable.ic_lotte);
                } else if (item.title.contains("코스트코")) {
                    imageViewBadge.setImageResource(R.drawable.ic_costco);
                }
                return mCalloutBalloon;
            }

            @Override
            public View getPressedCalloutBalloon(MapPOIItem poiItem) {
                return null;
            }

        }

        private void hideSoftKeyboard() {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);
        }

        private void showToast(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MartHolidayGPSSearchActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showResult(List<Item> itemList) {
            MapPointBounds mapPointBounds = new MapPointBounds();

            // TODO 이마트 , 홈플러스 , 롯데마트 , 코스트코
            // 이외 다른 검색된것들 제외.  클릭할대 체크하여 넘길수있는 지점만 디테일화면 이동.
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                if(item.title != null
//                        && item.title.contains(query)
                        && item.category.contains("대형마트")
                        && ( item.title.contains("이마트 ")
                        ||  item.title.contains("롯데마트 ") ||  item.title.contains("홈플러스 ") ||  item.title.contains("코스트코 ") )
                        ) {

                    MapPOIItem poiItem = new MapPOIItem();
                    poiItem.setItemName(item.title);
                    poiItem.setTag(i);
                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
                    poiItem.setMapPoint(mapPoint);
                    mapPointBounds.add(mapPoint);
                    poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
                    poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                    poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
                    poiItem.setCustomImageAutoscale(true);
                    poiItem.setCustomImageAnchor(0.5f, 1.0f);

                    mMapView.addPOIItem(poiItem);
                    mTagItemMap.put(poiItem.getTag(), item);
                }
            }

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
        gps = new GpsInfo(MartHolidayGPSSearchActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 3, false);
            startSearch();

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

//        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

        if(JavaUtil.isDoubleClick()) return ;

        Item item = mTagItemMap.get(mapPOIItem.getTag());
//        StringBuilder sb = new StringBuilder();
//        sb.append("title=").append(item.title).append("\n");
//        sb.append("imageUrl=").append(item.imageUrl).append("\n");
//        sb.append("address=").append(item.address).append("\n");
//        sb.append("newAddress=").append(item.newAddress).append("\n");
//        sb.append("zipcode=").append(item.zipcode).append("\n");
//        sb.append("phone=").append(item.phone).append("\n");
//        sb.append("category=").append(item.category).append("\n");
//        sb.append("longitude=").append(item.longitude).append("\n");
//        sb.append("latitude=").append(item.latitude).append("\n");
//        sb.append("distance=").append(item.distance).append("\n");
//        sb.append("direction=").append(item.direction).append("\n");

        if( ! item.title.contains("점") || item.title.contains("이마트트레")) return;

        String pointName = item.title.replace("이마트 ","").trim();
        if(item.title.contains("이마트 ")){
            pointName = item.title.replace("이마트","").trim();
            martCode = RestUtil.MART_CODE_EMART;
        }else if(item.title.contains("롯데마트")){
            pointName = item.title.replace("롯데마트","").trim();
            martCode = RestUtil.MART_CODE_LOTTE;
        }else if(item.title.contains("홈플러스")){
            pointName = item.title.replace("홈플러스","").trim();
            martCode = RestUtil.MART_CODE_HOMEPLUS;
        } else if(item.title.contains("코스트코")){
            pointName = item.title.replace("코스트코","").trim();
            martCode = RestUtil.MART_CODE_COSTCO;
        }

        String address = item.newAddress;

        Query ref;
        if(martCode == RestUtil.MART_CODE_COSTCO){
            ref = mDatabase.child(AUtil.getMartNameByCode(martCode)).orderByChild("pointName").equalTo(pointName);
        } else if ( martCode == RestUtil.MART_CODE_HOMEPLUS && (address.contains("인천") || address.contains("경기")) ){
            ref = mDatabase.child(AUtil.getMartNameByCode(martCode)).child("경기").child("인천").orderByChild("pointName").equalTo(pointName);
        } else if (martCode == RestUtil.MART_CODE_EMART && (address.contains("경남") || address.contains("경북"))){
            ref = mDatabase.child(AUtil.getMartNameByCode(martCode)).child("경상").orderByChild("pointName").equalTo(pointName);
        } else if (martCode == RestUtil.MART_CODE_EMART && (address.contains("충남") || address.contains("충북"))){
            ref = mDatabase.child(AUtil.getMartNameByCode(martCode)).child("충청").orderByChild("pointName").equalTo(pointName);
        } else {
            String convAddress = address.substring(0,2);
            ref = mDatabase.child(AUtil.getMartNameByCode(martCode)).child(convAddress).orderByChild("pointName").equalTo(pointName);
        }
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        HashMap<String,HashMap<String, Object>> favMarts = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue(true);

                        RestMartInfo martInfo = null;
                        if(favMarts != null && favMarts.size() > 0){
                            Iterator<String> iterator = favMarts.keySet().iterator();
                            martInfo = new RestMartInfo();
                            while (iterator.hasNext()) {
                                String key = iterator.next(); // 키 얻기
                                HashMap<String, Object> data = favMarts.get(key);
                                martInfo.setMartCode( ((Long)data.get("martCode")).intValue() );
                                martInfo.setPointCode( ((Long)data.get("pointCode")).intValue() );
                                martInfo.setPointName( (String) data.get("pointName") );
                                martInfo.setRegionName( (String)data.get("regionName") );
                                martInfo.setRestDateInfo( (String)data.get("restDateInfo") );
                                martInfo.setRestDateInfoNext( (String) data.get("restDateInfoNext") );
                                martInfo.setOpenHours( (String) data.get("openHours"));
                                martInfo.setPhone( (String) data.get("phone"));
                                martInfo.setLocation( (String) data.get("location"));
                                martInfo.setLatitude( (Double)data.get("latitude") );
                                martInfo.setLongitude( (Double)data.get("longitude") );
                                martInfo.setFavusersCount( ((Long)data.get("favusersCount")).intValue()); ;
                                 if(data.get("favusers") !=null){
                                    Map<String, Boolean> ddd = (Map<String, Boolean>)data.get("favusers");
                                    martInfo.setFavusers( ddd ) ;
                                    martInfo.setFavusersCount( ddd.size());
                                }
                                martInfo.setRefkey(key);
                            }
                        }

//                        RestMartInfo favMartInfo = ()favMarts.get(favMarts.);

                        // TODO for 문으로 지점명매칭... 별수없다...
                        Logger.d("tag", "getMartInfoGPS:onDataChange martInfo = " + martInfo);
                        if(martInfo!=null){
                            EventBus.getDefault().postSticky(martInfo);
                            Intent i = new Intent(mContext, MartHolidayMartDetailInfoActivity.class);
                            i.putExtra(AppConst.INTENT_EXTRA_IS_GPS_VIEW, true);
                            startActivityForResult(i,0);
                            overridePendingTransition(0,0);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Logger.d("tag", "getMartInfoGPS:onCancelled " + databaseError.toException());
                    }
                });

    }

    @Override
    @Deprecated
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Logger.d("MartGPSSearch", "onPOIItemSelected " + mapPOIItem.toString());
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
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewDragEnded " + mapPoint);
        latitude = mapPoint.getMapPointGeoCoord().latitude;
        longitude =  mapPoint.getMapPointGeoCoord().longitude;
        startSearch();
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Logger.d("MartGPSSearch", "onMapViewMoveFinished " + mapPoint);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        Logger.d("MartGPSSearch", "onMapViewZoomLevelChanged zoomLevel " + zoomLevel);
    }

    private void setSpinner(Spinner spinner ,final int arrResId, AdapterView.OnItemSelectedListener listener) {

        /* 스피너 Prompt 없애고 Index 를 1부터 시작하기 위한 커스텀 Adpater*/
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.item_spinner, parent, false);
                TextView label = (TextView) row.findViewById(android.R.id.text1);
                label.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16); // dp
//                if (position == 0) {
//                    label.setText(R.string.btn_select);
//                    label.setTextColor(getResources().getColor(R.color.text_color_3));
//                } else {
                label.setTextColor(getResources().getColor(R.color.white));
                label.setText(getItem(position));
//                }

                return row;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                ((TextView)v).setTextColor(getResources().getColor(R.color.white));
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        String[] arrRes = getResources().getStringArray(arrResId);
        for (int i = 0; i < arrRes.length; i++) {
            String res = arrRes[i];
            spnAdapter.add(res);
        }
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spnAdapter);
        spinner.setOnItemSelectedListener(listener);
    }

    private AdapterView.OnItemSelectedListener mOnMartItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(i == 0){
                query = "이마트";
                martCode = RestUtil.MART_CODE_EMART;
            } else if(i == 1){
                query = "홈플러스";
                martCode = RestUtil.MART_CODE_HOMEPLUS;
            } else if(i == 2){
                query = "롯데마트";
                martCode = RestUtil.MART_CODE_LOTTE;
            } else if(i == 3){
                query = "코스트코";
                martCode = RestUtil.MART_CODE_COSTCO;
            }
            startSearch();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

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
