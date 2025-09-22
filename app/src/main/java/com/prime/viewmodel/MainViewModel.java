package com.prime.viewmodel;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.prime.AppConfig;
import com.prime.PrimeApplication;
import com.prime.R;
import com.prime.dto.ProfileItem;
import com.prime.dto.ServersCache;
import com.prime.handler.MmkvManager;
import com.prime.handler.SettingsManager;
import com.prime.util.MessageUtil;
import com.prime.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private List<String> serverList = MmkvManager.decodeServerList();
    private String subscriptionId = MmkvManager.decodeSettingsString(AppConfig.CACHE_SUBSCRIPTION_ID, "");
    private String keywordFilter = "";
    private List<ServersCache> serversCache = new ArrayList<>();
    
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>();
    private final MutableLiveData<Integer> updateListAction = new MutableLiveData<>();
    private final MutableLiveData<String> updateTestResultAction = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
    }

    /**
     * Starts listening to broadcast messages.
     */
    public void startListenBroadcast() {
        isRunning.setValue(false);
        IntentFilter filter = new IntentFilter(AppConfig.BROADCAST_ACTION_ACTIVITY);
        ContextCompat.registerReceiver(getApplication(), mMsgReceiver, filter, Utils.receiverFlags());
        MessageUtil.sendMsg2Service(getApplication(), AppConfig.MSG_REGISTER_CLIENT, "");
    }

    /**
     * Called when the ViewModel is cleared.
     */
    @Override
    protected void onCleared() {
        getApplication().unregisterReceiver(mMsgReceiver);
        Log.i(AppConfig.TAG, "Main ViewModel is cleared");
        super.onCleared();
    }

    /**
     * Reloads the server list.
     */
    public void reloadServerList() {
        serverList = MmkvManager.decodeServerList();
        updateCache();
        updateListAction.setValue(-1);
    }

    /**
     * Removes a server by its GUID.
     */
    public void removeServer(String guid) {
        serverList.remove(guid);
        MmkvManager.removeServer(guid);
        int index = getPosition(guid);
        if (index >= 0) {
            serversCache.remove(index);
        }
    }

    /**
     * Swaps the positions of two servers.
     */
    public void swapServer(int fromPosition, int toPosition) {
        if (subscriptionId.isEmpty()) {
            Collections.swap(serverList, fromPosition, toPosition);
        } else {
            int fromPosition2 = serverList.indexOf(serversCache.get(fromPosition).guid);
            int toPosition2 = serverList.indexOf(serversCache.get(toPosition).guid);
            Collections.swap(serverList, fromPosition2, toPosition2);
        }
        Collections.swap(serversCache, fromPosition, toPosition);
        MmkvManager.encodeServerList(serverList);
    }

    /**
     * Updates the cache of servers.
     */
    public synchronized void updateCache() {
        serversCache.clear();
        for (String guid : serverList) {
            ProfileItem profile = MmkvManager.decodeServerConfig(guid);
            if (profile == null) continue;

            if (!subscriptionId.isEmpty() && !subscriptionId.equals(profile.subscriptionId)) {
                continue;
            }

            if (keywordFilter.isEmpty() || profile.remarks.toLowerCase().contains(keywordFilter.toLowerCase())) {
                serversCache.add(new ServersCache(guid, profile));
            }
        }
    }

    /**
     * Updates the configuration via subscription for all servers.
     */
    public int updateConfigViaSubAll() {
        if (subscriptionId.isEmpty()) {
            // return AngConfigManager.updateConfigViaSubAll();
            return 0;
        } else {
            // SubscriptionItem subItem = MmkvManager.decodeSubscription(subscriptionId);
            // return AngConfigManager.updateConfigViaSub(Pair.create(subscriptionId, subItem));
            return 0;
        }
    }

    /**
     * Exports all servers.
     */
    public int exportAllServer() {
        List<String> serverListCopy;
        if (subscriptionId.isEmpty() && keywordFilter.isEmpty()) {
            serverListCopy = serverList;
        } else {
            List<String> copy = new ArrayList<>();
            for (ServersCache cache : serversCache) {
                copy.add(cache.guid);
            }
            serverListCopy = copy;
        }

        // int ret = AngConfigManager.shareNonCustomConfigsToClipboard(
        //     getApplication(),
        //     serverListCopy
        // );
        return 0;
    }

    /**
     * Tests the TCP ping for all servers.
     */
    public void testAllTcping() {
        // Implementation for TCP ping testing
    }

    /**
     * Tests the real ping for all servers.
     */
    public void testAllRealPing() {
        MessageUtil.sendMsg2TestService(getApplication(), AppConfig.MSG_MEASURE_CONFIG_CANCEL, "");
        // Clear test results and update UI
        updateListAction.setValue(-1);
    }

    /**
     * Tests the real ping for the current server.
     */
    public void testCurrentServerRealPing() {
        MessageUtil.sendMsg2Service(getApplication(), AppConfig.MSG_MEASURE_DELAY, "");
    }

    /**
     * Changes the subscription ID.
     */
    public void subscriptionIdChanged(String id) {
        if (!subscriptionId.equals(id)) {
            subscriptionId = id;
            MmkvManager.encodeSettings(AppConfig.CACHE_SUBSCRIPTION_ID, subscriptionId);
            reloadServerList();
        }
    }

    /**
     * Gets the subscriptions.
     */
    public Pair<List<String>, List<String>> getSubscriptions(Context context) {
        // Implementation for getting subscriptions
        return null;
    }

    /**
     * Gets the position of a server by its GUID.
     */
    public int getPosition(String guid) {
        for (int i = 0; i < serversCache.size(); i++) {
            if (serversCache.get(i).guid.equals(guid)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes duplicate servers.
     */
    public int removeDuplicateServer() {
        // Implementation for removing duplicate servers
        return 0;
    }

    /**
     * Removes all servers.
     */
    public int removeAllServer() {
        int count;
        if (subscriptionId.isEmpty() && keywordFilter.isEmpty()) {
            count = MmkvManager.removeAllServer();
        } else {
            List<ServersCache> serversCopy = new ArrayList<>(serversCache);
            for (ServersCache item : serversCopy) {
                MmkvManager.removeServer(item.guid);
            }
            count = serversCache.size();
        }
        return count;
    }

    /**
     * Removes invalid servers.
     */
    public int removeInvalidServer() {
        // Implementation for removing invalid servers
        return 0;
    }

    /**
     * Sorts servers by their test results.
     */
    public void sortByTestResults() {
        // Implementation for sorting by test results
    }

    /**
     * Creates an intelligent selection configuration containing all currently filtered servers.
     */
    public void createIntelligentSelectionAll() {
        // Implementation for intelligent selection
    }

    /**
     * Initializes assets.
     */
    public void initAssets(AssetManager assets) {
        SettingsManager.initAssets(getApplication(), assets);
    }

    /**
     * Filters the configuration by a keyword.
     */
    public void filterConfig(String keyword) {
        if (keyword.equals(keywordFilter)) {
            return;
        }
        keywordFilter = keyword;
        MmkvManager.encodeSettings(AppConfig.CACHE_KEYWORD_FILTER, keywordFilter);
        reloadServerList();
    }

    // Getters
    public MutableLiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    public MutableLiveData<Integer> getUpdateListAction() {
        return updateListAction;
    }

    public MutableLiveData<String> getUpdateTestResultAction() {
        return updateTestResultAction;
    }

    public List<ServersCache> getServersCache() {
        return serversCache;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    private final BroadcastReceiver mMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int key = intent.getIntExtra("key", 0);
            switch (key) {
                case AppConfig.MSG_STATE_RUNNING:
                    isRunning.setValue(true);
                    break;
                case AppConfig.MSG_STATE_NOT_RUNNING:
                    isRunning.setValue(false);
                    break;
                case AppConfig.MSG_STATE_START_SUCCESS:
                    Toast.makeText(getApplication(), R.string.toast_services_success, Toast.LENGTH_SHORT).show();
                    isRunning.setValue(true);
                    break;
                case AppConfig.MSG_STATE_START_FAILURE:
                    Toast.makeText(getApplication(), R.string.toast_services_failure, Toast.LENGTH_SHORT).show();
                    isRunning.setValue(false);
                    break;
                case AppConfig.MSG_STATE_STOP_SUCCESS:
                    isRunning.setValue(false);
                    break;
                case AppConfig.MSG_MEASURE_DELAY_SUCCESS:
                    updateTestResultAction.setValue(intent.getStringExtra("content"));
                    break;
                case AppConfig.MSG_MEASURE_CONFIG_SUCCESS:
                    // Handle config measurement success
                    break;
            }
        }
    };

    // Simple Pair class for Java
    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public static <F, S> Pair<F, S> create(F first, S second) {
            return new Pair<>(first, second);
        }
    }
}
