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
        FileIOUtils.writeFileFromString(LOG_FILE_PATH, "------uncaughtException--------\n", true);
        String m = getThrowableInfo(e);
        FileIOUtils.writeFileFromString(LOG_FILE_PATH, m, true);
        LogUtil.e("TvExceptionHandler", m);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    private String getThrowableInfo(Throwable ex) {
        if (ex == null) return "";

        StackTraceElement[] trace = ex.getStackTrace();
        StringBuffer buffer = new StringBuffer();
        String tab = "                     ";
        buffer.append(ex.toString()).append("\n");
        for (StackTraceElement traceElement : trace)
            buffer.append(tab).append(traceElement.toString()).append("\n");

        // Print suppressed exceptions, if any
        for (Throwable se : ex.getSuppressed()) {
            StackTraceElement[] suppressed = se.getStackTrace();
            buffer.append("Suppressed Exception : ").append("\n");
            for (StackTraceElement traceElement : suppressed)
                buffer.append(tab).append(traceElement.toString()).append("\n");
        }

        // Print cause, if any
        Throwable ourCause = ex.getCause();
        if (ourCause != null) {
            StackTraceElement[] cause = ourCause.getStackTrace();
            buffer.append("Caused Exception : ").append("\n");
            for (StackTraceElement traceElement : cause)
                buffer.append(tab).append(traceElement.toString()).append("\n");
        }
        return buffer.toString();
    }
}
