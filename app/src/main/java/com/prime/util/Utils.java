package com.prime.util;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import java.util.UUID;

public class Utils {
    
    /**
     * Gets the clipboard content.
     */
    public static String getClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null && clip.getItemCount() > 0) {
                return clip.getItemAt(0).getText().toString();
            }
        }
        return "";
    }

    /**
     * Generates a UUID.
     */
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Checks if a URL is valid.
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://") || 
               url.startsWith("vmess://") || url.startsWith("vless://") ||
               url.startsWith("ss://") || url.startsWith("trojan://");
    }

    /**
     * Checks if a string is a pure IP address.
     */
    public static boolean isPureIpAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        String[] parts = address.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        try {
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Gets the user asset path.
     */
    public static String userAssetPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * Gets the device ID for XUDP base key.
     */
    public static String getDeviceIdForXUDPBaseKey() {
        // This would typically return a device-specific identifier
        return "default-device-id";
    }

    /**
     * Gets receiver flags for registering broadcast receivers.
     */
    public static int receiverFlags() {
        return Context.RECEIVER_NOT_EXPORTED;
    }
}
