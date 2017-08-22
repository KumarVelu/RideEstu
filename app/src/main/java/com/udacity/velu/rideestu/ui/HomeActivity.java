package com.udacity.velu.rideestu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.udacity.velu.rideestu.R;
import com.udacity.velu.rideestu.model.RideRequest;
import com.udacity.velu.rideestu.utils.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.from_text)
    EditText mFromText;
    @BindView(R.id.to_text)
    EditText mToText;

    private static final int RC_PLACE_AUTOCOMPLETE = 201;
    private boolean mFromClicked;
    private RideRequest mRideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        initializeUi();
    }

    private void initializeUi() {
        mRideRequest = new RideRequest();
        mFromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFromClicked = true;
                launchAutocompleteWidget();
            }
        });

        mToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFromClicked = false;
                launchAutocompleteWidget();
            }
        });
    }

    private void launchAutocompleteWidget() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            showDefaultError();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PLACE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                Log.i(TAG, "onActivityResult: " + place.getLatLng());

                if(mFromClicked){
                    mFromText.setText(place.getName());
                    mRideRequest.setmFromLoc(place.getName().toString());
                    mRideRequest.setmSourceLat(String.valueOf(latLng.latitude));
                    mRideRequest.setmSourceLong(String.valueOf(latLng.longitude));
                }else{
                    mToText.setText(place.getName());
                    mRideRequest.setToLoc(place.getName().toString());
                    mRideRequest.setmDestLat(String.valueOf(latLng.latitude));
                    mRideRequest.setmDestLong(String.valueOf(latLng.longitude));
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "onActivityResult: cancelled ");
            }
        }
    }

    public void estimate(View view) {
        if(TextUtils.isEmpty(mFromText.getText())){
            Toast.makeText(this, getString(R.string.error_enter_from), Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(mToText.getText())){
            Toast.makeText(this, getString(R.string.error_to_location), Toast.LENGTH_SHORT).show();
        }
        else {
            Log.i(TAG, "estimate: mRideRequest " + mRideRequest);

            if(NetworkUtil.isNetworkAvailable(this)){
                Intent intent = new Intent(this, RideEstimateActivity.class);
                intent.putExtra("rideRequest", mRideRequest);
                startActivity(intent);
            }else{
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
