package com.example.xu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xu on 2017/4/10.
 */
public class Country extends DataSupport {
    private int CountyId;
    private String Countryname;
    private int weatherId;

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

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
