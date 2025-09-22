package com.prime.util;

import android.content.Context;
import android.content.Intent;
import com.prime.AppConfig;

public class MessageUtil {
    
    /**
     * Sends a message to the service.
     */
    public static void sendMsg2Service(Context context, int key, String content) {
        Intent intent = new Intent(AppConfig.BROADCAST_ACTION_SERVICE);
        intent.putExtra("key", key);
        intent.putExtra("content", content);
        context.sendBroadcast(intent);
    }

    /**
     * Sends a message to the UI.
     */
    public static void sendMsg2UI(Context context, int key, String content) {
        Intent intent = new Intent(AppConfig.BROADCAST_ACTION_ACTIVITY);
        intent.putExtra("key", key);
        intent.putExtra("content", content);
        context.sendBroadcast(intent);
    }

    /**
     * Sends a message to the test service.
     */
    public static void sendMsg2TestService(Context context, int key, String content) {
        Intent intent = new Intent(AppConfig.BROADCAST_ACTION_SERVICE);
        intent.putExtra("key", key);
        intent.putExtra("content", content);
        context.sendBroadcast(intent);
    }
}
