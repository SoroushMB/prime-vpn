package com.prime.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.prime.AppConfig;
import com.prime.R;
import com.prime.dto.EConfigType;
import com.prime.handler.MmkvManager;
import com.prime.handler.V2RayServiceManager;
import com.prime.util.Utils;
import com.prime.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TabLayout tabGroup;
    private View layoutTest;
    private View tvTestState;
    private View pbWaiting;
    
    private ActivityResultLauncher<Intent> requestVpnPermission;
    private ActivityResultLauncher<Intent> requestSubSettingActivity;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> chooseFileForCustomConfig;
    private ActivityResultLauncher<Intent> scanQRCodeForConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupViewModels();
        setupActivityResultLaunchers();
        setupRecyclerView();
        setupNavigation();
        setupFloatingActionButton();
        setupTestLayout();
        
        initGroupTab();
        migrateLegacy();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        tabGroup = findViewById(R.id.tabGroup);
        layoutTest = findViewById(R.id.layoutTest);
        tvTestState = findViewById(R.id.tvTestState);
        pbWaiting = findViewById(R.id.pbWaiting);
    }

    private void setupViewModels() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        mainViewModel.getUpdateListAction().observe(this, index -> {
            if (index >= 0) {
                // Update specific item
                // adapter.notifyItemChanged(index);
            } else {
                // Update all items
                // adapter.notifyDataSetChanged();
            }
        });
        
        mainViewModel.getUpdateTestResultAction().observe(this, this::setTestState);
        
        mainViewModel.getIsRunning().observe(this, isRunning -> {
            if (isRunning) {
                fab.setImageResource(R.drawable.ic_stop_24dp);
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_fab_active)));
                setTestState(getString(R.string.connection_connected));
                layoutTest.setFocusable(true);
            } else {
                fab.setImageResource(R.drawable.ic_play_24dp);
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_fab_inactive)));
                setTestState(getString(R.string.connection_not_connected));
                layoutTest.setFocusable(false);
            }
        });
        
        mainViewModel.startListenBroadcast();
        mainViewModel.initAssets(getAssets());
    }

    private void setupActivityResultLaunchers() {
        requestVpnPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    startV2Ray();
                }
            }
        );

        requestSubSettingActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> initGroupTab()
        );

        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Handle permission granted
                } else {
                    Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        );

        chooseFileForCustomConfig = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Handle file selection
                }
            }
        );

        scanQRCodeForConfig = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Handle QR code scan result
                }
            }
        );
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        if (MmkvManager.decodeSettingsBool(AppConfig.PREF_DOUBLE_COLUMN_DISPLAY, false)) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        // Add adapter here
    }

    private void setupNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, findViewById(R.id.drawerLayout), findViewById(R.id.toolbar), 
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        findViewById(R.id.drawerLayout).setOnClickListener(v -> {});
        toggle.syncState();
        
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);
    }

    private void setupFloatingActionButton() {
        fab.setOnClickListener(v -> {
            if (mainViewModel.getIsRunning().getValue() == Boolean.TRUE) {
                V2RayServiceManager.stopVService(this);
            } else if (AppConfig.VPN.equals(MmkvManager.decodeSettingsString(AppConfig.PREF_MODE, AppConfig.VPN))) {
                Intent intent = VpnService.prepare(this);
                if (intent == null) {
                    startV2Ray();
                } else {
                    requestVpnPermission.launch(intent);
                }
            } else {
                startV2Ray();
            }
        });
    }

    private void setupTestLayout() {
        layoutTest.setOnClickListener(v -> {
            if (mainViewModel.getIsRunning().getValue() == Boolean.TRUE) {
                setTestState(getString(R.string.connection_test_testing));
                mainViewModel.testCurrentServerRealPing();
            }
        });
    }

    private void initGroupTab() {
        tabGroup.removeOnTabSelectedListener(tabGroupListener);
        tabGroup.removeAllTabs();
        tabGroup.setVisibility(View.GONE);

        // Get subscriptions and setup tabs
        // Implementation would go here
        
        tabGroup.setVisibility(View.VISIBLE);
    }

    private void migrateLegacy() {
        // Implementation for legacy migration
    }

    private void startV2Ray() {
        if (MmkvManager.getSelectServer() == null || MmkvManager.getSelectServer().trim().isEmpty()) {
            Toast.makeText(this, R.string.title_file_chooser, Toast.LENGTH_SHORT).show();
            return;
        }
        V2RayServiceManager.startVService(this);
    }

    private void setTestState(String content) {
        // Set test state text
        if (tvTestState != null) {
            // tvTestState.setText(content);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.search_view);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mainViewModel.filterConfig(newText != null ? newText : "");
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.import_qrcode) {
            importQRcode();
            return true;
        } else if (itemId == R.id.import_clipboard) {
            importClipboard();
            return true;
        } else if (itemId == R.id.import_local) {
            importConfigLocal();
            return true;
        } else if (itemId == R.id.import_manually_vmess) {
            importManually(EConfigType.VMESS.getValue());
            return true;
        } else if (itemId == R.id.import_manually_vless) {
            importManually(EConfigType.VLESS.getValue());
            return true;
        } else if (itemId == R.id.import_manually_ss) {
            importManually(EConfigType.SHADOWSOCKS.getValue());
            return true;
        } else if (itemId == R.id.import_manually_socks) {
            importManually(EConfigType.SOCKS.getValue());
            return true;
        } else if (itemId == R.id.import_manually_http) {
            importManually(EConfigType.HTTP.getValue());
            return true;
        } else if (itemId == R.id.import_manually_trojan) {
            importManually(EConfigType.TROJAN.getValue());
            return true;
        } else if (itemId == R.id.import_manually_wireguard) {
            importManually(EConfigType.WIREGUARD.getValue());
            return true;
        } else if (itemId == R.id.import_manually_hysteria2) {
            importManually(EConfigType.HYSTERIA2.getValue());
            return true;
        } else if (itemId == R.id.export_all) {
            exportAll();
            return true;
        } else if (itemId == R.id.ping_all) {
            Toast.makeText(this, getString(R.string.connection_test_testing_count, mainViewModel.getServersCache().size()), Toast.LENGTH_SHORT).show();
            mainViewModel.testAllTcping();
            return true;
        } else if (itemId == R.id.real_ping_all) {
            Toast.makeText(this, getString(R.string.connection_test_testing_count, mainViewModel.getServersCache().size()), Toast.LENGTH_SHORT).show();
            mainViewModel.testAllRealPing();
            return true;
        } else if (itemId == R.id.intelligent_selection_all) {
            if (!"0".equals(MmkvManager.decodeSettingsString(AppConfig.PREF_OUTBOUND_DOMAIN_RESOLVE_METHOD, "1"))) {
                Toast.makeText(this, R.string.pre_resolving_domain, Toast.LENGTH_SHORT).show();
            }
            mainViewModel.createIntelligentSelectionAll();
            return true;
        } else if (itemId == R.id.service_restart) {
            restartV2Ray();
            return true;
        } else if (itemId == R.id.del_all_config) {
            delAllConfig();
            return true;
        } else if (itemId == R.id.del_duplicate_config) {
            delDuplicateConfig();
            return true;
        } else if (itemId == R.id.del_invalid_config) {
            delInvalidConfig();
            return true;
        } else if (itemId == R.id.sort_by_test_results) {
            sortByTestResults();
            return true;
        } else if (itemId == R.id.sub_update) {
            importConfigViaSub();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void importManually(int createConfigType) {
        Intent intent = new Intent();
        intent.putExtra("createConfigType", createConfigType);
        intent.putExtra("subscriptionId", mainViewModel.getSubscriptionId());
        intent.setClass(this, ServerActivity.class);
        startActivity(intent);
    }

    private void importQRcode() {
        String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            scanQRCodeForConfig.launch(new Intent(this, ScannerActivity.class));
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    private void importClipboard() {
        try {
            String clipboard = Utils.getClipboard(this);
            importBatchConfig(clipboard);
        } catch (Exception e) {
            Log.e(AppConfig.TAG, "Failed to import config from clipboard", e);
        }
    }

    private void importBatchConfig(String server) {
        // Implementation for importing batch config
    }

    private void importConfigLocal() {
        // Implementation for importing local config
    }

    private void importConfigViaSub() {
        // Implementation for importing via subscription
    }

    private void exportAll() {
        // Implementation for exporting all
    }

    private void delAllConfig() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.del_config_comfirm)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                // Implementation for deleting all configs
            })
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                // Do nothing
            })
            .show();
    }

    private void delDuplicateConfig() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.del_config_comfirm)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                // Implementation for deleting duplicate configs
            })
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                // Do nothing
            })
            .show();
    }

    private void delInvalidConfig() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.del_invalid_config_comfirm)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                // Implementation for deleting invalid configs
            })
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                // Do nothing
            })
            .show();
    }

    private void sortByTestResults() {
        // Implementation for sorting by test results
    }

    private void restartV2Ray() {
        if (mainViewModel.getIsRunning().getValue() == Boolean.TRUE) {
            V2RayServiceManager.stopVService(this);
        }
        // Delay and restart
        startV2Ray();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.sub_setting) {
            requestSubSettingActivity.launch(new Intent(this, SubSettingActivity.class));
        } else if (itemId == R.id.per_app_proxy_settings) {
            startActivity(new Intent(this, PerAppProxyActivity.class));
        } else if (itemId == R.id.routing_setting) {
            requestSubSettingActivity.launch(new Intent(this, RoutingSettingActivity.class));
        } else if (itemId == R.id.user_asset_setting) {
            startActivity(new Intent(this, UserAssetActivity.class));
        } else if (itemId == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("isRunning", mainViewModel.getIsRunning().getValue() == Boolean.TRUE);
            startActivity(intent);
        } else if (itemId == R.id.promotion) {
            Utils.openUri(this, AppConfig.APP_PROMOTION_URL + "?t=" + System.currentTimeMillis());
        } else if (itemId == R.id.logcat) {
            startActivity(new Intent(this, LogcatActivity.class));
        } else if (itemId == R.id.check_for_update) {
            startActivity(new Intent(this, CheckUpdateActivity.class));
        } else if (itemId == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        findViewById(R.id.drawerLayout).setVisibility(View.GONE);
        return true;
    }

    private TabLayout.OnTabSelectedListener tabGroupListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String selectId = tab.getTag().toString();
            if (!selectId.equals(mainViewModel.getSubscriptionId())) {
                mainViewModel.subscriptionIdChanged(selectId);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // Do nothing
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // Do nothing
        }
    };
}
