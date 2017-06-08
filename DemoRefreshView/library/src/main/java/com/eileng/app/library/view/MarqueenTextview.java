package com.eileng.app.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by enlong on 2017/4/27.
 */

public class MarqueenTextview extends TextView {
    public MarqueenTextview(Context context) {
        super(context);
    }

    public MarqueenTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueenTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
