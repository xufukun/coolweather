package com.example.xu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xu on 2017/4/10.
 */
public class Province extends DataSupport {

    private int id;
    private String ProviceName;
    private int ProvinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProviceName() {
        return ProviceName;
    }

    public void setProviceName(String proviceName) {
        ProviceName = proviceName;
    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        ProvinceCode = provinceCode;
    }
}
