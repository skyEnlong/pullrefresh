package com.eileng.app.library.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;

/**
 * Created by enlong on 2017/3/7.
 */

public class GlideLoader {

    public static void loadImage(String url, int defId, ImageView view) {
        Glide.with(view.getContext()).load(url).placeholder(defId).error(defId).centerCrop().into(view);
    }

    public static void loadImage(String url, int defId, int wid, int height, ImageView view) {
        Glide.with(view.getContext()).load(url).placeholder(defId).
                error(defId).override(wid, height).into(view);
    }

    public static void loadImage(Context c, String url, int defId, ImageView view) {
        Glide.with(c).load(url).placeholder(defId).error(defId).into(view);
    }

    public static void loadImage(Context c,
                                 String url,
                                 int defId,
                                 Transformation transformation,
                                 ImageView view) {
        Glide.with(c).load(url).placeholder(defId).error(defId).bitmapTransform(transformation).into(view);

    }

    public static void loadImage(Context c,
                                 Uri uri,
                                 int defId,
                                 Transformation transformation,
                                 ImageView view) {
        Glide.with(c).load(uri).placeholder(defId).bitmapTransform(transformation).into(view);

    }

    public static void cleanCache(Context context) {
        Glide.get(context.getApplicationContext()).clearDiskCache();
    }
}
