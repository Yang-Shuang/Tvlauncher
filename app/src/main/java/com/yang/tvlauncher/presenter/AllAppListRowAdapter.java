package com.yang.tvlauncher.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;

import java.util.List;

public class AllAppListRowAdapter extends RecyclerView.Adapter<DialogListItemHolder> {

    private List<AppInfoBean> data;
    private OnItemClickListener listener;

    public AllAppListRowAdapter(List<AppInfoBean> data) {
        this.data = data;
    }

    @Override
    public DialogListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_button, null);

        return new DialogListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogListItemHolder holder, final int position) {
        holder.mesureLayoutSize(10);
        final AppInfoBean bean = data.get(position);
        holder.icon.setImageDrawable(bean.getAppIcon());
        holder.name.setText(bean.getAppName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position, bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object data);
    }
}
