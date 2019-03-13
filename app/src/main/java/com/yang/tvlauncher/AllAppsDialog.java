package com.yang.tvlauncher;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.bean.HomeRowBean;
import com.yang.tvlauncher.presenter.AllAppListAdapter;
import com.yang.tvlauncher.presenter.AppButtonPresenter;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by
 * yangshuang on 2019/1/2.
 */

public class AllAppsDialog extends DialogFragment {

    //    AppListFragment fragment;
    private RecyclerView mRecyclerView;
    private AllAppListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnimation;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_all_apps, container);
//        fragment = (AppListFragment) getFragmentManager().findFragmentById(R.id.app_list_fragment);

        View view = inflater.inflate(R.layout.dialog_all_apps_list, container);
        mRecyclerView = view.findViewById(R.id.dialog_all_app_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getColor(R.color.bg_all_app_list)));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(getContext()).getPackageInfos();
                List<HashMap<String, Object>> list = new ArrayList<>();
                List<HomeRowBean> rowBeans = DataManager.getInstance(getActivity()).getAllRows();
                for (HomeRowBean bean : rowBeans) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", bean.getName());
                    List<AppInfoBean> appInfoBeans = DataManager.getInstance(getActivity()).getRowApps(bean.getRid(), true);
                    map.put("data", appInfoBeans);
                    list.add(map);
                }
                adapter = new AllAppListAdapter(list);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.scrollToPosition(0);
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == 0) {
                            View child = recyclerView.getFocusedChild();
                            if (child.getHeight() < ScreenUtil.screen_height && child.getTop() < 0) {
                                mRecyclerView.smoothScrollBy(0, child.getTop());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.clearOnScrollListeners();
    }

    public void show(FragmentManager manager) {
        super.show(manager, "AllAppsDialog");
    }

}
