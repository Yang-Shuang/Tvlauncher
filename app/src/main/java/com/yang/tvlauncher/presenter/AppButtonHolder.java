package com.yang.tvlauncher.presenter;

import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.ScreenUtil;

import java.util.HashMap;

/**
 * Created by yangshuang
 * on 2018/12/3.
 */

public class AppButtonHolder extends Presenter.ViewHolder {

    ImageView icon;
    TextView name;

    public AppButtonHolder(View view) {
        super(view);
        icon = view.findViewById(R.id.item_app_icon_iv);
        name = view.findViewById(R.id.item_app_name_tv);

        BaseCardView item_app_bcv = view.findViewById(R.id.item_app_bcv);

        int width = (int) (ScreenUtil.screen_width / 10.0f);
        int height = (int) (ScreenUtil.screen_width / 10.0f);

        item_app_bcv.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        item_app_bcv.setFocusable(true);
        item_app_bcv.setFocusableInTouchMode(true);
        item_app_bcv.setPadding(0, (int) (width / 5.0f), 0, 0);

        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) icon.getLayoutParams();
//        p.width = (int) (width / 2.0f);
        p.height = (int) (width / 2.0f);
        icon.setLayoutParams(p);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) name.getLayoutParams();
        params1.width = width;
        params1.topMargin = p.height;
        params1.leftMargin = ScreenUtil.dp2px(10);
        params1.rightMargin = ScreenUtil.dp2px(10);
        name.setLayoutParams(params1);

    }
}
