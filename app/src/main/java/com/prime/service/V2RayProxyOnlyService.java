package com.prime.service;

import android.app.Service;
import android.content.Intent;
import android.util.Log;
import com.prime.AppConfig;
import com.prime.handler.NotificationManager;
import com.prime.handler.V2RayServiceManager;

public class V2RayProxyOnlyService extends Service implements ServiceControl {
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        V2RayServiceManager.setServiceControl(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager.cancelNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (V2RayServiceManager.startCoreLoop()) {
            startService();
        }
        return START_STICKY;
    }

    @Override
    public Service getService() {
        return this;
    }

    @Override
    public void startService() {
        isRunning = true;
        // Implementation for starting proxy-only service
    }

    @Override
    public void stopService() {
        stopV2Ray(true);
    }

    @Override
    public boolean vpnProtect(int socket) {
        // Proxy-only mode doesn't need VPN protection
        return true;
    }

    /**
     * Stops the V2Ray service.
     */
    private void stopV2Ray(boolean isForced) {
        isRunning = false;
        V2RayServiceManager.stopCoreLoop();

        if (isForced) {
            stopSelf();
        }
    }
}
