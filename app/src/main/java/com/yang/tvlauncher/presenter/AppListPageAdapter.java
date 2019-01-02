package com.yang.tvlauncher.presenter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by
 * yangshuang on 2018/12/26.
 */

public class AppListPageAdapter extends PagerAdapter {

    private ArrayList<AppInfoBean> mData;
    private int count;
    private RecyclerView[] views;
    private onItemSelectedListener onItemSelectedListener;
    private int lastIndex = 0;

    public void setOnItemSelectedListener(AppListPageAdapter.onItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public AppListPageAdapter(ArrayList<AppInfoBean> mData) {
        this.mData = mData;
        if (mData.size() < 0) {
            count = 0;
        } else {
            count = mData.size() % 18 == 0 ? mData.size() / 18 : mData.size() / 18 + 1;
        }
        lastIndex = 0;
        views = new RecyclerView[count];
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RecyclerView recyclerView = null;
        if (views[position] == null) {
            recyclerView = new RecyclerView(container.getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 6));
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.width = (int) (ScreenUtil.screen_width / 10f * 7);
            params.height = (int) (ScreenUtil.screen_width / 3f);
            recyclerView.setLayoutParams(params);
            int[] indexs = getIndex(position);
            DialogListAdapter adapter = new DialogListAdapter(mData.subList(indexs[0], indexs[1]));
            adapter.setListener(new DialogListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    AppInfoBean bean = (AppInfoBean) data;
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onSelectedApp(bean);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            views[position] = recyclerView;
            container.addView(recyclerView);
        } else {
            recyclerView = views[position];
        }
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return count;
    }

    public RecyclerView getRecyclerView(int position) {
        return views[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private int[] getIndex(int position) {
        int[] index = new int[2];
        if (mData.size() == 0) {
            index[0] = 0;
            index[1] = 0;
        } else {
            int startIndex = position * 18;
            int endIndex = startIndex;
            if (position == count - 1) {
                endIndex = mData.size();
            } else {
                endIndex = startIndex + 18;
            }
            index[0] = startIndex;
            index[1] = endIndex;
        }
        return index;
    }

    public void onPageChange(int position) {
        if (position < lastIndex) {
            views[position].getChildAt(5).requestFocus();
        }
        lastIndex = position;
    }


    public interface onItemSelectedListener {
        void onSelectedApp(AppInfoBean bean);
    }
}
