package com.prime;

import android.content.Context;
import androidx.multidex.MultiDexApplication;
import androidx.work.Configuration;
import androidx.work.WorkManager;
import com.tencent.mmkv.MMKV;
import com.prime.handler.SettingsManager;

public class PrimeApplication extends MultiDexApplication {
    private static PrimeApplication application;

    /**
     * Gets the application instance.
     * @return The application instance.
     */
    public static PrimeApplication getInstance() {
        return application;
    }

    /**
     * Attaches the base context to the application.
     * @param base The base context.
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
    }

    private final Configuration workManagerConfiguration = new Configuration.Builder()
            .setDefaultProcessName(AppConfig.PRIME_PACKAGE + ":bg")
            .build();

    /**
     * Initializes the application.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        MMKV.initialize(this);

        SettingsManager.setNightMode();
        // Initialize WorkManager with the custom configuration
        WorkManager.initialize(this, workManagerConfiguration);

        SettingsManager.initRoutingRulesets(this);

        es.dmoral.toasty.Toasty.Config.getInstance()
                .setGravity(android.view.Gravity.BOTTOM, 0, 200)
                .apply();
    }
}
