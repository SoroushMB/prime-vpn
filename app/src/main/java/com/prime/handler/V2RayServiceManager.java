package com.prime.handler;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.prime.AppConfig;
import com.prime.R;
import com.prime.dto.EConfigType;
import com.prime.dto.ProfileItem;
import com.prime.service.ServiceControl;
import com.prime.service.V2RayProxyOnlyService;
import com.prime.service.V2RayVpnService;
import com.prime.util.MessageUtil;
import com.prime.util.Utils;

public class V2RayServiceManager {
    
    private static ServiceControl serviceControl;
    private static ProfileItem currentConfig;

    /**
     * Starts the V2Ray service from a toggle action.
     */
    public static boolean startVServiceFromToggle(Context context) {
        if (MmkvManager.getSelectServer() == null || MmkvManager.getSelectServer().trim().isEmpty()) {
            android.widget.Toast.makeText(context, R.string.app_tile_first_use, android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }
        startContextService(context);
        return true;
    }

    /**
     * Starts the V2Ray service.
     */
    public static void startVService(Context context) {
        startVService(context, null);
    }

    /**
     * Starts the V2Ray service.
     */
    public static void startVService(Context context, String guid) {
        if (guid != null) {
            MmkvManager.setSelectServer(guid);
        }
        startContextService(context);
    }

    /**
     * Stops the V2Ray service.
     */
    public static void stopVService(Context context) {
        android.widget.Toast.makeText(context, R.string.toast_services_stop, android.widget.Toast.LENGTH_SHORT).show();
        MessageUtil.sendMsg2Service(context, AppConfig.MSG_STATE_STOP, "");
    }

    /**
     * Checks if the V2Ray service is running.
     */
    public static boolean isRunning() {
        // This would typically check with the core controller
        return false;
    }

    /**
     * Gets the name of the currently running server.
     */
    public static String getRunningServerName() {
        return currentConfig != null ? currentConfig.remarks : "";
    }

    /**
     * Starts the context service for V2Ray.
     */
    private static void startContextService(Context context) {
        if (isRunning()) {
            return;
        }
        String guid = MmkvManager.getSelectServer();
        if (guid == null) return;
        
        ProfileItem config = MmkvManager.decodeServerConfig(guid);
        if (config == null) return;
        
        if (config.configType != EConfigType.CUSTOM
            && !Utils.isValidUrl(config.outboundBean != null ? config.outboundBean.settings.servers.get(0).address : "")
            && !Utils.isPureIpAddress(config.outboundBean != null ? config.outboundBean.settings.servers.get(0).address : "")) {
            return;
        }

        if (MmkvManager.decodeSettingsBool(AppConfig.PREF_PROXY_SHARING)) {
            android.widget.Toast.makeText(context, R.string.toast_warning_pref_proxysharing_short, android.widget.Toast.LENGTH_SHORT).show();
        } else {
            android.widget.Toast.makeText(context, R.string.toast_services_start, android.widget.Toast.LENGTH_SHORT).show();
        }
        
        Intent intent;
        if (AppConfig.VPN.equals(MmkvManager.decodeSettingsString(AppConfig.PREF_MODE, AppConfig.VPN))) {
            intent = new Intent(context.getApplicationContext(), V2RayVpnService.class);
        } else {
            intent = new Intent(context.getApplicationContext(), V2RayProxyOnlyService.class);
        }
        
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * Starts the V2Ray core service.
     */
    public static boolean startCoreLoop() {
        if (isRunning()) {
            return false;
        }

        Service service = getService();
        if (service == null) return false;
        
        String guid = MmkvManager.getSelectServer();
        if (guid == null) return false;
        
        ProfileItem config = MmkvManager.decodeServerConfig(guid);
        if (config == null) return false;
        
        // V2rayConfigManager.getV2rayConfig would be implemented here
        // For now, we'll assume it returns a successful result
        
        try {
            IntentFilter filter = new IntentFilter(AppConfig.BROADCAST_ACTION_SERVICE);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            ContextCompat.registerReceiver(service, mMsgReceive, filter, Utils.receiverFlags());
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to register broadcast receiver", e);
            return false;
        }

        currentConfig = config;

        try {
            // coreController.startLoop(result.content) would be called here
            // For now, we'll simulate success
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to start Core loop", e);
            return false;
        }

        if (!isRunning()) {
            MessageUtil.sendMsg2UI(service, AppConfig.MSG_STATE_START_FAILURE, "");
            NotificationManager.cancelNotification();
            return false;
        }

        try {
            MessageUtil.sendMsg2UI(service, AppConfig.MSG_STATE_START_SUCCESS, "");
            NotificationManager.showNotification(currentConfig);
            // PluginServiceManager.runPlugin would be called here
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to startup service", e);
            return false;
        }
        return true;
    }

    /**
     * Stops the V2Ray core service.
     */
    public static boolean stopCoreLoop() {
        Service service = getService();
        if (service == null) return false;

        if (isRunning()) {
            try {
                // coreController.stopLoop() would be called here
            } catch (Exception e) {
                Log.e(AppConfig.TAG, "Failed to stop V2Ray loop", e);
            }
        }

        MessageUtil.sendMsg2UI(service, AppConfig.MSG_STATE_STOP_SUCCESS, "");
        NotificationManager.cancelNotification();

        try {
            service.unregisterReceiver(mMsgReceive);
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to unregister broadcast receiver", e);
        }
        // PluginServiceManager.stopPlugin() would be called here

        return true;
    }

    /**
     * Queries the statistics for a given tag and link.
     */
    public static long queryStats(String tag, String link) {
        // This would typically query the core controller
        return 0;
    }

    /**
     * Gets the current service instance.
     */
    private static Service getService() {
        return serviceControl != null ? serviceControl.getService() : null;
    }

    /**
     * Sets the service control reference.
     */
    public static void setServiceControl(ServiceControl control) {
        serviceControl = control;
    }

    /**
     * Broadcast receiver for handling messages sent to the service.
     */
    private static final BroadcastReceiver mMsgReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            if (serviceControl == null) return;
            
            int key = intent.getIntExtra("key", 0);
            switch (key) {
                case AppConfig.MSG_REGISTER_CLIENT:
                    if (isRunning()) {
                        MessageUtil.sendMsg2UI(serviceControl.getService(), AppConfig.MSG_STATE_RUNNING, "");
                    } else {
                        MessageUtil.sendMsg2UI(serviceControl.getService(), AppConfig.MSG_STATE_NOT_RUNNING, "");
                    }
                    break;
                case AppConfig.MSG_UNREGISTER_CLIENT:
                    // nothing to do
                    break;
                case AppConfig.MSG_STATE_START:
                    // nothing to do
                    break;
                case AppConfig.MSG_STATE_STOP:
                    Log.i(AppConfig.TAG, "Stop Service");
                    serviceControl.stopService();
                    break;
                case AppConfig.MSG_STATE_RESTART:
                    Log.i(AppConfig.TAG, "Restart Service");
                    serviceControl.stopService();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    startVService(serviceControl.getService());
                    break;
                case AppConfig.MSG_MEASURE_DELAY:
                    measureV2rayDelay();
                    break;
            }

            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(AppConfig.TAG, "SCREEN_OFF, stop querying stats");
                NotificationManager.stopSpeedNotification(currentConfig);
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(AppConfig.TAG, "SCREEN_ON, start querying stats");
                NotificationManager.startSpeedNotification(currentConfig);
            }
        }
    };

    /**
     * Measures the connection delay for the current V2Ray configuration.
     */
    private static void measureV2rayDelay() {
        if (!isRunning()) {
            return;
        }

        // This would typically run in a background thread
        // and measure the delay using the core controller
    }
}
