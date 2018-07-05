package com.okitoki.checklist.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.core.OKCartApplication;
import com.okitoki.checklist.network.NetManager;
import com.okitoki.checklist.network.OnNetworkListener;
import com.okitoki.checklist.network.model.ProductItem;
import com.okitoki.checklist.network.model.Rss;
import com.okitoki.checklist.network.protocol.ReqType;
import com.okitoki.checklist.network.task.NaverTask;
import com.okitoki.checklist.ui.adapter.ProductInfoRecvAdapter;
import com.okitoki.checklist.ui.base.CoreActivity;
import com.okitoki.checklist.utils.AUtil;
import com.okitoki.checklist.utils.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_product_search)
public class ProductInfoSearchActivity extends CoreActivity implements OnNetworkListener<Rss>,  ProductInfoRecvAdapter.ItemClickListener{

    public static final String TAG = ProductInfoSearchActivity.class.getSimpleName();

    @ViewById
    RelativeLayout RL_PROFILE_PROGRESS, rl_cart_item_empty;

    @ViewById
    Toolbar toolbar_actionbar;

    @ViewById
    Spinner spin_mart_filter, spin_order_filter;

    @ViewById
    EditText et_search_keyowrd;

    @ViewById
    ImageView iv_btn_search;

    @ViewById
    RecyclerView rcv_productserch_listView;

    private List<ProductItem> items = new ArrayList<>();

//    private SlideInBottomAnimationAdapter slideInAdapter;
    private ProductInfoRecvAdapter mAdapter;

    private String mKeyword;
    private String curSelectMart = "";
    private String curOrderMode = "sim";

