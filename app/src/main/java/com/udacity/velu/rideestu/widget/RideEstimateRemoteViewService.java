package com.udacity.velu.rideestu.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Velu on 21/08/17.
 */

public class RideEstimateRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
