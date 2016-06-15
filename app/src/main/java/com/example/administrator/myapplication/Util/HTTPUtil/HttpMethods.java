package com.example.administrator.myapplication.Util.HTTPUtil;


import com.example.administrator.myapplication.Model.Weather;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by Administrator on 2016-5-4.
 */
public class HttpMethods {
    public static final String BASE_URL = "http://route.showapi.com/";
    public static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private WeatherApi weapi;


    private HttpMethods(){
    OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
     retrofit = new Retrofit.Builder()
            .client(httpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build();
    weapi= retrofit.create(WeatherApi.class);
}

private static class SingletonHolder {
    private static final HttpMethods INSTANCE = new HttpMethods();
}

public static HttpMethods getInstance() {
    return SingletonHolder.INSTANCE;
}

    public Observable<Weather> geWeather(String lng, String lat) {
        return weapi.Get(lng,lat);
    }

}
