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

        int height = (int) ((ScreenUtil.screen_height - ScreenUtil.dp2px(80)) / 9f * 2);
        int width = (int) (height / 2f * 1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = ScreenUtil.dp2px(25);
        view.setLayoutParams(params);

        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) icon.getLayoutParams();
        p.width = width;
        p.height = width;
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
