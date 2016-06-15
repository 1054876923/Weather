package com.example.administrator.myapplication.Service;



import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.example.administrator.myapplication.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.UI.MainActivity;

public class AutoUpdateService extends Service {


    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    private PendingIntent pendingIntent;
    @Override
    public void onCreate() {

        super.onCreate();
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.location);
        builder.setContentIntent(pendingIntent);
        Intent notificationintent = new Intent(this,MainActivity.class);
        pendingIntent=PendingIntent.getActivity(this,0,notificationintent,PendingIntent.FLAG_CANCEL_CURRENT);
        startForeground(1,builder.build());





    }
}
