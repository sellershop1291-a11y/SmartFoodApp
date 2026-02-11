# Firebase Setup Guide for SmartFood

This app uses **Firebase Authentication** and **Cloud Firestore**. Follow these steps to set it up:

## 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/).
2. Add a new project (e.g., "SmartFood").
3. Enable Google Analytics (optional).

## 2. Add Android App
1. Click the **Android** icon.
2. Package name: `com.example.smartfood`.
3. Download `google-services.json`.
4. Place this file in `SmartFood/app/google-services.json`.

## 3. Enable Authentication
1. Go to **Build > Authentication**.
2. Click **Get Started**.
3. Enable **Email/Password** provider.

## 4. Enable Cloud Firestore
1. Go to **Build > Firestore Database**.
2. Click **Create Database**.
3. Start in **Test Mode** (for development).
4. Choose a location.

## 5. Add Sample Data
Manually create a collection named `foods` and add documents:
- **id** (auto-ID)
- **name**: "Cheeseburger"
- **description**: "Juicy beef patty with cheese"
- **price**: 9.99 (number)
- **imageUrl**: "https://example.com/burger.jpg"
- **categoryId**: "burger_cat_id"
- **popular**: true (boolean)

Create a collection named `categories`:
- **id**: "burger_cat_id"
- **name**: "Burger"
- **imageUrl**: "..."

## 6. Run the App
Sync Gradle and run on an Emulator/Device.
