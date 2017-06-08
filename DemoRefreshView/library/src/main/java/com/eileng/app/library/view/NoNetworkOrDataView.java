package com.eileng.app.library.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eileng.app.library.R;
import com.eileng.app.library.util.NetUtil;


/**
 * Created by chenqiang on 16/8/15.
 */

public class NoNetworkOrDataView extends RelativeLayout {

    private Context mContext;
    private View headLayout;
    private ImageView iv_no_net;
    private ImageView iv_no_record;
    private TextView tv_no_net;
    private TextView tv_no_record;
    private View no_data_View;
    private View no_net_view;
    private SwipeRefreshLayout.OnRefreshListener mRefreshLisenter;


    public NoNetworkOrDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        headLayout = LayoutInflater.from(context).inflate(R.layout.view_no_net_data, null);
        iv_no_record = (ImageView) headLayout.findViewById(R.id.iv_no_record);
        tv_no_record = (TextView) headLayout.findViewById(R.id.tv_no_record);
        no_net_view = headLayout.findViewById(R.id.no_net);
        no_data_View = headLayout.findViewById(R.id.no_data);

        this.addView(headLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        no_net_view.findViewById(R.id.btn_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRefreshLisenter && NetUtil.isNetEnable(getContext())) {
                    no_net_view.setVisibility(GONE);
                    mRefreshLisenter.onRefresh();
                }
            }
        });
    }


    public void setNoDataHint(String str) {
        tv_no_record.setText(str);

    }

    public void setNoDataHint(int resId) {
        tv_no_record.setText(resId);
    }

    public void showNoNetworkView() {
        setVisibility(VISIBLE);
        no_net_view.setVisibility(VISIBLE);
        no_data_View.setVisibility(GONE);
    }

    public void showNoDataView() {
        setVisibility(VISIBLE);
        no_net_view.setVisibility(GONE);
        no_data_View.setVisibility(VISIBLE);
    }

    public void setHasContent(boolean hasContent){
        if(hasContent) setVisibility(GONE);
        else showNoDataView();
    }
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener refreshListener) {
        this.mRefreshLisenter = refreshListener;
    }
}
