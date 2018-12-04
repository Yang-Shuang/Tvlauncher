package com.yang.tvlauncher;

import android.app.Application;

import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.ToastUtil;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class TVApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ScreenUtil.init(this);
        ToastUtil.init(this);
    }
}
