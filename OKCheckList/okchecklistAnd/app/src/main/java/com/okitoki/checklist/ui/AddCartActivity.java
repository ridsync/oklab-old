package com.okitoki.checklist.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.database.DBManager;
import com.okitoki.checklist.database.model.PhotoEvent;
import com.okitoki.checklist.database.model.T_CART_INFO;
import com.okitoki.checklist.database.model.T_CART_ITEM;
import com.okitoki.checklist.ui.adapter.BaseRecyclerExtendsAdapter;
import com.okitoki.checklist.ui.adapter.CartItemRecvAdapter;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.ui.components.CropCircleTransform;
import com.okitoki.checklist.ui.components.OKDefaultToolTip;
import com.okitoki.checklist.ui.components.OKToolTip;
import com.okitoki.checklist.ui.fragment.AlertDialogFragment;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.PreferUtil;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.TourGuide;

/**@
 * Created by okc on 2015-06-20.
 */
@EActivity(R.layout.activity_ca_cart_add)
public class AddCartActivity extends CoreActivity implements AlertDialogFragment.OnClickListener {

    public static final int SCREEN_MODE_ADD = 0;
    public static final int SCREEN_MODE_MODIFY = 1;
    public static final int SCREEN_MODE_DELETE = 2;
    private int SCREEN_MODE = SCREEN_MODE_ADD;
    private int SCREEN_MODE_BEFORE = SCREEN_MODE_ADD;

    @ViewById Toolbar toolbar_actionbar;

    @ViewById EditText et_goods_name,et_prices, et_area, et_migrateDate ,et_location,
            et_address, et_around_status, et_fee,et_parking, et_usage ,et_etc , et_noise;

    @ViewById RadioGroup rg_address_score, rg_sunshine_score, rg_interior_score,rg_facility_score, rg_roomcnt_score,
    rg_bathroomcnt_score, rg_school_score,rg_traffic_score , rg_around_status_score;

    @ViewById CheckBox cb_mijung;

    @ViewById RelativeLayout ll_icon_layout_parent;

    @ViewById RecyclerView myRecycler;

    @ViewById Button btn_add_photo;
    @ViewById ImageView iv_add_gps;

    Animation showAnim;
    Animation hideAnim;

    private T_CART_INFO cartInfo = new T_CART_INFO();

    private boolean mIsModify = false ;
    private int mCartInfoId = -1 ;
    Calendar selectDay =null;

    private DBManager dbMgr = null;
    private int mCheckItemCount = 0;
    private boolean isTouring = false;
    boolean isUseTtts = false ;

