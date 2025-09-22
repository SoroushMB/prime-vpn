package com.prime.dto;

import com.prime.AppConfig;

public class ProfileItem {
    public int configVersion = 3;
    public EConfigType configType;
    public String subscriptionId = "";
    public long addedTime = System.currentTimeMillis();
    public String remarks = "";
    public V2rayConfig.OutboundBean outboundBean;
    public V2rayConfig fullConfig;

    public ProfileItem() {
    }

    public ProfileItem(EConfigType configType) {
        this.configType = configType;
    }

    public static ProfileItem create(EConfigType configType) {
        ProfileItem profile = new ProfileItem(configType);
        
        switch (configType) {
            case VMESS:
            case VLESS:
                profile.outboundBean = new V2rayConfig.OutboundBean();
                profile.outboundBean.protocol = configType.name().toLowerCase();
                profile.outboundBean.settings = new V2rayConfig.OutboundBean.OutSettingsBean();
                profile.outboundBean.settings.vnext = new java.util.ArrayList<>();
                V2rayConfig.OutboundBean.OutSettingsBean.VnextBean vnext = 
                    new V2rayConfig.OutboundBean.OutSettingsBean.VnextBean();
                vnext.users = new java.util.ArrayList<>();
                vnext.users.add(new V2rayConfig.OutboundBean.OutSettingsBean.VnextBean.UsersBean());
                profile.outboundBean.settings.vnext.add(vnext);
                profile.outboundBean.streamSettings = new V2rayConfig.OutboundBean.StreamSettingsBean();
                break;
                
            case CUSTOM:
                // Custom config doesn't need outboundBean
                break;
                
            case SHADOWSOCKS:
            case SOCKS:
            case HTTP:
            case TROJAN:
            case HYSTERIA2:
                profile.outboundBean = new V2rayConfig.OutboundBean();
                profile.outboundBean.protocol = configType.name().toLowerCase();
                profile.outboundBean.settings = new V2rayConfig.OutboundBean.OutSettingsBean();
                profile.outboundBean.settings.servers = new java.util.ArrayList<>();
                profile.outboundBean.settings.servers.add(
                    new V2rayConfig.OutboundBean.OutSettingsBean.ServersBean());
                profile.outboundBean.streamSettings = new V2rayConfig.OutboundBean.StreamSettingsBean();
                break;
                
            case WIREGUARD:
                profile.outboundBean = new V2rayConfig.OutboundBean();
                profile.outboundBean.protocol = configType.name().toLowerCase();
                profile.outboundBean.settings = new V2rayConfig.OutboundBean.OutSettingsBean();
                profile.outboundBean.settings.secretKey = "";
                profile.outboundBean.settings.peers = new java.util.ArrayList<>();
                profile.outboundBean.settings.peers.add(
                    new V2rayConfig.OutboundBean.OutSettingsBean.WireGuardBean());
                break;
        }
        
        return profile;
    }

    public V2rayConfig.OutboundBean getProxyOutbound() {
        if (configType != EConfigType.CUSTOM) {
            return outboundBean;
        }
        return fullConfig != null ? fullConfig.getProxyOutbound() : null;
    }

    public java.util.List<String> getAllOutboundTags() {
        if (configType != EConfigType.CUSTOM) {
            java.util.List<String> tags = new java.util.ArrayList<>();
            tags.add(AppConfig.TAG_PROXY);
            tags.add(AppConfig.TAG_DIRECT);
            tags.add(AppConfig.TAG_BLOCKED);
            return tags;
        }
        if (fullConfig != null) {
            java.util.List<String> tags = new java.util.ArrayList<>();
            for (V2rayConfig.OutboundBean outbound : fullConfig.outbounds) {
                tags.add(outbound.tag);
            }
            return tags;
        }
        return new java.util.ArrayList<>();
    }
}
