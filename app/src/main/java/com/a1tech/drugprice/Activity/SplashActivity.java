package com.a1tech.drugprice.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.a1tech.drugprice.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isConnected()){
            if (isLocationEnabled(this)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                }, 3000);
            }else {
                new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Внимание!")
                        .setContentText("Устройство GPS оключено")
                        .setConfirmText("Повторить")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                                startActivity(getIntent());
                            }
                        }).show();
            }
        } else {
            new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Внимание!")
                    .setContentText("Подключение к интернету отсутсвует!")
                    .setConfirmText("Повторить")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                            startActivity(getIntent());
                        }
                    }).show();
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        } else
            return false;
    }
}