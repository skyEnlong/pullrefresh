package com.eileng.app.library.view;

import android.graphics.drawable.Drawable;

/**
 * Created by peacepassion on 15/8/23.
 */
public class TabDataHolder {

    Drawable back;
    Drawable front;
    String title;
    int titleTargetColor;

    public TabDataHolder(Drawable back, Drawable front, String title, int titleTargetColor) {
        this.back = back;
        this.front = front;
        this.title = title;
        this.titleTargetColor = titleTargetColor;
    }
}
