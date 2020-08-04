package com.yang.tvlauncher.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeekerUtils {

    private static Handler handler;

    public interface SeekerListener<T> {
        void onSeekUpdate(T t);

        void onSeekComplete(List<T> ts);

        void onSeekError(String msg);

        void onSeekTitle(String title);

        boolean isExclude(T t);
    }

    public static void seekBannerImage(final String url, final SeekerListener<String> listener) {
        LogUtil.e("请求网址，获取HTML ： " + url);
        if (handler == null) handler = new Handler(Looper.getMainLooper());
        NetUitls.getHtmlString(url, new NetUitls.ReqeustListener() {
            @Override
            public void onResponse(String htmlString) {
                LogUtil.e(url + " 请求网址，获取HTML成功 ： " + htmlString.length());
                final Document document = Jsoup.parse(htmlString);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekTitle(document.title());
                    }
                });
                final List<String> images = getBannerUrl(url, htmlString, null, listener);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekComplete(images);
                    }
                });
            }

            @Override
            public void onFailure(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekError(msg);
                    }
                });
            }
        });
    }

    /**
     * 根据图片服务器地址获获取图片
     */
    public static void seekBannerImageWithDoMain(final String url, final String domain, final SeekerListener<String> listener) {
        LogUtil.e("请求网址，获取HTML ： " + url);
        if (handler == null) handler = new Handler(Looper.getMainLooper());
        NetUitls.getHtmlString(url, new NetUitls.ReqeustListener() {
            @Override
            public void onResponse(String htmlString) {
                LogUtil.e(url + " 请求网址，获取HTML成功 ： " + htmlString.length());
                final Document document = Jsoup.parse(htmlString);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekTitle(document.title());
                    }
                });
                final List<String> images = getBannerUrl(url, htmlString, domain, listener);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekComplete(images);
                    }
                });
            }

            @Override
            public void onFailure(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSeekError(msg);
                    }
                });
            }
        });
    }


    /**
     * 根据后缀名获取图片
     */
    private static Set<String> getAllImageWithStuff(String host, String htmlString) {
        LogUtil.e(host + " 解析HTML 筛选图片URL开始");
        if (htmlString == null || htmlString.equals("")) return null;
        Set<String> list = new HashSet<>();
        long time = System.currentTimeMillis();
        if (htmlString.contains(".png")) {
            List<String> pngLis1 = getPicUrl(htmlString, ".png");
            if (pngLis1 != null && !pngLis1.isEmpty()) {
                list.addAll(pngLis1);
            }
        }
        if (htmlString.contains(".PNG")) {
            List<String> pngLis2 = getPicUrl(htmlString, ".PNG");
            if (pngLis2 != null && !pngLis2.isEmpty()) {
                list.addAll(pngLis2);
            }
        }
        if (htmlString.contains(".jpg")) {
            List<String> jpgLis1 = getPicUrl(htmlString, ".jpg");
            if (jpgLis1 != null && !jpgLis1.isEmpty()) {
                list.addAll(jpgLis1);
            }
        }
        if (htmlString.contains(".JPG")) {
            List<String> jpgLis2 = getPicUrl(htmlString, ".JPG");
            if (jpgLis2 != null && !jpgLis2.isEmpty()) {
                list.addAll(jpgLis2);
            }
        }
        long remind = System.currentTimeMillis() - time;
        LogUtil.e(host + " 解析HTML 筛选图片URL ： 耗时 ： " + remind);
        return list;
    }

    /**
     * 根据图片服务器地址获获取图片
     */
    private static Set<String> getAllImageWidhDoMain(String htmlString, String domain) {
        String[] images = htmlString.split(domain);
        Set<String> list = new HashSet<>();
        if (images.length > 1) {
            for (int i = 1; i < images.length - 1; i++) {
                if (images[i].contains("\"")) {
                    int index = images[i].indexOf("\"");
                    String picUrl = "http://" + domain + images[i].substring(0, index);
                    list.add(picUrl);
                }
            }
        }
        return list;
    }

    private static List<String> getPicUrl(String htmlString, String stuff) {
        String pngs[] = htmlString.split(stuff);
        List<String> list = new ArrayList<>();
        if (pngs.length > 1) {
            for (int i = 0; i < pngs.length - 2; i++) {
                if (pngs[i].contains("//")) {
                    int index = pngs[i].lastIndexOf("//");
                    String picUrl = "http:" + pngs[i].substring(index) + stuff;
                    if (picUrl.contains(";") || picUrl.contains("\"") || picUrl.contains("<") || picUrl.contains(">"))
                        continue;
                    list.add(picUrl);
                }
            }
        }
        return list;
    }

    private static List<String> getBannerUrl(String host, String htmlString, String domain, final SeekerListener<String> listener) {
        Set<String> list = null;
        if (domain != null && !domain.equals("")) {
            // 根据图片域名筛选（图片无后缀）
            list = getAllImageWidhDoMain(htmlString, domain);
        } else {
            // 根据图片后缀筛选
            list = getAllImageWithStuff(host, htmlString);
        }
        List<String> newList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            LogUtil.e(host + " 过滤banner图片 ： 个数 ： " + list.size());
            long time = System.currentTimeMillis();
            for (final String url : list) {
                if (StringUtil.isEmpty(url)) continue;
                if (ImageManager.get(url) != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSeekUpdate(url);
                        }
                    });
                } else {
                    try {
                        Bitmap bitmap = Glide.with(AppUtil.getAppContext()).asBitmap().load(url).submit().get();
//                    bitmap = Picasso.with(AppUtil.getAppContext()).load(url).get();
                        // 高度在500至650之间，宽度大于1000 视为banner大图片
                        if (bitmap == null) continue;
                        if (!listener.isExclude(url) && bitmap.getHeight() > 500 && bitmap.getHeight() < 650 && bitmap.getWidth() > 1000 && bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2) != 0) {
                            newList.add(url);
                            ImageManager.put(url, bitmap);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSeekUpdate(url);
                                }
                            });
                        } else {
                            bitmap.recycle();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            long remind = System.currentTimeMillis() - time;
            LogUtil.e(host + " 过滤banner图片 ： 结束 ：" + remind);
        }
//        if (newList.size() > 0) {
//            for (String s : newList) {
//                LogUtil.e(s);
//            }
//        }
        return newList;
    }
}
