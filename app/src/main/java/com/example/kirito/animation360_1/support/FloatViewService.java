package com.example.kirito.animation360_1.support;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kirito on 2016.11.24.
 */

//通过service启动悬浮球
public class FloatViewService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatWindowManager fwm = FloatWindowManager.getInstance(getApplicationContext());
        fwm.showFloatCircleView();
    }
}
