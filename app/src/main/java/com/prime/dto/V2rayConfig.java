package com.prime.dto;

import java.util.List;

public class V2rayConfig {
    public String version = "2.0";
    public String remark = "";
    public List<InboundBean> inbounds;
    public List<OutboundBean> outbounds;
    public RoutingBean routing;
    public DnsBean dns;
    public PolicyBean policy;
    public ApiBean api;
    public StatsBean stats;
    public LogBean log;

    public static class InboundBean {
        public String tag;
        public String port;
        public String protocol;
        public InSettingsBean settings;
        public StreamSettingsBean streamSettings;
        public SniffingBean sniffing;
    }

    public static class InSettingsBean {
        public String auth;
        public List<ClientBean> clients;
        public String address;
        public int port;
        public String network;
        public String user;
        public String pass;
    }

    public static class ClientBean {
        public String id;
        public int level;
        public String email;
        public String alterId;
    }

    public static class OutboundBean {
        public String tag;
        public String protocol;
        public OutSettingsBean settings;
        public StreamSettingsBean streamSettings;
        public MuxBean mux;
    }

    public static class OutSettingsBean {
        public List<VnextBean> vnext;
        public List<ServersBean> servers;
        public String secretKey;
        public List<WireGuardBean> peers;
    }

    public static class VnextBean {
        public String address;
        public int port;
        public List<UsersBean> users;
    }

    public static class UsersBean {
        public String id;
        public int level;
        public String email;
        public String alterId;
        public String security;
        public String encryption;
        public String flow;
    }

    public static class ServersBean {
        public String address;
        public int port;
        public String method;
        public String password;
        public String user;
        public String pass;
        public boolean ota;
        public int level;
        public String email;
    }

    public static class WireGuardBean {
        public String publicKey;
        public String preSharedKey;
        public String endpoint;
        public int keepAlive;
        public List<String> allowedIPs;
    }

    public static class StreamSettingsBean {
        public String network;
        public String security;
        public TlsSettingsBean tlsSettings;
        public TcpSettingsBean tcpSettings;
        public KcpSettingsBean kcpSettings;
        public WsSettingsBean wsSettings;
        public HttpSettingsBean httpSettings;
        public QuicSettingsBean quicSettings;
        public GrpcSettingsBean grpcSettings;
        public SockoptBean sockopt;
    }

    public static class TlsSettingsBean {
        public String serverName;
        public boolean allowInsecure;
        public String alpn;
        public String fingerprint;
        public String minVersion;
        public String maxVersion;
        public List<String> cipherSuites;
    }

    public static class TcpSettingsBean {
        public boolean acceptProxyProtocol;
        public HeaderBean header;
    }

    public static class KcpSettingsBean {
        public int mtu;
        public int tti;
        public int uplinkCapacity;
        public int downlinkCapacity;
        public boolean congestion;
        public int readBufferSize;
        public int writeBufferSize;
        public HeaderBean header;
    }

    public static class WsSettingsBean {
        public String path;
        public HeadersBean headers;
    }

    public static class HttpSettingsBean {
        public String host;
        public String path;
    }

    public static class QuicSettingsBean {
        public String security;
        public String key;
        public HeaderBean header;
    }

    public static class GrpcSettingsBean {
        public String serviceName;
        public boolean multiMode;
        public int idle_timeout;
        public int health_check_timeout;
        public boolean permit_without_stream;
        public int user_agent;
    }

    public static class SockoptBean {
        public int mark;
        public boolean tcpFastOpen;
        public String tcpCork;
        public String domainStrategy;
        public String dialerProxy;
        public String tcpKeepAliveInterval;
    }

    public static class HeaderBean {
        public String type;
        public RequestBean request;
        public ResponseBean response;
    }

    public static class RequestBean {
        public String version;
        public String method;
        public List<String> path;
        public HeadersBean headers;
    }

    public static class ResponseBean {
        public String version;
        public String status;
        public String reason;
        public HeadersBean headers;
    }

    public static class HeadersBean {
        public String Connection;
        public String Upgrade;
        public String Host;
        public String SecWebSocketKey;
        public String SecWebSocketAccept;
        public String SecWebSocketProtocol;
        public String SecWebSocketVersion;
    }

    public static class SniffingBean {
        public boolean enabled;
        public List<String> destOverride;
        public List<String> metadataOnly;
    }

    public static class RoutingBean {
        public String domainStrategy;
        public String domainMatcher;
        public List<RuleBean> rules;
        public List<BalancerBean> balancers;
    }

    public static class RuleBean {
        public String type;
        public List<String> domain;
        public List<String> ip;
        public String port;
        public String network;
        public List<String> source;
        public List<String> user;
        public List<String> inboundTag;
        public List<String> protocol;
        public String attrs;
        public String outboundTag;
        public String balancerTag;
    }

    public static class BalancerBean {
        public String tag;
        public List<String> selector;
        public String strategy;
    }

    public static class DnsBean {
        public List<String> servers;
        public List<String> clientIp;
        public String tag;
        public List<HostsBean> hosts;
        public List<NameserverBean> nameservers;
        public List<String> domains;
        public List<String> expectIPs;
    }

    public static class HostsBean {
        public String type;
        public String domain;
        public String ip;
        public String addr;
    }

    public static class NameserverBean {
        public String address;
        public int port;
        public List<String> domains;
        public List<String> expectIPs;
    }

    public static class PolicyBean {
        public LevelsBean levels;
        public SystemBean system;
    }

    public static class LevelsBean {
        public String _0;
    }

    public static class SystemBean {
        public boolean statsInboundUplink;
        public boolean statsInboundDownlink;
        public boolean statsOutboundUplink;
        public boolean statsOutboundDownlink;
    }

    public static class ApiBean {
        public String tag;
        public String service;
    }

    public static class StatsBean {
        public boolean enabled;
    }

    public static class LogBean {
        public String access;
        public String error;
        public String loglevel;
    }

    public OutboundBean getProxyOutbound() {
        if (outbounds != null) {
            for (OutboundBean outbound : outbounds) {
                if (AppConfig.TAG_PROXY.equals(outbound.tag)) {
                    return outbound;
                }
            }
        }
        return null;
    }
}
