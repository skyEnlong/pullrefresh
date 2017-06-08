package com.eileng.app.library.util;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by enlong on 2017/2/4.
 */

public class ViewCompat {


    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            SDK16.postOnAnimation(view, runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    public static void setProgress(ProgressBar progressbar, int progress) {
        if (Build.VERSION.SDK_INT >= 24) {
            progressbar.setProgress(progress,true);

        }else {
            progressbar.setProgress(progress);

        }
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setLayerType(View view, int layerType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SDK11.setLayerType(view, layerType);
        }
    }

    @TargetApi(11)
    static class SDK11 {

        public static void setLayerType(View view, int layerType) {
            view.setLayerType(layerType, null);
        }
    }

    @TargetApi(16)
    static class SDK16 {

        public static void postOnAnimation(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        public static void setBackground(View view, Drawable background) {
            view.setBackground(background);
        }

    }


}
