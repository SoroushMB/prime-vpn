package com.prime.handler;

import android.content.Context;
import android.content.res.AssetManager;
import com.prime.AppConfig;

public class SettingsManager {
    
    /**
     * Sets the night mode based on user preferences.
     */
    public static void setNightMode() {
        // Implementation for setting night mode
        // This would typically involve checking user preferences and applying theme
    }

    /**
     * Initializes routing rulesets.
     */
    public static void initRoutingRulesets(Context context) {
        // Implementation for initializing routing rulesets
        // This would typically load default routing rules from assets
    }

    /**
     * Initializes assets.
     */
    public static void initAssets(Context context, AssetManager assets) {
        // Implementation for initializing assets
        // This would typically load configuration files from assets
    }

    /**
     * Gets the delay test URL.
     */
    public static String getDelayTestUrl() {
        return getDelayTestUrl(false);
    }

    /**
     * Gets the delay test URL with alternative flag.
     */
    public static String getDelayTestUrl(boolean alternative) {
        String url = MmkvManager.decodeSettingsString(AppConfig.PREF_DELAY_TEST_URL);
        if (url == null || url.trim().isEmpty()) {
            return alternative ? AppConfig.DELAY_TEST_URL2 : AppConfig.DELAY_TEST_URL;
        }
        return url;
    }

    /**
     * Gets the VPN DNS servers.
     */
    public static String[] getVpnDnsServers() {
        String dns = MmkvManager.decodeSettingsString(AppConfig.PREF_VPN_DNS, AppConfig.DNS_VPN);
        if (dns == null || dns.trim().isEmpty()) {
            return new String[]{AppConfig.DNS_VPN};
        }
        return dns.split(",");
    }

    /**
     * Gets the VPN MTU.
     */
    public static int getVpnMtu() {
        return MmkvManager.decodeSettingsBool(AppConfig.PREF_VPN_MTU) ? 
            Integer.parseInt(MmkvManager.decodeSettingsString(AppConfig.PREF_VPN_MTU, String.valueOf(AppConfig.VPN_MTU))) : 
            AppConfig.VPN_MTU;
    }

    /**
     * Gets the current VPN interface address config.
     */
    public static VpnInterfaceAddressConfig getCurrentVpnInterfaceAddressConfig() {
        // This would return a VpnInterfaceAddressConfig object
        // For now, return a default implementation
        return new VpnInterfaceAddressConfig();
    }

    /**
     * Checks if routing rulesets bypass LAN.
     */
    public static boolean routingRulesetsBypassLan() {
        return MmkvManager.decodeSettingsBool(AppConfig.PREF_VPN_BYPASS_LAN, true);
    }

    /**
     * Gets the HTTP port.
     */
    public static int getHttpPort() {
        String port = MmkvManager.decodeSettingsString(AppConfig.PREF_SOCKS_PORT, AppConfig.PORT_SOCKS);
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return Integer.parseInt(AppConfig.PORT_SOCKS);
        }
    }

    /**
     * VPN Interface Address Configuration class.
     */
    public static class VpnInterfaceAddressConfig {
        public String ipv4Client = "172.19.0.1";
        public String ipv6Client = "fdfe:dcba:9876::1";
    }
}
