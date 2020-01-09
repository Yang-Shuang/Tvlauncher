package com.yang.tvlauncher.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.bean.BaseListItem;

import java.util.List;

public class AllAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseListItem> mItems;

    public AllAppsAdapter(List<BaseListItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class AppsViewHolder extends RecyclerView.ViewHolder{

        public AppsViewHolder(View itemView) {
            super(itemView);
        }
    }
    static class AppsViewTitleHolder extends RecyclerView.ViewHolder{

        public AppsViewTitleHolder(View itemView) {
            super(itemView);
        }
    }
}
