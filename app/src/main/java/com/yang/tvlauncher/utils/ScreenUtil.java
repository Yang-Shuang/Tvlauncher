package com.yang.tvlauncher.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by yangshuang
 * on 2018/11/29.
 */

public class ScreenUtil {

    public static int screen_width;
    public static int screen_height;
    public static float screen_density;
    public static float screen_densityDpi;

    public ScreenUtil() {
    }

    public static void init(Context context) {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metric);
            screen_width = metric.widthPixels;
            screen_height = metric.heightPixels;
            screen_density = metric.density;
            screen_densityDpi = (float) metric.densityDpi;
        } catch (Exception var3) {
            LogUtil.e(var3.getMessage());
        }

    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * screen_density + 0.5F);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / screen_density + 0.5F);
    }
}
