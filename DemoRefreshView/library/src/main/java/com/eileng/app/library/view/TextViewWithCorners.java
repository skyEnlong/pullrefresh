package com.eileng.app.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.eileng.app.library.R;
import com.eileng.app.library.util.DensityUtil;


/**
 * Created by enlong on 16/7/21.
 */
public class TextViewWithCorners extends TextView{

    protected GradientDrawable mGradientDrawable;
    private int raduis;
    private int color;

    private int defColor = 0x2aba66;// 默认颜色

    public TextViewWithCorners(Context context) {
        super(context);
        init();
    }

    public TextViewWithCorners(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.TextViewCorner);

        if(null != a){
            raduis = a.getDimensionPixelSize(R.styleable.TextViewCorner_border_raduis,
                    (int) (12 * getResources().getDisplayMetrics().density));

            color = a.getColor(
                    R.styleable.TextViewCorner_border_color_in, defColor);

            a.recycle();
            setRaduis(raduis);
            setBackgroundColor(color);
        }
    }



    private void init() {

        setGravity(Gravity.CENTER);
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        ViewCompat.setBackground(this, mGradientDrawable);

        setPadding(DensityUtil.dip2px(getContext(), 3),
                DensityUtil.dip2px(getContext(), 0),
                DensityUtil.dip2px(getContext(), 3),
                DensityUtil.dip2px(getContext(), 0));
//        setRaduis(DensityUtil.dip2px(getContext(), 0));
//        setBackgroundResource(R.drawable.user_info_intreset_label);
    }

    @Override
    public void setBackgroundColor(int color) {
        mGradientDrawable = (GradientDrawable)getBackground();
        mGradientDrawable.setColor(color);
    }

    public void setFillColor(int color){
        mGradientDrawable = (GradientDrawable)getBackground();
        mGradientDrawable.setColor(color);
    }

    @TargetApi(21)
    public void setBackgroundColor(ColorStateList colorStateList) {
        mGradientDrawable = (GradientDrawable)getBackground();
        mGradientDrawable.setColor(colorStateList);
    }

    public void setRaduis(float raduis){
        mGradientDrawable = (GradientDrawable)getBackground();

        mGradientDrawable.setCornerRadius(raduis * getResources().getDisplayMetrics().density);

    }

    public void setStroke(int w, int color) {
        mGradientDrawable = (GradientDrawable)getBackground();
        mGradientDrawable.setStroke(w, color);
    }

    public void setBounds(int l, int t, int r, int b) {
        mGradientDrawable.setBounds(l, t, r, b);
    }

}
