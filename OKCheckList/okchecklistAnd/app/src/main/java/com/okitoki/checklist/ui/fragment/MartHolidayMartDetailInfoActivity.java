package com.okitoki.checklist.ui.fragment;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.holiday.RestMartInfo;
import com.okitoki.checklist.holiday.RestUtil;
import com.okitoki.checklist.okperm.OKPermission;
import com.okitoki.checklist.ui.base.HolidayCoreActivity;
import com.okitoki.checklist.ui.decorators.EventDecorator;
import com.okitoki.checklist.ui.decorators.OneDayDecorator;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.PreferUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by ojungwon on 2015-04-08.
 */
public class MartHolidayMartDetailInfoActivity extends HolidayCoreActivity {

    public static final String TAG = MartHolidayMartDetailInfoActivity.class.getSimpleName();

    public MartHolidayMartDetailInfoActivity() {}

    @Bind(R.id.tv_fav_item_mart_logo)
    TextView tv_fav_item_mart_logo;
    @Bind(R.id.tv_fav_item_mart_name)
    TextView tv_fav_item_mart_name;
    @Bind(R.id.iv_fav_item_mart_status)
    TextView iv_fav_item_mart_status;
    @Bind(R.id.tv_mart_info_address)
    TextView tv_mart_info_address;
    @Bind(R.id.tv_mart_info_phone)
    TextView tv_mart_info_phone;
    @Bind(R.id.tv_mart_info_opentime)
    TextView tv_mart_info_opentime;
    @Bind(R.id.tv_mart_info_restdate_info)
    TextView  tv_mart_info_restdate_info;

    @Bind(R.id.iv_fav_item_mart_fav)
    ImageView iv_fav_item_mart_fav;

//    @Bind(R.id.nMapView)
//    NMapView mMapView;
//    private NMapController mMapController;
//    private NMapViewerResourceProvider mMapViewerResourceProvider;
//    private NMapPOIdataOverlay mFloatingPOIdataOverlay;
//    private NMapPOIitem mFloatingPOIitem;
//    private NMapOverlayManager mOverlayManager;

    @Bind(R.id.BTN_map_view)
    Button btnMapView;
    @Bind(R.id.BTN_calendar_view)
    Button btnCalendarView;
    @Bind(R.id.calendar_view)
    RelativeLayout rlCalendarView;
    @Bind(R.id.bg_view)
    RelativeLayout rlBgView;
    @Bind(R.id.llCalMapContainer)
    LinearLayout llCalMapContainer;
    @Bind(R.id.thisMonth)
    TextView thisMonth;

    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Bind(R.id.ll_main_content_cardview)
    CardView cardView;

    @Bind(R.id.daummap_view)
    RelativeLayout daummap_view_layout;
    net.daum.mf.map.api.MapView mapView;

//    GoogleMap googlemap;
//    LatLng geoData;

    RestMartInfo martInfo;
    int reIdBlueOpen;
    int reIdRedClosed;

    boolean isFavView;
    boolean isGpsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.1f; // 투명도 0 ~ 1
        getWindow().setAttributes(layoutParams);

        // 삽입 광고를 만듭니다.

        martInfo = EventBus.getDefault().removeStickyEvent(RestMartInfo.class);
        isFavView = getIntent().getBooleanExtra(AppConst.INTENT_EXTRA_IS_FAV_VIEW,false);
        isGpsView = getIntent().getBooleanExtra(AppConst.INTENT_EXTRA_IS_GPS_VIEW,false);

        setContentView(R.layout.activity_dialog_mart_detailinfo);
        ButterKnife.bind(this);


        // 리스트뷰의 아이템뷰들과 대응되는 View가 있으면 넣어준다. 애니효과 !!
//        ViewCompat.setTransitionName(tv_fav_item_mart_name, getString(R.string.transition_title));
//        ViewCompat.setTransitionName(tv_fav_item_mart_logo, getString(R.string.transition_body));
//        ViewCompat.setTransitionName(iv_fav_item_mart_status, getString(R.string.transition_date));
//        ViewCompat.setTransitionName(iv_fav_item_mart_fav, getString(R.string.transition_star));

