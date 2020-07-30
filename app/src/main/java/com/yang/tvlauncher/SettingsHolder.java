package com.yang.tvlauncher;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.orhanobut.hawk.Hawk;
import com.yang.tvlauncher.utils.AppConst;

public class SettingsHolder {

    private HomeActivity mActivity;
    private LinearLayout home_settings_rl;
    private Switch sw_mode;

    private Animation translateInAnimation, translateOutAnimation;

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (animation.equals(translateOutAnimation)) {
                mActivity.getDesktopRootView().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation.equals(translateInAnimation)) {
                mActivity.getDesktopRootView().setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public SettingsHolder(HomeActivity mActivity) {
        this.mActivity = mActivity;
        initView();
    }

    private void initView() {
        home_settings_rl = mActivity.findViewById(R.id.home_settings_rl);
        sw_mode = mActivity.findViewById(R.id.sw_mode);

        home_settings_rl.setVisibility(View.GONE);
        home_settings_rl.postInvalidate();

        translateInAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, -1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        translateOutAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, -1);
        translateInAnimation.setDuration(300);
        translateOutAnimation.setDuration(300);
        translateInAnimation.setAnimationListener(animationListener);
        translateOutAnimation.setAnimationListener(animationListener);

        sw_mode.setChecked(Hawk.get(AppConst.SP.MODE, true));

        sw_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hawk.put(AppConst.SP.MODE, isChecked);
            }
        });
    }

    public boolean isSimple() {
        return sw_mode.isChecked();
    }


    public void show() {
        home_settings_rl.setVisibility(View.VISIBLE);
        home_settings_rl.startAnimation(translateInAnimation);
    }

    public void hide() {
        home_settings_rl.clearAnimation();
        home_settings_rl.startAnimation(translateOutAnimation);
        home_settings_rl.setVisibility(View.GONE);
    }

    public boolean isShow() {
        return home_settings_rl.getVisibility() == View.VISIBLE;
    }

}
