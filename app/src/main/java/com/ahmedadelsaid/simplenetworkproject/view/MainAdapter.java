package com.ahmedadelsaid.simplenetworkproject.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmedadelsaid.simplenetworklibrary.imagerequest.ImageRequest;
import com.ahmedadelsaid.simplenetworklibrary.imagerequest.OnCompleteImageListener;
import com.ahmedadelsaid.simplenetworkproject.R;
import com.ahmedadelsaid.simplenetworkproject.model.ItemList;

import java.util.ArrayList;

/**
 * Created by Ahmed Adel on 19/06/2017.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private final LayoutInflater layoutInflater;
    private ArrayList<ItemList> items;
    private ImageRequest imageRequest;
    private int lastPosition = -1;
    private final OnCompleteImageListener onCompleteImageListener;

    public MainAdapter(Context context, ArrayList<ItemList> items, ImageRequest imageRequest,
                       OnCompleteImageListener onCompleteImageListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.imageRequest = imageRequest;
        this.onCompleteImageListener = onCompleteImageListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemList item = items.get(position);
        holder.userNameTextView.setText(item.getUser().getName());
        imageRequest.loadImage(item.getUser().getProfileImage().getMedium(),
                holder.userImageView, R.drawable.warning, R.drawable.loading, onCompleteImageListener);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView userNameTextView;

        ViewHolder(View itemView) {
            super(itemView);

            userImageView = (ImageView) itemView.findViewById(R.id.user_image_iv);
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_tv);

        }
    }
}
