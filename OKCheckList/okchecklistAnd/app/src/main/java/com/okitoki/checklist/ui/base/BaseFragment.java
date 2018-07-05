package com.okitoki.checklist.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.okitoki.checklist.ui.fragment.AlertDialogFragment;

public class BaseFragment extends Fragment implements AlertDialogFragment.OnClickListener {

    /***************************
     * Static Member Variable
     ***************************/

    /***************************
     * Logical Member Variable
     ***************************/

    /***************************
     * Android's Variable
     ***************************/
    protected Bundle mBundle;
    protected View mRootView;
    protected TabLayout tabLayout;
    protected Context mContext;

    /***************************
     * Android LifeCycle
     ***************************/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity!=null)
            mContext = activity.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
//        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
    }
    @Override
    public void onStop() {
        super.onStop();
//        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
    }

    @Override
    public void onDestroyView() {
//        NetManager.cancelAllTask();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***************************
     * Member Methods or Others
     ***************************/
    @Override
    public void onClickPositive(AlertDialogFragment dialog) {
    }

    @Override
    public void onClickNegative(AlertDialogFragment dialog) {
    }

    @Override
    public void onClickNeutral(AlertDialogFragment dialog) {
    }

    /**
     *  텍스트 메세지 표시 닫히는 심플 + Auto
     * @param message
     */
    protected void alertDialog(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MaterialAlertDialog);
//        builder.setTitle(R.string.popup_title_completed_adjst_good_item);
//        builder.setMessage(R.string.popup_desc_completed_adjst_good_item);
//        builder.setCancelable(false);
//        builder.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (isVisible())
//                    AUtil.getFragmentManager().popBackStackImmediate();
//            }
//        });
//        builder.show();
    }

    /**
     * Payment Fragment's Methods End
     */

    protected void sendScreenToGA(String screenName){
//        if(mTracker==null){
//            mTracker = ((OKCartApplication)(getActivity().getApplication())).getDefaultTracker();
//        }
//        mTracker.setScreenName(screenName);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}