package com.yang.tvlauncher;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.yang.tvlauncher.presenter.AppListPageAdapter;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by
 * yangshuang on 2018/12/18.
 */

public class ChooseShortCutDialog extends DialogFragment {


    private ViewPager pager;
    private CirclePageIndicator indicator;
    private ArrayList<AppInfoBean> beans;
    private onSelectedAppListener onSelectedAppListener;
    private int currentPageIndex = 0;
    private AppListPageAdapter adapter;

    public void setOnSelectedAppListener(ChooseShortCutDialog.onSelectedAppListener onSelectedAppListener) {
        this.onSelectedAppListener = onSelectedAppListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_app_list_layout, container);
        pager = view.findViewById(R.id.dialog_apps_pager);
        indicator = view.findViewById(R.id.dialog_apps_indicator);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pager.getLayoutParams();
        params.width = (int) (ScreenUtil.screen_width / 10f * 7);
        params.height = (int) (ScreenUtil.screen_width / 3f);
        pager.setLayoutParams(params);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        adapter = new AppListPageAdapter(beans);
        adapter.setOnItemSelectedListener(new AppListPageAdapter.onItemSelectedListener() {
            @Override
            public void onSelectedApp(AppInfoBean bean) {
                dismiss();
                if (onSelectedAppListener != null) {
                    onSelectedAppListener.onSelectedApp(bean);
                }
            }
        });
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        currentPageIndex = 0;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                adapter.onPageChange(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public void setData(ArrayList<AppInfoBean> beans) {
        this.beans = beans;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pager != null && pager.getAdapter() != null) {
            pager.setCurrentItem(currentPageIndex);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        pager.clearOnPageChangeListeners();
        pager.setAdapter(null);
        adapter = null;
    }

    public void show(FragmentManager manager) {
        super.show(manager, "ChooseShortCutDialog");
    }

    public interface onSelectedAppListener {
        void onSelectedApp(AppInfoBean bean);
    }
}
