package com.example.test.oktest.pinnedsection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.oktest.ui.BaseFragment;
import com.example.test.oktest.R;

import java.util.Locale;

/**
 * Created by ojungwon on 2014-10-01.
 */
public class PinnedSectionListViewFragment extends BaseFragment implements View.OnClickListener {

    private boolean hasHeaderAndFooter;
    private boolean isFastScroll;
    private boolean addPadding;
    private boolean isShadowVisible = true;
    private int mDatasetUpdateCount;

    private PinnedSectionListView psListView = null;
    private PinnedSectionListView.PinnedSectionListAdapter psListAdapter = null;
    private SimpleAdapter simpleListAdapter = null;

    @Override
    protected void setActionBarOnResume(Activity activity, ActionBar actionBar) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_pinnedsection_listview, null);
        psListView = (PinnedSectionListView)view.findViewById(R.id.pinned_section_listview);

        initializeHeaderAndFooter();
        initializeAdapter();
        initializePadding();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isFastScroll = savedInstanceState.getBoolean("isFastScroll");
            addPadding = savedInstanceState.getBoolean("addPadding");
            isShadowVisible = savedInstanceState.getBoolean("isShadowVisible");
            hasHeaderAndFooter = savedInstanceState.getBoolean("hasHeaderAndFooter");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFastScroll", isFastScroll);
        outState.putBoolean("addPadding", addPadding);
        outState.putBoolean("isShadowVisible", isShadowVisible);
        outState.putBoolean("hasHeaderAndFooter", hasHeaderAndFooter);
    }
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Item item = (Item) psListView.getAdapter().getItem(position);
//        if (item != null) {
//            Toast.makeText(getActivity(), "Item " + position + ": " + item.text, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getActivity(), "Item " + position, Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.pinned_section, menu);
//        menu.getItem(0).setChecked(isFastScroll);
//        menu.getItem(1).setChecked(addPadding);
//        menu.getItem(2).setChecked(isShadowVisible);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fastscroll:
                isFastScroll = !isFastScroll;
                item.setChecked(isFastScroll);
                initializeAdapter();
                break;
            case R.id.action_addpadding:
                addPadding = !addPadding;
                item.setChecked(addPadding);
                initializePadding();
                break;
            case R.id.action_showShadow:
                isShadowVisible = !isShadowVisible;
                item.setChecked(isShadowVisible);
                psListView.setShadowVisible(isShadowVisible);
                break;
            case R.id.action_showHeaderAndFooter:
                hasHeaderAndFooter = !hasHeaderAndFooter;
                item.setChecked(hasHeaderAndFooter);
                initializeHeaderAndFooter();
                break;
            case R.id.action_updateDataset:
                updateDataset();
                break;
        }
        return true;
    }
    private void updateDataset() {
        mDatasetUpdateCount++;
//        SimpleAdapter adapter = (SimpleAdapter) getListAdapter();
//        switch (mDatasetUpdateCount % 4) {
//            case 0: adapter.generateDataset('A', 'B', true); break;
//            case 1: adapter.generateDataset('C', 'M', true); break;
//            case 2: adapter.generateDataset('P', 'Z', true); break;
//            case 3: adapter.generateDataset('A', 'Z', true); break;
//        }
//        adapter.notifyDataSetChanged();
    }

    private void initializePadding() {
        float density = getResources().getDisplayMetrics().density;
        int padding = addPadding ? (int) (16 * density) : 0;
        psListView.setPadding(padding, padding, padding, padding);
    }

    private void initializeHeaderAndFooter() {
        psListView.setAdapter(null);
        if (hasHeaderAndFooter) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            TextView header1 = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, psListView, false);
            header1.setText("First header");
            psListView.addHeaderView(header1);
            TextView header2 = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, psListView, false);
            header2.setText("Second header");
            psListView.addHeaderView(header2);
            TextView footer = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, psListView, false);
            footer.setText("Single footer");
            psListView.addFooterView(footer);
        }
        initializeAdapter();
    }
    @SuppressLint("NewApi")
    private void initializeAdapter() {
        psListView.setFastScrollEnabled(isFastScroll);
        if (isFastScroll) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                psListView.setFastScrollAlwaysVisible(true);
            }
            psListView.setAdapter(new FastScrollAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1));
        } else {
            psListView.setAdapter(new SimpleAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1));
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Item: " + v.getTag(), Toast.LENGTH_SHORT).show();
    }


    static class SimpleAdapter extends ArrayAdapter<Item> implements PinnedSectionListView.PinnedSectionListAdapter {
        private static final int[] COLORS = new int[] {
                R.color.green_light, R.color.orange_light,
                R.color.blue_light, R.color.red_light };
        public SimpleAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
            generateDataset('A', 'Z', false);
        }


        public void generateDataset(char from, char to, boolean clear) {
            if (clear) clear();
            final int sectionsNumber = to - from + 1;
            prepareSections(sectionsNumber);
            int sectionPosition = 0, listPosition = 0;
            for (char i=0; i<sectionsNumber; i++) {
                Item section = new Item(Item.SECTION, String.valueOf((char)('A' + i)));
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                onSectionAdded(section, sectionPosition);
                add(section);
                final int itemsNumber = (int) Math.abs((Math.cos(2f*Math.PI/3f * sectionsNumber / (i+1f)) * 25f));
                for (int j=0;j<itemsNumber;j++) {
                    Item item = new Item(Item.ITEM, section.text.toUpperCase(Locale.ENGLISH) + " - " + j);
                    item.sectionPosition = sectionPosition;
                    item.listPosition = listPosition++;
                    add(item);
                }
                sectionPosition++;
            }
        }
        protected void prepareSections(int sectionsNumber) { }
        protected void onSectionAdded(Item section, int sectionPosition) { }
        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.DKGRAY);
            view.setTag("" + position);
            Item item = getItem(position);
            if (item.type == Item.SECTION) {
//view.setOnClickListener(PinnedSectionListActivity.this);
                view.setBackgroundColor(parent.getResources().getColor(COLORS[item.sectionPosition % COLORS.length]));
            }
            return view;
        }
        @Override public int getViewTypeCount() {
            return 2;
        }
        @Override public int getItemViewType(int position) {
            return getItem(position).type;
        }
        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }
    }
    static class FastScrollAdapter extends SimpleAdapter implements SectionIndexer {
        private Item[] sections;
        public FastScrollAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }
        @Override protected void prepareSections(int sectionsNumber) {
            sections = new Item[sectionsNumber];
        }
        @Override protected void onSectionAdded(Item section, int sectionPosition) {
            sections[sectionPosition] = section;
        }
        @Override public Item[] getSections() {
            return sections;
        }
        @Override public int getPositionForSection(int section) {
            if (section >= sections.length) {
                section = sections.length - 1;
            }
            return sections[section].listPosition;
        }
        @Override public int getSectionForPosition(int position) {
            if (position >= getCount()) {
                position = getCount() - 1;
            }
            return getItem(position).sectionPosition;
        }
    }
    static class Item {
        public static final int ITEM = 0;
        public static final int SECTION = 1;
        public final int type;
        public final String text;
        public int sectionPosition;
        public int listPosition;
        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
        @Override public String toString() {
            return text;
        }
    }
}
