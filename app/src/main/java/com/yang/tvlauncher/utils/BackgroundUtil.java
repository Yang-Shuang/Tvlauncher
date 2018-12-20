package com.yang.tvlauncher.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;
import com.yang.tvlauncher.R;
/**
 * Created by
 * yangshuang on 2018/11/30.
 */

public class BackgroundUtil {


    public static void init(Activity context) {
        final BackgroundManager mBackgroundManager = BackgroundManager.getInstance(context);
        if(!mBackgroundManager.isAttached()){
            mBackgroundManager.attach(context.getWindow());
        }
        Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.bg_default_background);
        DisplayMetrics mMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mBackgroundManager.setDrawable(mDefaultBackground);
    }
}
