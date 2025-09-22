# Prime VPN

A complete Android V2ray client implementation written in Java, converted from the original Kotlin V2ray client.

## Features

- **VPN Service**: Full Android VPN API integration with tun2socks
- **Proxy-only Mode**: Alternative operation without VPN permissions
- **Multiple Protocols**: Support for VMess, VLESS, Shadowsocks, SOCKS, HTTP, Trojan, WireGuard, and Hysteria2
- **Server Management**: Add, edit, delete, and test server configurations
- **Subscription Support**: Automatic configuration updates from subscription URLs
- **QR Code Scanning**: Easy configuration import via QR codes
- **Per-app Proxy**: Selective application routing
- **Routing Configuration**: Custom routing rules and domain strategies
- **Settings Management**: Persistent storage with MMKV
- **Notification System**: Service status and speed monitoring

## Architecture

- **MVVM Pattern**: ViewModels with LiveData for reactive UI
- **Service-based**: Background services for VPN and proxy operations
- **Broadcast Communication**: Inter-component messaging
- **Proper Lifecycle Management**: Android component lifecycle handling

## Project Structure

```
app/src/main/java/com/prime/
├── dto/           # Data Transfer Objects
├── handler/       # Business Logic Handlers
├── service/       # Background Services
├── ui/           # User Interface Activities
├── util/         # Utility Classes
└── viewmodel/    # MVVM ViewModels
```

## Key Components

### Core Classes
- `PrimeApplication`: Main application class
- `AppConfig`: Configuration constants and settings
- `EConfigType`: Protocol type enumeration

### Services
- `V2RayVpnService`: VPN service implementation
- `V2RayProxyOnlyService`: Proxy-only service
- `ServiceControl`: Service control interface

### Handlers
- `V2RayServiceManager`: Core service management
- `MmkvManager`: Data persistence with MMKV
- `SettingsManager`: Application settings management
- `NotificationManager`: Notification handling

### UI
- `MainActivity`: Main activity with full functionality
- `MainViewModel`: ViewModel for MVVM architecture
- Multiple activity classes for different features

## Dependencies

- **AndroidX**: Core Android libraries
- **Material Design**: UI components
- **MMKV**: Fast key-value storage
- **Gson**: JSON serialization
- **Toasty**: Toast notifications
- **WorkManager**: Background task management
- **Lifecycle Components**: MVVM architecture support

## Building

1. **Prerequisites**:
   - Java 17 or higher
   - Android SDK
   - Gradle 8.12.2+

2. **Build Commands**:
   ```bash
   ./gradlew assembleDebug    # Debug APK
   ./gradlew assembleRelease  # Release APK
   ```

3. **Installation**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## Configuration

The app supports various configuration options through the settings:

- **VPN Mode**: Choose between VPN and proxy-only modes
- **Protocol Settings**: Configure different proxy protocols
- **Routing Rules**: Custom domain and IP routing
- **Subscription Management**: Automatic configuration updates
- **Per-app Proxy**: Selective application routing

## Permissions

Required permissions:
- `INTERNET`: Network access
- `ACCESS_NETWORK_STATE`: Network state monitoring
- `CAMERA`: QR code scanning
- `FOREGROUND_SERVICE`: Background service operation
- `BIND_VPN_SERVICE`: VPN service binding
- `POST_NOTIFICATIONS`: Status notifications

## License

This project is based on the original V2ray client and maintains compatibility with V2ray/Xray configurations.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## Support

For issues and questions, please create an issue in the GitHub repository.

---

**Note**: This is a Java implementation of a V2ray client. Make sure to comply with local laws and regulations when using VPN services.
