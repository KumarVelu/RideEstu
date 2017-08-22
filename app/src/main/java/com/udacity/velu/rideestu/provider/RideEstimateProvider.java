package com.udacity.velu.rideestu.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Velu on 20/08/17.
 */

public class RideEstimateProvider extends ContentProvider {

    private static final String TAG = RideEstimateProvider.class.getSimpleName();
    private DbHelper mDbHelper;
    private static final int RIDE_ESTIMATE = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(RideContract.AUTHORITY, RideContract.PATH_RIDE_ESTIMATE, RIDE_ESTIMATE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)) {

            case RIDE_ESTIMATE:
                retCursor = db.query(RideContract.RideEstimateEntry.TABLE_NAME,
                        projection, null, null, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (sUriMatcher.match(uri)){
            case RIDE_ESTIMATE:
                rowsDeleted = db.delete(RideContract.RideEstimateEntry.TABLE_NAME,
                        null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int returnCount = 0;

        switch (sUriMatcher.match(uri)) {
            case RIDE_ESTIMATE: {
                db.beginTransaction();
                try {
                    for (ContentValues contentValues : values) {
                        long _id = db.insert(RideContract.RideEstimateEntry.TABLE_NAME, null, contentValues);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
            break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        Log.i(TAG, "bulkInsert: " + returnCount);
        return returnCount;
    }
}
