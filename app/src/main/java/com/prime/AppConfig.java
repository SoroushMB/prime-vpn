package com.prime;

public class AppConfig {
    
    /** The application's package name. */
    public static final String PRIME_PACKAGE = BuildConfig.APPLICATION_ID;
    public static final String TAG = BuildConfig.APPLICATION_ID;

    /** Directory names used in the app's file system. */
    public static final String DIR_ASSETS = "assets";
    public static final String DIR_BACKUPS = "backups";

    /** Legacy configuration keys. */
    public static final String ANG_CONFIG = "ang_config";

    /** Preferences mapped to MMKV storage. */
    public static final String PREF_SNIFFING_ENABLED = "pref_sniffing_enabled";
    public static final String PREF_ROUTE_ONLY_ENABLED = "pref_route_only_enabled";
    public static final String PREF_PER_APP_PROXY = "pref_per_app_proxy";
    public static final String PREF_PER_APP_PROXY_SET = "pref_per_app_proxy_set";
    public static final String PREF_BYPASS_APPS = "pref_bypass_apps";
    public static final String PREF_LOCAL_DNS_ENABLED = "pref_local_dns_enabled";
    public static final String PREF_FAKE_DNS_ENABLED = "pref_fake_dns_enabled";
    public static final String PREF_APPEND_HTTP_PROXY = "pref_append_http_proxy";
    public static final String PREF_LOCAL_DNS_PORT = "pref_local_dns_port";
    public static final String PREF_VPN_DNS = "pref_vpn_dns";
    public static final String PREF_VPN_BYPASS_LAN = "pref_vpn_bypass_lan";
    public static final String PREF_VPN_INTERFACE_ADDRESS_CONFIG_INDEX = "pref_vpn_interface_address_config_index";
    public static final String PREF_VPN_MTU = "pref_vpn_mtu";
    public static final String PREF_ROUTING_DOMAIN_STRATEGY = "pref_routing_domain_strategy";
    public static final String PREF_ROUTING_RULESET = "pref_routing_ruleset";
    public static final String PREF_MUX_ENABLED = "pref_mux_enabled";
    public static final String PREF_MUX_CONCURRENCY = "pref_mux_concurrency";
    public static final String PREF_MUX_XUDP_CONCURRENCY = "pref_mux_xudp_concurrency";
    public static final String PREF_MUX_XUDP_QUIC = "pref_mux_xudp_quic";
    public static final String PREF_FRAGMENT_ENABLED = "pref_fragment_enabled";
    public static final String PREF_FRAGMENT_PACKETS = "pref_fragment_packets";
    public static final String PREF_FRAGMENT_LENGTH = "pref_fragment_length";
    public static final String PREF_FRAGMENT_INTERVAL = "pref_fragment_interval";
    public static final String SUBSCRIPTION_AUTO_UPDATE = "pref_auto_update_subscription";
    public static final String SUBSCRIPTION_AUTO_UPDATE_INTERVAL = "pref_auto_update_interval";
    public static final String SUBSCRIPTION_DEFAULT_UPDATE_INTERVAL = "1440"; // Default is 24 hours
    public static final String SUBSCRIPTION_UPDATE_TASK_NAME = "subscription_updater";
    public static final String PREF_SPEED_ENABLED = "pref_speed_enabled";
    public static final String PREF_CONFIRM_REMOVE = "pref_confirm_remove";
    public static final String PREF_START_SCAN_IMMEDIATE = "pref_start_scan_immediate";
    public static final String PREF_DOUBLE_COLUMN_DISPLAY = "pref_double_column_display";
    public static final String PREF_LANGUAGE = "pref_language";
    public static final String PREF_UI_MODE_NIGHT = "pref_ui_mode_night";
    public static final String PREF_PREFER_IPV6 = "pref_prefer_ipv6";
    public static final String PREF_PROXY_SHARING = "pref_proxy_sharing_enabled";
    public static final String PREF_ALLOW_INSECURE = "pref_allow_insecure";
    public static final String PREF_SOCKS_PORT = "pref_socks_port";
    public static final String PREF_REMOTE_DNS = "pref_remote_dns";
    public static final String PREF_DOMESTIC_DNS = "pref_domestic_dns";
    public static final String PREF_DNS_HOSTS = "pref_dns_hosts";
    public static final String PREF_DELAY_TEST_URL = "pref_delay_test_url";
    public static final String PREF_LOGLEVEL = "pref_core_loglevel";
    public static final String PREF_OUTBOUND_DOMAIN_RESOLVE_METHOD = "pref_outbound_domain_resolve_method";
    public static final String PREF_INTELLIGENT_SELECTION_METHOD = "pref_intelligent_selection_method";
    public static final String PREF_MODE = "pref_mode";
    public static final String PREF_IS_BOOTED = "pref_is_booted";
    public static final String PREF_CHECK_UPDATE_PRE_RELEASE = "pref_check_update_pre_release";
    public static final String PREF_GEO_FILES_SOURCES = "pref_geo_files_sources";
    public static final String PREF_USE_HEV_TUNNEL = "pref_use_hev_tunnel";
    public static final String PREF_HEV_TUNNEL_LOGLEVEL = "pref_hev_tunnel_loglevel";
    public static final String PREF_HEV_TUNNEL_RW_TIMEOUT = "pref_hev_tunnel_rw_timeout";

