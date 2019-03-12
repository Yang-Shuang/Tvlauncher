package com.yang.tvlauncher;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
        DataManager.getInstance(getContext()).getPackageInfos();
        List<HashMap<String, Object>> list = new ArrayList<>();
        List<HomeRowBean> rowBeans = DataManager.getInstance(getActivity()).getAllRows();
        for (HomeRowBean bean : rowBeans) {
            HeaderItem header = new HeaderItem(0, bean.getName());
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", bean.getName());
            List<AppInfoBean> appInfoBeans = DataManager.getInstance(getActivity()).getRowApps(bean.getRid(), true);
            map.put("data", appInfoBeans);
            list.add(map);
        }
        adapter = new AllAppListAdapter(list);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (fragment != null)
//            getFragmentManager().beginTransaction().remove(fragment).commit();

    }

    public void show(FragmentManager manager) {
        super.show(manager, "AllAppsDialog");
    }

}
