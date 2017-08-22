package com.udacity.velu.rideestu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.udacity.velu.rideestu.R;

/**
 * Created by Velu on 19/08/17.
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
    }

    void showDefaultError(){
        Toast.makeText(this, getString(R.string.error_default), Toast.LENGTH_SHORT).show();
    }

}
