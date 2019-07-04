package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.tvlauncher.presenter.HomeShortCartHolder;
import com.yang.tvlauncher.presenter.HomeVideoButtonHolder;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.FileIOUtils;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.StringUtil;
import com.yang.tvlauncher.utils.TimeUtil;
import com.yang.tvlauncher.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class HomeActivity extends Activity {

    private static class MyHandler extends Handler {
        WeakReference<HomeActivity> mReference;

        public MyHandler(HomeActivity activity) {
            mReference = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (AppUtil.isNetworkAvailable(mReference.get())) {
                mReference.get().loadVideoButton();
            } else {
                sendEmptyMessageDelayed(101, 1000);
            }
            mReference.get().changeBackground();
        }
    }

    private float imageHeight;
    private TextView timeTv, dateTv;
    private Disposable subscription;
    private LinearLayout mParent;
    private LinearLayout mShortCutParent;
    private ChooseShortCutDialog dialog;
    private LinearLayout home_content_ll;

    private OnShortCutsClickListener onShortCutsClickListener;
    private OnBannerClickListener onBannerClickListener;
    private int clickPosition;
    private AllAppsDialog mAllAppsDialog;
    private MyHandler mHandler;
    private static final int[] backgroundIds = {R.drawable.bg_default_background, R.drawable.bg_background1, R.drawable.bg_background2, R.drawable.bg_background3, R.drawable.bg_background4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DataManager.getInstance(HomeActivity.this).initDataBases();
        DataManager.getInstance(HomeActivity.this).getPackageInfos();

        initView();
        initEventListener();
        initClock();

        mHandler = new MyHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(101);
        loadShortCut();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        mParent = findViewById(R.id.home_header_ll);
        home_content_ll = findViewById(R.id.home_content_ll);
        mShortCutParent = findViewById(R.id.home_shortcut_fll);
        timeTv = findViewById(R.id.main_time_tv);
        dateTv = findViewById(R.id.main_date_tv);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mParent.getLayoutParams();
        params.height = (int) ((ScreenUtil.screen_height - ScreenUtil.dp2px(110)) / 3f * 2);

        imageHeight = params.height / 2;
        mParent.setLayoutParams(params);
        mParent.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        mShortCutParent.setPadding(ScreenUtil.dp2px(20), 0, ScreenUtil.dp2px(20), 0);
    }

    private void initClock() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        timeTv.setText(TimeUtil.getTimeStr());
                        dateTv.setText(TimeUtil.getDateStr() + "\n农历" + TimeUtil.getLunarStr());
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_clear_ll:
//                changeBackground();
                //com.coolux.clearnup
//                ToastUtil.toast("别点了，功能没加呢");
                Intent intent = AppUtil.getAppIntent("com.coolux.clearnup");
                if (intent != null) {
                    startActivity(intent);
                }
                break;
            case R.id.home_set_ll:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
        }
    }

    public int backGroundIdIndex = 0;
    public void changeBackground() {
        home_content_ll.setBackgroundResource(backgroundIds[backGroundIdIndex]);
        if (backGroundIdIndex >= 4){
            backGroundIdIndex = 0;
        } else {
            backGroundIdIndex++;
        }
    }

    public void loadVideoButton() {
        LogUtil.e("---------开始加载Banner---------");
        checkIqiyiAndLoad();
        checkTencentAndLoad();
        checkYoukuAndLoad();
        if (!mParent.hasFocus() && !mShortCutParent.hasFocus()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mParent.getChildCount() > 0 && !mParent.getChildAt(0).hasFocus()) {
                        mParent.getChildAt(0).requestFocus();
                    }
                }
            }, 500);
        }
    }

    private void loadShortCut() {
        LogUtil.e("---------开始加载快捷方式---------");
        for (int i = 0; i < 9; i++) {
            HomeShortCartHolder holder = null;
            View parent = null;
            if (mShortCutParent.getChildAt(i) != null) {
                parent = mShortCutParent.getChildAt(i);
                holder = (HomeShortCartHolder) parent.getTag(R.id.viewHolder);
            } else {
                parent = LayoutInflater.from(this).inflate(R.layout.home_shortcut_item, null);
                holder = new HomeShortCartHolder(parent);
                if (i == 8) {
                    ((LinearLayout.LayoutParams) parent.getLayoutParams()).rightMargin = ScreenUtil.dp2px(25);
                }
                parent.setTag(R.id.viewHolder, holder);
                mShortCutParent.addView(parent);
            }
            AppInfoBean bean = null;
//            if (i == 7) {
//                bean = new AppInfoBean();
//                bean.setAppName("系统设置");
//                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_sys_settings));
//            } else
            if (i == 8) {
                bean = new AppInfoBean();
                bean.setAppName("全部程序");
                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_apps));
            } else {
                bean = DataManager.getInstance(this).getShortCutApp(i);
            }
            if (bean == null) {
                bean = new AppInfoBean();
                bean.setAppName("添加");
                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_add));
            }
            final int finalI = i;
            holder.setData(bean);
            parent.setTag(R.id.viewData, bean);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShortCutsClickListener.onItemClick(finalI, v.getTag(R.id.viewData));
                }
            });
        }
    }

    private void initEventListener() {
        initDialog();
        if (onBannerClickListener == null) {
            onBannerClickListener = new OnBannerClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    clickPosition = position;
                    switch (position) {
                        case 101:
                        case 102:
                        case 103:
                            HashMap<String, Object> map = (HashMap<String, Object>) data;
                            String packageName = (String) map.get("package");
                            if (StringUtil.isEmpty(packageName)) {
                                ArrayList<AppInfoBean> beans = DataManager.getInstance(HomeActivity.this).getAllApps();
                                AppInfoBean bean = new AppInfoBean();
                                bean.setAppName("清除");
                                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_delete));
                                beans.add(bean);
                                dialog.setData(beans);
                                dialog.show(getFragmentManager());
                            } else {
                                Intent intent = AppUtil.getAppIntent(packageName);
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            }
                        default:
                            break;
                    }
                }
            };
        }
        if (onShortCutsClickListener == null) {
            onShortCutsClickListener = new OnShortCutsClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    clickPosition = position;
                    switch (position) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            if (data == null || StringUtil.isEmpty(((AppInfoBean) data).getPackageName())) {
                                ArrayList<AppInfoBean> beans = DataManager.getInstance(HomeActivity.this).getAllApps();
                                AppInfoBean bean = new AppInfoBean();
                                bean.setAppName("清除");
                                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_delete));
                                beans.add(bean);
                                dialog.setData(beans);
                                dialog.show(getFragmentManager());
                            } else {
                                String packageName = ((AppInfoBean) data).getPackageName();
                                Intent intent = AppUtil.getAppIntent(packageName);
                                if (intent != null) {
                                    startActivity(intent);
                                }
                            }
                            break;
                        case 8:
                            mAllAppsDialog.show(getFragmentManager());
                            break;
                    }
                }
            };
        }
    }

    private void initDialog() {
        dialog = new ChooseShortCutDialog();
        dialog.setOnSelectedAppListener(new ChooseShortCutDialog.onSelectedAppListener() {
            @Override
            public void onSelectedApp(AppInfoBean bean) {
                if (clickPosition < 100) {
                    if (mShortCutParent.getChildAt(clickPosition) != null) {
                        View parent = mShortCutParent.getChildAt(clickPosition);
                        HomeShortCartHolder holder = (HomeShortCartHolder) parent.getTag(R.id.viewHolder);
                        if (StringUtil.isEmpty(bean.getPackageName())) {
                            bean.setAppName("添加");
                            bean.setAppIcon(getResources().getDrawable(R.drawable.icon_add));
                            DataManager.getInstance(HomeActivity.this).deleteShortCut(clickPosition);
                        } else {
                            DataManager.getInstance(HomeActivity.this).saveShortCut(bean, clickPosition);
                        }
                        parent.setTag(R.id.viewData, bean);
                        holder.setData(bean);
                    }
                } else {
                    if (mParent.getChildAt(clickPosition - 101) != null) {
                        View parent = mParent.getChildAt(clickPosition - 101);
                        if (!StringUtil.isEmpty(bean.getPackageName())) {
                            DataManager.getInstance(HomeActivity.this).saveHomeVideoApp(clickPosition - 101, bean.getPackageName());
                        } else {
                            return;
                        }
                        HomeVideoButtonHolder holder = (HomeVideoButtonHolder) parent.getTag(R.id.viewHolder);
                        HashMap<String, Object> map = holder.getData();
                        map.put("name", bean.getAppName());
                        map.put("package", bean.getPackageName());
                        map.put("icon", bean.getAppIcon());
                        parent.setTag(R.id.viewData, map);
                        holder.setData(map);
                    }
                }
            }
        });
        mAllAppsDialog = new AllAppsDialog();
    }

    private void checkIqiyiAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getHomeVideoApp(0);
        if (bean == null) {
            bean = DataManager.getInstance(this).getMatchingApp("银河奇异果", HomeVideoButtonHolder.IQIYI);
            if (bean != null) {
                DataManager.getInstance(this).saveHomeVideoApp(0, bean.getPackageName());
            }
        }
        View parent = null;
        HomeVideoButtonHolder holder = null;
        if (mParent.getChildCount() == 0) {
            parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            ((LinearLayout.LayoutParams) parent.getLayoutParams()).leftMargin = ScreenUtil.dp2px(25);
            parent.setTag(R.id.viewHolder, holder);
            mParent.addView(parent);
        } else {
            parent = mParent.getChildAt(0);
            holder = (HomeVideoButtonHolder) parent.getTag(R.id.viewHolder);
        }
        if (holder.isHasStart()) return;
        HashMap<String, Object> iqiyiData = new HashMap<>();
        iqiyiData.put("type", HomeVideoButtonHolder.IQIYI);
        if (bean != null) {
            iqiyiData.put("name", bean.getAppName());
            iqiyiData.put("package", bean.getPackageName());
            iqiyiData.put("icon", bean.getAppIcon());
        }
        parent.setTag(R.id.viewData, iqiyiData);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBannerClickListener.onItemClick(101, v.getTag(R.id.viewData));
            }
        });
        holder.setData(iqiyiData);
    }

    private void checkTencentAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getHomeVideoApp(1);
        if (bean == null) {
            bean = DataManager.getInstance(this).getMatchingApp("云视听极光", HomeVideoButtonHolder.YUNSHITING);
            if (bean != null) {
                DataManager.getInstance(this).saveHomeVideoApp(1, bean.getPackageName());
            }
        }
        View parent = null;
        HomeVideoButtonHolder holder = null;
        if (mParent.getChildAt(1) == null) {
            parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            ((LinearLayout.LayoutParams) parent.getLayoutParams()).leftMargin = ScreenUtil.dp2px(25);
            parent.setTag(R.id.viewHolder, holder);
            mParent.addView(parent);
        } else {
            parent = mParent.getChildAt(1);
            holder = (HomeVideoButtonHolder) parent.getTag(R.id.viewHolder);
        }
        if (holder.isHasStart()) return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("type", HomeVideoButtonHolder.YUNSHITING);
        if (bean != null) {
            data.put("name", bean.getAppName());
            data.put("package", bean.getPackageName());
            data.put("icon", bean.getAppIcon());
        }
        parent.setTag(R.id.viewData, data);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBannerClickListener.onItemClick(102, v.getTag(R.id.viewData));
            }
        });
        holder.setData(data);
    }

    private void checkYoukuAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getHomeVideoApp(2);
        if (bean == null) {
            bean = DataManager.getInstance(this).getMatchingApp("CIBN酷喵影视", HomeVideoButtonHolder.KUMIAO);
            if (bean != null) {
                DataManager.getInstance(this).saveHomeVideoApp(2, bean.getPackageName());
            }
        }
        View parent = null;
        HomeVideoButtonHolder holder = null;
        if (mParent.getChildAt(2) == null) {
            parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            ((LinearLayout.LayoutParams) parent.getLayoutParams()).leftMargin = ScreenUtil.dp2px(25);
            parent.setTag(R.id.viewHolder, holder);
            mParent.addView(parent);
        } else {
            parent = mParent.getChildAt(2);
            holder = (HomeVideoButtonHolder) parent.getTag(R.id.viewHolder);
        }
        if (holder.isHasStart()) return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("type", HomeVideoButtonHolder.KUMIAO);
        if (bean != null) {
            data.put("name", bean.getAppName());
            data.put("package", bean.getPackageName());
            data.put("icon", bean.getAppIcon());
        }
        holder.setData(data);
        parent.setTag(R.id.viewData, data);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBannerClickListener.onItemClick(103, v.getTag(R.id.viewData));
            }
        });
    }

    private interface OnShortCutsClickListener {
        void onItemClick(int position, Object data);
    }

    private interface OnBannerClickListener {
        void onItemClick(int position, Object data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                if (mShortCutParent.hasFocus()) {
                    clickPosition = mShortCutParent.indexOfChild(mShortCutParent.getFocusedChild());
                    ArrayList<AppInfoBean> beans = DataManager.getInstance(HomeActivity.this).getAllApps();
                    AppInfoBean bean = new AppInfoBean();
                    bean.setAppName("清除");
                    bean.setAppIcon(getResources().getDrawable(R.drawable.icon_delete));
                    beans.add(bean);
                    dialog.setData(beans);
                    dialog.show(getFragmentManager());
                } else if (mParent.hasFocus()) {
                    clickPosition = 101 + mParent.indexOfChild(mParent.getFocusedChild());
                    ArrayList<AppInfoBean> beans = DataManager.getInstance(HomeActivity.this).getAllApps();
                    AppInfoBean bean = new AppInfoBean();
                    bean.setAppName("清除");
                    bean.setAppIcon(getResources().getDrawable(R.drawable.icon_delete));
                    beans.add(bean);
                    dialog.setData(beans);
                    dialog.show(getFragmentManager());
                }
                break;
            case KeyEvent.KEYCODE_INFO:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }


}
