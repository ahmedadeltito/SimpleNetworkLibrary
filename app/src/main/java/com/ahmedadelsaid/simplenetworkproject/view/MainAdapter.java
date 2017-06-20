package com.ahmedadelsaid.simplenetworkproject.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmedadelsaid.simplenetworklibrary.imagerequest.OnCompleteImageListener;
import com.ahmedadelsaid.simplenetworklibrary.imagerequest.RequestImageView;
import com.ahmedadelsaid.simplenetworkproject.R;
import com.ahmedadelsaid.simplenetworkproject.model.ItemList;

import java.util.ArrayList;

import static com.ahmedadelsaid.simplenetworkproject.R.drawable.loading;

/**
 * Created by Ahmed Adel on 19/06/2017.
 */

class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int VIEW_TYPE_ITEM = 0;
    static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ItemList> items;
    private int lastPosition = -1;
    private OnCompleteImageListener onCompleteImageListener;
    private OnMainAdapterClickListener onMainAdapterClickListener;
    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    MainAdapter(Context context, ArrayList<ItemList> items, RecyclerView recyclerView,
                OnCompleteImageListener onCompleteImageListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.onCompleteImageListener = onCompleteImageListener;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore(true);
                        }
                        isLoading = true;
                    } else {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore(false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            viewHolder = new ItemViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            viewHolder = new LoadingViewHolder(layoutInflater.inflate(R.layout.loading_progress_item, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemList item = items.get(position);
            ((ItemViewHolder) holder).userNameTextView.setText(item.getUser().getName());
            ((ItemViewHolder) holder).userImageView.setImageUrl(item.getUser().getProfileImage().getLarge(), R.drawable.warning,
                    loading, onCompleteImageListener);
            setAnimation(holder.itemView, position);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    void setItems(ArrayList<ItemList> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    void setOnMainAdapterClickListener(OnMainAdapterClickListener onMainAdapterClickListener) {
        this.onMainAdapterClickListener = onMainAdapterClickListener;
    }

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    void setLoaded(boolean isLoading) {
        this.isLoading = isLoading;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        RequestImageView userImageView;
        TextView userNameTextView;

        ItemViewHolder(View itemView) {
            super(itemView);

            userImageView = (RequestImageView) itemView.findViewById(R.id.user_image_iv);
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMainAdapterClickListener != null)
                        onMainAdapterClickListener.onMainAdapterClickListener(items.get(getAdapterPosition()),
                                userImageView);
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
        }
    }
}
