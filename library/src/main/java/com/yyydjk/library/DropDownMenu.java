package com.yyydjk.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dongjunkun on 2015/6/17.
 */
public class DropDownMenu extends LinearLayout {
    private static final String TAG = DropDownMenu.class.getSimpleName();
    //-1表示未选中
    private static final int NO_TAB_SELECTED = -1;

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，默认未选中
    private int current_tab_position = NO_TAB_SELECTED;

    //分割线
    private Drawable divider;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int textSize = 14;

    //tab选中图标
    @DrawableRes
    private int selectedIcon;
    //tab未选中图标
    @DrawableRes
    private int unselectedIcon;

    //弹出菜单高度最大占屏幕高度的比例
    private float maxHeighPercent = 0.5f;

    //菜单出现动画
    private Animation animationMenuIn;
    //遮罩出现动画
    private Animation animationMaskIn;
    //菜单消失动画
    private Animation animationMenuOut;
    //遮罩消失动画
    private Animation animationMaskOut;

    public DropDownMenu(Context context) {
        this(context, null);
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
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddm_underlineColor, underlineColor);
        divider = a.getDrawable(R.styleable.DropDownMenu_ddm_divider);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddm_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddm_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddm_backgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_ddm_maskColor, maskColor);
        textSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_textSize, textSize);
        selectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddm_selectedIcon, selectedIcon);
        unselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddm_unselectedIcon, unselectedIcon);
        maxHeighPercent = a.getFloat(R.styleable.DropDownMenu_ddm_maxHeightPercent, maxHeighPercent);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        if (divider != null) {
            tabMenuView.setDividerDrawable(divider);
            tabMenuView.setShowDividers(SHOW_DIVIDER_MIDDLE);
        }
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

        animationMenuIn = AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_in);
        animationMaskIn = AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_in);
        animationMenuOut = AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_out);
        animationMaskOut = AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_out);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);
        if (containerView.getChildAt(2) != null) {
            containerView.removeViewAt(2);
        }

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DeviceUtils.getScreenSize(getContext()).y * maxHeighPercent)));
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        final View tab = inflate(getContext(), R.layout.tab, null);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        TabViewHolder tabViewHolder = new TabViewHolder();
        tab.setTag(tabViewHolder);
        TextView tvLabel = (TextView) tab.findViewById(R.id.tv_label);
        tabViewHolder.tvLabel = tvLabel;
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvLabel.setText(tabTexts.get(i));
        final ImageView ivIcon = (ImageView) tab.findViewById(R.id.iv_icon);
        tabViewHolder.ivIcon = ivIcon;
        tabViewHolder.setSelected(false);
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != NO_TAB_SELECTED) {
            View curTab = tabMenuView.getChildAt(current_tab_position);
            ((TabViewHolder) curTab.getTag()).tvLabel.setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i++) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != NO_TAB_SELECTED) {
            View curTab = tabMenuView.getChildAt(current_tab_position);
            ((TabViewHolder) curTab.getTag()).setSelected(false);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.startAnimation(animationMenuOut);
            maskView.setVisibility(GONE);
            maskView.startAnimation(animationMaskOut);
            current_tab_position = NO_TAB_SELECTED;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != NO_TAB_SELECTED;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        Log.i(TAG, "current_tab_position = " + current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i++) {
            View tab = tabMenuView.getChildAt(i);
            TabViewHolder tabViewHolder = (TabViewHolder) tab.getTag();
            if (target == tab) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == NO_TAB_SELECTED) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.startAnimation(animationMenuIn);
                        maskView.setVisibility(VISIBLE);
                        maskView.startAnimation(animationMaskIn);
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    tabViewHolder.setSelected(true);
                }
            } else {
                tabViewHolder.setSelected(false);
                popupMenuViews.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    private int dpToPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    class TabViewHolder {
        TextView tvLabel;
        ImageView ivIcon;

        public void setSelected(boolean isSelected) {
            if (isSelected) {
                tvLabel.setTextColor(textSelectedColor);
                ivIcon.setImageResource(selectedIcon);
            } else {
                tvLabel.setTextColor(textUnselectedColor);
                ivIcon.setImageResource(unselectedIcon);
            }
        }
    }
}