    private UserListAdapter mListAdapter = null;
    ArrayList<String> arPhotoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        int mCartInfoId = getIntent().getIntExtra("cart_id", 0);
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        AUtil.hideIME(getApplicationContext(),et_address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @AfterViews
    public void doAfterViews(){
        Logger.d(this, "doAfterViews () ");
        dbMgr = new DBManager();

        setRecyclerView();
//        toolbar_actionbar.setNavigationIcon(R.drawable.abc_btn_check_material);
//        toolbar_actionbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        toolbar_actionbar.setTitle(R.string.menu_mode_add);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init Default
        selectDay = Calendar.getInstance();
        String date = AUtil.getDateExp(selectDay.getTime());
        et_migrateDate.setText(date);

        mCartInfoId = getIntent().getIntExtra("cart_id", 0);
        if (mCartInfoId > 0){ // 수정모드
            SCREEN_MODE = SCREEN_MODE_MODIFY;
            getCartItemDatas(mCartInfoId);
            AUtil.hideIME(mContext,et_goods_name);
            et_goods_name.clearFocus();
            //        if(et_item_title.length()>0)
//            et_item_title.setSelection(et_item_title.length());
        } else { // 일괄추가에서 아이템을 보냈을경우..
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    et_goods_name.requestFocus();
                    AUtil.showIME(mContext,et_goods_name);
                }
            },200);
        }
    }


    @Click(R.id.ll_icon_layout_parent)
    void onClickIconBg(View v){

    }
    @Click(R.id.btn_add_gps)
    void onClickGPS(View v){
        Bundle bundle = new Bundle();
        bundle.putInt("cart_id", cartInfo.getCart_id());
        bundle.putString("title", et_goods_name.getText().toString());
        bundle.putDouble("lati", cartInfo.getAddress_latitude());
        bundle.putDouble("long", cartInfo.getAddress_longitude());
        Intent i = new Intent();
        i.setClass(getApplicationContext(), CheckMapActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, 6565);
    }

    @Click(R.id.btn_add_photo)
    void onClickAddPhoto(View v){
        Bundle bundle = new Bundle();
        bundle.putInt(AppConst.BUNDLE_CAMERA_TYPE, 1);
        Intent i = new Intent();
        i.setClass(getApplicationContext(), PhotoActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, 1004);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1004) {
            if (resultCode == RESULT_OK && data != null) { // 사진찍기 완료
                // refresh
                arPhotoList.add( data.getStringExtra("photopath") );
                Log.d("PhotoResult", " photoPath = " +  data.getStringExtra("photopath"));
                cartInfo.setPhotoUris(new Gson().toJson(arPhotoList));
                mListAdapter.notifyDataSetChanged();
                mIsModify = true;
            }
        } else if (requestCode == 6565){
            if (resultCode == RESULT_OK && data != null) { // GPS 저장
                cartInfo.setAddress_latitude(data.getDoubleExtra("lati",0));
                cartInfo.setAddress_longitude(data.getDoubleExtra("long",0));
                mIsModify = true;
                iv_add_gps.setVisibility(View.VISIBLE);
                Toast.makeText(this, "GPS위치저장 완료", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click({R.id.tv_mart_icon1, R.id.tv_mart_icon2, R.id.tv_mart_icon3, R.id.tv_mart_icon4, R.id.tv_mart_icon5, R.id.tv_mart_icon6,
            R.id.tv_mart_text1, R.id.tv_mart_text2, R.id.tv_mart_text3, R.id.tv_mart_text4, R.id.tv_mart_text5, R.id.tv_mart_text6})
    void onClickIconMart(View v){
        int id = v.getId();
        String iconId = AppConst.SYMBOL_ICON_ID_GENERAL_MART;
        if (id == R.id.tv_mart_icon1 || id == R.id.tv_mart_text1){
            iconId = AppConst.SYMBOL_ICON_ID_EMART;
        } else if (id == R.id.tv_mart_icon2 || id == R.id.tv_mart_text2){
            iconId = AppConst.SYMBOL_ICON_ID_HOMEPLUS;
        }else if (id == R.id.tv_mart_icon3 || id == R.id.tv_mart_text3){
            iconId = AppConst.SYMBOL_ICON_ID_LOTTEMART;
        }else if (id == R.id.tv_mart_icon4 || id == R.id.tv_mart_text4){
            iconId = AppConst.SYMBOL_ICON_ID_GSHOME;
        }else if (id == R.id.tv_mart_icon5 || id == R.id.tv_mart_text5){
            iconId = AppConst.SYMBOL_ICON_ID_COSTCO;
        }else if (id == R.id.tv_mart_icon6 || id == R.id.tv_mart_text6){
            iconId = AppConst.SYMBOL_ICON_ID_GENERAL_MART;
        }
//        AUtil.setViewMartIcon(tv_mart_icon,iconId);
        ll_icon_layout_parent.setVisibility(View.GONE);
        mIsModify = true;
    }

    @Click(R.id.tv_mart_icon)
    void onClickSelectIcon(View v){

        AUtil.hideIME(getApplicationContext(), v);
        ll_icon_layout_parent.setVisibility(View.VISIBLE);

        Bundle bundle2 = new Bundle();
        bundle2.putString(AppConst.FA_PARAM_NORMAL, "normaltest");
        // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_CART_ADD_ICON_CHOICE, bundle2);

    }

        private void reloadActionButton(){
        if (mCheckItemCount > 0 ){
            SCREEN_MODE_BEFORE = SCREEN_MODE == SCREEN_MODE_ADD ? 0 : 1 ;
            SCREEN_MODE = SCREEN_MODE_DELETE;
            toolbar_actionbar.getMenu().getItem(0).setIcon(android.R.drawable.ic_menu_delete);
        } else {
            SCREEN_MODE = SCREEN_MODE_BEFORE;
            toolbar_actionbar.getMenu().getItem(0).setIcon(0);
            toolbar_actionbar.getMenu().getItem(0).setTitle(R.string.menu_mode_save);
        }
    }


    private void getCartItemDatas(int cartId) {
        if(cartId <= 0) return;

        toolbar_actionbar.setTitle(R.string.menu_mode_modify);
        this.cartInfo = dbMgr.getCartInfo(cartId);
        if (this.cartInfo.getCart_id() >0 ){
            Type type = new TypeToken<List<String>>(){}.getType();
            ArrayList<String> list = new Gson().fromJson(cartInfo.getPhotoList(),type);
            arPhotoList.addAll( list );
            mListAdapter.notifyDataSetChanged();
            et_goods_name.setText(cartInfo.getCart_title());
            et_prices.setText(cartInfo.getPrices());
            et_area.setText(cartInfo.getArea());
            et_migrateDate.setText(cartInfo.getMigrateDate());
            cb_mijung.setChecked(cartInfo.getIsMijung());
            et_location.setText(cartInfo.getLocation());
            et_address.setText(cartInfo.getAddress());
            rg_address_score.check(rg_address_score.getChildAt(cartInfo.getAddressScore()).getId());
            rg_sunshine_score.check(rg_sunshine_score.getChildAt(cartInfo.getSunshineRight()).getId());
            rg_interior_score.check(rg_interior_score.getChildAt(cartInfo.getInterior()).getId());
            rg_facility_score.check(rg_facility_score.getChildAt(cartInfo.getFacilityScore()).getId());
            rg_roomcnt_score.check(rg_roomcnt_score.getChildAt(cartInfo.getRoomsCnt()).getId());
            rg_bathroomcnt_score.check(rg_bathroomcnt_score.getChildAt(cartInfo.getBathroomsCnt()).getId());
            rg_school_score.check(rg_school_score.getChildAt(cartInfo.getSchoolScore()).getId());
            rg_traffic_score.check(rg_traffic_score.getChildAt(cartInfo.getTrafficScore()).getId());
            rg_around_status_score.check(rg_around_status_score.getChildAt(cartInfo.getAroundStatusScore()).getId());
            et_around_status.setText(cartInfo.getAroundStatus());
            et_fee.setText(cartInfo.getFee());
            et_parking.setText(cartInfo.getParking());
            et_usage.setText(cartInfo.getUsage());
            et_etc.setText(cartInfo.getEtc());
            et_noise.setText(cartInfo.getEt_noise());

        }

        if(cartInfo.getAddress_latitude() > 0
                && cartInfo.getAddress_longitude() > 0 ){
            iv_add_gps.setVisibility(View.VISIBLE);
        } else {
            iv_add_gps.setVisibility(View.GONE);
        }

        et_goods_name.addTextChangedListener(textWatcher);
        et_prices.addTextChangedListener(textWatcher);
        et_area.setText(cartInfo.getArea());
        et_migrateDate.addTextChangedListener(textWatcher);
        et_location.addTextChangedListener(textWatcher);
        et_address.addTextChangedListener(textWatcher);
        et_around_status.addTextChangedListener(textWatcher);
        et_fee.addTextChangedListener(textWatcher);
        et_around_status.addTextChangedListener(textWatcher);
        et_parking.addTextChangedListener(textWatcher);
        et_usage.addTextChangedListener(textWatcher);
        et_etc.addTextChangedListener(textWatcher);
        et_noise.addTextChangedListener(textWatcher);

        rg_address_score.setOnCheckedChangeListener(checkListener);
        rg_sunshine_score.setOnCheckedChangeListener(checkListener);
        rg_interior_score.setOnCheckedChangeListener(checkListener);
        rg_facility_score.setOnCheckedChangeListener(checkListener);
        rg_roomcnt_score.setOnCheckedChangeListener(checkListener);
        rg_bathroomcnt_score.setOnCheckedChangeListener(checkListener);
        rg_school_score.setOnCheckedChangeListener(checkListener);
        rg_traffic_score.setOnCheckedChangeListener(checkListener);
        rg_around_status_score.setOnCheckedChangeListener(checkListener);

    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mIsModify = true;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    RadioGroup.OnCheckedChangeListener checkListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mIsModify = true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_cart_add_menu, menu);
        return true;
    }

    /**
     * OptionMenu 아이템이 선택될 때 호출 된다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                if(mIsModify){
                    popUpNotifyCancel();
                } else {
                    finish();
                }
                return true;
            case R.id.menu_add_done:
                if(JavaUtil.isDoubleClick()) return true;

                if (SCREEN_MODE==SCREEN_MODE_ADD){
                    if (updateCartItemDatas()) {
                        AUtil.hideIME(getApplicationContext(), et_address);
                        setResult(RESULT_OK);
                        finish();
                    }
                } else if (SCREEN_MODE==SCREEN_MODE_MODIFY){ // Modify
                    if (updateCartItemDatas()) {
                        AUtil.hideIME(getApplicationContext(), et_address);
                        setResult(RESULT_OK);
                        finish();
                    }
                } else if (SCREEN_MODE==SCREEN_MODE_DELETE){

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean updateCartItemDatas() {
        boolean result = false;
        if (et_goods_name.getText().toString().length() <= 0) {
            Toast.makeText(mContext,"매물명을 입력하세요",Toast.LENGTH_SHORT).show();
            return result;
        } else if (et_prices.getText().toString().length() <= 0) {
            Toast.makeText(mContext,"분양가/매매가를 입력하세요",Toast.LENGTH_SHORT).show();
            return result;
        } else if (et_area.getText().toString().length() <= 0) {
            Toast.makeText(mContext,"면적을 입력하세요",Toast.LENGTH_SHORT).show();
            return result;
        } else if (cartInfo == null){
            cartInfo = new T_CART_INFO();
        }

        try {
            dbMgr.beginTransaction();

            cartInfo.setStore_id("100");
            cartInfo.setCart_title(et_goods_name.getText().toString());
            cartInfo.setBuy_date(selectDay.getTime());
            cartInfo.setPrices(et_prices.getText().toString());
            cartInfo.setArea(et_area.getText().toString());
            cartInfo.setIsMijung(cb_mijung.isChecked());
            cartInfo.setMigrateDate(et_migrateDate.getText().toString());
            cartInfo.setLocation(et_location.getText().toString()); // 위치
            cartInfo.setAddress(et_address.getText().toString()); // 주소

            String tag = (String)rg_address_score.findViewById(rg_address_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setAddressScore(Integer.valueOf(tag));
            String tag2 = (String)rg_sunshine_score.findViewById(rg_sunshine_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setSunshineRight(Integer.valueOf(tag2));
            String tag3 = (String)rg_interior_score.findViewById(rg_interior_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setInterior(Integer.valueOf(tag3));
            String tag4 = (String)rg_facility_score.findViewById(rg_facility_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setFacilityScore(Integer.valueOf(tag4));
            String tag5= (String)rg_roomcnt_score.findViewById(rg_roomcnt_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setRoomsCnt(Integer.valueOf(tag5));
            String tag6= (String)rg_bathroomcnt_score.findViewById(rg_bathroomcnt_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setBathroomsCnt(Integer.valueOf(tag6));
            String tag7= (String)rg_school_score.findViewById(rg_school_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setSchoolScore(Integer.valueOf(tag7));
            String tag8= (String)rg_traffic_score.findViewById(rg_traffic_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setTrafficScore(Integer.valueOf(tag8));
            String tag9= (String)rg_around_status_score.findViewById(rg_around_status_score.getCheckedRadioButtonId()).getTag();
            cartInfo.setAroundStatusScore(Integer.valueOf(tag9));
            int totalScore = Integer.valueOf(tag) + Integer.valueOf(tag2) + Integer.valueOf(tag3) + Integer.valueOf(tag4)
                     + Integer.valueOf(tag5) + Integer.valueOf(tag6) + Integer.valueOf(tag7) + Integer.valueOf(tag8)
                    + Integer.valueOf(tag9); // TODO 값이 인덱스라서
            cartInfo.setTotalScore(totalScore  + 9);
            cartInfo.setAroundStatus(et_around_status.getText().toString()); //
            cartInfo.setFee(et_fee.getText().toString()); //
            cartInfo.setParking(et_parking.getText().toString());
            cartInfo.setUsage(et_usage.getText().toString());
            cartInfo.setEtc(et_etc.getText().toString());
            cartInfo.setEt_noise(et_noise.getText().toString());

            String json = new Gson().toJson(arPhotoList);
            cartInfo.setPhotoUris(json);
            // 정보 추가 및 업데이트

            if(cartInfo.getCart_id() > 0){
                cartInfo.setMod_date(new Date());
                dbMgr.updateCartInfo(cartInfo);
            } else {
                cartInfo.setReg_date(new Date());
                dbMgr.insertCartInfo(cartInfo);
            }

            dbMgr.commit();
            result = true;
            Toast.makeText(mContext,"저장완료",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            dbMgr.rollback();
            Toast.makeText(mContext,"입력하지 않은항목 확이해주세요",Toast.LENGTH_SHORT).show();
        }finally {
            dbMgr.endTransaction();
        }
        return result;
    }

    @Override
    public void onBackPressed() {

        if(ll_icon_layout_parent.isShown()){
             ll_icon_layout_parent.setVisibility(View.GONE);
            return;
        }

        if (mIsModify){
            popUpNotifyCancel();
        } else {
            super.onBackPressed();
        }
    }

    private void setRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);
        mListAdapter = new UserListAdapter(arPhotoList);

//        SlideInBottomAnimationAdapter slideUpAdapter = new SlideInBottomAnimationAdapter(mListAdapter);
//        slideUpAdapter.setFirstOnly(true);
//        slideUpAdapter.setDuration(800);
//        slideUpAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        myRecycler.setAdapter(mListAdapter);

    }

    private void popUpNotifyCancel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    finish();
                    super.onPositiveActionClicked(fragment);
                }
                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };
            builder.title(getString(R.string.dialog_msg_modify_cancel))
                    .positiveAction(getString(R.string.btn_confirm))
                    .negativeAction(getString(R.string.fab_back));
            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), "test");
        } else {
            AlertDialogFragment.Builder dlgBuilder = new AlertDialogFragment.Builder(getApplicationContext(), R.style.DialogTheme);
            dlgBuilder.setTitle(R.string.alert_title);
            dlgBuilder.setMessage(R.string.dialog_msg_modify_cancel);
            dlgBuilder.setPositiveButton(R.string.btn_confirm, this);
            dlgBuilder.setNegativeButton(R.string.fab_back, this);
            dlgBuilder.setCancelable(false);
            dlgBuilder.show(getFragmentManager(), "MWarning");
        }
    }

    @Override
    public void onClickPositive(AlertDialogFragment dialog) {
        if("MWarning".equals(dialog.getTag()) ){
            finish();
        } else { // 삭제
            if(cartInfo.getCart_id() > 0)
                dbMgr.deleteCartInfo(cartInfo);
            setResult(RESULT_OK);
            finish();
        }
    }
    @Override
    public void onClickNegative(AlertDialogFragment dialog) {
    }
    @Override
    public void onClickNeutral(AlertDialogFragment dialog) {
    }

    @Click(R.id.btn_delete)
    public void onClickDelete (View v) {

        if(JavaUtil.isDoubleClick()) return ;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    if(cartInfo.getCart_id() > 0)
                        dbMgr.deleteCartInfo(cartInfo);
                    setResult(RESULT_OK);
                    finish();
                    super.onPositiveActionClicked(fragment);
                }
                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };
            builder.title(getString(R.string.dialog_msg_delete_cartinfo))
                    .positiveAction(getString(R.string.btn_confirm))
                    .negativeAction(getString(R.string.fab_back));
            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), "test");
        } else {
            AlertDialogFragment.Builder dlgBuilder = new AlertDialogFragment.Builder(getApplicationContext(), R.style.DialogTheme);
            dlgBuilder.setTitle(R.string.alert_title);
            dlgBuilder.setMessage(R.string.dialog_msg_delete_cartinfo);
            dlgBuilder.setPositiveButton(R.string.btn_confirm, this);
            dlgBuilder.setNegativeButton(R.string.fab_back, this);
            dlgBuilder.setCancelable(false);
            dlgBuilder.show(getFragmentManager(), "Warning");
        }

    }


    private class UserListAdapter extends BaseRecyclerExtendsAdapter<String> {

        public UserListAdapter(List<String> data) {
            super(data);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
//            Glide.clear(holder.itemView.findViewById(R.id.IV_USERLIST_PROFILE_IMAGE));
        }

        @Override
        public void onBindViewHolderImpl(final RecyclerView.ViewHolder viewHolder, BaseRecyclerExtendsAdapter<String> adapter, final int i) {
            // If you're using your custom handler (as you should of course)
            // you need to cast viewHolder to it.
            final String photoPath = getData().get(i);

            // View Set
            Glide.with(mContext)
                    .load(photoPath)
                    .bitmapTransform(new CropCircleTransform(mContext))
                    .crossFade(300)
                    .into(((MyCustomViewHolder) viewHolder).civProfileImage);

            ((MyCustomViewHolder) viewHolder).civProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    PhotoEvent event = new PhotoEvent(cartInfo.getPhotoList());
                    event.setSelectPhoto(i);
                    EventBus.getDefault().postSticky(event);
                    intent.setClass(getApplicationContext(), PhotoDetailActivity.class);
                    startActivityForResult(intent, 1004);
                }
            });
        }

        @Override
        public MyCustomViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final BaseRecyclerExtendsAdapter<String> adapter, int i) {
            // Here is where you inflate your row and pass it to the constructor of your ViewHolder
            MyCustomViewHolder view = new MyCustomViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photolist, viewGroup, false));
            return view;
        }

    };

    private static class MyCustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView civProfileImage;

        public MyCustomViewHolder(View itemView) {
            super(itemView);
            civProfileImage = (ImageView) itemView.findViewById(R.id.IV_USERLIST_PROFILE_IMAGE);
        }
    }

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // Add bottom margin only for other as not the first
            if(parent.getChildPosition(view) != 0){
//                outRect.left = space - 3;
//                outRect.right = space - 3;
                outRect.bottom = space;
            }

        }
    }
}
