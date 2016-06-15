package com.example.administrator.myapplication.Util.HTTPUtil;


import com.example.administrator.myapplication.Model.Weather;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016-5-17.
 */
public interface WeatherApi {
    @GET("/9-5?from=5&showapi_appid=18262&showapi_sign=b8de0c6baf194db8abf7faa5cec417f5&needMoreDay=1")
    Observable<Weather> Get(@Query("lng") String lng, @Query("lat") String lat);
}
