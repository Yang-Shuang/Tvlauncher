package com.yang.tvlauncher.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.ScreenUtil;

/**
 * Created by yangshuang
 * on 2018/12/18.
 */

public class DialogListItemHolder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView icon;
    FrameLayout item_app_bcv;

    public DialogListItemHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.item_app_icon_iv);
        name = itemView.findViewById(R.id.item_app_name_tv);

        item_app_bcv = itemView.findViewById(R.id.item_app_bcv);
        item_app_bcv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.bg_selected);
                } else {
                    v.setBackgroundResource(R.color.default_background);
                }
            }
        });
    }

    public void mesureLayoutSize(int spanCount) {
        int width = (int) (ScreenUtil.screen_width * 1f / spanCount);
        int height = (int) (ScreenUtil.screen_width * 1f / spanCount);

        item_app_bcv.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        item_app_bcv.setFocusable(true);
        item_app_bcv.setFocusableInTouchMode(true);
        item_app_bcv.setPadding(0, (int) (width / 5.0f), 0, 0);

        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) icon.getLayoutParams();
        p.height = (int) (width / 2.0f);
        icon.setLayoutParams(p);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) name.getLayoutParams();
        params1.width = width - ScreenUtil.dp2px(20);
        params1.topMargin = p.height;
        params1.leftMargin = ScreenUtil.dp2px(10);
        params1.rightMargin = ScreenUtil.dp2px(10);
        name.setLayoutParams(params1);
    }
}
