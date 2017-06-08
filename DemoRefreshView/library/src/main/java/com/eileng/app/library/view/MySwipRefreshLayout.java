package com.eileng.app.library.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.eileng.app.library.R;

/**
 * Created by enlong on 2017/3/15.
 */

public class MySwipRefreshLayout extends SwipeRefreshLayout {

    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;

    public MySwipRefreshLayout(Context context) {
        super(context, (AttributeSet) null);
    }

    public MySwipRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setColorSchemeResources(new int[]{R.color.green});
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!this.mMeasured) {
            this.mMeasured = true;
            this.setRefreshing(this.mPreMeasureRefreshing);
        }

    }

    public void setRefreshing(boolean refreshing) {
        if (this.mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            this.mPreMeasureRefreshing = refreshing;
        }

    }
}
