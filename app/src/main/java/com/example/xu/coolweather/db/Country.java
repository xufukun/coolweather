package com.example.xu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xu on 2017/4/10.
 */
public class Country extends DataSupport {
    private int CountyId;
    private String Countryname;
    private String weatherId;
    private int CityId;

    public int getCountyId() {
        return CountyId;
    }

    public void setCountyId(int countyId) {
        CountyId = countyId;
    }

    public String getCountryname() {
        return Countryname;
    }

    public void setCountryname(String countryname) {
        Countryname = countryname;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }


    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }
}
