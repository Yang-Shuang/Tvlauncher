package com.yang.tvlauncher.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.request.BaseRequest;
import com.yang.tvlauncher.request.IqiyiRequest;
import com.yang.tvlauncher.request.ResponseListener;
import com.yang.tvlauncher.request.TencentRequest;
import com.yang.tvlauncher.request.YoukuRequest;
import com.yang.tvlauncher.utils.ImageManager;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.youth.banner.Banner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.youth.banner.BannerConfig.NOT_INDICATOR;

/**
 * Created by yangshuang
 * on 2018/12/18.
 */

public class HomeVideoButtonHolder {

    public static final String IQIYI = "com.gitvvideo.huanqiuzhidacpa";
    public static final String YUNSHITING = "com.ktcp.video";
    public static final String KUMIAO = "com.cibn.tv";

    private HashMap<String, Object> map;
    private float imageHeight;
    private float imageWidth;
    private boolean firstLoad = true;
    private ImageView icon;
    private TextView desc;
    private TextView title;
    private Banner banner;
    private RelativeLayout descLayout;
    private Context mContext;
    private boolean hasStart = false;


    public HomeVideoButtonHolder(View view, Context context, int imageHeight) {
        this.mContext = context;
        this.imageHeight = imageHeight;
        this.imageWidth = this.imageHeight / 3f * 5;

        icon = view.findViewById(R.id.item_icon);
        desc = view.findViewById(R.id.item_desc);
        title = view.findViewById(R.id.item_title);
        banner = view.findViewById(R.id.item_banner);
        descLayout = view.findViewById(R.id.item_desc_rl);

        CardView layout = view.findViewById(R.id.item_ll);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.leftMargin = ScreenUtil.dp2px(30);
        layout.setLayoutParams(p);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) descLayout.getLayoutParams();
        params1.topMargin = (int) imageHeight;
        params1.width = (int) imageWidth;
        descLayout.setLayoutParams(params1);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) banner.getLayoutParams();
        params.width = (int) imageWidth;
        params.height = (int) imageHeight;
        banner.setLayoutParams(params);

        banner.updateBannerStyle(NOT_INDICATOR);
        banner.setFocusable(false);
        banner.setFocusableInTouchMode(false);
    }

    public boolean isHasStart() {
        return hasStart;
    }

    public void setData(HashMap<String, Object> map) {
        if (hasStart) return;
        this.map = map;
        icon.setImageDrawable((Drawable) map.get("icon"));
        title.setText(map.get("name").toString());
        requestBannerData();
    }

    private void requestBannerData() {

        String packageName = (String) this.map.get("package");
        BaseRequest request = getRequest(packageName);
        request.request(new ResponseListener() {
            @Override
            public void onResponse(List<HashMap<String, String>> data) {
                if (data != null && data.size() > 0) {
                    final List<String> imageUrls = new ArrayList<>();
                    final List<String> imageDescs = new ArrayList<>();
                    for (HashMap<String, String> map : data) {
                        imageDescs.add(map.get("name"));
                        imageUrls.add(map.get("image"));
                    }
                    desc.post(new Runnable() {
                        @Override
                        public void run() {
                            banner.setImageLoader(new ImageManager.GlideImageLoader());
                            banner.setImages(imageUrls);
                            banner.start();
                            View viewpager = banner.findViewById(R.id.bannerViewPager);
                            viewpager.setFocusable(false);
                            viewpager.setFocusableInTouchMode(false);
                            hasStart = true;
                            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    desc.setText(imageDescs.get(position));
                                    Bitmap bitmap = null;
                                    bitmap = ImageManager.get(imageUrls.get(position));
                                    if (bitmap != null) {
                                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                desc.setTextColor(palette.getLightVibrantColor(Color.WHITE));
                                                title.setTextColor(palette.getLightVibrantColor(Color.WHITE));
                                                descLayout.setBackgroundColor(palette.getDarkMutedColor(Color.DKGRAY));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            });
                        }
                    });
                }

            }
        }, firstLoad);
        firstLoad = false;
    }

    private BaseRequest getRequest(String packageName) {
        if (packageName.equals(IQIYI)) {
            return new IqiyiRequest();
        } else if (packageName.equals(YUNSHITING)) {
            return new TencentRequest();
        } else if (packageName.equals(KUMIAO)) {
            return new YoukuRequest();
        }
        return null;
    }
}