    /** Cache keys. */
    public static final String CACHE_SUBSCRIPTION_ID = "cache_subscription_id";
    public static final String CACHE_KEYWORD_FILTER = "cache_keyword_filter";

    /** Protocol identifiers. */
    public static final String PROTOCOL_FREEDOM = "freedom";

    /** Broadcast actions. */
    public static final String BROADCAST_ACTION_SERVICE = "com.prime.action.service";
    public static final String BROADCAST_ACTION_ACTIVITY = "com.prime.action.activity";
    public static final String BROADCAST_ACTION_WIDGET_CLICK = "com.prime.action.widget.click";

    /** Tasker extras. */
    public static final String TASKER_EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE";
    public static final String TASKER_EXTRA_STRING_BLURB = "com.twofortyfouram.locale.intent.extra.BLURB";
    public static final String TASKER_EXTRA_BUNDLE_SWITCH = "tasker_extra_bundle_switch";
    public static final String TASKER_EXTRA_BUNDLE_GUID = "tasker_extra_bundle_guid";
    public static final String TASKER_DEFAULT_GUID = "Default";

    /** Tags for different proxy modes. */
    public static final String TAG_PROXY = "proxy";
    public static final String TAG_DIRECT = "direct";
    public static final String TAG_BLOCKED = "block";
    public static final String TAG_FRAGMENT = "fragment";
    public static final String TAG_DNS = "dns-module";
    public static final String TAG_DOMESTIC_DNS = "domestic-dns";

    /** Network-related constants. */
    public static final String UPLINK = "uplink";
    public static final String DOWNLINK = "downlink";

    /** URLs for various resources. */
    public static final String GITHUB_URL = "https://github.com";
    public static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com";
    public static final String GITHUB_DOWNLOAD_URL = GITHUB_URL + "/%s/releases/latest/download";
    public static final String ANDROID_PACKAGE_NAME_LIST_URL = GITHUB_RAW_URL + "/2dust/androidpackagenamelist/master/proxy.txt";
    public static final String APP_URL = GITHUB_URL + "/2dust/v2rayNG";
    public static final String APP_API_URL = "https://api.github.com/repos/2dust/v2rayNG/releases";
    public static final String APP_ISSUES_URL = APP_URL + "/issues";
    public static final String APP_WIKI_MODE = APP_URL + "/wiki/Mode";
    public static final String APP_PRIVACY_POLICY = GITHUB_RAW_URL + "/2dust/v2rayNG/master/CR.md";
    public static final String APP_PROMOTION_URL = "aHR0cHM6Ly85LjIzNDQ1Ni54eXovYWJjLmh0bWw=";
    public static final String TG_CHANNEL_URL = "https://t.me/github_2dust";
    public static final String DELAY_TEST_URL = "https://www.gstatic.com/generate_204";
    public static final String DELAY_TEST_URL2 = "https://www.google.com/generate_204";
    public static final String IP_API_URL = "https://speed.cloudflare.com/meta";

