package com.prime.service;

import android.app.Service;

public interface ServiceControl {
    Service getService();
    void startService();
    void stopService();
    boolean vpnProtect(int socket);
}
