package com.yang.tvlauncher.request;

/**
 * Created by
 * yangshuang on 2018/11/30.
 */

public abstract class BaseRequest {
    public abstract void request(ResponseListener listener,boolean refresh);
}
