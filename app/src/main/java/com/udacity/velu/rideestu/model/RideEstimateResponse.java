package com.udacity.velu.rideestu.model;

import java.util.List;

/**
 * Created by Velu on 20/08/17.
 */

public class RideEstimateResponse {

    private List<RidePrice> mRidePriceList;

    public List<RidePrice> getmRidePriceList() {
        return mRidePriceList;
    }

    public void setmRidePriceList(List<RidePrice> mRidePriceList) {
        this.mRidePriceList = mRidePriceList;
    }

    @Override
    public String toString() {
        return "RideEstimateResponse{" +
                "mRidePriceList=" + mRidePriceList +
                '}';
    }
}
