package com.yang.tvlauncher.request;

import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.NetUitls;
import com.yang.tvlauncher.utils.SeekerUtils;
import com.yang.tvlauncher.utils.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yangshuang
 * on 2018/11/28.
 */

public class IqiyiRequest extends BaseRequest {

    private static ArrayList<HashMap<String, String>> data;

    @Override
    public void request(ResponseListener listener, boolean refresh) {

    }

    @Override
    public void seek(SeekerUtils.SeekerListener<String> listener, String url) {
        SeekerUtils.seekBannerImage(url, listener);
    }

    public void requestOld(final ResponseListener listener, boolean refresh) {
        if ((data == null || data.size() == 0) || refresh) {
            LogUtil.e("请求 iqiyi 数据......");
            NetUitls.getHtmlString("https://www.iqiyi.com/", new NetUitls.ReqeustListener() {
                @Override
                public void onResponse(String htmlString) {
                    if (htmlString != null && !htmlString.equals("")) {
                        Document document = Jsoup.parse(htmlString);
                        Elements elements = document.body().getElementsByClass("focus-index-item");
                        if (elements.size() > 0) {
                            data = new ArrayList<>();
                            for (Element element : elements) {
                                HashMap<String, String> map = new HashMap<>();
                                String imageUrl = "";
                                String data_jpg_img = element.attr("data-jpg-img");
                                if (StringUtil.isEmpty(data_jpg_img)) {
                                    String style = element.attr(":style");
                                    String back = style.substring(style.indexOf("url(") + 4, style.indexOf(")'"));
                                    imageUrl = "http:" + back;
                                } else {
                                    imageUrl = "http:" + data_jpg_img;
                                }
                                map.put("name", element.child(0).attr("alt"));
                                map.put("image", imageUrl);
                                data.add(map);
                            }
                            listener.onResponse(data);
                        } else {
                            listener.onResponse(null);
                        }
                    } else {
                        listener.onResponse(null);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    LogUtil.e("onFailure : " + msg);
                    listener.onResponse(null);
                }
            });
        } else {
            listener.onResponse(data);
        }
    }
}
