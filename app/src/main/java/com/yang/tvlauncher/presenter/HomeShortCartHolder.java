package com.yang.tvlauncher.presenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.StringUtil;

/**
 * Created by yangshuang
 * on 2018/12/18.
 */

public class HomeShortCartHolder {

    private ImageView icon;
    private TextView name;
    private AppInfoBean bean;

    public HomeShortCartHolder(View view) {
        icon = view.findViewById(R.id.home_shortcut_item_icon);
        name = view.findViewById(R.id.home_shortcut_item_name);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.leftMargin = ScreenUtil.dp2px(10);
        view.setLayoutParams(params);

        // 左右40 + 40   view间距25   padding 10  40 + 40 + 10 * 8 + 10
        int imageSize = (ScreenUtil.screen_height - ScreenUtil.dp2px(170)) / 9;
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) icon.getLayoutParams();
        p.width = imageSize;
        p.height = imageSize;
        icon.setLayoutParams(p);
    }

    public void setData(AppInfoBean data) {
        this.bean = data;
        icon.setImageDrawable(data.getAppIcon());
        name.setText(data.getAppName());
    }

    public boolean isHasApp() {
        return bean != null && !StringUtil.isEmpty(bean.getPackageName());
    }
}
