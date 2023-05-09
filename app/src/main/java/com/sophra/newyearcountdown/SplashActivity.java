package com.sophra.newyearcountdown;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    MyApplication.AppOpenAdManager appOpenAdManager;

    private boolean isAdShown = false;

    private boolean isAdDismissed = false;

    private boolean isLoadCompleted = false;

    private static  final long COUNTER_TIME = 3;

    Application application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        application = getApplication();
        appOpenAdManager = ((MyApplication)application).getAppOpenAdManager();

        appOpenAdManager.loadAd(getApplicationContext());

        createTimer(COUNTER_TIME);


    }


    private  void createTimer(long seconds) {

        CountDownTimer timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            @Override
            public void onFinish() {


                if(!(application instanceof MyApplication)){
                    launchMainScreen();
                    return;
                }

                appOpenAdManager.showAdIfAvailable(SplashActivity.this, new MyApplication.OnShowAdCompleteListener() {


                    @Override
                    public void onShowAdComplete() {
                        launchMainScreen();
                    }
                });

            }
        };
        timer.start();
    }

    private void launchMainScreen() {
        ActivityCompat.finishAffinity(SplashActivity.this);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

}
