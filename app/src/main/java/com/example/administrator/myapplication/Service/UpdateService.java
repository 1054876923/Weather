package com.example.administrator.myapplication.Service;



import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.myapplication.Model.Weather;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Reciver.Reciver;
import com.example.administrator.myapplication.UI.MainActivity;
import com.example.administrator.myapplication.Util.HTTPUtil.HttpMethods;
import com.example.administrator.myapplication.Util.LocationUtil;
import com.squareup.picasso.Picasso;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UpdateService extends Service implements AMapLocationListener {

    private PendingIntent pendingIntent;
    String lng;
    String lat;
    private RemoteViews remoteViews;
    private Notification notification;
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();






    }
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
        Intent i = new Intent(this, Reciver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        remoteViews.setOnClickPendingIntent(R.id.bt,pi);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.location);
        builder.setContent(remoteViews);
        Intent notificationintent = new Intent(this,MainActivity.class);
        notificationintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,notificationintent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notification=builder.build();
        startForeground(1,notification);
        AMapLocationClient aMapLocationClient = LocationUtil.getInstance().getLocationClient();
        aMapLocationClient.setLocationListener(this);
        aMapLocationClient.startLocation();
        if(aMapLocationClient.getLastKnownLocation()!=null){
            update(aMapLocationClient.getLastKnownLocation());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void update(AMapLocation location) {
        lng = String.valueOf(location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        Observable<Weather> weather = HttpMethods.getInstance().geWeather(lng, lat);//获得Observable
        weather.subscribeOn(Schedulers.io())//指定网络访问线程为Schedulers.io
                .observeOn(AndroidSchedulers.mainThread())//指定回调线程为主线程
                .subscribe(new Subscriber<Weather>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {



                    }

                    @Override
                    public void onNext(Weather weather) { //把天气信息显示在UI上

                        Picasso.with(UpdateService.this).load(weather.getShowapi_res_body().getNow().getWeather_pic()).into(remoteViews,R.id.ig,1,notification);
                        remoteViews.setTextViewText(R.id.te,weather.getShowapi_res_body().getNow().getTemperature()+"℃");
                        remoteViews.setTextViewText(R.id.c,weather.getShowapi_res_body().getCityInfo().getC3());
                        remoteViews.setTextViewText(R.id.t,weather.getShowapi_res_body().getNow().getTemperature_time());





                    }
                });

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
    update(aMapLocation);
    }
}
