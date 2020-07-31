package com.yang.tvlauncher.request;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.yang.tvlauncher.utils.AppUtil;
import com.yang.tvlauncher.utils.ImageManager;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.NetUitls;
import com.yang.tvlauncher.utils.SeekerUtils;
import com.yang.tvlauncher.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
            LogUtil.e("请求 youku 数据......");
            NetUitls.getHtmlString("http://www.youku.com/", new NetUitls.ReqeustListener() {
                @Override
                public void onResponse(String htmlString) {
                    if (htmlString != null && !htmlString.equals("")) {
                        Document document = Jsoup.parse(htmlString);
//                        document.
                        Elements childs = document.body().getElementsByClass("focusswiper_focus_item");
                        if (childs.size() > 0) {
                            ArrayList<HashMap<String, String>> data = new ArrayList<>();
                            for (Element element : childs) {
                                HashMap<String, String> map = new HashMap<>();
                                String imageUrl = "";
                                String style = element.attr("style");
                                if (!StringUtil.isEmpty(style) && style.contains("url(")) {
                                    imageUrl = style.substring(style.indexOf("url(") + 4, style.indexOf(")"));
                                }
                                if (!StringUtil.isEmpty(imageUrl)) {
                                    imageUrl = "http:" + imageUrl;
                                }
                                map.put("name", element.attr("alt"));
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

    @Override
    public void seek(final SeekerUtils.SeekerListener<String> listener, String url) {
//        SeekerUtils.seekBannerImage(url, listener);
        NetUitls.getHtmlString("http://www.youku.com/", new NetUitls.ReqeustListener() {
            @Override
            public void onResponse(String htmlString) {
                if (htmlString != null && !htmlString.equals("")) {
                    final Document document = Jsoup.parse(htmlString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSeekTitle(document.title());
                        }
                    });
                    Elements scripts = document.getElementsByTag("script");
                    String initData = null;
                    if (scripts != null && scripts.size() > 0) {
                        for (Element element : scripts) {
                            String text = element.data();
                            if (text != null && text.contains("window.__INITIAL_DATA__")) {
                                String s = text.replace("undefined", "\"\"");
                                s = s.split("window.__INITIAL_DATA__")[1];
                                initData = s.substring(s.indexOf("{"), s.indexOf("};")) + "}";
                                break;
                            }
                        }
                        if (initData == null) return;
                        try {
                            JSONObject object = new JSONObject(initData);
                            JSONArray moduleList = object.getJSONArray("moduleList");
                            if (moduleList != null && moduleList.length() > 0) {
                                JSONObject banner = null;
                                for (int i = 0; i < moduleList.length(); i++) {
                                    JSONObject obj = (JSONObject) moduleList.get(i);
                                    if (obj != null && "PC_BANNER".equals(obj.getString("type"))) {
                                        banner = obj;
                                        break;
                                    }
                                }
                                if (banner != null) {
                                    JSONArray focusList = banner.getJSONArray("focusList");
                                    if (focusList != null && focusList.length() > 0) {
                                        for (int i = 0; i < focusList.length(); i++) {
                                            JSONObject obj = (JSONObject) focusList.get(i);
                                            String image = obj.getString("img");
                                            if (image != null && !image.equals("")) {
                                                image = "http:" + image;
                                                onSeekUrl(listener, image);
                                                String subtitle = obj.getString("subtitle");
                                                ImageManager.put(image, subtitle);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSeekError(e.getMessage());
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSeekError("htmlString == null");
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onSeekError(msg);
            }
        });
    }

    private void onSeekUrl(final SeekerUtils.SeekerListener<String> listener, final String url) {
        try {
            Bitmap bitmap = Glide.with(AppUtil.getAppContext()).asBitmap().load(url).submit().get();
            if (bitmap == null) return;
            ImageManager.put(url, bitmap);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onSeekUpdate(url);
                }
            });
        } catch (Exception ignored) {
        }
    }
}
