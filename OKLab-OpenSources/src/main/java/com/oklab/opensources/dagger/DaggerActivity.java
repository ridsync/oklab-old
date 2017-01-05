package com.oklab.opensources.dagger;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oklab.opensources.R;
import com.oklab.opensources.dagger.di.EventBus;
import com.oklab.opensources.dagger.di.Twitter;
import com.oklab.opensources.dagger.utils.CollectionUtils;
import com.oklab.opensources.dagger.utils.NetworkStateManager;
import com.squareup.otto.Subscribe;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DaggerActivity extends BaseActivity {

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

    // 2) getMethod Module
    private CollectionUtils mD2CollectionUtils;

    @Inject
    NetworkStateManager networkStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DagApplication.component(this).inject(this);

        mD2CollectionUtils = DagApplication.component(this).getD2CollectionUtil();
        List<String> sortedAscending = mD2CollectionUtils.toSortedList(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return -1;
            }
        }, "zombie", "dog", "animal");
        Log.d(DaggerActivity.class.getSimpleName(), "[Dagger] toSortedList listget(0) = " + sortedAscending.get(0) );

        Log.d(DaggerActivity.class.getSimpleName(), "[Dagger] Network State = " + networkStateManager.isConnectedOrConnecting() );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    protected void onStop() {
        mEventBus.unregister(this);
        super.onStop();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(RealmTest test){
        Log.d(DaggerActivity.class.getSimpleName(),"[Dagger] EventBus onEvent = " + test.getName());
    }

    @OnClick(R.id.getWeatherBtn)
    public void setGetWeatherBtn(View view){

        twitter.tweet("Hey, I clicked button!");

        RealmTest test = new RealmTest();
        test.setId(2);
        test.setName(mlat.getText().toString());

        mRealm.beginTransaction();
        mRealm.insertOrUpdate(test);
        mRealm.commitTransaction();

        Log.d(DaggerActivity.class.getSimpleName(),  mEventBus.toString());
        mEventBus.post(test);

    }

    @OnClick(R.id.uploadBtn)
    public void onUpload(View view) {

        RealmTest test = mRealm.where(RealmTest.class).equalTo("id", 2).findFirst();

        mlon.setText(test.getId() + test.getName());
        Log.d(DaggerActivity.class.getSimpleName(), "[Dagger] RealmTest mlon = " + test.getName());

    }

}
