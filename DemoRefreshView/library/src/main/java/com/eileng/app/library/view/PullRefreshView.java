package com.eileng.app.library.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eileng.app.library.R;
import com.eileng.app.library.base.AbsRecyclerViewAdapter;
import com.eileng.app.library.base.LoadMoreListener;

/**
 * Created by enlong on 2017/3/15.
 */

public class PullRefreshView extends RelativeLayout {
    private MySwipRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AbsRecyclerViewAdapter adapter;

    private NoNetworkOrDataView errView;

    private LoadMoreListener moreListener;
    private TextView tvHint;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    public PullRefreshView(Context context) {
        super(context);
        initView(context);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context mContext) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_pull_refresh_loadmore, null);
        this.addView(v, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tvHint = (TextView) v.findViewById(R.id.tv_hint);

        refreshLayout = (MySwipRefreshLayout) v.findViewById(R.id.swipe_refresh);

        recyclerView = (RecyclerView) v.findViewById(R.id.load_more_recycler_view);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
//                if (JCVideoPlayerManager.getFirst() != null) {
//                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.getFirst();
//                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
//                        JCVideoPlayer.releaseAllVideos();
//                    }
//                }
            }
        });

        errView = (NoNetworkOrDataView) v.findViewById(R.id.err_view);
        errView.setRefreshListener(mRefreshListener);
        errView.setVisibility(GONE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != mRefreshListener) {
                    mRefreshListener.onRefresh();
                }
            }
        });
        setRefreshEnabled(true);
    }

    public void addDivider(RecyclerView.ItemDecoration decoration) {
        recyclerView.addItemDecoration(decoration);

    }

    /**
     * 处理在下拉控件上方添加文案的情况
     *
     * @return
     */
    public TextView getHintTextView() {
        return tvHint;
    }

    /**
     * 设置是否显示没有网络的页面
     *
     * @param hasNet
     */
    public void setHasNet(boolean hasNet) {
        if (!hasNet) {
            errView.showNoNetworkView();
        } else errView.setVisibility(GONE);
    }

    /**
     * 设置是否有没有内容的页面
     *
     * @param hasContent
     */
    public void setHasContent(boolean hasContent) {
        if (!hasContent) errView.showNoDataView();
        else errView.setVisibility(GONE);
    }


    /**
     * 设置没有内容的文案
     *
     * @param text
     */
    public void setNoContentHint(String text) {
        errView.setNoDataHint(text);
    }

    public void setNoContentHint(int id) {
        errView.setNoDataHint(id);
    }

    /**
     * 设置下拉刷新的回调
     *
     * @param onRefreshListener
     */
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.mRefreshListener = onRefreshListener;
    }


    /**
     * 设置上拉加载更多回调
     *
     * @param loadMoreListener
     */
    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.moreListener = loadMoreListener;
        if (null != adapter) {
            adapter.setLoadMoreListener(loadMoreListener);
        }

    }

    public void setAdapter(@NonNull AbsRecyclerViewAdapter adadpter) {
        this.adapter = adadpter;
        recyclerView.setAdapter(adadpter);
        if (null != moreListener) adadpter.setLoadMoreListener(moreListener);
    }


    public void stopLoadMore() {
        setLoadingMore(false);
    }

    public void stopRefresh() {
        setRefresh(false);
    }

    public void setRefresh(boolean isRefres) {
        refreshLayout.setRefreshing(isRefres);
    }


    public void setLoadingMore(boolean isLoadingMore) {
        adapter.setLoadingMore(isLoadingMore);
    }

    public void notifyLoadingMoreFinish(boolean hasMore) {

        setRefresh(false);
        adapter.notifyMoreFinish(hasMore);
    }

    public void setLoadMoreEnable(boolean isEnable) {
        adapter.setLoadMoreEnable(isEnable);
    }

    public void setOnScrollLister(RecyclerView.OnScrollListener scrollLister) {
        recyclerView.addOnScrollListener(scrollLister);
    }

    public void scrollToPositionWithOffset(int position, int office) {
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, office);
    }

    public int getFirstVisibleItem() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    public int getLastVisibleItem() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 设置是否能下拉刷新
     *
     * @param refreshEnabled
     */
    public void setRefreshEnabled(boolean refreshEnabled) {
        refreshLayout.setEnabled(refreshEnabled);
    }

    public void goBack() {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
    }
}
