package com.sophra.newyearcountdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //저장된 설정 가져오기
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //언어 설정
        Configuration config = new Configuration();
        if(sharedPreferences.getString("language_list","").equals("한국어(Korean)"))
        {
            Locale ko = Locale.KOREA;
            config.locale = ko;
            config.setLocale(ko);
            Locale.setDefault(Locale.KOREA);
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }
        else {
            Locale en = Locale.US;
            config.locale = en;
            config.setLocale(en);
            Locale.setDefault(Locale.US);
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }

        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }


}
