package com.yang.tvlauncher.request;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangshuang
 * on 2018/11/28.
 */

public interface ResponseListener {

    void onResponse(List<HashMap<String, String>> data);
}
