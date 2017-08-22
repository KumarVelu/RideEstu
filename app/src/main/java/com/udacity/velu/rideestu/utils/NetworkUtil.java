package com.udacity.velu.rideestu.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Velu on 21/08/17.
 */

public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
