package com.yang.tvlauncher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yang.tvlauncher.R;
import com.yang.tvlauncher.bean.AppInfoBean;
import com.yang.tvlauncher.request.BaseRequest;
import com.yang.tvlauncher.request.TencentRequest;
import com.yang.tvlauncher.request.YoukuRequest;
import com.yang.tvlauncher.utils.DataManager;

public class KuMiaoVideoButton extends VideoButton {
    private static final String packageName = "com.cibn.tv";
    private static final String requestUrl = "https://www.youku.com/";

    public KuMiaoVideoButton(@NonNull Context context) {
        super(context);
    }

    public KuMiaoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KuMiaoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KuMiaoVideoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected BaseRequest getRequest() {
        return new YoukuRequest();
    }

    @Override
    protected String getRequestUrl() {
        return requestUrl;
    }

    @Override
    protected int getMainImageResource() {
        return R.drawable.bg_kumiao;
    }

    @Override
    protected AppInfoBean getAppInfoBean() {
        return DataManager.getInstance(getContext()).getMatchingApp("CIBN酷喵影视", packageName);
    }
}
