# SET Android

Ứng dụng Android Native (Java) cho SET (Smart Expense Tracker).

## Cấu trúc Clean Architecture

```
com.smartexpense.android/
├── domain/         ← Entity, Repository Interface, UseCase
├── data/           ← Remote (Retrofit), Local (Room), Repository Impl
├── di/             ← Dependency Injection modules
└── presentation/   ← Activity, Fragment, ViewModel (theo tính năng)
```

## Mở bằng Android Studio

1. Mở Android Studio
2. File → Open → chọn folder `android/`
3. Đợi Gradle sync xong
4. Cắm thiết bị Android → bấm Run ▶

_Cập nhật chi tiết khi cấu hình xong._