    /** DNS server addresses. */
    public static final String DNS_PROXY = "1.1.1.1";
    public static final String DNS_DIRECT = "223.5.5.5";
    public static final String DNS_VPN = "1.1.1.1";
    public static final String GEOSITE_PRIVATE = "geosite:private";
    public static final String GEOSITE_CN = "geosite:cn";
    public static final String GEOIP_PRIVATE = "geoip:private";
    public static final String GEOIP_CN = "geoip:cn";

    /** Ports and addresses for various services. */
    public static final String PORT_LOCAL_DNS = "10853";
    public static final String PORT_SOCKS = "10808";
    public static final String WIREGUARD_LOCAL_ADDRESS_V4 = "172.16.0.2/32";
    public static final String WIREGUARD_LOCAL_ADDRESS_V6 = "2606:4700:110:8f81:d551:a0:532e:a2b3/128";
    public static final String WIREGUARD_LOCAL_MTU = "1420";
    public static final String LOOPBACK = "127.0.0.1";

    /** Message constants for communication. */
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_STATE_RUNNING = 11;
    public static final int MSG_STATE_NOT_RUNNING = 12;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_STATE_START = 3;
    public static final int MSG_STATE_START_SUCCESS = 31;
    public static final int MSG_STATE_START_FAILURE = 32;
    public static final int MSG_STATE_STOP = 4;
    public static final int MSG_STATE_STOP_SUCCESS = 41;
    public static final int MSG_STATE_RESTART = 5;
    public static final int MSG_MEASURE_DELAY = 6;
    public static final int MSG_MEASURE_DELAY_SUCCESS = 61;
    public static final int MSG_MEASURE_CONFIG = 7;
    public static final int MSG_MEASURE_CONFIG_SUCCESS = 71;
    public static final int MSG_MEASURE_CONFIG_CANCEL = 72;

    /** Notification channel IDs and names. */
    public static final String RAY_NG_CHANNEL_ID = "RAY_NG_M_CH_ID";
    public static final String RAY_NG_CHANNEL_NAME = "Prime Background Service";
    public static final String SUBSCRIPTION_UPDATE_CHANNEL = "subscription_update_channel";
    public static final String SUBSCRIPTION_UPDATE_CHANNEL_NAME = "Subscription Update Service";

    /** Protocols Scheme **/
    public static final String VMESS = "vmess://";
    public static final String CUSTOM = "";
    public static final String SHADOWSOCKS = "ss://";
    public static final String SOCKS = "socks://";
    public static final String HTTP = "http://";
    public static final String VLESS = "vless://";
    public static final String TROJAN = "trojan://";
    public static final String WIREGUARD = "wireguard://";
    public static final String TUIC = "tuic://";
    public static final String HYSTERIA2 = "hysteria2://";
    public static final String HY2 = "hy2://";

    /** Give a good name to this, IDK*/
    public static final String VPN = "VPN";
    public static final int VPN_MTU = 1500;

    /** hev-sock5-tunnel read-write-timeout value */
    public static final String HEVTUN_RW_TIMEOUT = "300000";

    // Google API rule constants
    public static final String GOOGLEAPIS_CN_DOMAIN = "domain:googleapis.cn";
    public static final String GOOGLEAPIS_COM_DOMAIN = "googleapis.com";

