package com.ahmedadelsaid.simplenetworkproject.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahmedadelsaid.simplenetworklibrary.imagerequest.OnCompleteImageListener;
import com.ahmedadelsaid.simplenetworkproject.CustomSwipeToRefresh;
import com.ahmedadelsaid.simplenetworkproject.R;
import com.ahmedadelsaid.simplenetworkproject.SpacesItemDecoration;
import com.ahmedadelsaid.simplenetworkproject.controller.ItemController;
import com.ahmedadelsaid.simplenetworkproject.controller.OnResponseListener;
import com.ahmedadelsaid.simplenetworkproject.model.ItemList;

import java.util.ArrayList;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * MainActivity is the main screen that loads the main items from pastebin API.
 */

public class MainActivity extends AppCompatActivity implements OnResponseListener, OnCompleteImageListener,
        OnMainAdapterClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private MainAdapter adapter;
    private ArrayList<ItemList> items;
    private CustomSwipeToRefresh swipeLayout;
    private ItemController itemController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        initView();

        startLoading();

        itemController = new ItemController(MainActivity.this, MainActivity.this);
        itemController.execute();

    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(getString(R.string.main_screen));
        }

        swipeLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_to_refresh);
        swipeLayout.setColorSchemeResources(R.color.loader_green,
                R.color.loader_blue, R.color.loader_red, R.color.loader_orange);
        swipeLayout.setOnRefreshListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(glm);

        adapter = new MainAdapter(MainActivity.this, items, mRecyclerView, this);
        adapter.setOnMainAdapterClickListener(this);
        adapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case MainAdapter.VIEW_TYPE_ITEM:
                        return 1;
                    case MainAdapter.VIEW_TYPE_LOADING:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
    }

    private void startDetailsActivity(ItemList itemList, View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.name), itemList.getUser().getName());
        intent.putExtra(getString(R.string.user_name), itemList.getUser().getUsername());
        intent.putExtra(getString(R.string.user_profile_image), itemList.getUser().getProfileImage().getMedium());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, getString(R.string.user_image));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            startActivity(intent, options.toBundle());
        else
            startActivity(intent);
    }

    private void startLoading() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
    }


    private void stopLoading() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSuccess(final ArrayList<ItemList> itemLists) {
        items = itemLists;
        adapter.setItems(items);
        stopLoading();
    }

    @Override
    public void onError(String error, String message, int code) {
        Toast.makeText(MainActivity.this, error + " / " + message + " / " + code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(Bitmap bitmap) {
        Log.i("image loaded", bitmap.toString());
    }

    @Override
    public void onMainAdapterClickListener(ItemList itemList, View view) {
        startDetailsActivity(itemList, view);
    }

    @Override
    public void onRefresh() {
        adapter.setLoaded(false);
        adapter.clear();
        if (itemController != null)
            itemController.execute();
    }

    @Override
    public void onLoadMore(boolean isLoading) {
        if (isLoading) {
            items.add(null);
            adapter.notifyItemInserted(items.size() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    items.remove(items.size() - 1);
                    adapter.notifyItemRemoved(items.size());
                }
            }, 5000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemController != null)
            itemController.cancel(true);
    }
}
