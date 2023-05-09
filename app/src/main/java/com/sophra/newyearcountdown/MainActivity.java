package com.sophra.newyearcountdown;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    private View decorView;
    private int	uiOption;

    TextView days;
    TextView hours;
    TextView minutes;
    TextView seconds;

    TextView nextyear;

    Button btn_setting;

    LocalDateTime newyear;
    private Handler mHandler = new Handler();

    Timer timer = new Timer();

    LinearLayout timelayout;
    LinearLayout textlayout;

    int year;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void updateTimeRemaining(long currentTime) {  //남은 시간 계산
        long timeDiff = currentTime;  //남은시간
        if (timeDiff > 0) {

            int day = (int) (timeDiff / (60 * 60 * 24));
            int hour = (int) ((timeDiff - day * 60 * 60 * 24) / (60 * 60));
            int min = (int) ((timeDiff - day * 60 * 60 * 24 - hour * 3600) / 60);
            int sec = (int) (timeDiff % 60);

            //Log.v("newyearwe", "" + day + "일" + hour + "시간" + min + "분" + sec + "초");

            days.setText(String.valueOf(day));
            hours.setText(String.valueOf(hour));
            minutes.setText(String.valueOf(min));
            seconds.setText(String.valueOf(sec));

        } else {
            if(Locale.getDefault().equals(Locale.KOREA))
            {
                nextyear.setText(year + "년 " +"새해가 밝았습니다!");
            }
            else {
                nextyear.setText("WELCOME TO " + year);
            }
            textlayout.setVisibility(View.VISIBLE); // 새해 복 많이 받으세요!
            timelayout.setVisibility(View.GONE); //타이머 작동이 끝났을 때 - 시계
        }
    }

    private long backpressedTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        //저장된 설정 가져오기
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //전체화면 설정
        Log.v("newyearwe", "전체화면 여부 : " + sharedPreferences.getBoolean("fullscreen",false));
        if(sharedPreferences.getBoolean("fullscreen",false) == true)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            decorView = getWindow().getDecorView();
            uiOption = getWindow().getDecorView().getSystemUiVisibility();
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            decorView.setSystemUiVisibility( uiOption );
        }
        else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

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

        setContentView(R.layout.activity_main);

        days = findViewById(R.id.days);
        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);

        timelayout = findViewById(R.id.timelayout);
        textlayout = findViewById(R.id.textlayout);

        nextyear = findViewById(R.id.nextyear);

        textlayout.setVisibility(View.GONE);
        timelayout.setVisibility(View.VISIBLE);

        //배경색 설정
        //Log.v("newyearwe", sharedPreferences.getInt("bg", 0) + "");
        LinearLayout mainlayout = findViewById(R.id.mainlayout);
        mainlayout.setBackgroundColor(sharedPreferences.getInt("bg", 0));

        //글자색 설정 - > 안해도 될듯

        LocalDateTime current_date = LocalDateTime.now();  //현재시간
        year = current_date.getYear() + 1;
        if(config.locale.getLanguage() == "ko")
        {
            nextyear.setText(year + "년 " + "까지");
        }
        else {
            nextyear.setText("COUNTDOWN TO " + year);
        }

        newyear = LocalDateTime.of(year,1,1,0,0);
        //newyear = LocalDateTime.of(2022,10,18,15,33); //테스트용 날짜

        Duration duration = Duration.between(current_date, newyear); //남은 기간 길이

        timer.schedule(new TimerTask() { //타이머 코드
            Runnable updateRemainingTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (mHandler) {
                        LocalDateTime current_date = LocalDateTime.now();
                        Duration duration = Duration.between(current_date, newyear);
                        long time = Long.parseLong(String.valueOf(duration.getSeconds()));
                        updateTimeRemaining(time);
                        //if(String.valueOf(current_date.getMonth()) != "1" || ) //1월 1일이 아닌 경우에만 작동하게 만들기
                    }
                }
            };



            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 100, 100);

        btn_setting = findViewById(R.id.btn_setting);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);

            }
        });
    }
}