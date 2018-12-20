package com.yang.tvlauncher.request;

import com.yang.tvlauncher.utils.NetUitls;
import com.yang.tvlauncher.utils.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by
 * yangshuang on 2018/11/28.
 */

public class YoukuRequest extends BaseRequest {
    private static ArrayList<HashMap<String, String>> data;

    public void request(final ResponseListener listener, boolean refresh) {
        if ((data == null || data.size() == 0) || refresh) {
            NetUitls.getHtmlString("http://www.youku.com/", new NetUitls.ReqeustListener() {
                @Override
                public void onResponse(String htmlString) {
                    if (htmlString != null && !htmlString.equals("")) {
                        Document document = Jsoup.parse(htmlString);
                        Elements elements = document.body().getElementsByClass("focus-list");
                        Elements childs = elements.get(0).children();
                        if (childs.size() > 0) {
                            ArrayList<HashMap<String, String>> data = new ArrayList<>();
                            for (Element element : childs) {
                                HashMap<String, String> map = new HashMap<>();
                                String imageUrl = "";
                                String style = element.attr("style");
                                if (!StringUtil.isEmpty(style) && style.contains("url(")) {
                                    imageUrl = "http:" + style.substring(style.indexOf("url(") + 5, style.indexOf("')"));
                                } else {
                                    String _lazy = element.attr("_lazy");
                                    if (!StringUtil.isEmpty(_lazy) && _lazy.contains("url("))
                                        imageUrl = "http:" + _lazy.substring(_lazy.indexOf("url(") + 5, _lazy.indexOf("')"));
                                }
                                Element c = element.child(0);
                                map.put("name", c.attr("alt"));
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
                    listener.onResponse(null);
                }
            });
        } else {
            listener.onResponse(data);
        }
    }
}
