package com.example.platformchannel;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.EventChannel;

public class ShakeEventListener implements SensorEventListener {
    private static final float MOV_THRESHOLD = 10;
    private static final long SHAKE_WINDOW_TIME_INTERVAL = 10;
    private static final int MOV_COUNTS = 3;
    private int counter = 0;
    private long firstMovTime;
    private final EventChannel.EventSink events;

    public ShakeEventListener(Context context, EventChannel.EventSink events) {
        this.events = events;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float maxAcc = calcMaxAcceleration(event);
        Log.d("SwA", "Max Acc ["+maxAcc+"]");
        if (maxAcc >= MOV_THRESHOLD) {
            if (counter == 0) {
                counter++;
                firstMovTime = System.currentTimeMillis();
                Log.d("SwA", "First mov..");
            } else {
                long now = System.currentTimeMillis();
                if ((now - firstMovTime) < SHAKE_WINDOW_TIME_INTERVAL)
                    counter++;
                else {
                    counter = 0;
                    return;
                }
                Log.d("SwA", "Mov counter ["+counter+"]");

                if (counter >= MOV_COUNTS)
                    events.success("is shaking");

            }
        }
    }

    private float calcMaxAcceleration(SensorEvent event) {
        float accX = event.values[0];
        float accY = event.values[1];
        float accZ = event.values[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

/*    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("movimiento", event.toString());
        events.success(event);
    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
