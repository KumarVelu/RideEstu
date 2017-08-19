package com.udacity.velu.rideestu.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.udacity.velu.rideestu.R;

/**
 * Created by Velu on 19/08/17.
 */

public class BaseActivity extends AppCompatActivity{

    void showDefaultError(){
        Toast.makeText(this, getString(R.string.error_default), Toast.LENGTH_SHORT).show();
    }

}
