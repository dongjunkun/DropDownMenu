package com.yyy.djk.multipledropdownmenu;

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

import java.util.List;


/**
 * Created by sdmt-gjw on 2015/6/17.
 */
public class DropDownMenu extends LinearLayout {

    public static final String TAG = DropDownMenu.class.getSimpleName();

    private LinearLayout headView;
    private FrameLayout containerView;
    private FrameLayout coverView;
    private List<String> texts;
    private View mask;
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

        headView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headView.setOrientation(HORIZONTAL);
        headView.setBackgroundResource(android.R.color.white);
        headView.setLayoutParams(params);
        addView(headView, 0);

        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1.0f)));
        underLine.setBackgroundColor(Color.parseColor("#cccccc"));
        addView(underLine, 1);

        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

        dropdown_in = AnimationUtils.loadAnimation(context, R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_out);
        dropdown_mask_in = AnimationUtils.loadAnimation(context, R.anim.dropdown_mask_in);
        dropdown_mask_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_mask_out);

    }


    public void setDropDownMenu(List<String> headerTexts, List<View> views, View contentView) {
        this.texts = headerTexts;
        for (int i = 0; i < texts.size(); i++) {
            final TextView textView = new TextView(getContext());
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTarget(textView);
                }
            });
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
            textView.setText(texts.get(i));
            int padding = dpToPx(10f);
            textView.setPadding(padding, padding, padding, padding);
            headView.addView(textView);
            if (i < texts.size() - 1) {
                View view = new View(getContext());
                view.setLayoutParams(new LayoutParams(dpToPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
                view.setBackgroundColor(Color.parseColor("#cccccc"));
                headView.addView(view);
            }
        }
        containerView.addView(contentView, 0);

        coverView = new FrameLayout(getContext());
        coverView.setVisibility(GONE);
        containerView.addView(coverView, 1);

        mask = new View(getContext());
        mask.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mask.setBackgroundColor(Color.parseColor("#88888888"));
        mask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMenu();
            }
        });
        coverView.addView(mask, 0);
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            coverView.addView(views.get(i), i + 1);
        }

    }

    public void setText(View view, String text) {
        for (int i = 1; i < coverView.getChildCount(); i++) {
            if (view == coverView.getChildAt(i)) {
                ((TextView) headView.getChildAt(i * 2 - 2)).setText(text);
            }
        }

    }


    public void resetMenu() {
        for (int i = 0; i < headView.getChildCount(); i = i + 2) {
            if (currentView == headView.getChildAt(i)) {
                coverView.getChildAt(i / 2 + 1).clearAnimation();
                coverView.getChildAt(i / 2 + 1).startAnimation(dropdown_out);
                mask.startAnimation(dropdown_mask_out);
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
                ((TextView) headView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_unselected));
                ((TextView) headView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
            }
        }

    }

    public boolean isShowing() {
        return currentView != null;
    }

    private void updateTarget(View target) {
        for (int i = 0; i < headView.getChildCount(); i = i + 2) {
            if (target == headView.getChildAt(i)) {
                if (currentView == headView.getChildAt(i)) {
                    resetMenu();
                } else {
                    coverView.getChildAt(i / 2 + 1).setVisibility(View.VISIBLE);
                    Log.i(TAG, String.valueOf(i));
                    if (coverView.getVisibility() == View.GONE) {
                        coverView.setVisibility(View.VISIBLE);
                        mask.clearAnimation();
                        mask.startAnimation(dropdown_mask_in);
                        coverView.getChildAt(i / 2 + 1).clearAnimation();
                        coverView.getChildAt(i / 2 + 1).startAnimation(dropdown_in);
                    }
                    ((TextView) headView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_selected));
                    ((TextView) headView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.drop_down_selected_icon), null);
                    currentView = target;
                }
            } else {
                ((TextView) headView.getChildAt(i)).setTextColor(getResources().getColor(R.color.drop_down_unselected));
                ((TextView) headView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.mipmap.drop_down_unselected_icon), null);
                coverView.getChildAt(i / 2 + 1).setVisibility(View.GONE);
            }
        }
    }

    public int dpToPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm);
    }
}
