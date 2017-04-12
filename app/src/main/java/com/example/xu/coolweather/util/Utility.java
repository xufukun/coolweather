package com.example.xu.coolweather.util;

import android.text.TextUtils;

import com.example.xu.coolweather.db.City;
import com.example.xu.coolweather.db.Country;
import com.example.xu.coolweather.db.Province;
import com.example.xu.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xu on 2017/4/10.
 */
public class Utility {
    //解析服务器返回的升级数据
    public static boolean handleProvinceResponse(String response)
    {
        if(!TextUtils.isEmpty(response))
        {
           try {
               JSONArray jsonArray=new JSONArray(response);
               for(int i=0;i<jsonArray.length();i++)
               {
                   JSONObject object=jsonArray.getJSONObject(i);
                   Province province=new Province();
                   province.setProviceName(object.getString("name"));
                   province.setProvinceCode(object.getInt("id"));
                   province.save();

               }
               return true;
           }catch (Exception e)
           {
                e.printStackTrace();
           }

        }
        return  false;
    }
    //解析服务器返回的市级数据
    public  static boolean handleCityResponse(String response,int provinceId)
    {
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray array=new JSONArray(response);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject object=array.getJSONObject(i);
                    City city=new City();
                    city.setCityName(object.getString("name"));
                    city.setCityCode(object.getInt("id"));
                    city.setProinceId(provinceId);
                    city.save();

                }
                return  true;
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return false;
    }

//解析服务器返回的县级数据
    public  static boolean handleCountryResponse(String response,int cityId)
    {
       try {
           JSONArray array=new JSONArray(response);
           for(int i=0;i<array.length();i++)
           {
               JSONObject object=array.getJSONObject(i);
               Country country=new Country();
               country.setCountryname(object.getString("name"));
               country.setWeatherId(object.getString("weather_id"));
               country.setCityId(cityId);
               country.save();
           }
           return true;
       }catch (Exception e)
       {
           e.printStackTrace();
       }
        return false;
    }


    //解析天气
    public static Weather handleWeatherResponse(String response)
    {
        try {
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray("HeWeather");
            String weatherContent=array.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
