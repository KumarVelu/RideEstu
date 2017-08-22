package com.udacity.velu.rideestu.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Velu on 20/08/17.
 */

public class RideContract {

    static final String AUTHORITY = "com.udacity.velu.rideestu";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_RIDE_ESTIMATE = "rideEstimate";

    public static final class RideEstimateEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RIDE_ESTIMATE).build();

        static final String TABLE_NAME = "ride_estimate";

        public static final String COLUMN_DISPLAY_NAME = "display_name";
        public static final String COLUMN_ESTIMATE = "estimate";
        public static final String COLUMN_CURRENCY_CODE = "currency_code";
    }
}
