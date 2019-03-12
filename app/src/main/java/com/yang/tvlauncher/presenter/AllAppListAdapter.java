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

public class AllAppListAdapter extends RecyclerView.Adapter<AllAppListRowHolder> {


    private List<HashMap<String, Object>> beans;

    public AllAppListAdapter(List<HashMap<String, Object>> beans) {
        this.beans = beans;
    }

    @Override
    public AllAppListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_all_app_list_row, null);
        return new AllAppListRowHolder(view);
    }

    @Override
    public void onBindViewHolder(AllAppListRowHolder holder, int position) {
        HashMap<String, Object> data = beans.get(position);
        String name = (String) data.get("name");
        List<AppInfoBean> beans = (List<AppInfoBean>) data.get("data");
        if (position == 0) {
            holder.setTopSpace(ScreenUtil.screen_height / 4);
        }
        holder.setTitle(name);
        holder.setData(beans);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }
}
