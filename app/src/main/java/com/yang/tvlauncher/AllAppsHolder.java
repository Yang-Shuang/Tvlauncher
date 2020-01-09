package com.yang.tvlauncher;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.bean.HomeRowBean;
import com.yang.tvlauncher.presenter.AllAppListAdapter;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllAppsHolder {

    private HomeActivity mActivity;
    private RelativeLayout home_apps_rl;
    private RecyclerView home_apps_rv;
    private AllAppListAdapter adapter;
    private Animation translateInAnimation, translateOutAnimation;

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (animation.equals(translateOutAnimation)){
                mActivity.getDesktopRootView().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation.equals(translateInAnimation)){
                mActivity.getDesktopRootView().setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


    public AllAppsHolder(HomeActivity mActivity) {
        this.mActivity = mActivity;
        initView();
    }

    private void initView() {
        home_apps_rl = mActivity.findViewById(R.id.home_apps_rl);
        home_apps_rv = mActivity.findViewById(R.id.home_apps_rv);
        home_apps_rv.setLayoutManager(new LinearLayoutManager(mActivity));

        home_apps_rl.setVisibility(View.GONE);
        home_apps_rl.postInvalidate();

        translateInAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        translateOutAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
        translateInAnimation.setDuration(300);
        translateOutAnimation.setDuration(300);
        translateInAnimation.setAnimationListener(animationListener);
        translateOutAnimation.setAnimationListener(animationListener);
    }

    public void getData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(mActivity).getPackageInfos();
                List<HashMap<String, Object>> list = new ArrayList<>();
                List<HomeRowBean> rowBeans = DataManager.getInstance(mActivity).getAllRows();
                for (HomeRowBean bean : rowBeans) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", bean.getName());
                    List<AppInfoBean> appInfoBeans = DataManager.getInstance(mActivity).getRowApps(bean.getRid(), true);
                    map.put("data", appInfoBeans);
                    list.add(map);
                }
                adapter = new AllAppListAdapter(list);
                home_apps_rv.setAdapter(adapter);
                home_apps_rv.scrollToPosition(0);
                home_apps_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == 0) {
                            View child = recyclerView.getFocusedChild();
                            if (child.getHeight() < ScreenUtil.screen_height && child.getTop() < 0) {
                                home_apps_rv.smoothScrollBy(0, child.getTop());
                            }
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }
        });
    }

    public void show() {
        home_apps_rl.setVisibility(View.VISIBLE);
        home_apps_rv.scrollToPosition(0);
        home_apps_rl.startAnimation(translateInAnimation);
    }

    public void hide() {
        home_apps_rl.clearAnimation();
        home_apps_rl.startAnimation(translateOutAnimation);
        home_apps_rl.setVisibility(View.GONE);
    }

    public boolean isShow() {
        return home_apps_rl.getVisibility() == View.VISIBLE;
    }
}
