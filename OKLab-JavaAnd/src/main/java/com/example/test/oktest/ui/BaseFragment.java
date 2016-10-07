package com.example.test.oktest.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ojungwon on 2014-10-01.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        getActivity().getActionBar().removeAllTabs();
        setActionBarOnResume(getActivity(), getActivity().getActionBar());
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach ");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach ");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause ");
        super.onPause();
    }

    protected abstract void setActionBarOnResume(Activity activity, ActionBar actionBar);

}
