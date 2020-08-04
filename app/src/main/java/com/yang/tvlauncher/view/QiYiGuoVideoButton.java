package com.yang.tvlauncher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.request.BaseRequest;
import com.yang.tvlauncher.request.IqiyiRequest;
import com.yang.tvlauncher.utils.DataManager;

public class QiYiGuoVideoButton extends VideoButton {

    private static final String packageName = "com.gitvvideo.huanqiuzhidacpa";
    private static final String requestUrl = "https://www.iqiyi.com/";

    public QiYiGuoVideoButton(@NonNull Context context) {
        super(context);
    }

    public QiYiGuoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QiYiGuoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public QiYiGuoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected BaseRequest getRequest() {
        return new IqiyiRequest();
    }

    @Override
    protected String getRequestUrl() {
        return requestUrl;
    }

    @Override
    protected int getMainImageResource() {
        return R.drawable.bg_gitv;
    }

    @Override
    protected AppInfoBean getAppInfoBean() {
        return DataManager.getInstance(getContext()).getMatchingApp("银河奇异果", packageName);
    }
}
