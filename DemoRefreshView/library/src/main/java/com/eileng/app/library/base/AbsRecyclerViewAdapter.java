package com.eileng.app.library.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.eileng.app.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecycleView通用适配器,
 * 默认线性布局
 */
public abstract class AbsRecyclerViewAdapter extends RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {

    public Activity context;

    /**
     * item 类型
     * if you has more type, type value  can't be TYPE_FOOTER == 1 ,  TYPE_LIST == 2
     */
    public final static int TYPE_FOOTER = -9999;//底部--往往是loading_more
    public final static int TYPE_LIST = 9999;//代表item展示的模式是list模式

    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore;

    /**
     * 是否允许加载更多
     */
    private boolean mIsFooterEnable = false;

    /**
     * 标记加载更多的position
     */
    private int mLoadMorePosition;

    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private LoadMoreListener mListener;

    private LinearLayoutManager mLinearLayoutManager;

    private int totalItemCount, lastVisibleItem;

    protected RecyclerView mRecyclerView;

    protected boolean isItemClickAble;

    public void setItemClickAble(boolean isItemClickAble) {
        this.isItemClickAble = isItemClickAble;
    }


    public interface OnItemClickListener {

        public void onItemClick(int position, ClickableViewHolder holder);
    }

    public interface OnItemLongClickListener {

        public boolean onItemLongClick(int position, ClickableViewHolder holder);
    }

    private OnItemClickListener itemClickListener;

    private OnItemLongClickListener itemLongClickListener;

    protected List<RecyclerView.OnScrollListener> mListeners = new ArrayList<RecyclerView.OnScrollListener>();

    public AbsRecyclerViewAdapter(final Activity mContext, RecyclerView recyclerView) {

        bindContext(mContext);
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.setAdapter(this);
        mLinearLayoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        this.mRecyclerView.setLayoutManager(mLinearLayoutManager);
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                if (mContext != null && !mContext.isFinishing()) {
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            Glide.with(mContext).pauseRequests();
                            break;
                        case RecyclerView.SCROLL_STATE_IDLE:
                            Glide.with(mContext).resumeRequests();
                            break;
                    }
                    for (RecyclerView.OnScrollListener listener : mListeners) {
                        listener.onScrollStateChanged(rv, newState);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrolled(rv, dx, dy);
                }

                totalItemCount = getItemCount();
                lastVisibleItem = getLastVisiblePosition();

                if (null != mListener && mIsFooterEnable && !mIsLoadingMore
                        && (lastVisibleItem > totalItemCount - 3) && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == getItemCount()) {
                        setLoadingMore(true);
                        mLoadMorePosition = lastVisiblePosition;
                        mListener.onLoadMore();
                    }
                }
            }
        });
    }


    /**
     * 需要计算上加载更多
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count = getDataCount();
        if (mIsFooterEnable) count++;
        return count;
    }


    @Override
    public int getItemViewType(int position) {
        int footerPosition = getItemCount() - 1;
        if (footerPosition == position && mIsFooterEnable) {
            return TYPE_FOOTER;
        } else {
            return getItemDataType(position);
        }
    }

    public abstract int getItemDataType(int position);

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        return mLinearLayoutManager.findLastVisibleItemPosition();
    }

    /**
     * 通知更多的数据已经加载
     * <p>
     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表
     *
     * @param hasMore
     */
    public void notifyMoreFinish(boolean hasMore) {
        if (mIsLoadingMore) {
            mIsLoadingMore = false;
            notifyItemRemoved(mLoadMorePosition);

        }
        mIsFooterEnable = hasMore;

    }

    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }


    /**
     * @param scrollListener
     */
    public void addOnScrollListener(RecyclerView.OnScrollListener scrollListener) {

        mListeners.add(scrollListener);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {

        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {

        this.itemLongClickListener = listener;
    }

    public void bindContext(Activity context) {

        this.context = context;

    }

    public Context getContext() {

        return this.context;
    }

    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position) {


        int type = getItemViewType(position);

        if (type != TYPE_FOOTER) {
            holder.getParentView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position, holder);
                    }
                }
            });
            holder.getParentView().setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    if (itemLongClickListener != null) {
                        return itemLongClickListener.onItemLongClick(position, holder);
                    } else {
                        return false;
                    }
                }
            });

            try {
                initItemView(type, holder, position);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == TYPE_FOOTER) {
            // get footer view
            v = LayoutInflater.from(getContext()).inflate(R.layout.footer_loading_more_layout, null);
            return new ClickableViewHolder(v);
        } else {
            //get list view
            try {
                v = getListItemView(parent, viewType);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        ClickableViewHolder holder = null;
        try {
            holder = createHolder(viewType, v);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.getParentView().setClickable(isItemClickAble);
        return holder;
    }

    public boolean isLoadingMore() {
        return isLoadingMore();
    }

    public View $(int layout) {
        return LayoutInflater.from(context).inflate(layout, null);
    }

    /**
     * @param v
     * @return sub class of ClickableViewHolder
     */
    protected abstract ClickableViewHolder createHolder(int viewType, View v);


    /**
     * return item view
     *
     * @param parent
     * @param viewType
     */
    protected abstract View getListItemView(ViewGroup parent, int viewType);


    /**
     * return content size
     *
     * @return
     */
    public abstract int getDataCount();

    /**
     * init the list item view by position
     *
     * @param viewType
     * @param holder
     * @param position
     */
    protected abstract void initItemView(int viewType, ClickableViewHolder holder, int position);


    /**
     * Created by 0H7RXL on 2016/7/11.
     */
    public static class ClickableViewHolder extends RecyclerView.ViewHolder {

        private View parentView;
        public Context mContext;

        public ClickableViewHolder(View itemView) {
            super(itemView);

            this.parentView = itemView;
            mContext = itemView.getContext();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
        }

        public View getParentView() {

            return parentView;
        }

        public <T extends View> T $(@IdRes int id) {

            return (T) parentView.findViewById(id);
        }

    }
}