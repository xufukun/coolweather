package com.example.xu.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.xu.coolweather.gson.Weather;
import com.example.xu.coolweather.util.HttpUtil;
import com.example.xu.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdate extends Service {


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updatepic();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int hour=8*60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+hour;
        Intent it=new Intent(this,AutoUpdate.class);
        PendingIntent pi=PendingIntent.getService(this,0,it,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }
//更新天气信息
    private void updateWeather()
    {
        SharedPreferences spf=PreferenceManager.getDefaultSharedPreferences(this);
        String WeatherString=spf.getString("weather",null);
        if(WeatherString!=null)
        {
            final Weather weather= Utility.handleWeatherResponse(WeatherString);
            String weatherId=weather.basic.weatherId;
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=e5552a061bd64509aef32226953d60a8";
            HttpUtil.SendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(res);
                    if(weather!=null&&"ok".equals(weather.status))
                    {
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdate.this).edit();
                        editor.putString("weather",res);
                        editor.apply();
                    }
                }
            });
        }

    }

    //更新每日一图
    private void updatepic()
    {
        String request="http://guolin.tech/api/bing_pic";
        HttpUtil.SendOkHttpRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdate.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();

            }
        });
    }
}
