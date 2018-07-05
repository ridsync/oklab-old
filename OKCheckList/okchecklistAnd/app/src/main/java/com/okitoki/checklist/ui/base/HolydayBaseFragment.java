package com.okitoki.checklist.ui.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public abstract class HolydayBaseFragment extends BaseFragment {

    protected static final String TAG = "RestEmartListFragment";

    // [START define_database_reference]
    protected DatabaseReference mDatabase;
    // [END define_database_reference]

    protected RecyclerView mRecycler;
    protected LinearLayoutManager mManager;

    public HolydayBaseFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

    }


    public abstract Query getQuery(DatabaseReference databaseReference);

}
