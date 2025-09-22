package com.prime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.prime.handler.MmkvManager;
import com.prime.AppConfig;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Check if auto-start is enabled
            if (MmkvManager.decodeSettingsBool(AppConfig.PREF_IS_BOOTED, false)) {
                // Start the service if auto-start is enabled
                // This would typically start the V2Ray service
            }
        }
    }
}
