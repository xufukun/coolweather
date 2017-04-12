package com.example.xu.coolweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xu.coolweather.db.City;
import com.example.xu.coolweather.db.Country;
import com.example.xu.coolweather.db.Province;
import com.example.xu.coolweather.gson.Weather;
import com.example.xu.coolweather.util.HttpUtil;
import com.example.xu.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xu on 2017/4/10.
 */
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTRY=2;

    private ProgressDialog progressDialog;
    private TextView textView;
    private ListView lv;
    private Button back_btn;
    private ArrayAdapter<String> adapter;
    private List<String> list=new ArrayList<>();

    private List<Province> provinceList; //省列表
    private List<City> cityList;  //市列表
    private List<Country> countryList;//县列表

    private Province selectedProvince;//选中的省份
    private City selectedCity;//选中城市
    private int currentlevel;//选中级别
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        textView=(TextView)view.findViewById(R.id.title_text);
        back_btn=(Button)view.findViewById(R.id.back_btn);
        lv=(ListView)view.findViewById(R.id.lv);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(currentlevel==LEVEL_PROVINCE)
                {
                    selectedProvince=provinceList.get(position);
                    queryCity();
                }else if(currentlevel==LEVEL_CITY)
                {
                    selectedCity=cityList.get(position);
                    querycountry();
                }else if(currentlevel==LEVEL_COUNTRY)
                {
                    String WeatherId=countryList.get(position).getWeatherId();
                   if(getActivity()instanceof  MainActivity)
                   {

                       Intent it=new Intent(getActivity(),WeatherActivity.class);
                       it.putExtra("weather_id",WeatherId);
                       startActivity(it);
                       getActivity().finish();
                   }
                    else if(getActivity()instanceof WeatherActivity)
                   {
                       WeatherActivity activity=(WeatherActivity)getActivity();
                       activity.drawerLayout.closeDrawers();
                       activity.swipeRefreshLayout.setRefreshing(true);
                       activity.requestWeather(WeatherId);
                   }
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentlevel==LEVEL_COUNTRY)
                {
                    queryCity();
                }else if(currentlevel==LEVEL_CITY)
                {
                    queryProvince();
                }
            }
        });
        queryProvince();


    }

    //查询全国所有的省，优先从数据库查询，若没有则到服务器上查询
    private void queryProvince()
    {
        textView.setText("中国");
        back_btn.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0)
        {
            list.clear();
            for(Province province:provinceList)
            {
                list.add(province.getProviceName());
            }
            adapter.notifyDataSetChanged();
            lv.setSelection(0);
            currentlevel=LEVEL_PROVINCE;
        }else
        {
            String url="http://guolin.tech/api/china";
            queryFormService(url,"province");
        }

    }

    //查询选中省内所有的市，优先从数据困查询，若没有着去服务器上查询
    private void queryCity()
    {
        textView.setText(selectedProvince.getProviceName());
        back_btn.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("ProinceId=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0)
        {
            list.clear();
            for(City city:cityList)
            {
                list.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            lv.setSelection(0);
            currentlevel=LEVEL_CITY;
        } else
        {
            int provincecode=selectedProvince.getProvinceCode();
            String url="http://guolin.tech/api/china/"+provincecode;
            queryFormService(url,"city");
        }
    }

    //查询选中市内所有县，优先从数据库查询，，如果没有再到服务器查询
    private void querycountry()
    {
        textView.setText(selectedCity.getCityName());
        back_btn.setVisibility(View.VISIBLE);
        countryList=DataSupport.where("CityId=?",String.valueOf(selectedCity.getCityId())).find(Country.class);
        if(countryList.size()>0)
        {
            list.clear();
            for(Country country:countryList)
            {
                list.add(country.getCountryname());
            }
            adapter.notifyDataSetChanged();
            lv.setSelection(0);
            currentlevel=LEVEL_COUNTRY;
        }else
        {
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String url ="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFormService(url,"county");
        }
    }


    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryFormService(String url,final String type)
    {
        showProgressDialog();
        HttpUtil.SendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(getContext(),"加载失败……",Toast.LENGTH_SHORT).show();
                        }
                    });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type))
                {
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type))
                {
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type))
                {
                    result=Utility.handleCountryResponse(responseText,selectedCity.getCityId());
                }
                if(result)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type))
                            {
                                queryProvince();
                            }else if("city".equals(type))
                            {
                                queryCity();
                            }
                            else if("county".equals(type))
                            {
                                querycountry();
                            }
                        }
                    });
                }
            }
        });
    }

    //显示进度对话框
    private void showProgressDialog()
    {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载……");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog()
    {
        if (progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }

}
