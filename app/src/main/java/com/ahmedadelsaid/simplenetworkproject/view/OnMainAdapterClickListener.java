package com.ahmedadelsaid.simplenetworkproject.view;

import android.view.View;

import com.ahmedadelsaid.simplenetworkproject.model.ItemList;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * OnMainAdapterClickListener that fires once any items in the main adapter is clicked
 */

interface OnMainAdapterClickListener {
    void onMainAdapterClickListener(ItemList itemList, View view);
}
