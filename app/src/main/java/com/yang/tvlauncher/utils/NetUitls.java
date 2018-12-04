package com.yang.tvlauncher.utils;


import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangshuang
 * on 2018/11/28.
 */

public class NetUitls {


    public static String byte2String(byte[] data, String charsetName) {
        try {
            return new String(data, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String getHtmlString(String urlString) {
//        try {
//            URL url = new URL(urlString);
//            URLConnection ucon = url.openConnection();
//            InputStream instr = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(instr);
//            ByteArrayBuilder baf = new ByteArrayBuilder(500);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//            return byte2String(baf.toByteArray(), "gbk");
//        } catch (Exception e) {
//            return "";
//        }
//    }

    public static void getHtmlString(String url, final ReqeustListener listener) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.eventListener(new MyEventListener(listener));
//        OkHttpClient client = new OkHttpClient();
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user_agent", "chrome")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(response.body().string());
            }
        });
    }

    public interface ReqeustListener {
        void onResponse(String data);

        void onFailure(String msg);
    }
}
