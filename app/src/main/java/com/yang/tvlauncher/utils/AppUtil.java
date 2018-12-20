package com.yang.tvlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by
 * yangshuang on 2018/12/19.
 */

public class AppUtil {

    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static Intent getAppIntent(String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = null;
        intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            intent = packageManager.getLeanbackLaunchIntentForPackage(packageName);
        }
        return intent;
    }
}
