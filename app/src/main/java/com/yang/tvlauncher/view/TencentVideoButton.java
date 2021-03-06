package com.yang.tvlauncher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.request.BaseRequest;
import com.yang.tvlauncher.request.TencentRequest;
import com.yang.tvlauncher.utils.DataManager;

public class TencentVideoButton extends VideoButton {
    private static final String packageName = "com.ktcp.video";
    private static final String requestUrl = "https://v.qq.com/";

    public TencentVideoButton(@NonNull Context context) {
        super(context);
    }

    public TencentVideoButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TencentVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TencentVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected BaseRequest getRequest() {
        return new TencentRequest();
    }

    @Override
    protected String getRequestUrl() {
        return requestUrl;
    }

    @Override
    protected int getMainImageResource() {
        return R.drawable.bg_tencent;
    }

    @Override
    protected AppInfoBean getAppInfoBean() {
        return DataManager.getInstance(getContext()).getMatchingApp("云视听极光", packageName);
    }
}
