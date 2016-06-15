package com.example.administrator.myapplication.Util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.myapplication.MyApplication;
import com.example.administrator.myapplication.UI.MainActivity;

/**
 * Created by Administrator on 2016-5-29.
 */
public class LocationUtil {
    public static AMapLocationClient LocationClient;
private LocationUtil(){

    LocationClient=new AMapLocationClient(MyApplication.getContext());

}
    private static class SingletonHolder {
        private static final LocationUtil INSTANCE = new LocationUtil();
    }

    public static LocationUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AMapLocationClient getLocationClient() {

        return  LocationClient;
    }

}









