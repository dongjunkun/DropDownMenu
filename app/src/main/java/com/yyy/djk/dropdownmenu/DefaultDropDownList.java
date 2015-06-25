package com.yyy.djk.dropdownmenu;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by sdmt-gjw on 2015/6/18.
 */
public class DefaultDropDownList {
    private ListView listView;
    private DefaultDropDownAdapter dropDownAdapter;

    public DefaultDropDownList(Context context, List<String> list) {
        dropDownAdapter = new DefaultDropDownAdapter(context, list);
        listView = new ListView(context);
        listView.setDividerHeight(0);
        listView.setAdapter(dropDownAdapter);
    }

    public ListView getListView() {
        return listView;
    }

    public DefaultDropDownAdapter getAdapter() {
        return dropDownAdapter;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        listView.setOnItemClickListener(onItemClickListener);
    }
}
