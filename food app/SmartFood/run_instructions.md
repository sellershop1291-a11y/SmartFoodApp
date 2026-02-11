# How to Run SmartFood App

## Prerequisites
- Android Studio (Latest Version)
- JDK 17 (Embedded in Android Studio)
- Android Emulator or Physical Device

## Steps
1. **Open Project**: Open Android Studio and select "Open" -> navigate to `SmartFood` folder.
2. **Sync Gradle**: Wait for the project to index and sync dependencies.
3. **Add Firebase Config**:
    - Follow `firebase_setup_guide.md` to get your `google-services.json`.
    - Place it in `SmartFood/app/`.
4. **Build**: Go to `Build > Make Project`.
5. **Run**: Click the green "Run" button (Shift+F10).

## Troubleshooting
- **Build Errors**: Check your JDK version in `File > Project Structure`.
- **Firebase Errors**: Ensure `google-services.json` package name matches `com.example.smartfood`.
- **Runtime Crashes**: Check Logcat. Common issue is missing internet permission (already added) or Firestore rules (ensure test mode).

## Features
- **Login/Signup**: Email authentication.
- **Home**: Search, Categories, Popular items.
- **Detail**: Add to cart logic.
- **Cart**: Edit quantities, view total.
- **Checkout**: Place dummy orders.
