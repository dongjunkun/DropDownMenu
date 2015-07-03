package com.yyy.djk.dropdownmenu;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class DefaultDropDownList {
    private ListView listView;
    private ListDropDownAdapter dropDownAdapter;

    public DefaultDropDownList(Context context, List<String> list) {
        dropDownAdapter = new ListDropDownAdapter(context, list);
        listView = new ListView(context);
        listView.setDividerHeight(0);
        listView.setAdapter(dropDownAdapter);
    }

    public ListView getListView() {
        return listView;
    }

    public ListDropDownAdapter getAdapter() {
        return dropDownAdapter;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        listView.setOnItemClickListener(onItemClickListener);
    }
}
