package com.example.administrator.myapplication.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.example.administrator.myapplication.Service.UpdateService;

/**
 * Created by Administrator on 2016-6-20.
 */
public class Reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, UpdateService.class);
            context.startService(i);
    }
}
