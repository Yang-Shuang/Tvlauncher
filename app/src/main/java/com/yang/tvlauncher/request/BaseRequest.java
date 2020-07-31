package com.yang.tvlauncher.request;

import android.os.Handler;

import com.yang.tvlauncher.utils.SeekerUtils;

/**
 * Created by
 * yangshuang on 2018/11/30.
 */

public abstract class BaseRequest {

    protected Handler handler = new Handler();

    public abstract void request(ResponseListener listener, boolean refresh);

    public abstract void seek(SeekerUtils.SeekerListener<String> listener, String url);

}
