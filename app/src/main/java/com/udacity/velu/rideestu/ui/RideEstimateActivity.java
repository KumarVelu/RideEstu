package com.udacity.velu.rideestu.ui;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.velu.rideestu.BuildConfig;
import com.udacity.velu.rideestu.R;
import com.udacity.velu.rideestu.adapter.RideEstimateAdapter;
import com.udacity.velu.rideestu.constants.Constants;
import com.udacity.velu.rideestu.constants.UrlConstants;
import com.udacity.velu.rideestu.model.RideEstimateResponse;
import com.udacity.velu.rideestu.model.RidePrice;
import com.udacity.velu.rideestu.model.RideRequest;
import com.udacity.velu.rideestu.provider.RideContract.RideEstimateEntry;
import com.udacity.velu.rideestu.widget.WidgetDataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideEstimateActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.error)
    TextView mErrorText;
    @BindView(R.id.ic_fab)
    FloatingActionButton mFab;

    private static final String TAG = RideEstimateActivity.class.getSimpleName();
    private static final int RIDE_ESTIMATE_LOADER = 100;
    private RideEstimateAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<RidePrice> mRidePriceList;
    private SharedPreferences mSharedPreferences;
    private RideRequest mRideRequest;

    private final String[] RIDE_ESTIMATE_COLUMNS = {
            RideEstimateEntry.COLUMN_DISPLAY_NAME,
            RideEstimateEntry.COLUMN_ESTIMATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_estimate);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RideEstimateAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);
        mProgressDialog = new ProgressDialog(this);
        mFab.setOnClickListener(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mRideRequest = (RideRequest) getIntent().getSerializableExtra("rideRequest");
        new RideEstimateAsyncTask().execute(mRideRequest);

        getLoaderManager().initLoader(RIDE_ESTIMATE_LOADER, null, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ic_fab:
                Toast.makeText(this, getString(R.string.bookmarked), Toast.LENGTH_SHORT).show();
                bookmark();
                break;
        }
    }

    private class RideEstimateAsyncTask extends AsyncTask<RideRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(RideRequest... rideRequests) {
            Log.i(TAG, "doInBackground: " + rideRequests[0]);

            if (rideRequests.length == 0)
                return null;

            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            String rideEstimateRespJson = null;

            RideRequest rideRequest = rideRequests[0];
            Log.i(TAG, "doInBackground: " + rideRequest);

            try {
                Uri uri = Uri.parse(UrlConstants.UBER_RIDE_ESTIMATE_URL).buildUpon()
                        .appendQueryParameter(UrlConstants.QUERY_PARAM_START_LAT, rideRequest.getmSourceLat())
                        .appendQueryParameter(UrlConstants.QUERY_PARAM_START_LONG, rideRequest.getmSourceLong())
                        .appendQueryParameter(UrlConstants.QUERY_PARAM_END_LAT, rideRequest.getmDestLat())
                        .appendQueryParameter(UrlConstants.QUERY_PARAM_END_LONG, rideRequest.getmDestLong())
                        .build();

                Log.i(TAG, "doInBackground: url " + uri.toString());

                URL url = new URL(uri.toString());

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", "Token " + BuildConfig.UBER_SECRET_TOKEN);
                httpURLConnection.connect();

                Log.i(TAG, "doInBackground: resp code " + httpURLConnection.getResponseCode());
                Log.i(TAG, "doInBackground: resp msg " + httpURLConnection.getResponseMessage());

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null) {
                    rideEstimateRespJson = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    rideEstimateRespJson = null;
                }

                rideEstimateRespJson = stringBuffer.toString();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return rideEstimateRespJson;
                }else{
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void showError() {
            mRecyclerView.setVisibility(View.GONE);
            mErrorText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            if(result == null){
                showError();
            }else{
                RideEstimateResponse estimateResponse = null;
                try {
                    estimateResponse = getRideEstimateFromJson(result);

                    if(estimateResponse == null){
                        showError();
                    }else{
                        mRidePriceList = estimateResponse.getmRidePriceList();
                        insertRideEstimateData(estimateResponse.getmRidePriceList());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private RideEstimateResponse getRideEstimateFromJson(String estimateRespStr) throws JSONException {

        RideEstimateResponse rideEstimateResponse = null;
        List<RidePrice> ridePriceList = new ArrayList<>();

        JSONObject estimateListJson = new JSONObject(estimateRespStr);
        JSONArray rideEstimateArr = estimateListJson.getJSONArray("prices");

        if(rideEstimateArr != null && rideEstimateArr.length() != 0){
            rideEstimateResponse = new RideEstimateResponse();
            for (int i = 0; i < rideEstimateArr.length(); i++) {
                JSONObject ridePriceJson = rideEstimateArr.getJSONObject(i);

                RidePrice ridePrice = new RidePrice();
                ridePrice.parse(ridePriceJson);

                ridePriceList.add(ridePrice);
            }

            rideEstimateResponse.setmRidePriceList(ridePriceList);
        }

        Log.i(TAG, "getRideEstimateFromJson: rideEstimateResponse " + rideEstimateResponse);

        return rideEstimateResponse;
    }

    private void insertRideEstimateData(List<RidePrice> ridePriceList) {
        if (!ridePriceList.isEmpty()) {
            List<ContentValues> ridePriceCvList = new ArrayList<>();
            for (RidePrice ridePrice : ridePriceList) {

                if(ridePrice.getmLowEstimate() != 0 && ridePrice.getmHighEstimate() != 0){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RideEstimateEntry.COLUMN_DISPLAY_NAME, ridePrice.getmDisplayName());
                    contentValues.put(RideEstimateEntry.COLUMN_ESTIMATE, ridePrice.getmEstimate());
                    contentValues.put(RideEstimateEntry.COLUMN_CURRENCY_CODE, ridePrice.getmCurrencyCode());

                    ridePriceCvList.add(contentValues);
                }
            }

            Uri rideEstimateUri = RideEstimateEntry.CONTENT_URI;
            ContentValues[] ridePriceCvs = ridePriceCvList.toArray(new ContentValues[ridePriceCvList.size()]);
            getContentResolver().bulkInsert(rideEstimateUri, ridePriceCvs);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri rideEstimateUri = RideEstimateEntry.CONTENT_URI;
        return new CursorLoader(this, rideEstimateUri,
                RIDE_ESTIMATE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0){
            setUiWithRideEstimates(cursor);
        }
    }

    private void setUiWithRideEstimates(Cursor cursor) {
        mAdapter.swapData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Uri rideEstimateUri = RideEstimateEntry.CONTENT_URI;
        getContentResolver().delete(rideEstimateUri, null, null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.ESTIMATION_LIST)) {
            // update widget
            Intent intent = new Intent(this, WidgetDataProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            int [] ids = AppWidgetManager.getInstance(getApplication())
                    .getAppWidgetIds(new ComponentName(getApplication(), WidgetDataProvider.class));

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void bookmark() {
        SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(this).edit();
        spe.putString(Constants.FROM_LOC_KEY, mRideRequest.getmFromLoc());
        spe.putString(Constants.TO_LOC_KEY, mRideRequest.getToLoc());
        spe.apply();

        Gson gson = new Gson();
        String json = gson.toJson(mRidePriceList);
        spe.putString(Constants.ESTIMATION_LIST, json).apply();
    }

}
