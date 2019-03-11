package com.yang.tvlauncher.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.bean.HomeRowBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class DataManager {

    private static DataManager manager;
    private Context mContext;
    private HashMap<String, Drawable> imageMap;

    private DataManager(Context context) {
        mContext = context;
    }

    public static DataManager getInstance(Context context) {
        if (manager == null) manager = new DataManager(context);
        return manager;
    }

    public void initDataBases() {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.close();
    }

    public void getPackageInfos() {
        ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>(); //用来存储获取的应用信息数据
        appList.addAll(0, getTestData());
        List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
        imageMap = new HashMap<>();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfoBean tmpInfo = new AppInfoBean();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            LogUtil.e("package---" + packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                tmpInfo.setCid(ClassifyUtil.getCategoryId(tmpInfo.getPackageName()));
            } else {
                tmpInfo.setCid(AppUtil.getAppIntent(tmpInfo.getPackageName()) == null ? 106 : 105);
            }
            imageMap.put(tmpInfo.getPackageName(), tmpInfo.getAppIcon());
            tmpInfo.setAppIcon(null);
            appList.add(tmpInfo);
        }
        saveAllApps(appList);
    }

    public void saveAllApps(List<AppInfoBean> beans) {
        LogUtil.i("保存并更新所有应用信息--" + beans.size() + "条....");
        long time = System.currentTimeMillis();
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        try {
            database.delete("t_apps", null, null);
        } catch (Exception e) {
        }
        int r = 0;
        for (AppInfoBean bean : beans) {
            ContentValues values = new ContentValues();
            values.put("name", bean.getAppName());
            values.put("versionName", bean.getVersionName());
            values.put("versionCode", bean.getVersionCode());
            values.put("cid", bean.getCid());
            int result = database.update("t_apps", values, "package=?", new String[]{bean.getPackageName()});
            if (result < 1) {
                values.put("rid", getRid(bean.getCid()));
                values.put("package", bean.getPackageName());
                r += database.insert("t_apps", null, values);
            } else {
                r += result;
            }
        }
        beans.clear();
        beans = null;
        LogUtil.i("保存并更新所有应用信息耗时 " + (System.currentTimeMillis() - time) + " 毫秒,成功结果：" + r);
    }

    public ArrayList<HomeRowBean> getAllRows() {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_home_row", null, null, null, null, null, "position ASC", null);
        ArrayList<HomeRowBean> homeRowBeans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                HomeRowBean bean = new HomeRowBean();
                bean.setRid(cursor.getInt(cursor.getColumnIndex("rid")));
                bean.setName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
                homeRowBeans.add(bean);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return homeRowBeans;
    }

    public List<AppInfoBean> getShortCutApp() {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_shortcuts", null, null, null, null, null, "position ASC", null);
        List<AppInfoBean> beans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AppInfoBean bean = new AppInfoBean();
                bean.setAppName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPackageName(cursor.getString(cursor.getColumnIndex("package")));
                bean.setAppIcon(imageMap.get(bean.getPackageName()));
                beans.add(bean);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return beans;
    }

    public AppInfoBean getShortCutApp(int position) {
        AppInfoBean bean = null;
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_shortcuts", null, "position=?", new String[]{"" + position}, null, null, "position ASC", null);
        List<AppInfoBean> beans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                bean = new AppInfoBean();
                bean.setAppName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPackageName(cursor.getString(cursor.getColumnIndex("package")));
                bean.setAppIcon(imageMap.get(bean.getPackageName()));
                break;
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return bean;
    }

    public List<AppInfoBean> getRowApps(int rid, boolean same) {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_apps", null, same ? "rid=?" : "rid!=?", new String[]{"" + rid}, null, null, "aid ASC", null);
        List<AppInfoBean> beans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AppInfoBean bean = new AppInfoBean();
                bean.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
                bean.setAppName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPackageName(cursor.getString(cursor.getColumnIndex("package")));
                bean.setAppIcon(imageMap.get(bean.getPackageName()));
                beans.add(bean);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return beans;
    }

    public ArrayList<AppInfoBean> getAllApps() {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_apps", null, null, null, null, null, "aid ASC", null);
        ArrayList<AppInfoBean> beans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AppInfoBean bean = new AppInfoBean();
                bean.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
                bean.setAppName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPackageName(cursor.getString(cursor.getColumnIndex("package")));
                bean.setAppIcon(imageMap.get(bean.getPackageName()));
                beans.add(bean);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return beans;
    }

    public AppInfoBean getAppInfo(String packageName) {
        AppInfoBean bean = null;
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_apps", null, "package=?", new String[]{packageName}, null, null, "aid ASC", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                bean = new AppInfoBean();
                bean.setAid(cursor.getInt(cursor.getColumnIndex("aid")));
                bean.setAppName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setPackageName(cursor.getString(cursor.getColumnIndex("package")));
                bean.setAppIcon(imageMap.get(packageName));
                break;
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return bean;
    }

    public void saveShortCut(AppInfoBean bean, int position) {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", bean.getAppName());
        values.put("package", bean.getPackageName());
        values.put("position", position);
        int result = database.update("t_shortcuts", values, "position=?", new String[]{"" + position});
        if (result < 1) {
            database.insert("t_shortcuts", null, values);
        }
    }

    public void saveHomeVideoApp(int position, String packageName) {
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        int result = 0;
        ContentValues values = new ContentValues();
        values.put("position", position);
        if (!StringUtil.isEmpty(packageName)) {
            values.put("package", packageName);
            result = database.update("t_home_videos", values, "position=?", new String[]{"" + position});
        }
        if (result < 1) {
            database.insert("t_home_videos", null, values);
        }
    }

    public AppInfoBean getHomeVideoApp(int position) {
        AppInfoBean bean = null;
        TVDataHelper helper = TVDataHelper.getIntance(mContext);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("t_home_videos", null, "position=?", new String[]{"" + position}, null, null, null, null);
        String packageName = null;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                packageName = cursor.getString(cursor.getColumnIndex("package"));
                break;
            }
        }
        if (StringUtil.isEmpty(packageName)) return null;
        bean = getAppInfo(packageName);
        cursor.close();
        sqLiteDatabase.close();
        return bean;
    }

    private int getRid(int category) {
        switch (category) {
            case 101:
            case 102:
                return 1;
            case 103:
                return 2;
            case 104:
                return 3;
            case 105:
                return 4;
            case 106:
                return 5;
            case 999:
                return 6;
            default:
                return 6;
        }
    }

    private List<AppInfoBean> getTestData() {
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
        List<AppInfoBean> beans = new ArrayList<>();

        AppInfoBean a = new AppInfoBean();
        a.setAppIcon(drawable);
        a.setRid(1);
        a.setCid(101);
        a.setVersionCode(1);
        a.setVersionName("1");
        a.setAppName("银河奇异果");
        a.setPackageName("com.gitvvideo.huanqiuzhidacpa");
        beans.add(a);

        AppInfoBean b = new AppInfoBean();
        b.setAppIcon(drawable);
        b.setRid(1);
        b.setCid(101);
        b.setVersionCode(1);
        b.setVersionName("1");
        b.setAppName("CIBN微视听");
        b.setPackageName("net.myvst.v2");
        beans.add(b);

        AppInfoBean c = new AppInfoBean();
        c.setAppIcon(drawable);
        c.setRid(1);
        c.setCid(101);
        c.setVersionCode(1);
        c.setVersionName("1");
        c.setAppName("云视听极光");
        c.setPackageName("com.ktcp.video");
        beans.add(c);

        AppInfoBean d = new AppInfoBean();
        d.setAppIcon(drawable);
        d.setRid(1);
        d.setCid(101);
        d.setVersionCode(1);
        d.setVersionName("1");
        d.setAppName("CIBN酷喵影视");
        d.setPackageName("com.cibn.tv");
        beans.add(d);

        AppInfoBean e = new AppInfoBean();
        e.setAppIcon(drawable);
        e.setRid(2);
        e.setCid(103);
        e.setVersionCode(1);
        e.setVersionName("1");
        e.setAppName("QQ音乐");
        e.setPackageName("com.tencent.qqmusictv");
        beans.add(e);

        AppInfoBean f = new AppInfoBean();
        f.setAppIcon(drawable);
        f.setRid(2);
        f.setCid(103);
        f.setVersionCode(1);
        f.setVersionName("1");
        f.setAppName("HiFi音乐");
        f.setPackageName("com.tongyong.xxbox");
        beans.add(f);

        AppInfoBean g = new AppInfoBean();
        g.setAppIcon(drawable);
        g.setRid(3);
        g.setCid(104);
        g.setVersionCode(1);
        g.setVersionName("1");
        g.setAppName("当贝影视快搜");
        g.setPackageName("com.tv.kuaisou");
        beans.add(g);
        return beans;
    }
}
