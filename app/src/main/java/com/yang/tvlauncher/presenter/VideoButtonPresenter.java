package com.yang.tvlauncher.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by
 * yangshuang on 2018/11/28.
 */

public class VideoButtonPresenter extends Presenter {

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_header, null);
        return new VideoButtonHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        VideoButtonHolder holder = (VideoButtonHolder) viewHolder;
        holder.setData((List<HashMap<String, Object>>) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
//        VideoButtonHolder holder = (VideoButtonHolder) viewHolder;
//        holder.banner.releaseBanner();
//        holder.banner = null;
//        holder.desc = null;
//        if (holder.mapList != null) {
//            holder.mapList.clear();
//            holder.mapList = null;
//        }
//        if (holder.imageDescs != null) {
//            holder.imageDescs.clear();
//            holder.imageDescs = null;
//        }
//        if (holder.imageUrls != null) {
//            holder.imageUrls.clear();
//            holder.imageUrls = null;
//        }
    }
}
