package com.yang.tvlauncher.utils;

import android.content.Context;
import android.content.Intent;

import com.yang.tvlauncher.LogActivity;

public class TvExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static TvExceptionHandler handler;
    public static String LOG_FILE_PATH;
    private static Context mContext;

    private TvExceptionHandler() {
    }

    public static void init(Context context) {
        if (handler == null) {
            handler = new TvExceptionHandler();
            mContext = context;
            LOG_FILE_PATH = context.getFilesDir().getPath() + "/log/log.txt";
        }
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        FileIOUtils.writeFileFromString(LOG_FILE_PATH,"------uncaughtException--------\n",true);
        FileIOUtils.writeFileFromString(LOG_FILE_PATH,e.getMessage(),true);
    }
}
