package com.yang.tvlauncher.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class ToastUtil {
    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
