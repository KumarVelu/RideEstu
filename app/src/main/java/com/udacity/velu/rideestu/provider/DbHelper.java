package com.udacity.velu.rideestu.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.velu.rideestu.provider.RideContract.RideEstimateEntry;

/**
 * Created by Velu on 20/08/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "rideEstimate.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RIDE_ESTIMATE_TABLE = "CREATE TABLE " + RideEstimateEntry.TABLE_NAME + "(" +
                RideEstimateEntry._ID + " INTEGER PRIMARY KEY, " +
                RideEstimateEntry.COLUMN_DISPLAY_NAME + " TEXT NOT NULL, " +
                RideEstimateEntry.COLUMN_ESTIMATE + " TEXT NOT NULL, " +
                RideEstimateEntry.COLUMN_CURRENCY_CODE + " TEXT NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_RIDE_ESTIMATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
