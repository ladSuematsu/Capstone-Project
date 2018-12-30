package com.ladsuematsu.capstoneproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.ladsuematsu.capstoneproject.core.data.persistence.WidgetDataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.newplace.detail.PlaceDetailsActivity;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.overview.MapActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PlaceWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.place_widget);
        views.setTextViewText(R.id.appwidget_default_text, widgetText);

        refreshWidgetContent(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void refreshWidgetContent(Context context, RemoteViews remoteViews) {

        WidgetDataProvider widgetDataModule = AppComponent.getInstance().getAppWidgetDataModule();

        String placeUid = widgetDataModule.getLastPlaceUid();
        if (!placeUid.isEmpty()) {
            remoteViews.setViewVisibility(R.id.appwidget_default_text, View.GONE);
            remoteViews.setViewVisibility(R.id.appwidget_place_info_container, View.VISIBLE);

            String placeName = widgetDataModule.getlastPlaceName();
            String placeAddress = widgetDataModule.getLastPlaceAddress();
            String phoneNumber = widgetDataModule.getLastPlacePhoneNumber();

            remoteViews.setTextViewText(R.id.appwidget_place_name, placeName);
            remoteViews.setTextViewText(R.id.appwidget_place_address, placeAddress);
            remoteViews.setTextViewText(R.id.appwidget_place_address, phoneNumber);

        }

        PendingIntent pendingIntent = buildActivityStartPendingIntent(placeUid, context);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_root_view, pendingIntent);

    }

    private static PendingIntent buildActivityStartPendingIntent(String placeUid, Context context) {
        Intent activityIntent = placeUid.isEmpty()
                ? new Intent(context, MapActivity.class)
                : new Intent(context, PlaceDetailsActivity.class).putExtra(NewPlaceMvp.EXTRA_PLACE_KEY, placeUid);

        return PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

