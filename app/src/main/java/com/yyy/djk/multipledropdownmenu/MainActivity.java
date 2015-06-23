package com.yyy.djk.multipledropdownmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.dropDownMenu) DropDownMenu mDropDownMenu;
    private String headers[] = {"武汉", "不限年龄", "不限性别"};
    private List<View> popuViews = new ArrayList<>();

    private DefaultDropDownList cityViews;
    private DefaultDropDownList ageViews;
    private DefaultDropDownList sexViews;

    private String citys[] = {"武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限年龄", "18岁以下", "18到22岁", "23岁到27岁", "28到35岁", "36到50岁", "50岁以上"};
    private String sexs[] = {"不限性别", "只看男", "只看女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        cityViews = new DefaultDropDownList(this, Arrays.asList(citys));
        ageViews = new DefaultDropDownList(this, Arrays.asList(ages));
        sexViews = new DefaultDropDownList(this, Arrays.asList(sexs));

        popuViews.add(cityViews.getListView());
        popuViews.add(ageViews.getListView());
        popuViews.add(sexViews.getListView());

        cityViews.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityViews.getAdapter().setCheckItem(position);
                mDropDownMenu.setText( cityViews.getListView(),citys[position]);
                mDropDownMenu.resetMenu();
            }
        });
        ageViews.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageViews.getAdapter().setCheckItem(position);
                mDropDownMenu.setText( ageViews.getListView(),ages[position]);
                mDropDownMenu.resetMenu();
            }
        });

        sexViews.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexViews.getAdapter().setCheckItem(position);
                mDropDownMenu.setText( sexViews.getListView(),sexs[position]);
                mDropDownMenu.resetMenu();
            }
        });

        //内容
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popuViews, contentView);
    }

    @Override
    public void onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.resetMenu();
        } else {
            super.onBackPressed();
        }
    }
}
