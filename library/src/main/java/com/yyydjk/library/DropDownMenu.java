package com.yyydjk.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Orientation {
    Context mContext;
    private Drawable unSelectedDrawable;
    private Drawable selectedDrawable;
    private int orientation;

    public Orientation(Context context) {
        mContext = context;
    }

    public static final int left = 0;
    public static final int top = 1;
    public static final int right = 2;
    public static final int bottom = 3;

    public Drawable getLeft(boolean close) {
        return orientation == left ? (close ? unSelectedDrawable : selectedDrawable) : null;
    }

    public Drawable getTop(boolean close) {
        return orientation == top ? (close ? unSelectedDrawable : selectedDrawable) : null;
    }

    public Drawable getRight(boolean close) {
        return orientation == right ? (close ? unSelectedDrawable : selectedDrawable) : null;
    }

    public Drawable getBottom(boolean close) {
        return orientation == bottom ? (close ? unSelectedDrawable : selectedDrawable) : null;
    }

    /**
     * 初始化位置参数
     *
     * @param orientation
     * @param menuUnselectedIcon
     */
    public void init(int orientation, int menuSelectedIcon, int menuUnselectedIcon) {

        unSelectedDrawable = mContext.getResources().getDrawable(menuUnselectedIcon);
        selectedDrawable = mContext.getResources().getDrawable(menuSelectedIcon);
        this.orientation = orientation;
    }
}

/**
 * Created by dongjunkun on 2015/6/17.
 */
public class DropDownMenu extends LinearLayout {
    //记录tabTexts的顺序
    List<View> dropTabViews = new ArrayList<>();
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;
    private float dividerHeight;
    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;
    //icon的方向
    private static int iconOrientation = Orientation.right;//默认右则
    private Orientation mOrientation;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;


    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor,
                textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor,
                textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor,
                menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize,
                menuTextSize);
        dividerHeight = a.getDimensionPixelSize(R.styleable.DropDownMenu_dddividerHeight, ViewGroup.LayoutParams.MATCH_PARENT);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon,
                menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon,
                menuUnselectedIcon);
        iconOrientation = a.getInt(R.styleable.DropDownMenu_ddmenuIconOrientation, iconOrientation);
        a.recycle();
        //初始化位置参数
        mOrientation = new Orientation(getContext());
        mOrientation.init(iconOrientation, menuSelectedIcon, menuUnselectedIcon);

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dpTpPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);


    }


    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews,
                                @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal" +
                    " popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            if (popupViews.get(i).getLayoutParams() == null) {
                popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    /**
     * 自定义插入的tabView，如果包含R.id.tv_tab就当做普通的tabtext会统一做变色处理和向下的角标处理
     *
     * @param tab
     * @param index 0start
     */
    public void addTab(View tab, int index) {
        if (index == (tabMenuView.getChildCount() + 1) / 2) {
            addTabEnd(tab);
            return;
        }
        tabMenuView.addView(tab, index * 2);
        tabMenuView.addView(getDividerView(), index * 2 + 1);
    }

    public void addTabEnd(View tab) {
        tabMenuView.addView(getDividerView(), tabMenuView.getChildCount());
        tabMenuView.addView(tab, tabMenuView.getChildCount());
    }

    private void addTab(@NonNull List<String> tabTexts, final int i) {
        final View tab = inflate(getContext(), R.layout.tab_item, null);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView textView = getTabTextView(tab);
        textView.setText(tabTexts.get(i));
        textView.setTextColor(textUnselectedColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        setTextDrawables(textView, true);
        tabMenuView.addView(tab);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemMenuClickListener != null) {
                    itemMenuClickListener.OnItemMenuClick(textView, i);
                }
                switchMenu(v);
            }
        });

        //添加分割线
        if (i < tabTexts.size() - 1) {
            tabMenuView.addView(getDividerView());
        }
        dropTabViews.add(tab);
    }

    private View getDividerView() {
        View view = new View(getContext());
        double height = dividerHeight > 0 ? dpTpPx(dividerHeight) : dividerHeight;
        LayoutParams params = new LayoutParams(dpTpPx(0.5f), (int) height);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);
        view.setBackgroundColor(dividerColor);
        return view;
    }

    /**
     * 获取tabView中id为tv_tab的textView
     *
     * @param tabView
     * @return
     */
    private TextView getTabTextView(View tabView) {
        TextView tabtext = (TextView) tabView.findViewById(R.id.tv_tab);
        return tabtext;
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            getTabTextView(tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            TextView textView = getTabTextView(tabMenuView.getChildAt(current_tab_position));
            textView.setTextColor(textUnselectedColor);
            setTextDrawables(textView, true);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim
                    .dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;
        }
    }

    public boolean isActive() {
        return current_tab_position != -1;
    }

    public void setTextDrawables(TextView textview, boolean close) {
        textview.setCompoundDrawablesWithIntrinsicBounds(mOrientation.getLeft(close), mOrientation.getTop(close),
                mOrientation.getRight(close), mOrientation.getBottom(close));
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();//关闭
                } else {//打开
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R
                                .anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim
                                .dd_mask_in));
                    }
                    View listView = getListView(tabMenuView.getChildAt(i));
                    if (listView != null) {
                        listView.setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    TextView textView = getTabTextView(tabMenuView.getChildAt(i));
                    textView.setTextColor(textSelectedColor);
                    setTextDrawables(textView, false);
                }
            } else {//关闭
                TextView textView = getTabTextView(tabMenuView.getChildAt(i));
                if (textView != null) {
                    textView.setTextColor(textUnselectedColor);

                }
                View listView = getListView(tabMenuView.getChildAt(i));
                if (listView != null) {
                    if (textView != null) {
                        setTextDrawables(textView, true);
                    }
                    listView.setVisibility(View.GONE);
                }
            }
        }
    }

    OnItemMenuClickListener itemMenuClickListener;

    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
        itemMenuClickListener = listener;
    }

    public interface OnItemMenuClickListener {
        void OnItemMenuClick(TextView tabView, int position);
    }

    /**
     * 获取dropTabViews中对应popupMenuViews数组中的ListView
     *
     * @param view
     * @return
     */
    private View getListView(View view) {
        if (dropTabViews.contains(view)) {
            int index = dropTabViews.indexOf(view);
            return popupMenuViews.getChildAt(index);
        } else {
            return null;
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}