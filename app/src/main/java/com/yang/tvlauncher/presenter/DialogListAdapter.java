package com.yang.tvlauncher.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.AppInfoBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangshuang
 * on 2018/12/18.
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListItemHolder> {

    private ArrayList<AppInfoBean> mData;
    private OnItemClickListener listener;

    public DialogListAdapter(ArrayList<AppInfoBean> mData) {
        this.mData = mData;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DialogListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_button,null);

        return new DialogListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogListItemHolder holder, final int position) {
        final AppInfoBean bean = mData.get(position);
        holder.icon.setImageDrawable(bean.getAppIcon());
        holder.name.setText(bean.getAppName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onItemClick(position,bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position,Object data);
    }
}
