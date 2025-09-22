package com.prime.dto;

import com.prime.AppConfig;

public enum EConfigType {
    VMESS(1, AppConfig.VMESS),
    CUSTOM(2, AppConfig.CUSTOM),
    SHADOWSOCKS(3, AppConfig.SHADOWSOCKS),
    SOCKS(4, AppConfig.SOCKS),
    VLESS(5, AppConfig.VLESS),
    TROJAN(6, AppConfig.TROJAN),
    WIREGUARD(7, AppConfig.WIREGUARD),
    HYSTERIA2(9, AppConfig.HYSTERIA2),
    HTTP(10, AppConfig.HTTP);

    private final int value;
    private final String protocolScheme;

    EConfigType(int value, String protocolScheme) {
        this.value = value;
        this.protocolScheme = protocolScheme;
    }

    public int getValue() {
        return value;
    }

    public String getProtocolScheme() {
        return protocolScheme;
    }

    public static EConfigType fromInt(int value) {
        for (EConfigType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
