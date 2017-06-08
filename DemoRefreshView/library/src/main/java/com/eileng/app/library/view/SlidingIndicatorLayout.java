package com.eileng.app.library.view;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eileng.app.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peacepassion on 15/8/23.
 */
public class SlidingIndicatorLayout extends ViewGroup implements OnPageChangeListener {

    private List<ViewHolder> viewHolders = new ArrayList<>();
    private OnTabClickListener mTabClickListener;

    public SlidingIndicatorLayout(Context context) {
        super(context);
    }

    public SlidingIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingIndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initData(List<TabIndicatorHolder> dataHolders) {
        for (int i = 0; i < dataHolders.size(); ++i) {
            View v =
                    LayoutInflater.from(getContext()).inflate(
                            R.layout.sliding_indicator_unit, this, false);
            final int j = i;

            TabIndicatorHolder dataHolder = dataHolders.get(i);

            ViewHolder holder = new ViewHolder(v);

            holder.tvSelectIndicate.setText(dataHolder.tartgetStr);
            holder.tvIndicate.setText(dataHolder.tartgetStr);

            holder.tvSelectIndicate.setAlpha((float) 0);
            holder.ivSelectIndicate.setAlpha((float) 0);

            holder.ivSelectIndicate.setImageDrawable(dataHolder.selectDrawable);
            holder.ivIndicate.setImageDrawable(dataHolder.commonDrawable);

            viewHolders.add(holder);

            v.setTag(dataHolder);

            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectTab(j);
                    if (null != mTabClickListener) mTabClickListener.onTabClick(v, j);
                }
            });
            addView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
    }

    public void setTabClickListener(OnTabClickListener mTabClickListener) {
        this.mTabClickListener = mTabClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int wrapW = 0, wrapH = 0;
        int count = getChildCount();
        if (count == 0) {
            wrapW = 0;
            wrapH = getPaddingBottom() + getPaddingTop();
        } else {
            int cw = wSize / count;
            measureChildren(MeasureSpec.makeMeasureSpec(cw, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.AT_MOST));
            wrapH = getChildAt(0).getMeasuredHeight() + getPaddingBottom() + getPaddingTop();
        }

        if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wrapW, wrapH);
        } else if (wMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wrapW, hSize);
        } else if (hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSize, wrapH);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        if (count != 0) {
            int t = getPaddingTop(), r = 0, cw = getMeasuredWidth() / count;
            for (int i = 0; i < count; ++i) {
                View child = getChildAt(i);
                int rl = r + (cw - child.getMeasuredWidth()) / 2;
                child.layout(rl, t, rl + child.getMeasuredWidth(), t + child.getMeasuredHeight());
                r += cw;
            }
        }
    }

    public void setSelectTab(int position) {

        for (int i = 0; i < viewHolders.size(); i++) {
            if (i == position) {
                viewHolders.get(position).ivSelectIndicate.setAlpha(1.0f);
                viewHolders.get(position).tvSelectIndicate.setAlpha(1.0f);

            } else {
                viewHolders.get(position).ivSelectIndicate.setAlpha(0.0f);
                viewHolders.get(position).tvSelectIndicate.setAlpha(0.0f);
            }

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        viewHolders.get(position).ivSelectIndicate.setAlpha(1 - positionOffset);
        viewHolders.get(position).tvSelectIndicate.setAlpha(1 - positionOffset);
        if (position + 1 < viewHolders.size()) {
            viewHolders.get(position + 1).ivSelectIndicate.setAlpha(positionOffset);
            viewHolders.get(position + 1).tvSelectIndicate.setAlpha(positionOffset);
        }
        for (int i = 0; i < position; ++i) {
            viewHolders.get(i).ivSelectIndicate.setAlpha((float) 0);
            viewHolders.get(i).tvSelectIndicate.setAlpha(0);
        }
        for (int i = position + 2; i < viewHolders.size(); ++i) {
            viewHolders.get(i).ivSelectIndicate.setAlpha((float) 0);
            viewHolders.get(i).tvSelectIndicate.setAlpha(0);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnTabClickListener {
        public void onTabClick(View v, int position);
    }

    protected class ViewHolder {
        private ImageView ivIndicate;
        private TextView tvIndicate;
        private ImageView ivSelectIndicate;
        private TextView tvSelectIndicate;

        public ViewHolder(View view) {
            ivIndicate = (ImageView) view.findViewById(R.id.iv_indicate);
            ivSelectIndicate = (ImageView) view.findViewById(R.id.iv_indicate_select);

            tvIndicate = (TextView) view.findViewById(R.id.tv_indicate);
            tvSelectIndicate = (TextView) view.findViewById(R.id.tv_indicate_select);
        }
    }
}
