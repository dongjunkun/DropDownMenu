package com.yyy.djk.dropdownmenu;

import android.content.Context;
import android.graphics.Color;
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

    //菜单导航栏
    private LinearLayout navigateMenuView;
    //包含菜单以及内容
    private FrameLayout containerView;
    //包含maskView以及所有菜单
    private FrameLayout coverView;
    //当菜单不足一屏时底下半透明阴影区域，点击可关闭菜单
    private View maskView;
    //选中的菜单
    private View currentView;
    private Animation dropdown_in, dropdown_out, dropdown_mask_in, dropdown_mask_out;



    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        navigateMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        navigateMenuView.setOrientation(HORIZONTAL);
        navigateMenuView.setBackgroundResource(android.R.color.white);
        navigateMenuView.setLayoutParams(params);
        addView(navigateMenuView, 0);

        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1.0f)));
        underLine.setBackgroundResource(R.color.gray);
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
            menu.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            menu.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            menu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchMenu(menu);
                }
            });
            menu.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
            menu.setText(texts.get(i));
            int padding = dpToPx(12f);
            menu.setPadding(padding, padding, padding, padding);
            navigateMenuView.addView(menu);
            if (i < texts.size() - 1) {
                View view = new View(getContext());
                view.setLayoutParams(new LayoutParams(dpToPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
                view.setBackgroundColor(Color.parseColor("#cccccc"));
                navigateMenuView.addView(view);
            }
        }
        containerView.addView(contentView, 0);

        coverView = new FrameLayout(getContext());
        coverView.setVisibility(GONE);
        containerView.addView(coverView, 1);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundResource(R.color.mask_color);
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
                ((TextView) navigateMenuView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_unselected));
                ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
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
                    ((TextView) navigateMenuView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_selected));
                    ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.drop_down_selected_icon), null);
                    currentView = target;
                }
            } else {
                ((TextView) navigateMenuView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_unselected));
                ((TextView) navigateMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
                coverView.getChildAt(i / 2 + 1).setVisibility(View.GONE);
            }
        }
    }

    public int dpToPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm)+0.5);
    }
}
