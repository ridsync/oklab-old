package com.oklab.opensources.dagger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oklab.opensources.R;
import com.oklab.opensources.dagger.di.EventBus;
import com.oklab.opensources.dagger.di.Twitter;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    // 1) Tiwtter Ijection
    @Inject
    protected Twitter twitter;

    // 2) Dagger2 injection
    @Inject
    protected Realm mRealm;

    @Inject
    protected EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        DagApplication.component(this).inject(this);

    }
}
