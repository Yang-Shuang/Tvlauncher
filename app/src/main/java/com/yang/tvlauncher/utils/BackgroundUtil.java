package com.yang.tvlauncher.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.yang.tvlauncher.MainFragment;
import com.yang.tvlauncher.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by
 * yangshuang on 2018/11/30.
 */

public class BackgroundUtil {
    public static HashMap<String, Bitmap> bitmapHashMap;

    public static void init(Activity context) {
        bitmapHashMap = new HashMap<>();
        final BackgroundManager mBackgroundManager = BackgroundManager.getInstance(context);
        if(!mBackgroundManager.isAttached()){
            mBackgroundManager.attach(context.getWindow());
        }
        Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.bg_default_background);
        DisplayMetrics mMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mBackgroundManager.setDrawable(mDefaultBackground);
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (bitmapHashMap.size() > 0) {
//                    timer.cancel();
//                    startBackgroundTimer(mBackgroundManager);
//                }
//            }
//        }, 3000, 2000);
    }

    private static void startBackgroundTimer(final BackgroundManager mBackgroundManager) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mBackgroundManager.setBitmap(getRandomBitmap());
            }
        }, 0, 3000);
    }

    private static Bitmap getRandomBitmap() {
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        arrayList.addAll(bitmapHashMap.values());
        return arrayList.get(new Random().nextInt(arrayList.size()));
    }

}
