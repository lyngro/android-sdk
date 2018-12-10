package com.lyngro.android.sdk;
import android.os.Handler;
import android.os.SystemClock;

import com.lyngro.android.sdk.models.LyngroHeartbeat;

/**
 * Created by j.nasrallah on 4/6/2017.
 */

public class LyngroHeartbeatHandler {
    private int MAX_REQUESTS = 50;
    private int number_of_heartbeats = 0;

    private Handler handler = new Handler();
    private long startTime;
    private long timeInMilliseconds;
    private long timeSwapBuff;
    private long updatedTime;
    private final long TIMER_INTERVAL = 10000L;
    private LyngroTracker tracker;
    private LyngroHeartbeat heartbeat;

    public LyngroHeartbeatHandler(LyngroTracker tracker, LyngroHeartbeat heartbeat) {
        try {
            this.tracker = tracker;
            this.heartbeat = heartbeat;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid parametes for heartbeat.");
        }
    }

    private void InitTimer() {
        startTime = SystemClock.uptimeMillis();
        timeInMilliseconds = 0L;
        timeSwapBuff = TIMER_INTERVAL;
        updatedTime = 0L;
    }

    public void start() {
        InitTimer();
        handler.postDelayed(updateTimerThread, 0);
    }

    public void pause() {
        timeSwapBuff -= timeInMilliseconds;
        handler.removeCallbacks(updateTimerThread);

        //Toast.makeText(MainActivity.this, "Fire Event OnPause:" + (int)(timeInMilliseconds/1000.0 + 0.5), Toast.LENGTH_LONG).show();
        heartbeat.duration = (int) (timeInMilliseconds / 1000.0 + 0.5);
        heartbeat.sendHearbeat(heartbeat, tracker);

        InitTimer();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff - timeInMilliseconds;

            if (updatedTime <= 0 && number_of_heartbeats++ < MAX_REQUESTS) {
                //Toast.makeText(MainActivity.this, "Fire Event End Timer: " + (int)(timeInMilliseconds/1000.0), Toast.LENGTH_LONG).show();
                heartbeat.duration = (int) (timeInMilliseconds / 1000.0);
                heartbeat.sendHearbeat(heartbeat, tracker);
                InitTimer();
            }

            handler.postDelayed(this, 0);
        }
    };
}
