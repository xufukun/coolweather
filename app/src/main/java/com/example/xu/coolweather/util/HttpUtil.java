package com.example.xu.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xu on 2017/4/10.
 */
public class HttpUtil {

    public static void SendOkHttpRequest(String url,okhttp3.Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
