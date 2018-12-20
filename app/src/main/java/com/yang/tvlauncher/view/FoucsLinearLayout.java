package com.yang.tvlauncher.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
import com.yang.tvlauncher.utils.LogUtil;

/**
 * Created by yangshuang
 * on 2018/12/17.
 */

public class FoucsLinearLayout extends LinearLayout{
    public FoucsLinearLayout(Context context) {
        super(context);
    }

    public FoucsLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public FoucsLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FoucsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    private void init(){
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
                if (hasFocus) {
                    YoYo.with(new BaseViewAnimator() {
                        @Override
                        protected void prepare(View target) {
                            getAnimatorAgent().playTogether(
                                    ObjectAnimator.ofFloat(target, "scaleX", 1f, 1.15f),
                                    ObjectAnimator.ofFloat(target, "scaleY", 1f, 1.15f)
                            );
                        }
                    }).duration(100).repeat(0).playOn(v);
                } else {
                    YoYo.with(new BaseViewAnimator() {
                        @Override
                        protected void prepare(View target) {
                            getAnimatorAgent().playTogether(
                                    ObjectAnimator.ofFloat(target, "scaleX", 1.15f, 1f),
                                    ObjectAnimator.ofFloat(target, "scaleY", 1.15f, 1f)
                            );
                        }
                    }).duration(100).repeat(0).playOn(v);
                }
            }
        });
    }
}
