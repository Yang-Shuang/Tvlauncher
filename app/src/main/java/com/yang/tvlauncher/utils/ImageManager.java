package com.yang.tvlauncher.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by
 * yangshuang on 2018/12/19.
 */

public class ImageManager {

    private static HashMap<String, Bitmap> bitmapHashMap;

    public static void init() {
        if (bitmapHashMap == null)
            bitmapHashMap = new HashMap<>();
    }

    public static Bitmap getRandomBitmap() {
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        arrayList.addAll(bitmapHashMap.values());
        return arrayList.get(new Random().nextInt(arrayList.size()));
    }

    public static void put(String key, Bitmap bitmap) {
        bitmapHashMap.put(key, bitmap);
    }

    public static Bitmap get(String key) {
        return bitmapHashMap.get(key);
    }

    public static class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, final Object path, ImageView imageView) {
            Glide.with(context).load(path).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    ImageManager.put(path.toString(), resource);
                }
            });
        }
    }

}
