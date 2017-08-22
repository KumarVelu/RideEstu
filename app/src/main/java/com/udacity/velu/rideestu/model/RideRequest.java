package com.udacity.velu.rideestu.model;

import java.io.Serializable;

/**
 * Created by Velu on 19/08/17.
 */

public class RideRequest implements Serializable{

    private String mFromLoc;
    private String toLoc;
    private String mSourceLat;
    private String mSourceLong;
    private String mDestLat;
    private String mDestLong;

    public String getmFromLoc() {
        return mFromLoc;
    }

    public void setmFromLoc(String mFromLoc) {
        this.mFromLoc = mFromLoc;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getmSourceLat() {
        return mSourceLat;
    }

    public void setmSourceLat(String mSourceLat) {
        this.mSourceLat = mSourceLat;
    }

    public String getmSourceLong() {
        return mSourceLong;
    }

    public void setmSourceLong(String mSourceLong) {
        this.mSourceLong = mSourceLong;
    }

    public String getmDestLat() {
        return mDestLat;
    }

    public void setmDestLat(String mDestLat) {
        this.mDestLat = mDestLat;
    }

    public String getmDestLong() {
        return mDestLong;
    }

    public void setmDestLong(String mDestLong) {
        this.mDestLong = mDestLong;
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "mFromLoc='" + mFromLoc + '\'' +
                ", toLoc='" + toLoc + '\'' +
                ", mSourceLat='" + mSourceLat + '\'' +
                ", mSourceLong='" + mSourceLong + '\'' +
                ", mDestLat='" + mDestLat + '\'' +
                ", mDestLong='" + mDestLong + '\'' +
                '}';
    }
}
