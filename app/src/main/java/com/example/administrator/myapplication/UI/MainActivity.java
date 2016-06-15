package com.example.administrator.myapplication.UI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.myapplication.Model.Weather;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Service.AutoUpdateService;
import com.example.administrator.myapplication.Util.HTTPUtil.HttpMethods;
import com.example.administrator.myapplication.Util.LocationUtil;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AMapLocationListener, View.OnClickListener {


    @Bind(R.id.locationview)
    ImageView locationview;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.refresh)
    ImageButton refresh;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.weather)
    ImageView weather1;
    @Bind(R.id.srl_news_list)
    SwipeRefreshLayout srlNewsList;
    @Bind(R.id.weather2)
    ImageView weather2;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.weather3)
    ImageView weather3;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.weather4)
    ImageView weather4;
    @Bind(R.id.text4)
    TextView text4;
    @Bind(R.id.weather5)
    ImageView weather5;
    @Bind(R.id.text5)
    TextView text5;
    @Bind(R.id.weather6)
    ImageView weather6;
    @Bind(R.id.text6)
    TextView text6;
    @Bind(R.id.weather7)
    ImageView weather7;
    @Bind(R.id.text7)
    TextView text7;
    @Bind(R.id.erro)
    TextView erro;
    private String lng;
    private String lat;
    public static AMapLocationClient LocationClient;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        refresh.setOnClickListener(this);
        srlNewsList.setOnRefreshListener(this);
        srlNewsList.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LocationClient = LocationUtil.getInstance().getLocationClient();
        LocationClient.setLocationListener(this);
        LocationClient.startLocation();
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);



    }




    public void Start(AMapLocation location) {

        lng = String.valueOf(location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        Observable<Weather> weather = HttpMethods.getInstance().geWeather(lng, lat);//获得Observable
        weather.compose(this.<Weather>bindUntilEvent(ActivityEvent.PAUSE))//在Activity执行PAUSE时解除订阅，防止内存泄漏
                .subscribeOn(Schedulers.io())//指定网络访问线程为Schedulers.io
                .observeOn(AndroidSchedulers.mainThread())//指定回调线程为主线程
                .doOnSubscribe(new Action0() {//订阅时显示刷新动画
                    @Override
                    public void call() {
                        showProgress();
                    }
                })
                .doOnUnsubscribe(new Action0() {//解除订阅时隐藏刷新动画
                    @Override
                    public void call() {
                        hideProgress();
                    }
                })
                .subscribe(new Subscriber<Weather>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        erro.setText("加载失败，请重试");


                    }

                    @Override
                    public void onNext(Weather weather) { //把天气信息显示在UI上
                        erro.setText("");
                        city.setText(weather.getShowapi_res_body().getCityInfo().getC5());
                        time.setText(weather.getShowapi_res_body().getNow().getTemperature_time());
                        temperature.setText(weather.getShowapi_res_body().getNow().getTemperature() + "℃");
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getNow().getWeather_pic()).into(weather1);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF2().getDay_weather_pic()).into(weather2);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF3().getDay_weather_pic()).into(weather3);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF4().getDay_weather_pic()).into(weather4);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF5().getDay_weather_pic()).into(weather5);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF6().getDay_weather_pic()).into(weather6);
                        Picasso.with(MainActivity.this).load(weather.getShowapi_res_body().getF7().getDay_weather_pic()).into(weather7);
                        text2.setText(weather.getShowapi_res_body().getF2().getDay_air_temperature() + "℃");
                        text3.setText(weather.getShowapi_res_body().getF3().getDay_air_temperature() + "℃");
                        text4.setText(weather.getShowapi_res_body().getF4().getDay_air_temperature() + "℃");
                        text5.setText(weather.getShowapi_res_body().getF5().getDay_air_temperature() + "℃");
                        text6.setText(weather.getShowapi_res_body().getF6().getDay_air_temperature() + "℃");
                        text7.setText(weather.getShowapi_res_body().getF7().getDay_air_temperature() + "℃");


                    }
                });

    }

    @Override
    public void onRefresh() {

        Start(LocationClient.getLastKnownLocation());

    }

    public void showProgress() {
        srlNewsList.post(new Runnable() {
            @Override
            public void run() {
                //刷新动画
                srlNewsList.setRefreshing(true);
            }
        });
    }

    public void hideProgress() {
        srlNewsList.post(new Runnable() {
            @Override
            public void run() {
                srlNewsList.setRefreshing(false);
            }
        });
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Start(aMapLocation);
        LocationClient.stopLocation();


    }


    @Override
    protected void onPause() {
        super.onPause();
        LocationClient.onDestroy();

    }


    @Override
    public void onClick(View v) {
        Start(LocationClient.getLastKnownLocation());
    }
}



