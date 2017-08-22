package com.udacity.velu.rideestu.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Velu on 20/08/17.
 */

public class RidePrice {

    private String mDisplayName;
    private Double mDistance;
    private int mHighEstimate;
    private int mLowEstimate;
    private String mEstimate;
    private String mCurrencyCode;

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public Double getmDistance() {
        return mDistance;
    }

    public void setmDistance(Double mDistance) {
        this.mDistance = mDistance;
    }

    public int getmHighEstimate() {
        return mHighEstimate;
    }

    public void setmHighEstimate(int mHighEstimate) {
        this.mHighEstimate = mHighEstimate;
    }

    public int getmLowEstimate() {
        return mLowEstimate;
    }

    public void setmLowEstimate(int mLowEstimate) {
        this.mLowEstimate = mLowEstimate;
    }

    public String getmEstimate() {
        return mEstimate;
    }

    public void setmEstimate(String mEstimate) {
        this.mEstimate = mEstimate;
    }

    public String getmCurrencyCode() {
        return mCurrencyCode;
    }

    public void setmCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }

    public void parse(JSONObject jsonObject) throws JSONException{
        mDisplayName = jsonObject.has("localized_display_name") ? jsonObject.getString("localized_display_name")
                : "";
        mDistance = jsonObject.has("distance") ? jsonObject.getDouble("distance") : 0.0;
        mHighEstimate = jsonObject.has("high_estimate") ? jsonObject.getInt("high_estimate") : 0;
        mLowEstimate = jsonObject.has("low_estimate") ? jsonObject.getInt("low_estimate") : 0;
        mEstimate = jsonObject.has("estimate") ? jsonObject.getString("estimate") : "";
        mCurrencyCode = jsonObject.has("currency_code") ? jsonObject.getString("currency_code") : "";
    }



    @Override
    public String toString() {
        return "RidePrice{" +
                "mDisplayName='" + mDisplayName + '\'' +
                ", mDistance=" + mDistance +
                ", mEstimate='" + mEstimate + '\'' +
                ", mCurrencyCode='" + mCurrencyCode + '\'' +
                '}';
    }
}
