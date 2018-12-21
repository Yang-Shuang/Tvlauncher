package com.yang.tvlauncher.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.ViewHolder;
import com.yang.tvlauncher.R;
import com.yang.tvlauncher.utils.AppInfoBean;
import com.yang.tvlauncher.utils.ScreenUtil;

/**
 * Created by yangshuang
 * on 2018/12/21.
 */

public class DialogAppInfoViewHolder extends ViewHolder {

    private ImageView icon;
    private TextView name;
    private TextView packageName;
    private TextView version;
    private AppInfoBean mBean;

    public DialogAppInfoViewHolder(int viewResourceId) {
        super(viewResourceId);
    }

    public DialogAppInfoViewHolder(View contentView) {
        super(contentView);
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(ScreenUtil.dp2px(300), ScreenUtil.screen_height);
        p.gravity = Gravity.RIGHT;
        icon = contentView.findViewById(R.id.dialog_app_icon_iv);
        name = contentView.findViewById(R.id.dialog_app_name_tv);
        packageName = contentView.findViewById(R.id.dialog_app_package_tv);
        version = contentView.findViewById(R.id.dialog_app_version_tv);
        contentView.setLayoutParams(p);
    }

    public void setData(AppInfoBean bean) {
        if (bean != null) {
            this.mBean = bean;
            icon.setImageDrawable(mBean.getAppIcon());
            name.setText(mBean.getAppName());
            packageName.setText(mBean.getPackageName());
            version.setText(mBean.getVersionName());
        }
    }


}
