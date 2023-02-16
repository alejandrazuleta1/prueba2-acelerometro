package com.example.platformchannel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import io.flutter.plugin.common.EventChannel;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private final EventChannel.EventSink events;

    public MyBroadcastReceiver(EventChannel.EventSink events) {
        this.events = events;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(Sensor.STRING_TYPE_ACCELEROMETER, -1);
        try {
            SensorManager sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sManager.registerListener(
                    new ShakeEventListener(context, events),
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        } catch (Exception e) {
            events.error("UNAVAILABLE", "Accelerometer unavailable", null);
        }
    }
}
