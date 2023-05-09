package com.sophra.newyearcountdown;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

import petrov.kristiyan.colorpicker.ColorPicker;

public class SettingPreferenceFragment extends PreferenceFragment {

    SharedPreferences prefs;

    ListPreference soundPreference;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        soundPreference = (ListPreference)findPreference("language_list");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(!prefs.getString("language_list", "").equals("")){
            soundPreference.setSummary(prefs.getString("language_list", ""));
        }
        else
        { //설정 언어가 비어있을 경우 - 최초실행

            if(Locale.getDefault().getLanguage() == "ko") //기기 언어가 한국어일시
            {
                soundPreference.setValueIndex(0); //한국어로
            }
            else
            {
                soundPreference.setValueIndex(1); //영어로
            }
            soundPreference.setSummary(prefs.getString("language_list", ""));
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        //배경색 설정 눌렀을때
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#2062AF");
        colors.add("#58aeb7");
        colors.add("#f5b528");
        colors.add("#dd3e48");
        colors.add("#bf89ae");
        colors.add("#5c88be");
        colors.add("#59bc10");
        colors.add("#e87034");
        colors.add("#f84c44");
        colors.add("#8c47fb");
        colors.add("#51c1ee");
        colors.add("#8cc453");
        colors.add("#c2987d");
        colors.add("#ce7777");
        colors.add("#484848");
        Preference messagePref = findPreference("message");
        messagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPicker colorPicker = new ColorPicker(getActivity());
                colorPicker.setColors(colors);
                colorPicker.setRoundColorButton(true);
                colorPicker.setDefaultColorButton(prefs.getInt("bg", 0));
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        //Log.v("newyearwe", "" + position);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("bg",color);
                        editor.commit();
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));
                        Log.v("newyearwe", "" + hexColor);
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                });
                return false;
            }
        });

        //오픈소스 라이센스 창 여는거
        Preference open = findPreference("open");
        open.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), opensource.class);
                startActivity(intent);
                return false;
            }
        });


    }// onCreate

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("language_list")){
                soundPreference.setSummary(prefs.getString("language_list", ""));
                Log.v("newyearwe", "변경함");
            }

        }
    };
}
