package com.example.test.oktest.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.oktest.NavigationActivity;
import com.example.test.oktest.R;
import com.example.test.oktest.ScalableLayout;
import com.example.test.oktest.eventbus.MyEvent;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScalablelayoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScalablelayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ScalablelayoutFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "SclablelayoutFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private static final String DebugTag = "ScalableLayout_TestAndroid";
    private static void log(String pLog) {
        Log.e(DebugTag, "MainActivity] " + pLog);
    }
    private TextView mTV_Text;
    private ScalableLayout mSL;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SclablelayoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScalablelayoutFragment newInstance(String param1, String param2, int position) {
        ScalablelayoutFragment fragment = new ScalablelayoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, position);
        fragment.setArguments(args);

        /**
         * Event is posted on {@link com.example.test.oktest.NavigationActivity}.
         */
//        EventBus.getDefault().register(fragment);

        return fragment;
    }

    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // if this is set true,
        // Activity.onCreateOptionsMenu will call Fragment.onCreateOptionsMenu
        // Activity.onOptionsItemSelected will call Fragment.onOptionsItemSelected
        setHasOptionsMenu(true);

        EventBus.getDefault().registerSticky(this);
        // onCreate 시점이 늦는다... postEvent보다 늦게 호출되어 이벤트 못받음.. 그래서 registerSticky 로 받는다.
    }

    // EventBus Receive Event Methods
    public void onEvent(MyEvent event){
        Log.d(TAG, "EventBus  onEvent called !!!!! ");
    }

    // EventBus Receive Event Methods posted by a background thread
    public void onEventMainThread(MyEvent event){
        Log.d(TAG, "EventBus  onEventMainThread called !!!!! ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout)inflater.inflate(R.layout.scalable_main, null);

        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

        ((NavigationActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_PARAM3));
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
        EventBus.getDefault().unregister(this);
    }

    //    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }


    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("Navigation", ScalablelayoutFragment.class.getSimpleName() + "=> onCreateOptionsMenu");
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        inflater.inflate(R.menu.swipe_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Navigation", ScalablelayoutFragment.class.getSimpleName() + "=> onOptionsItemSelected");
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "action_example on ScalableFragment", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
