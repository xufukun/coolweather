package com.example.xu.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences spf= PreferenceManager.getDefaultSharedPreferences(this);
        if(spf.getString("weather",null)!=null)
        {
            Intent it=new Intent(this,WeatherActivity.class);
            startActivity(it);
            finish();
        }
    }
}
