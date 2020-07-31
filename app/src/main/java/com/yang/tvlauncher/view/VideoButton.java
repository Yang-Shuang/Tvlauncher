package com.yang.tvlauncher.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.request.BaseRequest;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.ImageManager;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.SeekerUtils;
import com.yang.tvlauncher.utils.ToastUtil;
import com.youth.banner.Banner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.youth.banner.BannerConfig.NOT_INDICATOR;

public abstract class VideoButton extends FrameLayout implements View.OnClickListener {


    private static class EventHandler extends Handler {
        private WeakReference<VideoButton> reference;

        public EventHandler(VideoButton button) {
            this.reference = new WeakReference<>(button);
        }

        boolean isAttach() {
            return reference != null && reference.get() != null;
        }

        void load() {
            sendEmptyMessage(101);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    if (isAttach()) {
                        if (AppUtil.isNetworkAvailable(reference.get().getContext())) {
                            reference.get().loadData();
                        } else {
                            sendEmptyMessageDelayed(101, 1000);
                        }
                    }
                    break;
                case 105:
                    if (isAttach()) {
                        reference.get().updateImage((String) msg.obj);
                    }
                    break;
            }
        }

    }

    protected EventHandler handler;
    private boolean isSimpleImage = true;
    protected AppInfoBean mAppInfoBean;

    protected LinearLayout content;
    protected RelativeLayout appInfo;
    protected ImageView mainImage;
    protected Banner banner;
    protected ImageView appIcon;
    protected TextView appName;
    protected TextView appDesc;

    private List<String> imageUrls = new ArrayList<>();

    public VideoButton(@NonNull Context context) {
        this(context, null);
    }

    public VideoButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new EventHandler(this);
        initView();
    }

    public VideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isSimpleImage() {
        return isSimpleImage;
    }

    public void setSimpleImage(boolean simpleImage) {
        isSimpleImage = simpleImage;
    }

    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_video_button, this);
        content = findViewById(R.id.content_ll);
        mainImage = findViewById(R.id.button_image);
        banner = findViewById(R.id.banner);
        appInfo = findViewById(R.id.rl_app_info);
        appIcon = findViewById(R.id.iv_app_icon);
        appName = findViewById(R.id.tv_app_name);
        appDesc = findViewById(R.id.tv_app_desc);

        setOnClickListener(this);

        banner.updateBannerStyle(NOT_INDICATOR);
        banner.setFocusable(false);
        banner.setFocusableInTouchMode(false);
        banner.setDelayTime(5000);
        banner.isAutoPlay(true);

        banner.setImageLoader(new ImageManager.GlideImageLoader());
        View viewpager = banner.findViewById(R.id.bannerViewPager);
        viewpager.setFocusable(false);
        viewpager.setFocusableInTouchMode(false);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Bitmap bitmap = null;
                if (imageUrls.isEmpty()) return;
                bitmap = ImageManager.get(imageUrls.get(position));
                if (bitmap != null) {
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            appName.setTextColor(palette.getLightVibrantColor(Color.WHITE));
                            appDesc.setTextColor(palette.getLightVibrantColor(Color.WHITE));
                            appInfo.setBackgroundColor(palette.getDarkMutedColor(Color.DKGRAY));
                        }
                    });
                }
                String title = ImageManager.getTitle(imageUrls.get(position));
                if (title != null && !"".equals(title)) {
                    appDesc.setText(title);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mAppInfoBean == null) {
            ToastUtil.toast("未安装此应用");
        } else {
            Intent intent = AppUtil.getAppIntent(mAppInfoBean.getPackageName());
            if (intent != null) {
                getContext().startActivity(intent);
            }
        }
    }

    private void loadData() {
        if (isSimpleImage()) {
            mainImage.setVisibility(VISIBLE);
            content.setVisibility(GONE);
            mainImage.setImageResource(getMainImageResource());
        } else {
            mainImage.setVisibility(GONE);
            content.setVisibility(VISIBLE);
            requestData();

            if (mAppInfoBean != null) {
                appIcon.setImageDrawable(mAppInfoBean.getAppIcon());
                appName.setText(mAppInfoBean.getAppName());
            }
        }
    }

    void updateImage(String s) {
        if (s == null || "".equals(s)) return;
        imageUrls.add(s);
        banner.update(new ArrayList<>(imageUrls));
    }

    private void requestData() {

        BaseRequest request = getRequest();
        if (request == null) return;
        imageUrls.clear();
        banner.update(new ArrayList<>());
        request.seek(new SeekerUtils.SeekerListener<String>() {
            @Override
            public void onSeekUpdate(String s) {
                LogUtil.e("onSeekUpdate : " + s);
                handler.sendMessage(handler.obtainMessage(105, s));
            }

            @Override
            public void onSeekComplete(List<String> strings) {

            }

            @Override
            public void onSeekError(String msg) {

            }

            @Override
            public void onSeekTitle(String title) {
                appDesc.setText(title);
            }

            @Override
            public boolean isExclude(String s) {
                return false;
            }
        }, getRequestUrl());

    }

    protected abstract BaseRequest getRequest();

    protected abstract String getRequestUrl();

    protected abstract int getMainImageResource();

    public void load() {
        if (handler != null)
            handler.load();
    }
}
