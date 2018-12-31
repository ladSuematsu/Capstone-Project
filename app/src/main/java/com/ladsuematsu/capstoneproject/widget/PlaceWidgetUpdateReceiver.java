package com.ladsuematsu.capstoneproject.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.data.persistence.WidgetDataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

public class PlaceWidgetUpdateReceiver extends BroadcastReceiver {
    public static final String DEFAULT_ACTION = "com.ladsuematsu.capstoneproject.action.LOCAL_ACTION_PLACE_WIDGET_UPDATE";

    public static final String EXTRA_PLACE_INFO = "extra_place_info";

    private final WidgetDataProvider widgetDataProvider = AppComponent.getInstance().getAppWidgetDataModule();

    @Override
    public void onReceive(Context context, Intent intent) {
        updateWidgetData(intent);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(
                new ComponentName(context, PlaceWidget.class));


        PlaceWidget.updateAppWidgets(context,  widgetManager, appWidgetIds);
    }

    private void updateWidgetData(Intent intent) {
        if (intent.hasExtra(EXTRA_PLACE_INFO)) {

            PlaceEntry placeEntry = intent.getParcelableExtra(EXTRA_PLACE_INFO);
            widgetDataProvider.setLastPlaceEntry(placeEntry.getUid(),
                                                placeEntry.getName(),
                                                placeEntry.getAddress(),
                                                placeEntry.getPhoneNumber());
        }
    }
}