    // Android Private DNS constants
    public static final String DNS_DNSPOD_DOMAIN = "dot.pub";
    public static final String DNS_ALIDNS_DOMAIN = "dns.alidns.com";
    public static final String DNS_CLOUDFLARE_ONE_DOMAIN = "one.one.one.one";
    public static final String DNS_CLOUDFLARE_DNS_COM_DOMAIN = "dns.cloudflare.com";
    public static final String DNS_CLOUDFLARE_DNS_DOMAIN = "cloudflare-dns.com";
    public static final String DNS_GOOGLE_DOMAIN = "dns.google";
    public static final String DNS_QUAD9_DOMAIN = "dns.quad9.net";
    public static final String DNS_YANDEX_DOMAIN = "common.dot.dns.yandex.net";

    public static final int DEFAULT_PORT = 443;
    public static final String DEFAULT_SECURITY = "auto";
    public static final int DEFAULT_LEVEL = 8;
    public static final String DEFAULT_NETWORK = "tcp";
    public static final String TLS = "tls";
    public static final String REALITY = "reality";
    public static final String HEADER_TYPE_HTTP = "http";

    public static final String[] DNS_ALIDNS_ADDRESSES = {"223.5.5.5", "223.6.6.6", "2400:3200::1", "2400:3200:baba::1"};
    public static final String[] DNS_CLOUDFLARE_ONE_ADDRESSES = {"1.1.1.1", "1.0.0.1", "2606:4700:4700::1111", "2606:4700:4700::1001"};
    public static final String[] DNS_CLOUDFLARE_DNS_COM_ADDRESSES = {"104.16.132.229", "104.16.133.229", "2606:4700::6810:84e5", "2606:4700::6810:85e5"};
    public static final String[] DNS_CLOUDFLARE_DNS_ADDRESSES = {"104.16.248.249", "104.16.249.249", "2606:4700::6810:f8f9", "2606:4700::6810:f9f9"};
    public static final String[] DNS_DNSPOD_ADDRESSES = {"1.12.12.12", "120.53.53.53"};
    public static final String[] DNS_GOOGLE_ADDRESSES = {"8.8.8.8", "8.8.4.4", "2001:4860:4860::8888", "2001:4860:4860::8844"};
    public static final String[] DNS_QUAD9_ADDRESSES = {"9.9.9.9", "149.112.112.112", "2620:fe::fe", "2620:fe::9"};
    public static final String[] DNS_YANDEX_ADDRESSES = {"77.88.8.8", "77.88.8.1", "2a02:6b8::feed:0ff", "2a02:6b8:0:1::feed:0ff"};

    //minimum list https://serverfault.com/a/304791
    public static final String[] ROUTED_IP_LIST = {
        "0.0.0.0/5",
        "8.0.0.0/7",
        "11.0.0.0/8",
        "12.0.0.0/6",
        "16.0.0.0/4",
        "32.0.0.0/3",
        "64.0.0.0/2",
        "128.0.0.0/3",
        "160.0.0.0/5",
        "168.0.0.0/6",
        "172.0.0.0/12",
        "172.32.0.0/11",
        "172.64.0.0/10",
        "172.128.0.0/9",
        "173.0.0.0/8",
        "174.0.0.0/7",
        "176.0.0.0/4",
        "192.0.0.0/9",
        "192.128.0.0/11",
        "192.160.0.0/13",
        "192.169.0.0/16",
        "192.170.0.0/15",
        "192.172.0.0/14",
        "192.176.0.0/12",
        "192.192.0.0/10",
        "193.0.0.0/8",
        "194.0.0.0/7",
        "196.0.0.0/6",
        "200.0.0.0/5",
        "208.0.0.0/4",
        "240.0.0.0/4"
    };

    public static final String[] PRIVATE_IP_LIST = {
        "0.0.0.0/8",
        "10.0.0.0/8",
        "127.0.0.0/8",
        "172.16.0.0/12",
        "192.168.0.0/16",
        "169.254.0.0/16",
        "224.0.0.0/4"
    };

    public static final String[] GEO_FILES_SOURCES = {
        "Loyalsoldier/v2ray-rules-dat",
        "runetfreedom/russia-v2ray-rules-dat",
        "Chocolate4U/Iran-v2ray-rules"
    };
}
