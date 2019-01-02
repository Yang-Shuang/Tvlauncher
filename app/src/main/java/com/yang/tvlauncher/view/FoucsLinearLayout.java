package com.yang.tvlauncher.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.LogUtil;

/**
 * Created by yangshuang
 * on 2018/12/17.
 */

public class FoucsLinearLayout extends LinearLayout {
    private int highLightType;
    private static final int ZOOM = 0;
    private static final int BACKGROUND = 1;

    public FoucsLinearLayout(Context context) {
        super(context);
    }

    public FoucsLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FoucsLinearLayout, 0, 0);
        highLightType = a.getInt(R.styleable.FoucsLinearLayout_highLightType, ZOOM);
        a.recycle();
    }

    public FoucsLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FoucsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    private void init() {
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        child.setFocusable(true);
        child.setFocusableInTouchMode(true);
        child.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                boolean hasZoom = v.getTag(R.id.hasHighLight) != null && (boolean) v.getTag(R.id.hasHighLight);
                if (highLightType == ZOOM) {
                    if ((hasFocus && !hasZoom) || (!hasFocus && hasZoom)) {
                        zoom(hasFocus, v);
                    }
                } else if (highLightType == BACKGROUND) {
                    changeBack(hasFocus, v);
                }
            }
        });
    }

    private void zoom(boolean hasFocus, View v) {
        final float startX = hasFocus ? 1f : 1.15f;
        final float endX = hasFocus ? 1.15f : 1f;
        final float startY = hasFocus ? 1f : 1.15f;
        final float endY = hasFocus ? 1.15f : 1f;
        YoYo.with(new BaseViewAnimator() {
            @Override
            protected void prepare(View target) {
                getAnimatorAgent().playTogether(
                        ObjectAnimator.ofFloat(target, "scaleX", startX, endX),
                        ObjectAnimator.ofFloat(target, "scaleY", startY, endY)
                );
            }
        }).duration(100).repeat(0).playOn(v);
        v.setTag(R.id.hasHighLight, hasFocus);
    }

    private void changeBack(boolean hasFocus, View v) {
        v.setBackgroundResource(hasFocus ? R.color.trans_default_background : R.color.transparent);
    }
}
