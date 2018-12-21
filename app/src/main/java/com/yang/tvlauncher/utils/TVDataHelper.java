package com.yang.tvlauncher.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yang.tvlauncher.BuildConfig;
import com.yang.tvlauncher.bean.CategoryBean;
import com.yang.tvlauncher.bean.DBInfoBean;
import com.yang.tvlauncher.bean.HomeRowBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class TVDataHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String SPACE = " ";
    private static final String CREATE = "create";
    private static final String TABLE = "table";
    private static final String PRIMARY = "primary key";
    private static final String FOREIGN = "foreign key";
    private static final String AUTO_INCREMENT = "autoincrement";
    private static final String NOT_NULL = "not null";

    private static TVDataHelper mMySqliteHelper;

    public static TVDataHelper getIntance(Context context) {
        if (mMySqliteHelper == null) {
            mMySqliteHelper = new TVDataHelper(context);
        }
        return mMySqliteHelper;
    }

    public TVDataHelper(Context context) {
        super(context, "TV_LAUNCHER_DB", null, BuildConfig.DB_VERSION);
        this.mContext = context;
    }

    public TVDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.i("创建数据库...");
        DBInfoBean bean = getInfo();
        if (bean == null || bean.getInfoList() == null || bean.getInfoList().size() == 0) {
            LogUtil.i("解析数据库预设信息失败...");
        } else {
            List<String> sqls = createSqls(false, bean, 0);
            if (sqls == null || sqls.size() == 0) {
                LogUtil.i("SQL语句为空...");
                return;
            }
            for (String s : sqls) {
                try {
                    db.execSQL(s);
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                }
            }
            insertDefaultRows(db);
            insertDefaultCategory(db);
        }
    }

    private void insertDefaultRows(SQLiteDatabase database) {
        LogUtil.i("增加默认行信息...");
        try {
            InputStream s = mContext.getAssets().open("default_rows.json");
            List<HomeRowBean> beans = JsonParser.jsonFile2List(s, HomeRowBean.class);
            for (HomeRowBean bean : beans) {
                ContentValues values = new ContentValues();
                values.put("rid", bean.getRid());
                values.put("name", bean.getName());
                values.put("position", bean.getPosition());
                database.insert("t_home_row", null, values);
            }
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        }
    }

    private void insertDefaultCategory(SQLiteDatabase database) {
        LogUtil.i("增加默认分类信息...");
        try {
            InputStream s = mContext.getAssets().open("category.json");
            List<CategoryBean> beans = JsonParser.jsonFile2List(s, CategoryBean.class);
            for (CategoryBean bean : beans) {
                ContentValues values = new ContentValues();
                values.put("cid", bean.getCid());
                values.put("name", bean.getName());
                database.insert("t_category", null, values);
            }
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("更新数据库...");
        DBInfoBean bean = getInfo();
        if (bean == null || bean.getInfoList() == null || bean.getInfoList().size() == 0) {
            LogUtil.i("解析数据库预设信息失败...");
        } else {
            List<String> sqls = createSqls(true, bean, oldVersion);
            if (sqls == null || sqls.size() == 0) {
                LogUtil.i("SQL语句为空...");
                return;
            }
            for (String s : sqls) {
                try {
                    db.execSQL(s);
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                }
            }
        }
    }

    private DBInfoBean getInfo() {
        LogUtil.i("获取数据库预设信息...");
        try {
            InputStream s = mContext.getAssets().open("db_info.json");
            return JsonParser.jsonFile2Bean(s, DBInfoBean.class);
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
            return null;
        }
    }

    private List<String> createSqls(boolean alter, DBInfoBean bean, int oldversion) {
        LogUtil.i("创建SQL语句，类型 [ " + (alter ? "修改" : "创建") + " ]...");
        ArrayList<String> sqlStrings = null;
        if (alter) {
            //
        } else {
            List<DBInfoBean.InfoListBean> beans = bean.getInfoList();
            DBInfoBean.InfoListBean info = null;
            for (DBInfoBean.InfoListBean b : beans) {
                if (b.getVersion() == BuildConfig.DB_VERSION) {
                    info = b;
                    break;
                }
            }
            if (info != null) {
                sqlStrings = new ArrayList<>();
                for (DBInfoBean.InfoListBean.TablesBean tablesBean : info.getTables()) {
                    String sqlString = "create table " + tablesBean.getName() + "(";
                    for (int i = 0; i < tablesBean.getColumn().size(); i++) {
                        DBInfoBean.InfoListBean.TablesBean.ColumnBean columnBean = tablesBean.getColumn().get(i);
                        String key = "";
                        String foreignSql = "";
                        if (columnBean.getKeyType() != null) {
                            key = columnBean.getKeyType().equalsIgnoreCase("primary") ? PRIMARY : columnBean.getKeyType().equalsIgnoreCase("foreign") ? FOREIGN : "";
                            if (columnBean.getKeyType().equalsIgnoreCase("foreign")) {
                                foreignSql = ",FOREIGN KEY(" + columnBean.getName() + ") REFERENCES " + columnBean.getReferences().getTname() + "(" + columnBean.getReferences().getCname() + ")";
                            }
                        }
                        String increment = columnBean.isIncrement() ? AUTO_INCREMENT : "";
                        String isNull = columnBean.isAllowEmpty() ? "" : NOT_NULL;

                        sqlString = sqlString + columnBean.getName() + SPACE
                                + columnBean.getType() + SPACE
                                + (key.length() > 0 ? key + SPACE : "")
                                + (increment.length() > 0 ? increment + SPACE : "") + isNull + foreignSql + (i == tablesBean.getColumn().size() - 1 ? ")" : ",");
                    }
                    sqlStrings.add(sqlString);
                }
            }
        }
        return sqlStrings;
    }
}
