# ModernApp

Ứng dụng Android hiện đại được xây dựng bằng Kotlin và Jetpack Compose, tuân theo Clean Architecture và hỗ trợ build APK tự động qua GitHub Actions.

![Build APK](https://github.com/<YOUR_USERNAME>/ModernApp/actions/workflows/build.yml/badge.svg)

---

## Tính năng

- **Splash Screen** — Animation fade-in và scale đẹp mắt
- **Home Screen** — Danh sách nội dung, tìm kiếm real-time, pull-to-refresh
- **Detail Screen** — Xem chi tiết, chỉnh sửa inline, đánh dấu yêu thích
- **Settings Screen** — Đổi giao diện (Sáng/Tối/Hệ thống), ngôn ngữ, quản lý dữ liệu
- **Dark Mode / Light Mode** — Hỗ trợ đầy đủ Material Design 3
- **Offline-first** — Room Database lưu trữ cục bộ
- **Network sync** — Retrofit + OkHttp kết nối API
- **Lazy Loading** — LazyColumn với staggered animations
- **Navigation Animations** — Slide & fade transitions

---

## Yêu cầu hệ thống

| Yêu cầu | Phiên bản |
|---|---|
| Android Studio | Hedgehog (2023.1.1) trở lên |
| JDK | 17 |
| Android SDK | API 26+ (Android 8.0+) |
| Gradle | 8.9 |
| Kotlin | 2.0.21 |

---

## Công nghệ sử dụng

| Lớp | Công nghệ |
|---|---|
| Ngôn ngữ | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Kiến trúc | Clean Architecture + MVVM + Repository Pattern |
| DI | Hilt |
| Networking | Retrofit + OkHttp |
| Database | Room |
| Async | Kotlin Coroutines + StateFlow |
| Navigation | Navigation Compose |
| Serialization | Kotlin Serialization |
| Image | Coil |
| Preferences | DataStore |
| Splash | Core SplashScreen API |

---

## Cấu trúc dự án

```
ModernApp/
├── .github/
│   └── workflows/
│       ├── build.yml          # Build tự động khi push
│       └── release.yml        # Build & release khi tạo tag
├── app/
│   └── src/main/
│       ├── java/com/modernapp/app/
│       │   ├── di/            # Hilt Dependency Injection modules
│       │   ├── database/      # Room database, DAO, Entities
│       │   ├── domain/
│       │   │   ├── model/     # Domain models
│       │   │   └── usecase/   # Business logic use cases
│       │   ├── navigation/    # Navigation graph
│       │   ├── network/       # Retrofit API service & DTOs
│       │   ├── repository/    # Repository interface & implementation
│       │   ├── ui/
│       │   │   ├── components/ # Reusable Compose components
│       │   │   ├── screens/   # Screen composables
│       │   │   └── theme/     # Color, Typography, Theme
│       │   ├── utils/         # Constants, Extensions, Result
│       │   ├── viewmodel/     # ViewModels
│       │   ├── MainActivity.kt
│       │   └── ModernApplication.kt
│       └── res/
│           ├── drawable/      # Vector icons
│           ├── mipmap-*/      # App icons (adaptive)
│           ├── values/        # Strings, Colors, Themes
│           └── xml/           # Backup rules
├── gradle/
│   ├── libs.versions.toml     # Version catalog
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew
```

---

## Cài đặt và Build APK

### Cách 1: Android Studio

```bash
# 1. Clone repository
git clone https://github.com/<YOUR_USERNAME>/ModernApp.git
cd ModernApp

# 2. Mở Android Studio > Open Project
# 3. Đợi Gradle sync hoàn tất
# 4. Build > Generate Signed Bundle/APK
#    hoặc Run 'app' trực tiếp trên thiết bị/emulator
```

### Cách 2: Command Line

```bash
# Clone
git clone https://github.com/<YOUR_USERNAME>/ModernApp.git
cd ModernApp

# Build Debug APK
./gradlew assembleDebug

# Build Release APK (unsigned)
./gradlew assembleRelease

# APK được lưu tại:
# app/build/outputs/apk/debug/app-debug.apk
# app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## Build APK tự động với GitHub Actions

### Bước 1 — Tạo repository GitHub

1. Truy cập [github.com](https://github.com) → **New repository**
2. Đặt tên repository: `ModernApp`
3. Chọn **Public** hoặc **Private**
4. Nhấn **Create repository**

### Bước 2 — Upload source code

```bash
cd ModernApp

git init
git add .
git commit -m "Initial commit: ModernApp Android project"
git branch -M main
git remote add origin https://github.com/<YOUR_USERNAME>/ModernApp.git
git push -u origin main
```

### Bước 3 — GitHub Actions tự động chạy

Sau khi push code, GitHub Actions sẽ tự động:

1. ✅ Checkout code
2. ✅ Cài JDK 17
3. ✅ Cài Android SDK
4. ✅ Cache Gradle dependencies
5. ✅ Build Debug APK
6. ✅ Build Release APK
7. ✅ Upload APK vào Artifacts

Theo dõi tiến trình tại: `https://github.com/<YOUR_USERNAME>/ModernApp/actions`

### Bước 4 — Tải APK từ Artifacts

1. Vào tab **Actions** trên GitHub
2. Chọn workflow run mới nhất
3. Cuộn xuống phần **Artifacts**
4. Tải `ModernApp-Debug-<run_number>` hoặc `ModernApp-Release-<run_number>`
5. Giải nén file `.zip` để lấy file `.apk`
6. Cài lên thiết bị Android (cần bật "Install unknown apps")

---

## Build Release (có ký APK)

### Bước 1 — Tạo keystore

```bash
keytool -genkey -v -keystore modernapp-release.jks \
  -alias modernapp -keyalg RSA -keysize 2048 -validity 10000
```

### Bước 2 — Thêm secrets vào GitHub

Vào **Settings > Secrets and variables > Actions > New repository secret**:

| Secret name | Giá trị |
|---|---|
| `KEYSTORE_BASE64` | `base64 modernapp-release.jks` |
| `KEYSTORE_PASSWORD` | Mật khẩu keystore |
| `KEY_ALIAS` | `modernapp` |
| `KEY_PASSWORD` | Mật khẩu key |

### Bước 3 — Tạo Release

1. Vào **Releases > Create a new release**
2. Tạo tag mới (ví dụ `v1.0.0`)
3. Nhấn **Publish release**
4. Workflow `release.yml` tự động chạy và đính kèm APK vào release

---

## Cấu hình API

Mặc định app dùng JSONPlaceholder API (https://jsonplaceholder.typicode.com/) làm demo.

Để đổi sang API khác, sửa `Constants.kt`:

```kotlin
const val BASE_URL = "https://your-api.com/"
```

---

## Giấy phép

```
Copyright 2024 ModernApp

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
