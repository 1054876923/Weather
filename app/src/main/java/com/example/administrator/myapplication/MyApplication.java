package com.example.administrator.myapplication;

import android.app.Application;
import android.content.Context;
import com.amap.api.location.AMapLocationListener;


/**
 * Created by Administrator on 2016-5-29.
 */
public class MyApplication extends Application    {

    private static Context context;
    private AMapLocationListener aMapLocationListener;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }




}









