package com.example.xu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xu on 2017/4/10.
 */
public class City extends DataSupport {
    private int CityId;
    private String CityName;
    private int CityCode;
    private int ProinceId;
    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public int getProinceId() {
        return ProinceId;
    }

    public void setProinceId(int proinceId) {
        ProinceId = proinceId;
    }
}
