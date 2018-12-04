package com.yang.tvlauncher.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.AppInfoBean;

/**
 * Created by yangshuang
 * on 2018/12/3.
 */

public class AppButtonPresenter extends Presenter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_button,null);
        return new AppButtonHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        AppButtonHolder holder = (AppButtonHolder) viewHolder;
        AppInfoBean bean = (AppInfoBean) item;
        holder.icon.setImageDrawable(bean.getAppIcon());
        holder.name.setText(bean.getAppName());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        AppButtonHolder holder = (AppButtonHolder) viewHolder;
        holder.icon.setImageDrawable(null);
        holder.name.setText("");
    }
}
