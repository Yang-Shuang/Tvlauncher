package com.yang.tvlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;

import com.yang.tvlauncher.presenter.AppButtonPresenter;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.bean.HomeRowBean;

import java.util.List;

/**
 * Created by
 * yangshuang on 2018/12/19.
 */

public class AppListFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHeadersState(BrowseFragment.HEADERS_DISABLED);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof AppInfoBean) {
                    AppInfoBean b = (AppInfoBean) item;
                    Intent intent = AppUtil.getAppIntent(b.getPackageName());
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loaddata();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loaddata() {
        if (mRowsAdapter == null) {
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            setAdapter(mRowsAdapter);
        }
        mRowsAdapter.clear();
        loadAllRows();
    }

    private void loadAllRows() {
        DataManager.getInstance(getContext()).getPackageInfos();
        List<HomeRowBean> rowBeans = DataManager.getInstance(getActivity()).getAllRows();
        for (HomeRowBean bean : rowBeans) {
            HeaderItem header = new HeaderItem(0, bean.getName());
            List<AppInfoBean> appInfoBeans = DataManager.getInstance(getActivity()).getRowApps(bean.getRid(), true);
            if (appInfoBeans != null && appInfoBeans.size() > 0) {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new AppButtonPresenter());
                listRowAdapter.addAll(0, appInfoBeans);
                mRowsAdapter.add(new ListRow(header, listRowAdapter));
            }
        }
    }


}