    boolean isFirstLoadFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        Logger.d("onEvent", "onResume ");
        super.onResume();
    }

    @AfterViews
    public void initViews() {
        Logger.d("onEvent", "@AfterViews ");
        toolbar_actionbar.setTitle(R.string.menu_mode_product_search);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcv_productserch_listView.setHasFixedSize(true);
        rcv_productserch_listView.setItemAnimator(new DefaultItemAnimator());
        rcv_productserch_listView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new ProductInfoRecvAdapter(this, R.layout.item_view_product_search, items, this);
//        slideInAdapter = new SlideInBottomAnimationAdapter(mAdapter);
//        slideInAdapter.setDuration(600);
//        slideInAdapter.setFirstOnly(true);
//        slideInAdapter.setInterpolator(new DecelerateInterpolator());
        rcv_productserch_listView.setAdapter(mAdapter);

        setSpinner(spin_mart_filter, R.array.mart_list, mOnMartItemSelectedListener);
        setSpinner(spin_order_filter, R.array.order_list, mOnOrderItemSelectedListener);

        et_search_keyowrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClickSearch();
                }
                return false;
            }
        });

        String query=null;
        String strMart=null;
        if(getIntent()!=null){
            strMart = getIntent().getStringExtra(AppConst.INTENT_EXTRA_SELECT_MART);
            query = getIntent().getStringExtra(AppConst.INTENT_EXTRA_PRODUCT_TITLE);
            if(query!=null && query.length() > 0){
                et_search_keyowrd.setText(query);
                setMartNameByInitial(strMart);
                onClickSearch();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_mode_close:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String getmKeyword() {
        return mKeyword;
    }

    @Click(R.id.iv_btn_search)
    public void onClickSearch(){
        mKeyword = et_search_keyowrd.getText().toString().trim();
        if(mKeyword !=null && mKeyword.length() > 0 ){
            reqSearchProductInfo();
            AUtil.hideIME(getApplicationContext(), et_search_keyowrd);

            Bundle bundle2 = new Bundle();
            bundle2.putString(AppConst.FA_PARAM_NORMAL, "normal");
            // OKCartApplication.getDefaultFBAnalytics().logEvent(AppConst.FA_EVENT_CART_PRODUCT_SEARCH, bundle2);

        } else {
            Toast.makeText(getApplicationContext(),R.string.input_search_empty,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void itemClicked(int position , ProductItem item, View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AppConst.INTENT_EXTRA_URL , item.getLink());
        intent.putExtra(AppConst.INTENT_EXTRA_PRODUCT_TITLE , item.getMallName());
        startActivity(intent);
    }

    @Override
    public void itemLongClicked(int position , ProductItem item) {

    }

    @Override
    public void onNetSuccess(Rss data, int reqType) {
        if(mAdapter !=null ){
            isFirstLoadFinished = true;

            List<ProductItem> items = data.getChannel().getItem();
            if(items!=null && items.size() >0 ){
                rl_cart_item_empty.setVisibility(View.GONE);
                mAdapter.setmKeyWord(mKeyword);
                if(curSelectMart!=null && curSelectMart.length() >0){
                    List<ProductItem> filterItems = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        String mallName = items.get(i).getMallName();
                        if(curSelectMart.length()>0
                                && mallName != null
                                && mallName.contains(curSelectMart) ){
                            filterItems.add(items.get(i));
                        }
                    }
                    if (filterItems != null && filterItems.size() > 0){
                        mAdapter.clearItem();
                        mAdapter.addAll(filterItems );
                    } else {
                        rl_cart_item_empty.setVisibility(View.VISIBLE);
                        mAdapter.clearItem();
                    }
                } else { // 전체추가.
                    mAdapter.clearItem();
                    mAdapter.addAll(items );
                }
            } else {
                rl_cart_item_empty.setVisibility(View.VISIBLE);
                mAdapter.clearItem();
            }
            mAdapter.notifyDataSetChanged();
            rcv_productserch_listView.scrollToPosition(0);
        }
        Logger.d(ProductInfoSearchActivity.this, "onNetSuccess  " + reqType + " / " + data.toString());
    }

    @Override
    public void onNetFail(int retCode, String strError, int reqType) {
        Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_SHORT).show();
        Logger.d(ProductInfoSearchActivity.this, "onNetFail  " + reqType + " / " +  retCode + " / " + strError);
    }

    @Override
    public void onLoadingDialog(int reqType) {
        if(RL_PROFILE_PROGRESS!=null)
            RL_PROFILE_PROGRESS.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingDialogClose(int reqType) {
        if(RL_PROFILE_PROGRESS!=null)
            RL_PROFILE_PROGRESS.setVisibility(View.GONE);
    }

    private void reqSearchProductInfo() {

        String keyword = curSelectMart + (curSelectMart.length() > 0 ? " " : "");
        keyword = keyword.concat(mKeyword);
        NaverTask task = new NaverTask(this, ReqType.REQ_GET_SEARCH_PRODUCT, this);
        HashMap<String,Object> param = new HashMap<>();
        param.put("query", keyword);
        param.put("display", 100);
        param.put("start", 1);
        param.put("sort", curOrderMode);
        task.setParam(param);
        NetManager.startTask(task);
    }

    private void setSpinner(Spinner spinner ,final int arrResId, AdapterView.OnItemSelectedListener listener) {

        /* 스피너 Prompt 없애고 Index 를 1부터 시작하기 위한 커스텀 Adpater*/
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.item_spinner, parent, false);
                TextView label = (TextView) row.findViewById(android.R.id.text1);
                label.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15); // dp
//                if (position == 0) {
//                    label.setText(R.string.btn_select);
//                    label.setTextColor(getResources().getColor(R.color.text_color_3));
//                } else {
                label.setTextColor(getResources().getColor(R.color.text_color_gray_59));
                label.setText(getItem(position));
//                }

                return row;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                ((TextView)v).setTextColor(getResources().getColor(R.color.text_color_gray_59));
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
            setFilterMartName(i);
            if(isFirstLoadFinished)
                onClickSearch();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private AdapterView.OnItemSelectedListener mOnOrderItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            setOrderStat(i);
            if(isFirstLoadFinished)
                onClickSearch();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };


    private void setOrderStat(int position){
        // sim(기본값), date, asc, dsc
        if(position == 0){
            curOrderMode = "sim";
        } else if(position == 1){
            curOrderMode = "asc";
        } else if(position == 2){
            curOrderMode = "dsc";
        } else if(position == 3){
            curOrderMode = "date";
        }
    }
    private void setFilterMartName(int position){
        // sim(기본값), date, asc, dsc
        if(position == 0){
            curSelectMart = "";
        } else if(position == 1){
            curSelectMart = (String)spin_mart_filter.getSelectedItem();
        } else if(position == 2){
            curSelectMart = (String)spin_mart_filter.getSelectedItem();
        } else if(position == 3){
            curSelectMart = (String)spin_mart_filter.getSelectedItem();
        } else if(position == 4){
            curSelectMart = (String)spin_mart_filter.getSelectedItem();
        } else if(position == 5){
            curSelectMart = (String)spin_mart_filter.getSelectedItem();
        }
    }

    private void setMartNameByInitial(String martInitial){
        // sim(기본값), date, asc, dsc
        if("E".equals(martInitial)){
            spin_mart_filter.setSelection(1);
            setFilterMartName(1);
        } else if("H".equals(martInitial)){
            spin_mart_filter.setSelection(2);
            setFilterMartName(2);
        } else if("L".equals(martInitial)){
            spin_mart_filter.setSelection(3);
            setFilterMartName(3);
        } else if("GS".equals(martInitial)){
            spin_mart_filter.setSelection(4);
            setFilterMartName(4);
        } else {
            spin_mart_filter.setSelection(0);
            setFilterMartName(0);
        }
    }
}
