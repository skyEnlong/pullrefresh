package com.eileng.app.library.view;

import android.graphics.drawable.Drawable;

/**
 * Created by enlong on 2017/5/23.
 */

public class TabIndicatorHolder {

    public Drawable commonDrawable;
    public Drawable selectDrawable;
    public int selectColor;
    public int commonTextColor;
    public String tartgetStr;

    public TabIndicatorHolder(Drawable commonDrawable,
                              Drawable selectDrawable,
                              int commonTextColor,
                              int selectColor,
                              String tartgetStr
    ) {
        this.commonDrawable = commonDrawable;
        this.selectColor = selectColor;
        this.commonTextColor = commonTextColor;
        this.selectDrawable = selectDrawable;
        this.tartgetStr = tartgetStr;
    }

    public TabIndicatorHolder() {

    }
}
