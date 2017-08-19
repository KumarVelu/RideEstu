package com.udacity.velu.rideestu.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.udacity.velu.rideestu.R;

/**
 * Created by Velu on 19/08/17.
 */

public class SwitchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSignedIn = sp.getBoolean(getString(R.string.pref_is_signed_in), false);

        if(isSignedIn){
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}
