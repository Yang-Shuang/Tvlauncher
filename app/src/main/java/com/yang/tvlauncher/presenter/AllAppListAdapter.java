package com.yang.tvlauncher.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.HashMap;
import java.util.List;

public class AllAppListAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private List<HashMap<String, Object>> beans;

    public AllAppListAdapter(List<HashMap<String, Object>> beans) {
        this.beans = beans;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_all_app_list_row, null);
            return new AllAppListRowHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_all_app_list_header, null);
            return new BaseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == 0) return;
        AllAppListRowHolder holder1 = (AllAppListRowHolder) holder;
        HashMap<String, Object> data = beans.get(position);
        String name = (String) data.get("name");
        List<AppInfoBean> beans = (List<AppInfoBean>) data.get("data");
        if (position == 0) {
            holder1.setTopSpace(ScreenUtil.screen_height / 4);
        }
        holder1.setTitle(name);
        holder1.setData(beans);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 1;
        }
    }
}
