package com.udacity.velu.rideestu.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.velu.rideestu.R;
import com.udacity.velu.rideestu.constants.Constants;
import com.udacity.velu.rideestu.model.RidePrice;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Velu on 21/08/17.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = WidgetDataProvider.class.getSimpleName();

    private List<RidePrice> mEstimateList;
    private Context mContext;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;

        populateEstimationList();
    }

    private void populateEstimationList() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String jsonStr = sp.getString(Constants.ESTIMATION_LIST, null);

        if(jsonStr != null){
            Type type = new TypeToken<List<RidePrice>>(){}.getType();
            mEstimateList = gson.fromJson(jsonStr, type);
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount: " + mEstimateList);
        return mEstimateList != null ? mEstimateList.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.row_item_widget);
        RidePrice ridePrice = mEstimateList.get(position);
        remoteViews.setTextViewText(R.id.display_name, ridePrice.getmDisplayName());
        remoteViews.setTextViewText(R.id.cost, ridePrice.getmEstimate());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

