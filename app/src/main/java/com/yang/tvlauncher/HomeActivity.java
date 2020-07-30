package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.tvlauncher.presenter.HomeShortCartHolder;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.DataManager;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.StringUtil;
import com.yang.tvlauncher.utils.TimeUtil;
import com.yang.tvlauncher.view.KuMiaoVideoButton;
import com.yang.tvlauncher.view.QiYiGuoVideoButton;
import com.yang.tvlauncher.view.TencentVideoButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends Activity {

    private TextView timeTv, dateTv;

    private QiYiGuoVideoButton qiYiGuoVideoButton;
    private TencentVideoButton tencentVideoButton;
    private KuMiaoVideoButton kuMiaoVideoButton;

    private LinearLayout mShortCutParent;
    private ChooseShortCutDialog dialog;
    private LinearLayout home_content_ll;

    private Disposable subscription;
    private OnShortCutsClickListener onShortCutsClickListener;

    private int clickPosition;
    private AllAppsHolder allAppsHolder;
    private SettingsHolder settingsHolder;
    private HomeActivity mActivity;

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
    }

    public ViewGroup getDesktopRootView() {
        return home_content_ll;
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadVideoButton();
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

        home_content_ll = findViewById(R.id.home_content_ll);
        mShortCutParent = findViewById(R.id.home_shortcut_fll);

        timeTv = findViewById(R.id.main_time_tv);
        dateTv = findViewById(R.id.main_date_tv);

        qiYiGuoVideoButton = findViewById(R.id.qiyiguo);
        tencentVideoButton = findViewById(R.id.tencent);
        kuMiaoVideoButton = findViewById(R.id.kumiao);

        mShortCutParent.setPadding(ScreenUtil.dp2px(20), 0, ScreenUtil.dp2px(20), 0);

        allAppsHolder = new AllAppsHolder(this);
        allAppsHolder.getData();

        settingsHolder = new SettingsHolder(this);

        home_content_ll.setBackgroundResource(R.drawable.bg_background1);
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
        if (backGroundIdIndex >= 4) {
            backGroundIdIndex = 0;
        } else {
            backGroundIdIndex++;
        }
    }

    public void loadVideoButton() {
        qiYiGuoVideoButton.setSimpleImage(settingsHolder.isSimple());
        tencentVideoButton.setSimpleImage(settingsHolder.isSimple());
        kuMiaoVideoButton.setSimpleImage(settingsHolder.isSimple());

        qiYiGuoVideoButton.load();
        tencentVideoButton.load();
        kuMiaoVideoButton.load();
    }

    private void loadShortCut() {
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
                            if (!allAppsHolder.isShow()) allAppsHolder.show();
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
                }
            }
        });
    }

    private interface OnShortCutsClickListener {
        void onItemClick(int position, Object data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.e("onKeyDown : " + keyCode);
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
                } else {
                    if (!settingsHolder.isShow())
                        settingsHolder.show();
                }
                break;
            case KeyEvent.KEYCODE_INFO:
                break;
        }
        boolean handle = super.onKeyDown(keyCode, event);
        LogUtil.e("onKeyDown : " + keyCode + " : " + handle);
        return handle;
    }

    @Override
    public void onBackPressed() {
        if (allAppsHolder.isShow()) allAppsHolder.hide();

        if (settingsHolder.isShow()) {
            settingsHolder.hide();
            loadVideoButton();
        }
    }

}
