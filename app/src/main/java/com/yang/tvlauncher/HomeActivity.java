package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.yang.tvlauncher.presenter.VideoButtonHolder;
import com.yang.tvlauncher.utils.AppInfoBean;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.StringUtil;
import com.yang.tvlauncher.utils.TimeUtil;
import com.yang.tvlauncher.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;

public class HomeActivity extends Activity {

    private float imageHeight;
    private TextView timeTv, dateTv;
    private Disposable subscription;
    private LinearLayout mParent;
    private LinearLayout mShortCutParent;
    private ChooseShortCutDialog dialog;

    private OnShortCutsClickListener onShortCutsClickListener;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DataManager.getInstance(HomeActivity.this).initDataBases();
        DataManager.getInstance(HomeActivity.this).getPackageInfos();

        initView();
        initEventListener();
        initClock();
        loadVideoButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShortCut();
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
        mShortCutParent = findViewById(R.id.home_shortcut_fll);
        timeTv = findViewById(R.id.main_time_tv);
        dateTv = findViewById(R.id.main_date_tv);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mParent.getLayoutParams();
        params.height = (int) ((ScreenUtil.screen_height - ScreenUtil.dp2px(80)) / 3f * 2);

        imageHeight = params.height / 2;
        mParent.setLayoutParams(params);
        mParent.setPadding(ScreenUtil.dp2px(20), 0, ScreenUtil.dp2px(20), 0);
        mParent.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

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

    private void loadVideoButton() {
        mParent.removeAllViews();
        checkIqiyiAndLoad();
        checkTencentAndLoad();
        checkYoukuAndLoad();
    }

    private void loadShortCut() {

        List<AppInfoBean> mdata = DataManager.getInstance(this).getShortCutApp();
        for (int i = 0; i < 9; i++) {
            HomeShortCartHolder holder = null;
            View parent = null;
            if (mShortCutParent.getChildAt(i) != null) {
                parent = mShortCutParent.getChildAt(i);
                holder = (HomeShortCartHolder) parent.getTag(R.id.viewHolder);
            } else {
                parent = LayoutInflater.from(this).inflate(R.layout.home_shortcut_item, null);
                holder = new HomeShortCartHolder(parent);
                parent.setTag(R.id.viewHolder, holder);
                mShortCutParent.addView(parent);
            }
            AppInfoBean bean = null;
            if (i == 6) {
                bean = new AppInfoBean();
                bean.setAppName("桌面设置");
                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_settings));
            } else if (i == 7) {
                bean = new AppInfoBean();
                bean.setAppName("系统设置");
                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_sys_settings));
            } else if (i == 8) {
                bean = new AppInfoBean();
                bean.setAppName("全部程序");
                bean.setAppIcon(getResources().getDrawable(R.drawable.icon_apps));
            } else if (mdata.size() >= i + 1) {
                bean = mdata.get(i);
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
                        case 6:
                            ToastUtil.toast("还没有");
                            break;
                        case 7:
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                            break;
                        case 8:
                            startActivity(new Intent(HomeActivity.this, AppListActivity.class));
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
                if (mShortCutParent.getChildAt(clickPosition) != null) {
                    View parent = mShortCutParent.getChildAt(clickPosition);
                    HomeShortCartHolder holder = (HomeShortCartHolder) parent.getTag(R.id.viewHolder);
                    if (StringUtil.isEmpty(bean.getPackageName())) {
                        bean.setAppName("添加");
                        bean.setAppIcon(getResources().getDrawable(R.drawable.icon_add));
                    } else {
                        DataManager.getInstance(HomeActivity.this).saveShortCut(bean, clickPosition);
                    }
                    parent.setTag(R.id.viewData, bean);
                    holder.setData(bean);
                }
            }
        });
    }

    private void checkIqiyiAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getAppInfo(VideoButtonHolder.IQIYI);
        if (bean != null) {
            HashMap<String, Object> iqiyiData = new HashMap<>();
            iqiyiData.put("name", bean.getAppName());
            iqiyiData.put("package", VideoButtonHolder.IQIYI);
            iqiyiData.put("icon", bean.getAppIcon());
            View parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            HomeVideoButtonHolder holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            ((LinearLayout.LayoutParams) parent.getLayoutParams()).leftMargin = ScreenUtil.dp2px(25);
            holder.setData(iqiyiData);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AppUtil.getAppIntent(VideoButtonHolder.IQIYI);
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
            });
            mParent.addView(parent);
        } else {
            mParent.addView(notInstallView("没有安装奇异果"));
        }
    }

    private void checkTencentAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getAppInfo(VideoButtonHolder.YUNSHITING);
        if (bean != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", bean.getAppName());
            data.put("package", VideoButtonHolder.YUNSHITING);
            data.put("icon", bean.getAppIcon());
            View parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            HomeVideoButtonHolder holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            holder.setData(data);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AppUtil.getAppIntent(VideoButtonHolder.YUNSHITING);
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
            });
            mParent.addView(parent);
        } else {
            mParent.addView(notInstallView("没有安装云视听极光"));
        }
    }

    private void checkYoukuAndLoad() {
        AppInfoBean bean = DataManager.getInstance(this).getAppInfo(VideoButtonHolder.KUMIAO);
        if (bean != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", bean.getAppName());
            data.put("package", VideoButtonHolder.KUMIAO);
            data.put("icon", bean.getAppIcon());
            View parent = LayoutInflater.from(this).inflate(R.layout.item_video_button, null);
            HomeVideoButtonHolder holder = new HomeVideoButtonHolder(parent, this, (int) imageHeight);
            ((LinearLayout.LayoutParams) parent.getLayoutParams()).rightMargin = ScreenUtil.dp2px(25);
            holder.setData(data);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AppUtil.getAppIntent(VideoButtonHolder.KUMIAO);
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
            });
            mParent.addView(parent);
        } else {
            mParent.addView(notInstallView("没有安装酷喵"));
        }
    }
    private View notInstallView(String message){
        TextView textView = new TextView(this);
        textView.setBackgroundColor(getColor(R.color.trans_default_background));
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(message);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams((int)(imageHeight/3f * 5),(int)(imageHeight/3f * 5));
        p.rightMargin = ScreenUtil.dp2px(25);
        textView.setLayoutParams(p);
        return textView;

    }

    private interface OnShortCutsClickListener {
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
                }
                break;
            case KeyEvent.KEYCODE_INFO:
//                Intent i = new Intent(this,
//                        HomeActivity.class);
//                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
