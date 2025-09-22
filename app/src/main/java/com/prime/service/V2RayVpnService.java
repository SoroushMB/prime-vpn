package com.prime.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.ProxyInfo;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.prime.AppConfig;
import com.prime.handler.MmkvManager;
import com.prime.handler.NotificationManager;
import com.prime.handler.SettingsManager;
import com.prime.handler.V2RayServiceManager;
import com.prime.util.Utils;

public class V2RayVpnService extends VpnService implements ServiceControl {
    private ParcelFileDescriptor mInterface;
    private boolean isRunning = false;

    @RequiresApi(Build.VERSION_CODES.P)
    private final NetworkRequest defaultNetworkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .build();

    private ConnectivityManager connectivity;

    @RequiresApi(Build.VERSION_CODES.P)
    private final ConnectivityManager.NetworkCallback defaultNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            setUnderlyingNetworks(new Network[]{network});
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            setUnderlyingNetworks(new Network[]{network});
        }

        @Override
        public void onLost(Network network) {
            setUnderlyingNetworks(null);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        V2RayServiceManager.setServiceControl(this);
        connectivity = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public void onRevoke() {
        stopV2Ray();
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
        setupService();
    }

    @Override
    public void stopService() {
        stopV2Ray(true);
    }

    @Override
    public boolean vpnProtect(int socket) {
        return protect(socket);
    }

    /**
     * Sets up the VPN service.
     */
    private void setupService() {
        Intent prepare = prepare(this);
        if (prepare != null) {
            return;
        }

        if (!configureVpnService()) {
            return;
        }

        runTun2socks();
    }

    /**
     * Configures the VPN service.
     */
    private boolean configureVpnService() {
        Builder builder = new Builder();

        // Configure network settings
        configureNetworkSettings(builder);

        // Configure app-specific settings
        configurePerAppProxy(builder);

        // Close the old interface
        try {
            if (mInterface != null) {
                mInterface.close();
            }
        } catch (Exception ignored) {
            // ignored
        }

        // Configure platform-specific features
        configurePlatformFeatures(builder);

        // Create a new interface
        try {
            mInterface = builder.establish();
            isRunning = true;
            return true;
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to establish VPN interface", e);
            stopV2Ray();
        }
        return false;
    }

    /**
     * Configures the basic network settings for the VPN.
     */
    private void configureNetworkSettings(Builder builder) {
        SettingsManager.VpnInterfaceAddressConfig vpnConfig = SettingsManager.getCurrentVpnInterfaceAddressConfig();
        boolean bypassLan = SettingsManager.routingRulesetsBypassLan();

        // Configure IPv4 settings
        builder.setMtu(SettingsManager.getVpnMtu());
        builder.addAddress(vpnConfig.ipv4Client, 30);

        // Configure routing rules
        if (bypassLan) {
            for (String ip : AppConfig.ROUTED_IP_LIST) {
                String[] addr = ip.split("/");
                builder.addRoute(addr[0], Integer.parseInt(addr[1]));
            }
        } else {
            builder.addRoute("0.0.0.0", 0);
        }

        // Configure IPv6 if enabled
        if (MmkvManager.decodeSettingsBool(AppConfig.PREF_PREFER_IPV6)) {
            builder.addAddress(vpnConfig.ipv6Client, 126);
            if (bypassLan) {
                builder.addRoute("2000::", 3);
                builder.addRoute("fc00::", 18);
            } else {
                builder.addRoute("::", 0);
            }
        }

        // Configure DNS servers
        String[] dnsServers = SettingsManager.getVpnDnsServers();
        for (String dns : dnsServers) {
            if (Utils.isPureIpAddress(dns)) {
                builder.addDnsServer(dns);
            }
        }

        builder.setSession(V2RayServiceManager.getRunningServerName());
    }

    /**
     * Configures platform-specific VPN features.
     */
    private void configurePlatformFeatures(Builder builder) {
        // Android P (API 28) and above: Configure network callbacks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                connectivity.requestNetwork(defaultNetworkRequest, defaultNetworkCallback);
            } catch (Exception e) {
                Log.e(AppConfig.TAG, "Failed to request default network", e);
            }
        }

        // Android Q (API 29) and above: Configure metering and HTTP proxy
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false);
            if (MmkvManager.decodeSettingsBool(AppConfig.PREF_APPEND_HTTP_PROXY)) {
                builder.setHttpProxy(ProxyInfo.buildDirectProxy(AppConfig.LOOPBACK, SettingsManager.getHttpPort()));
            }
        }
    }

    /**
     * Configures per-app proxy rules for the VPN builder.
     */
    private void configurePerAppProxy(Builder builder) {
        String selfPackageName = getPackageName();

        // If per-app proxy is not enabled, disallow the VPN service's own package
        if (!MmkvManager.decodeSettingsBool(AppConfig.PREF_PER_APP_PROXY)) {
            builder.addDisallowedApplication(selfPackageName);
            return;
        }

        // If no apps are selected, disallow the VPN service's own package
        java.util.Set<String> apps = MmkvManager.decodeSettingsStringSet(AppConfig.PREF_PER_APP_PROXY_SET);
        if (apps == null || apps.isEmpty()) {
            builder.addDisallowedApplication(selfPackageName);
            return;
        }

        boolean bypassApps = MmkvManager.decodeSettingsBool(AppConfig.PREF_BYPASS_APPS);
        // Handle the VPN service's own package according to the mode
        if (bypassApps) {
            apps.add(selfPackageName);
        } else {
            apps.remove(selfPackageName);
        }

        for (String app : apps) {
            try {
                if (bypassApps) {
                    // In bypass mode, disallow the selected apps
                    builder.addDisallowedApplication(app);
                } else {
                    // In proxy mode, only allow the selected apps
                    builder.addAllowedApplication(app);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(AppConfig.TAG, "Failed to configure app in VPN: " + e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Runs the tun2socks process.
     */
    private void runTun2socks() {
        // Implementation for running tun2socks
        // This would typically start the tun2socks process with appropriate parameters
    }

    /**
     * Stops the V2Ray service.
     */
    private void stopV2Ray(boolean isForced) {
        isRunning = false;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                connectivity.unregisterNetworkCallback(defaultNetworkCallback);
            } catch (Exception ignored) {
                // ignored
            }
        }

        V2RayServiceManager.stopCoreLoop();

        if (isForced) {
            stopSelf();

            try {
                if (mInterface != null) {
                    mInterface.close();
                }
            } catch (Exception e) {
                Log.e(AppConfig.TAG, "Failed to close VPN interface", e);
            }
        }
    }
}
