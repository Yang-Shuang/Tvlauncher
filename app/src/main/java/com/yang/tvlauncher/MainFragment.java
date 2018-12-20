package com.yang.tvlauncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yang.tvlauncher.presenter.AppButtonHolder;
import com.yang.tvlauncher.presenter.AppButtonPresenter;
import com.yang.tvlauncher.presenter.VideoButtonHolder;
import com.yang.tvlauncher.presenter.VideoButtonPresenter;
import com.yang.tvlauncher.utils.AppInfoBean;
import com.yang.tvlauncher.utils.BackgroundUtil;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.HomeRowBean;
import com.yang.tvlauncher.utils.LogUtil;

public class MainFragment extends BrowseFragment {

    private static final String TAG = "MainFragment";

    private ArrayObjectAdapter mRowsAdapter;
    private boolean isSelectApp = false;
    private ListRow headerListRow;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        setHeadersState(BrowseFragment.HEADERS_DISABLED);

        setupEventListeners();
    }

    public void refreshUI() {
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabase();
    }

    private void initDatabase() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(getContext()).initDataBases();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadData() {
        DataManager.getInstance(getActivity()).getPackageInfos();
        if (mRowsAdapter == null) {
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            setAdapter(mRowsAdapter);
        }
        loadHeaderRow();
        loadAllRows();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadHeaderRow() {
        Log.e("MainFragment", "addRows--header");
        HeaderItem header = new HeaderItem(0, "Video");
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new VideoButtonPresenter());
        List<HashMap<String, Object>> headerData = new ArrayList<>();
        checkIqiyiAndLoad(headerData);
        checkTencentAndLoad(headerData);
        checkYoukuAndLoad(headerData);
        if (headerData.size() > 0) {
            listRowAdapter.add(headerData);
            headerListRow = new ListRow(header, listRowAdapter);
            mRowsAdapter.add(headerListRow);
        }
        BackgroundUtil.init(getActivity());

    }

    private void checkIqiyiAndLoad(List<HashMap<String, Object>> list) {
        AppInfoBean bean = DataManager.getInstance(getActivity()).getAppInfo(VideoButtonHolder.IQIYI);
        if (bean != null) {
            HashMap<String, Object> iqiyiData = new HashMap<>();
            iqiyiData.put("name", "银河奇异果");
            iqiyiData.put("package", VideoButtonHolder.IQIYI);
            iqiyiData.put("icon", bean.getAppIcon());
            list.add(iqiyiData);
        }
    }

    private void checkTencentAndLoad(List<HashMap<String, Object>> list) {
        AppInfoBean bean = DataManager.getInstance(getActivity()).getAppInfo(VideoButtonHolder.YUNSHITING);
        if (bean != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", "云视听极光");
            data.put("package", VideoButtonHolder.YUNSHITING);
            data.put("icon", bean.getAppIcon());
            list.add(data);
        }
    }

    private void checkYoukuAndLoad(List<HashMap<String, Object>> list) {
        AppInfoBean bean = DataManager.getInstance(getActivity()).getAppInfo(VideoButtonHolder.KUMIAO);
        if (bean != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", "酷喵");
            data.put("package", VideoButtonHolder.KUMIAO);
            data.put("icon", bean.getAppIcon());
            list.add(data);
        }
    }

    private void loadAllRows() {
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


    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (itemViewHolder == null) return;
                if (itemViewHolder instanceof AppButtonHolder) {
                    if (rowViewHolder.getSelectedItem() == null) return;
                    AppInfoBean bean = (AppInfoBean) rowViewHolder.getSelectedItem();
                    LogUtil.e("onItemSelected : " + bean.getAppName());
                    isSelectApp = true;
                } else {
                    isSelectApp = false;
                }
            }
        });
    }

    public boolean isSelectApp() {
        return isSelectApp;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }

}
