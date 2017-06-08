package com.eileng.app.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eileng.app.library.R;

/**
 * Created by enlong on 2017/4/1.
 */

public class MyTabLayout extends RelativeLayout {

    private LinearLayout tabContainer;
    private LinearLayout indicator;
    private int currentTabIndex;
    private int lastSelectTabIndex;


    public interface OnTabChangeListener {
        public void onTabSelect(int index, String title);

        public void onTabDisSelect(int index, String title);

    }

    private OnTabChangeListener mOnTabChangeListener;

    public MyTabLayout(Context context) {
        super(context);
        initView(context);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_tab_container, null);
        this.addView(v, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabContainer = (LinearLayout) v.findViewById(R.id.tab_change_container);
        indicator = (LinearLayout) v.findViewById(R.id.tab_indicator);
    }

    public void setOnTabChangeListener(OnTabChangeListener mOnTabChangeListener) {
        this.mOnTabChangeListener = mOnTabChangeListener;
    }

    public TextView getTabAt(int index) {
        return (TextView) tabContainer.getChildAt(index);
    }

    /**
     * 增加tab
     *
     * @param titles
     * @param defaultIndex
     */
    public void setTabs(final String[] titles, final int defaultIndex) {
        float density = getResources().getDisplayMetrics().density;
        int child_count = titles.length;
        int width = (int) ((getResources().getDisplayMetrics().widthPixels)
                / child_count);
        final int tab_width = width;

        int color = getResources().getColor(R.color.black_2);

        int margin = tab_width / 6;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width * 2 / 3,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = margin;
        params.rightMargin = margin;


        final View tab_change_view = new View(getContext());
        tab_change_view.setBackgroundColor(getResources().getColor(R.color.green_2));
        indicator.addView(tab_change_view, params);

        LinearLayout.LayoutParams paramsTab = new LinearLayout.LayoutParams(width,
                LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < child_count; i++) {
            final TextView t = new TextView(getContext());
            t.setTextColor(color);
            t.setText(titles[i]);
            t.setTag(titles[i]);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tag = (String) v.getTag();

                    for (int i = 0; i < titles.length; i++) {
                        if (titles[i].equals(tag)) {
                            currentTabIndex = i;
                            break;
                        }
                    }

                    if (lastSelectTabIndex != currentTabIndex) {


                        TextView l_t = (TextView) tabContainer.getChildAt(lastSelectTabIndex);
                        l_t.setTextColor(t.getTextColors());

                        t.setTextColor(getResources().getColor(R.color.green_2));
                        startTabAnimaiton(tab_change_view, lastSelectTabIndex * tab_width,
                                (currentTabIndex % titles.length) * tab_width, 300);


                        if (null != mOnTabChangeListener) {
                            mOnTabChangeListener.onTabSelect(currentTabIndex, titles[currentTabIndex]);
                            mOnTabChangeListener.onTabDisSelect(lastSelectTabIndex, titles[lastSelectTabIndex]);
                        }

                        lastSelectTabIndex = currentTabIndex;

                    }

                }
            });

            tabContainer.addView(t, paramsTab);
        }


        if (defaultIndex != currentTabIndex) {
            tabContainer.getChildAt(defaultIndex).callOnClick();
        }

    }

    private void startTabAnimaiton(View tab_change_view, int startX, int endX, int duration) {
        TranslateAnimation tabChangeAnimation = new TranslateAnimation(startX, endX, 0, 0);
        tabChangeAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        tabChangeAnimation.setDuration(duration);
        tabChangeAnimation.setFillAfter(true);


        tab_change_view.clearAnimation();
        tab_change_view.startAnimation(tabChangeAnimation);
    }


}
