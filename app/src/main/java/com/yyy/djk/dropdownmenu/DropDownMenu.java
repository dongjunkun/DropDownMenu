package com.yyy.djk.dropdownmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyy.djk.multipledropdownmenu.R;

import java.util.List;


/**
 * Created by dongjunkun on 2015/6/17.
 */
public class DropDownMenu extends LinearLayout {

    public static final String TAG = DropDownMenu.class.getSimpleName();

    private LinearLayout navigateMenuView;
    private FrameLayout containerView;
    private FrameLayout coverView;
    private View maskView;
    private View currentView;

    private Animation dropdown_in, dropdown_out, dropdown_mask_in, dropdown_mask_out;


    private int dividerColor = 0xffcccccc;
    private int textSelectedColor = 0xff890c85;
    private int textUnselectedColor = 0xff111111;
    private int maskColor = 0x88888888;
    private int menuTextSize = 14;

    private int menuSelectedIcon = R.mipmap.drop_down_selected_icon;
    private int menuUnselectedIcon = R.mipmap.drop_down_unselected_icon;


    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
        menuTextSize = a.getDimensionPixelOffset(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);

        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);

        a.recycle();

        navigateMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        navigateMenuView.setOrientation(HORIZONTAL);
        navigateMenuView.setBackgroundColor(menuBackgroundColor);
        navigateMenuView.setLayoutParams(params);
        addView(navigateMenuView, 0);

        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pxToDp(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

        dropdown_in = AnimationUtils.loadAnimation(context, R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_out);
        dropdown_mask_in = AnimationUtils.loadAnimation(context, R.anim.dropdown_mask_in);
        dropdown_mask_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_mask_out);


    }

    public void setDropDownMenu(List<String> texts, List<View> menus, View contentView) {
        for (int i = 0; i < texts.size(); i++) {
            final TextView menu = new TextView(getContext());
            menu.setSingleLine();
            menu.setEllipsize(TextUtils.TruncateAt.END);
            menu.setGravity(Gravity.CENTER);
            menu.setTextSize(TypedValue.COMPLEX_UNIT_SP,menuTextSize);
            menu.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            menu.setTextColor(textUnselectedColor);
            menu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchMenu(menu);
                }
            });
            menu.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
            menu.setText(texts.get(i));
            menu.setPadding(pxToDp(5), pxToDp(12), pxToDp(5), pxToDp(12));
            navigateMenuView.addView(menu);
            if (i < texts.size() - 1) {
                View view = new View(getContext());
                view.setLayoutParams(new LayoutParams(pxToDp(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
                view.setBackgroundColor(dividerColor);
                navigateMenuView.addView(view);
            }
        }
        containerView.addView(contentView, 0);

        coverView = new FrameLayout(getContext());
        coverView.setVisibility(GONE);
        containerView.addView(coverView, 1);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        coverView.addView(maskView, 0);
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            coverView.addView(menus.get(i), i + 1);
        }

    }

    public void setMenuText(View view, String text) {
        for (int i = 1; i < coverView.getChildCount(); i++) {
            if (view == coverView.getChildAt(i)) {
                ((TextView) navigateMenuView.getChildAt(i * 2 - 2)).setText(text);
            }
        }

    }


    public void closeMenu() {
        for (int i = 0; i < navigateMenuView.getChildCount(); i = i + 2) {
            if (currentView == navigateMenuView.getChildAt(i)) {
                coverView.getChildAt(i / 2 + 1).clearAnimation();
                coverView.getChildAt(i / 2 + 1).startAnimation(dropdown_out);
                maskView.startAnimation(dropdown_mask_out);
                dropdown_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        coverView.setVisibility(View.GONE);
                        currentView = null;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ((TextView) navigateMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
            }
        }

    }

    public boolean isShowing() {
        return currentView != null;
    }

    private void switchMenu(View target) {
        for (int i = 0; i < navigateMenuView.getChildCount(); i = i + 2) {
            if (target == navigateMenuView.getChildAt(i)) {
                if (currentView == navigateMenuView.getChildAt(i)) {
                    closeMenu();
                } else {
                    coverView.getChildAt(i / 2 + 1).setVisibility(View.VISIBLE);
                    Log.i(TAG, String.valueOf(i));
                    if (coverView.getVisibility() == View.GONE) {
                        coverView.setVisibility(View.VISIBLE);
                        maskView.clearAnimation();
                        maskView.startAnimation(dropdown_mask_in);
                        coverView.getChildAt(i / 2 + 1).clearAnimation();
                        coverView.getChildAt(i / 2 + 1).startAnimation(dropdown_in);
                    }
                    ((TextView) navigateMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                    currentView = target;
                }
            } else {
                ((TextView) navigateMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
                coverView.getChildAt(i / 2 + 1).setVisibility(View.GONE);
            }
        }
    }

    public int pxToDp(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