        Animation showAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_detail_scale_in);
        cardView.setAnimation(showAnim);

        initFirst();
    }

    private void initFirst() {
        if (martInfo ==null) return;

        // Googlemap
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.googlemap);
//        mapFragment.getMapAsync(this);

        if(martInfo!=null && martInfo.getPointCode() != 0){
            reIdRedClosed = getResources().getColor(R.color.red_color);
            reIdBlueOpen = getResources().getColor(R.color.fab_blue_color);

            AUtil.setViewFavRestMartIcon(tv_fav_item_mart_logo, martInfo.getMartCode());
            tv_fav_item_mart_name.setText(martInfo.getPointName());

            if(AUtil.isHolidayMartToday(martInfo, Calendar.getInstance())){
                iv_fav_item_mart_status.setBackgroundResource(R.drawable.selector_purchase_completed);
                iv_fav_item_mart_status.setTextColor(reIdRedClosed);
                iv_fav_item_mart_status.setText( R.string.fav_mart_closed );
                iv_fav_item_mart_status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            } else {
                iv_fav_item_mart_status.setBackgroundResource(R.drawable.selector_mart_open_status);
                iv_fav_item_mart_status.setTextColor(reIdBlueOpen);
                iv_fav_item_mart_status.setText(R.string.fav_mart_open);
                iv_fav_item_mart_status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            }

            tv_mart_info_restdate_info.setText("1) 휴무일      :   " +martInfo.getRestDateInfo());
//            if(martInfo.getMartCode() == RestUtil.MART_CODE_EMART){
//            } else  if(martInfo.getMartCode() == RestUtil.MART_CODE_HOMEPLUS){
//                tv_mart_info_opentime.setText("2) 영업시간 : " +martInfo.getOpenHours());
//            } else  if(martInfo.getMartCode() == RestUtil.MART_CODE_LOTTE){
//                tv_mart_info_opentime.setText("2) 영업시간 : " +martInfo.getOpenHours());
//            } else  if(martInfo.getMartCode() == RestUtil.MART_CODE_COSTCO){
//
//                tv_mart_info_opentime.setText("2) 영업시간 : " +martInfo.getOpenHours());
//            }
            tv_mart_info_opentime.setText("2) 영업시간   :   " +martInfo.getOpenHours());
            tv_mart_info_phone.setText("3) 대표전화   :   " +martInfo.getPhone());
            tv_mart_info_address.setText("4) 주소          :   " + martInfo.getLocation());

            // 2) usersfavmartlist Update
            if (martInfo.favusers.containsKey(getUid())) {
                iv_fav_item_mart_fav.setImageResource(R.drawable.ic_favorites_on);
            } else {
                iv_fav_item_mart_fav.setImageResource(R.drawable.ic_favorites_off);
            }

            // 네이버지도 초기화
//            // create map view
////            mMapView = new NMapView(this);
//
//// 기존 API key 방식은 deprecated 함수이며, 2016년 말까지만 사용 가능합니다.
//// mMapView.setApiKey(API_KEY);
//
//// set Client ID for Open MapViewer Library
//            mMapView.setClientId("wASKsyyrWrU72MQwqVmG");
//// set the activity content to the map view
//// initialize map view
//            mMapView.setClickable(true);
//            mMapView.setEnabled(true);
//            mMapView.setFocusable(true);
//            mMapView.setFocusableInTouchMode(true);
//            mMapView.requestFocus();
//// register listener for map state changes
////            mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
////            mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
//// use map controller to zoom in/out, pan and set map center, zoom level etc.
//            mMapController = mMapView.getMapController();
//// create resource provider
//            mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
//// create overlay manager
//            mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
//            int markerId = NMapPOIflagType.PIN;
//// register callout overlay listener to customize it.
//            mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
//            // register callout overlay view listener to customize it.
//            mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);
//
//// set POI data
//            NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
//            poiData.beginPOIdata(2);
//            poiData.addPOIitem(martInfo.getLatitude(),martInfo.getLongitude(), martInfo.getPointName(), markerId, 0);
//            poiData.endPOIdata();
//
//// create POI data overlay
//            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
//            poiDataOverlay.showAllPOIdata(0);

            // 구글 지도
            //geoData  = new LatLng(martInfo.getLatitude(), martInfo.getLongitude());
        }

        if( isGpsView ){
            llCalMapContainer.setVisibility(View.GONE);
        } else {
            llCalMapContainer.setVisibility(View.VISIBLE);
            boolean isShowMap = PreferUtil.getPreferenceBoolean("selectMapView", getApplicationContext());
            if(isShowMap){
                initMapView();
                btnMapView.setBackgroundResource(R.color.main_accent_color);
                btnCalendarView.setBackgroundResource(R.color.main_selector_button_bg_pressed);
            } else {
                btnMapView.setBackgroundResource(R.color.main_selector_button_bg_pressed);
                btnCalendarView.setBackgroundResource(R.color.main_accent_color);
                rlCalendarView.setVisibility(View.VISIBLE);
            }

            // 캘린더 초기화.
            OneDayDecorator oneDayDecorator = new OneDayDecorator();

            Calendar today = Calendar.getInstance();
            materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
            materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            materialCalendarView.setSelectionColor( getResources().getColor(R.color.google_red) );
            materialCalendarView.setAllowClickDaysOutsideCurrentMonth(false);
            materialCalendarView.setDynamicHeightEnabled(true);
    //        materialCalendarView.setTileHeightDp(37);
            materialCalendarView.setTileWidthDp(49);
            materialCalendarView.setTopbarVisible(false);
            materialCalendarView.setPagingEnabled(false);
            materialCalendarView.setCurrentDate(today);
            materialCalendarView.addDecorators( oneDayDecorator );

            CalendarDay day = CalendarDay.from(today);
            List<CalendarDay> days = new ArrayList<>();
            days.add(day);
            materialCalendarView.addDecorator(new EventDecorator(Color.BLUE, days));

            thisMonth.setText( (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월");

            setHolidayDate();

        }
    }

    private void initMapView() {

        if(mapView==null){
            mapView = new MapView(this);
            mapView.setDaumMapApiKey(AppConst.API_KEY);
            daummap_view_layout.removeAllViews();
            daummap_view_layout.addView(mapView);
            daummap_view_layout.setVisibility(View.VISIBLE);

    //            // 다음 지도 초기화
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(martInfo.getLatitude(),martInfo.getLongitude());
            mapView.setMapCenterPointAndZoomLevel(mapPoint , 2, false);
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(martInfo.getPointName());
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setShowDisclosureButtonOnCalloutBalloon(true);
            mapView.addPOIItem(marker);
        }
    }

    private void setHolidayDate() {

        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> holidays = AUtil.getHolidayofMonth(martInfo);
        if(holidays.size() > 0){
            for (Integer holiday : holidays ) {
                cal.set(Calendar.DAY_OF_MONTH, holiday);
                materialCalendarView.setDateSelected(cal,true);
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(JavaUtil.isDoubleClick()) return;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        if(mapView !=null)
            mapView.setVisibility(View.GONE);
        if(rlBgView!=null)
            rlBgView.setBackgroundColor(getResources().getColor(R.color.transparent));
//        if(! isFavView){
            overridePendingTransition(0,0);
            Animation showAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_detail_scale_out);
            showAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            if(cardView!=null)
               cardView.startAnimation(showAnim);
//        } else { // Fav뷰에서는 SharedElement 효과때문에
//            super.onBackPressed();
//        }
    }

    @OnClick(R.id.BTN_map_view)
    public void onClickBtnMapview(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new OKPermission(this)
                    .setIsRetryPermRequest(true)
                    .setPermissionListener(permissionlistener)
                    .setDeniedTitleText(R.string.alert_title)
                    .setAllDeniedMessages("지도화면을 표시하기 위해 위치권한이 필요합니다.")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        } else {
           selectMapview();
        }
    }

    private void selectMapview() {

        initMapView();
        btnMapView.setBackgroundResource(R.color.main_accent_color);
        btnCalendarView.setBackgroundResource(R.color.main_selector_button_bg_pressed);
        daummap_view_layout.setVisibility(View.VISIBLE);
        rlCalendarView.setVisibility(View.GONE);

        PreferUtil.setSharedPreference("selectMapView", true , getApplicationContext());
    }

    @OnClick(R.id.BTN_calendar_view)
    public void onClickBtnCalendarview(){
        btnMapView.setBackgroundResource(R.color.main_selector_button_bg_pressed);
        btnCalendarView.setBackgroundResource(R.color.main_accent_color);
        daummap_view_layout.setVisibility(View.GONE);
        rlCalendarView.setVisibility(View.VISIBLE);

        PreferUtil.setSharedPreference("selectMapView", false , getApplicationContext());
    }

    @OnClick(R.id.cv_cd_cart_layout)
    public void onClickLayout(){
    }
    @OnClick(R.id.bg_view)
    public void onClickBgView(){
        onBackPressed();
    }

    @OnClick(R.id.iv_fav_item_mart_fav)
    public void onClickMArtFav(){
        // 2) usersfavmartlist Update

        if(martInfo ==null || JavaUtil.isDoubleClick()) return;

        if (martInfo.favusers.containsKey(getUid())) {
            DatabaseReference favRef = mDatabase.getRef()
                    .child("usersfavmartlist").child(getUid()).child(martInfo.getRefkey());
            favRef.removeValue();
            martInfo.favusers.remove(getUid());
            iv_fav_item_mart_fav.setImageResource(R.drawable.ic_favorites_off);
        } else {
            writeNewPost(martInfo.getRefkey(), martInfo);
            martInfo.favusers.put(getUid(), true);
            iv_fav_item_mart_fav.setImageResource(R.drawable.ic_favorites_on);

            OKCartApplication.martRestAddCount++;

        }

        String selectMart = AUtil.getMartNameByCode(martInfo.getMartCode());
        // 1) Each MartList Update
        if(martInfo.getMartCode() == RestUtil.MART_CODE_COSTCO){
            DatabaseReference martListRef= mDatabase.child(selectMart).child(martInfo.getRefkey());
            onFavClicked(martListRef);
        } else if ( martInfo.getMartCode() == RestUtil.MART_CODE_HOMEPLUS && "경기/인천".equalsIgnoreCase(martInfo.getRegionName() ) ){
            DatabaseReference martListRef= mDatabase.child(selectMart).child("경기").child("인천").child(martInfo.getRefkey());
            onFavClicked(martListRef);
        } else {
            DatabaseReference martListRef= mDatabase.child(selectMart).child(martInfo.getRegionName()).child(martInfo.getRefkey());
            onFavClicked(martListRef);
        }

        if(! isFavView){
            MartHolidayMainFavListFragment.isRefreshAdapter = true;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery;
        if("costco".equalsIgnoreCase(martInfo.getRegionName()) ){
            recentPostsQuery = databaseReference.child(martInfo.getRegionName());
        } else {
            recentPostsQuery = databaseReference.child(martInfo.getRegionName()).child(martInfo.getRefkey());
        }
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(daummap_view_layout!=null){
            daummap_view_layout.removeAllViews();
            mapView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void writeNewPost(String key, RestMartInfo postData) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
//        String key = mDatabase.child("userfavmart").push().getKey();
        String uId = getUid();

        Map<String, Object> postValues = postData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/usersfavmartlist/" + uId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    // [START post_stars_transaction]
    private void onFavClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                RestMartInfo p = mutableData.getValue(RestMartInfo.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.favusers.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.favusersCount = p.favusersCount - 1;
                    p.favusers.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.favusersCount = p.favusersCount + 1;
                    p.favusers.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

//    @Override
//    public void onMapReady(final GoogleMap map) {
//        googlemap = map;
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: 권한이 없다 요청해라. OKPerm !!
//            return;
//        }
//        googlemap.setMyLocationEnabled(true);
//
//        Marker seoul = googlemap.addMarker(new MarkerOptions().position(geoData)
//                .title(martInfo.getPointName()));
//
//        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom( geoData, 15));
//
//        googlemap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//
//    }

    OKPermission.PermissionListener permissionlistener = new OKPermission.PermissionListener() {
        @Override
        public void onPermissionGranted(ArrayList<String> deniedPermissions) {
            selectMapview();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            onClickBtnCalendarview();
        }

    };
}
