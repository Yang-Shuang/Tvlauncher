package com.yang.tvlauncher.presenter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.List;

public class AllAppListRowHolder extends RecyclerView.ViewHolder {


    private TextView title;
    private RecyclerView recyclerView;

    public AllAppListRowHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.row_title);
        recyclerView = itemView.findViewById(R.id.rows_rv);
    }

    private void mesureSize(int dataCount) {
        int margin = ScreenUtil.dp2px(40);
        int width = ScreenUtil.screen_width - margin * 2;
        float singleLineHeight = (width - ScreenUtil.dp2px(10) * 7) / 8f;
        int height = 0;
        if (dataCount % 8 == 0) {
            height = (int) (singleLineHeight * (dataCount / 8)) + (ScreenUtil.dp2px(10) * dataCount / 8);
        } else {
            height = (int) (singleLineHeight * ((dataCount / 8) + 1) + (ScreenUtil.dp2px(10) * dataCount / 8));
        }
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
        recyclerView.setLayoutParams(p);
        GridLayoutManager manager = new GridLayoutManager(recyclerView.getContext(), 8);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new MyItemDecoration(ScreenUtil.dp2px(10)));
    }

    public void setTopSpace(int topSpace) {
        LinearLayout linearLayout = (LinearLayout) title.getParent();
        linearLayout.setPadding(ScreenUtil.dp2px(40), topSpace, ScreenUtil.dp2px(40), ScreenUtil.dp2px(15));
    }

    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    public void setData(List<AppInfoBean> beans) {
        mesureSize(beans.size());
        recyclerView.setAdapter(new AllAppListRowAdapter(beans));
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int itemSpace;

        public MyItemDecoration(int itemSpace) {
            this.itemSpace = itemSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = itemSpace;
        }
    }
}
