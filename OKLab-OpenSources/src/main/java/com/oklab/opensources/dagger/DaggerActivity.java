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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    int idx = 0;
    Observable<RealmTest> delayedApiCall;
    private Subscription subscription;

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

        // EventBus
        Log.d(DaggerActivity.class.getSimpleName(),  mEventBus.toString());
        mEventBus.post(test);

        // RxJava interval 반복작업
        // Handler로 그냥 딜레이 반복 하는게 더 단순해보이네 ㅡㅡ;
        delayedApiCall = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
//                .startWith(0L)
                .map(new Func1<Long , RealmTest>() {
                    @Override
                    public RealmTest call(Long  seconds) {
                        System.out.println("call seconds = " + seconds);
                        RealmTest realm = new RealmTest();
                        realm.setName(""+idx++);
                        return realm;
                    }
                });
        subscription = delayedApiCall.subscribe(new Subscriber<RealmTest>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted = " + idx);
                tem.setText("RX onCompleted : " + idx);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError -> " + e.getMessage());
            }

            @Override
            public void onNext(RealmTest test) {
                System.out.println("onNext -> " + test.getName());
                tem.setText("RX onNext : " + test.getName());

                if(idx > 50) subscription.unsubscribe();
            }
        });
    }

    @OnClick(R.id.uploadBtn)
    public void onUpload(View view) {

        RealmTest test = mRealm.where(RealmTest.class).equalTo("id", 2).findFirst();

        mlon.setText(test.getId() + test.getName());
        Log.d(DaggerActivity.class.getSimpleName(), "[Dagger] RealmTest mlon = " + test.getName());

        // Stop
        if (subscription != null && !subscription.isUnsubscribed()) {
            System.out.println("stopped");
            subscription.unsubscribe();
        }
    }

}
