package com.prime.handler;

import com.tencent.mmkv.MMKV;
import com.prime.AppConfig;
import com.prime.dto.ProfileItem;
import com.prime.dto.ServerAffiliationInfo;
import com.prime.dto.SubscriptionItem;
import com.prime.util.JsonUtil;
import com.prime.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MmkvManager {
    
    private static final String ID_MAIN = "MAIN";
    private static final String ID_PROFILE_FULL_CONFIG = "PROFILE_FULL_CONFIG";
    private static final String ID_SERVER_RAW = "SERVER_RAW";
    private static final String ID_SERVER_AFF = "SERVER_AFF";
    private static final String ID_SUB = "SUB";
    private static final String ID_ASSET = "ASSET";
    private static final String ID_SETTING = "SETTING";
    private static final String KEY_SELECTED_SERVER = "SELECTED_SERVER";
    private static final String KEY_ANG_CONFIGS = "ANG_CONFIGS";
    private static final String KEY_SUB_IDS = "SUB_IDS";

    private static final MMKV mainStorage = MMKV.mmkvWithID(ID_MAIN, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV profileFullStorage = MMKV.mmkvWithID(ID_PROFILE_FULL_CONFIG, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV serverRawStorage = MMKV.mmkvWithID(ID_SERVER_RAW, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV serverAffStorage = MMKV.mmkvWithID(ID_SERVER_AFF, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV subStorage = MMKV.mmkvWithID(ID_SUB, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV assetStorage = MMKV.mmkvWithID(ID_ASSET, MMKV.MULTI_PROCESS_MODE);
    private static final MMKV settingsStorage = MMKV.mmkvWithID(ID_SETTING, MMKV.MULTI_PROCESS_MODE);

    //region Server

    /**
     * Gets the selected server GUID.
     */
    public static String getSelectServer() {
        return mainStorage.decodeString(KEY_SELECTED_SERVER);
    }

    /**
     * Sets the selected server GUID.
     */
    public static void setSelectServer(String guid) {
        mainStorage.encode(KEY_SELECTED_SERVER, guid);
    }

    /**
     * Encodes the server list.
     */
    public static void encodeServerList(List<String> serverList) {
        mainStorage.encode(KEY_ANG_CONFIGS, JsonUtil.toJson(serverList));
    }

    /**
     * Decodes the server list.
     */
    public static List<String> decodeServerList() {
        String json = mainStorage.decodeString(KEY_ANG_CONFIGS);
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        } else {
            return JsonUtil.fromJson(json, String[].class);
        }
    }

    /**
     * Decodes the server configuration.
     */
    public static ProfileItem decodeServerConfig(String guid) {
        if (guid == null || guid.trim().isEmpty()) {
            return null;
        }
        String json = profileFullStorage.decodeString(guid);
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        return JsonUtil.fromJson(json, ProfileItem.class);
    }

    /**
     * Encodes the server configuration.
     */
    public static String encodeServerConfig(String guid, ProfileItem config) {
        String key = (guid == null || guid.trim().isEmpty()) ? Utils.getUuid() : guid;
        profileFullStorage.encode(key, JsonUtil.toJson(config));
        List<String> serverList = decodeServerList();
        if (!serverList.contains(key)) {
            serverList.add(0, key);
            encodeServerList(serverList);
            if (getSelectServer() == null || getSelectServer().trim().isEmpty()) {
                mainStorage.encode(KEY_SELECTED_SERVER, key);
            }
        }
        return key;
    }

    /**
     * Removes the server configuration.
     */
    public static void removeServer(String guid) {
        if (guid == null || guid.trim().isEmpty()) {
            return;
        }
        if (guid.equals(getSelectServer())) {
            mainStorage.remove(KEY_SELECTED_SERVER);
        }
        List<String> serverList = decodeServerList();
        serverList.remove(guid);
        encodeServerList(serverList);
        profileFullStorage.remove(guid);
        serverAffStorage.remove(guid);
    }

    /**
     * Removes all server configurations.
     */
    public static int removeAllServer() {
        int count = profileFullStorage.allKeys() != null ? profileFullStorage.allKeys().length : 0;
        mainStorage.clearAll();
        profileFullStorage.clearAll();
        serverAffStorage.clearAll();
        return count;
    }

    /**
     * Decodes the server affiliation information.
     */
    public static ServerAffiliationInfo decodeServerAffiliationInfo(String guid) {
        if (guid == null || guid.trim().isEmpty()) {
            return null;
        }
        String json = serverAffStorage.decodeString(guid);
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        return JsonUtil.fromJson(json, ServerAffiliationInfo.class);
    }

    /**
     * Encodes the server test delay in milliseconds.
     */
    public static void encodeServerTestDelayMillis(String guid, long testResult) {
        if (guid == null || guid.trim().isEmpty()) {
            return;
        }
        ServerAffiliationInfo aff = decodeServerAffiliationInfo(guid);
        if (aff == null) {
            aff = new ServerAffiliationInfo();
        }
        aff.testDelayMillis = testResult;
        serverAffStorage.encode(guid, JsonUtil.toJson(aff));
    }

    //endregion

    //region Settings

    /**
     * Encodes the settings.
     */
    public static boolean encodeSettings(String key, String value) {
        return settingsStorage.encode(key, value);
    }

    /**
     * Encodes the settings.
     */
    public static boolean encodeSettings(String key, int value) {
        return settingsStorage.encode(key, value);
    }

    /**
     * Encodes the settings.
     */
    public static boolean encodeSettings(String key, boolean value) {
        return settingsStorage.encode(key, value);
    }

    /**
     * Encodes the settings.
     */
    public static boolean encodeSettings(String key, Set<String> value) {
        return settingsStorage.encode(key, value);
    }

    /**
     * Decodes the settings string.
     */
    public static String decodeSettingsString(String key) {
        return settingsStorage.decodeString(key);
    }

    /**
     * Decodes the settings string.
     */
    public static String decodeSettingsString(String key, String defaultValue) {
        return settingsStorage.decodeString(key, defaultValue);
    }

    /**
     * Decodes the settings boolean.
     */
    public static boolean decodeSettingsBool(String key) {
        return settingsStorage.decodeBool(key, false);
    }

    /**
     * Decodes the settings boolean.
     */
    public static boolean decodeSettingsBool(String key, boolean defaultValue) {
        return settingsStorage.decodeBool(key, defaultValue);
    }

    /**
     * Decodes the settings string set.
     */
    public static Set<String> decodeSettingsStringSet(String key) {
        return settingsStorage.decodeStringSet(key);
    }

    //endregion
}
