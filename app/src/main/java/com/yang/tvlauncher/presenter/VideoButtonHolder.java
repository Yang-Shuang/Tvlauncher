package com.yang.tvlauncher.presenter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
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

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;
import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;
import static com.youth.banner.BannerConfig.NOT_INDICATOR;

/**
 * Created by
 * yangshuang on 2018/11/28.
 */

public class VideoButtonHolder extends Presenter.ViewHolder {

    public static final String IQIYI = "com.gitvvideo.huanqiuzhidacpa";
    public static final String YUNSHITING = "com.ktcp.video";
    public static final String KUMIAO = "com.cibn.tv";

    List<HashMap<String, Object>> mapList;
    LinearLayout mParent;

    private float imageWidth;
    private float imageHeight;
    private boolean firstLoad = true;
    private Context mContext;


    public VideoButtonHolder(View view, Context context) {
        super(view);
        this.mContext = context;
        imageWidth = ScreenUtil.screen_width / 4.0f;
        imageHeight = imageWidth / 5.0f * 3;
        mParent = view.findViewById(R.id.item_header_ll);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mParent.getLayoutParams();
        params.height = (int) (imageHeight + ScreenUtil.dp2px(140));
        mParent.setLayoutParams(params);
        mParent.setPadding(ScreenUtil.dp2px(20), 0, ScreenUtil.dp2px(20), 0);
        mParent.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }

    public void setData(List<HashMap<String, Object>> mapList) {
        if (this.mapList != null && this.mapList.size() > 0)
            return;
        this.mapList = mapList;
        mParent.removeAllViews();
        int i = 0;
        for (HashMap<String, Object> data : mapList) {
            View parent = LayoutInflater.from(mContext).inflate(R.layout.item_video_button, null);
            bindData(parent, data);
            mParent.addView(parent);
        }

    }

    private void bindData(final View parent, final HashMap<String, Object> data) {
        ImageView icon = parent.findViewById(R.id.item_icon);
        final CardView layout = parent.findViewById(R.id.item_ll);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.rightMargin = ScreenUtil.dp2px(30);
        layout.setLayoutParams(p);

        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    YoYo.with(new BaseViewAnimator() {
                        @Override
                        protected void prepare(View target) {
                            getAnimatorAgent().playTogether(
                                    ObjectAnimator.ofFloat(target, "scaleX", 1f, 1.2f),
                                    ObjectAnimator.ofFloat(target, "scaleY", 1f, 1.2f)
                            );
                        }
                    }).duration(100).repeat(0).playOn(layout);
                    layout.setCardElevation(ScreenUtil.dp2px(5));
                } else {
                    YoYo.with(new BaseViewAnimator() {
                        @Override
                        protected void prepare(View target) {
                            getAnimatorAgent().playTogether(
                                    ObjectAnimator.ofFloat(target, "scaleX", 1.2f, 1f),
                                    ObjectAnimator.ofFloat(target, "scaleY", 1.2f, 1f)
                            );
                        }
                    }).duration(100).repeat(0).playOn(layout);
                    layout.setCardElevation(0);
                }
            }
        });

        icon.setImageDrawable((Drawable) data.get("icon"));
        requestBannerData(parent, data);
    }

    private void requestBannerData(View parent, HashMap<String, Object> data) {
        final TextView desc = parent.findViewById(R.id.item_desc);
        final TextView title = parent.findViewById(R.id.item_title);
        final Banner banner = parent.findViewById(R.id.item_banner);
        final RelativeLayout descLayout = parent.findViewById(R.id.item_desc_rl);

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

        title.setText(data.get("name").toString());
        String packageName = (String) data.get("package");
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
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
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
