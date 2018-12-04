package com.yang.tvlauncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BrowseFrameLayout;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.ScaleFrameLayout;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.tvlauncher.auto.BrowseErrorActivity;
import com.yang.tvlauncher.auto.DetailsActivity;
import com.yang.tvlauncher.auto.Movie;
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

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Timer mBackgroundTimer;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        setHeadersState(BrowseFragment.HEADERS_DISABLED);

//        loadData();
//        loadRows();

        setupEventListeners();
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
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadData() {
        DataManager.getInstance(getActivity()).getPackageInfos();
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);
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
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
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
                if (itemViewHolder == null)return;
                if (itemViewHolder instanceof VideoButtonHolder) {
                    VideoButtonHolder holder = (VideoButtonHolder) itemViewHolder;
                } else {
                    AppInfoBean bean = (AppInfoBean) rowViewHolder.getSelectedItem();
                    LogUtil.e("onItemSelected : " +  bean.getAppName());
                }
            }
        });
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            AppInfoBean bean = (AppInfoBean) item;
            ((TextView) viewHolder.view).setText(bean.getAppName());
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
