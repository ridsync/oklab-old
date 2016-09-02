package com.example.test.oktest;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.albinmathew.transitions.fragment.ExitFragmentTransition;
import com.albinmathew.transitions.fragment.FragmentTransition;
import com.albinmathew.transitions.fragment.FragmentTransitionLauncher;
import com.example.test.oktest.FadingActionBar.MainActivity;
import com.example.test.oktest.ImageLoaderLib.ImageLoaderMainFragment;
import com.example.test.oktest.eventbus.MyEvent;
import com.example.test.oktest.pinnedsection.PinnedSectionListViewFragment;
import com.example.test.oktest.ui.AutoScrollListViewFragment;
import com.example.test.oktest.ui.BaseFragment;
import com.example.test.oktest.ui.NestedScrollFragment;
import com.example.test.oktest.ui.ScalablelayoutFragment;
import com.example.test.oktest.ui.SwipeRereshFragment;
import com.example.test.oktest.ui.ViewPagerFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import de.greenrobot.event.EventBus;

//@EActivity(R.layout.activity_test_main)
public class NavigationActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     * http://itmir.tistory.com/526
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #setActionTitle()}.
     */
    private CharSequence mTitle = getTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        // Set up the action bar to show a dropdown list.
        // ActionBar를 각 Contents Fragment에서 설정하도록 하는게 나을듯?
        // ActionBar + ViewPager 연동을 하기에...
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //    @AfterViews
    public void initViews(){
        Log.d("TAG","@AfterViews ");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Context getActionBarThemedContextCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getActionBar().getThemedContext();
        } else {
            return this;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(NavDrawerItem item) {
        // update the main content by replacing fragments
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,
                        getFragmentView(getApplicationContext(), item))
                .commit();

        /**
         * Event Transfer For use in {@link ScalablelayoutFragment}.
         */
        MyEvent myEvent = new MyEvent();
        EventBus.getDefault().postSticky(myEvent);
    }

    private Fragment getFragmentView(Context mContext, NavDrawerItem item) {
        switch (item.getPosition()) {
            case 0:
                return new AutoScrollListViewFragment();
            case 1:
                return ScalablelayoutFragment.newInstance("SclablelayoutFragment", "test", item.getPosition());
            case 2:
                return new SwipeRereshFragment();
            case 3:
                return new PinnedSectionListViewFragment();
            case 4:
                return new ViewPagerFragment(); // ViewPager + FixedTabsSwipe
            case 5:
                return new ImageLoaderMainFragment();
            case 6:
                return PlaceholderFragment.newInstance(item.getPosition()); // PrelollipopTransition
            case 7:
                return new NestedScrollFragment(); // PrelollipopTransition
            default:
                return PlaceholderFragment.newInstance(item.getPosition());
        }
    }

    public void onSectionAttached(int number) {

         mTitle = mNavigationDrawerFragment.getNavMenuTitles(number);

        setActionTitle();
    }

    public void setActionTitle() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setSubtitle("subtitle");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.nav_main, menu);
//            setActionTitle();
            Log.d("Navigation", NavigationActivity.class.getSimpleName() + "=> onCreateOptionsMenu");
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Navigation", NavigationActivity.class.getSimpleName() + "=> onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        getFragmentManager().getBackStackEntryAt(0).
    }

    /**
     * A placeholder fragment containing a simple view.
     *  @see  PreLollipopTransition Test
     * // https://github.com/albinmathew/PreLollipopTransition
     */
    public static class PlaceholderFragment extends BaseFragment implements ActionBar.OnNavigationListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_DRAWER_ITEM = "nav_drawer_item";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            args.putParcelable(ARG_SECTION_DRAWER_ITEM, item);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.frag_test_main, container, false);
            final TextView tv = (TextView)rootView.findViewById(R.id.section_label_2);
            final ImageView iv = (ImageView)rootView.findViewById(R.id.start_image);
            Button bt = (Button)rootView.findViewById(R.id.section_label);
            bt.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
//                          Intent intent = new Intent(getActivity(), MainActivity.class);
//                            startActivity(intent);
                          final TargetFragment toFragment = TargetFragment.newInstance(2);
                          FragmentTransitionLauncher
                                  .with(iv.getContext())
                                  .image(((BitmapDrawable) iv.getDrawable()).getBitmap())
//                                  .image((BitmapFactory.decodeResource(getResources(), R.drawable.ny)) )
                                    .from(iv).prepare(toFragment);
                          getFragmentManager().beginTransaction().replace(R.id.container, toFragment).addToBackStack(null).commit();
                      }
                  }
            );
            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            tv.setText("SECTION_NUMBER  = " + String.valueOf(num));

            return rootView;
        }

        @Override
        public boolean onNavigationItemSelected(int position, long id) {
            // When the given dropdown item is selected, show its contents in the
            // container view.

            return true;
        }

        @Override
        protected void setActionBarOnResume(Activity activity, ActionBar actionbar) {
            ((NavigationActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TargetFragment extends BaseFragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_DRAWER_ITEM = "nav_drawer_item";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TargetFragment newInstance(int sectionNumber) {
            TargetFragment fragment = new TargetFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            args.putParcelable(ARG_SECTION_DRAWER_ITEM, item);
            fragment.setArguments(args);
            return fragment;
        }

        public TargetFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_target_main, container, false);
            final TextView tv = (TextView)rootView.findViewById(R.id.section_label_2);
            final ImageView iv = (ImageView)rootView.findViewById(R.id.target_image);
            final ExitFragmentTransition exitFragmentTransition = FragmentTransition.with(this).to(iv).start(savedInstanceState);
            exitFragmentTransition.startExitListening();

            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            tv.setText("SECTION_NUMBER  = " + String.valueOf(num));

            return rootView;
        }

        @Override
        protected void setActionBarOnResume(Activity activity, ActionBar actionbar) {
            ((NavigationActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
