package com.udacity.velu.rideestu.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.udacity.velu.rideestu.R;
import com.udacity.velu.rideestu.constants.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class RideEstimateWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ride_estimate_widget);
        Resources res = context.getResources();
        String from = String.format(res.getString(R.string.widget_from_loc), getFromLocation(context));
        views.setTextViewText(R.id.from_location, from);
        String to = String.format(res.getString(R.string.widget_to_loc), getToLocation(context));
        views.setTextViewText(R.id.to_location, to);
        Intent intent = new Intent(context, RideEstimateRemoteViewService.class);
        views.setRemoteAdapter(R.id.estimate_list, intent);
        views.setEmptyView(R.id.estimate_list, R.id.empty_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static String getFromLocation(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.FROM_LOC_KEY, "");
    }

    private static String getToLocation(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.TO_LOC_KEY, "");
    }
}

