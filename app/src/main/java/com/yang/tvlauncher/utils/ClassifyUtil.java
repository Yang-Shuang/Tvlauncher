package com.yang.tvlauncher.utils;

import java.util.HashMap;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class ClassifyUtil {

    private static final HashMap<String, Integer> referenceMap = new HashMap<String, Integer>() {{
        put("com.gitvdemo.video", 101); //银河奇异果
        put("com.ktcp.video", 101); //云视听极光
        put("com.cibn.tv", 101); //CIBN酷喵影视
        put("cn.cibntv.ott", 101); //CIBN高清影视
        put("com.pplive.androidxl", 101); //CIBN聚精彩
        put("net.myvst.v2", 101); //CIBN微视听
        put("com.dangbeimarket", 104); //当贝市场
        put("com.dangbei.tvlauncher", 104); //当贝桌面
        put("com.elinkway.tvlive2", 102); //电视家2.0
        put("com.dianshijia.newlive", 102); //电视家3.0
        put("com.moretv.android", 101); //云视听MoreTV
        put("com.cloudmedia.videoplayer", 102); //电视直播
        put("com.douyu.xl.douyutv", 101); //斗鱼
        put("com.tongyong.xxbox", 103); //HiFi音乐
        put("com.duowan.kiwitv", 101); //虎牙TV版
        put("com.pptv.tvsports", 101); //CIBN聚体育
        put("com.hpplay.happyplay.aw", 104); //HappyCast
        put("com.starcor.mango", 101); //芒果TV
        put("com.vcinema.client.tv", 101); //南瓜电影
        put("com.tencent.qqmusictv", 103); //QQ音乐
        put("com.tv.kuaisou", 104); //当贝影视快搜
    }};

    private ClassifyUtil() {
    }

    public static int getCategoryId(String packname) {
        if (packname == null) return 999;
        int cid = 999;
        if (referenceMap.containsKey(packname)) {
            cid = referenceMap.get(packname);
        }
        return cid;
    }

}
