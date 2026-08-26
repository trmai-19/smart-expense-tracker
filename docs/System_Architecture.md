# System Architecture

> Tài liệu mô tả kiến trúc tổng thể của hệ thống SET (Smart Expense Tracker).
> Cập nhật file này khi có thay đổi kiến trúc.

---

## 1. Tổng quan hệ thống

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Android    │  HTTP   │    Server    │         │  PostgreSQL  │
│  (Native)    │ ◄─────► │ (Spring Boot)│ ◄─────► │  Database    │
└──────────────┘  REST   └──────┬───────┘         └──────────────┘
                                │
                                ▼
                        ┌──────────────┐
                        │  Gemini API  │
                        │  (AI Vision) │
                        └──────────────┘
```

| Thành phần | Công nghệ | Build Tool |
|---|---|---|
| Server | Kotlin, Spring Boot 3.4.x | Gradle (`build.gradle`) |
| Android | Kotlin, Jetpack Compose (SDK 34) | Gradle (`build.gradle`) |
| Database | PostgreSQL | — |
| AI | Gemini API | — |

---

## 2. Clean Architecture — Quy tắc phụ thuộc

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation / UI                     │
├─────────────────────────────────────────────────────────┤
│                Infrastructure / Data                     │
├─────────────────────────────────────────────────────────┤
│                Application (Use Cases)                   │
├─────────────────────────────────────────────────────────┤
│                      Domain                              │
└─────────────────────────────────────────────────────────┘
            ▲ Mũi tên = chiều phụ thuộc (hướng vào trong)
```

> **QUY TẮC BẮT BUỘC:**
> - **Domain** → KHÔNG phụ thuộc bất kỳ layer nào.
> - **Application** → CHỈ phụ thuộc Domain.
> - **Infrastructure** → Phụ thuộc Application và Domain.
> - **Presentation** → Phụ thuộc Application và Domain. KHÔNG gọi trực tiếp Infrastructure.

---

## 3. Cấu trúc thư mục Android Client

```
android/app/src/main/kotlin/com/smartexpense/android/
├── domain/                              ← Core business logic & models
│   ├── model/                           Entity thuần Kotlin (Data class)
│   ├── repository/                      Interface repository (contract)
│   └── usecase/
│       ├── auth/                        Use case xác thực
│       └── expense/                     Use case chi tiêu
│
├── data/                                ← Data layer & local/remote sources
│   ├── remote/
│   │   ├── api/                         Retrofit API interface
│   │   ├── dto/                         Network DTO
│   │   └── mapper/                      DTO ↔ Domain Model mapper
│   ├── local/
│   │   ├── dao/                         Room DAO
│   │   └── entity/                      Room Entity
│   └── repository/                      Repository implementation
│
├── di/                                  Dependency Injection modules
│
└── presentation/                        ← UI / View Layer (Android Jetpack Compose)
    ├── auth/
    │   ├── login/                       LoginScreen.kt (Màn hình Đăng nhập)
    │   └── register/                    RegisterScreen.kt (Màn hình Đăng ký)
    ├── camera/
    │   ├── CameraScreen.kt              Trang CameraX 3:4 & Vertical Timeline
    │   ├── TimelineFeed.kt              UI timeline dọc (Trang 0 preview, 1..N history)
    │   └── confirm/
    │       └── ConfirmScreen.kt         Xác nhận ảnh chụp, nhập caption, đăng bài
    ├── widget/
    │   ├── WidgetGridScreen.kt          Tab 0: Lưới widget chi tiêu Locket
    │   └── WidgetGrid.kt                UI danh sách ảnh lưới
    ├── dashboard/
    │   └── DashboardScreen.kt           Tab 1: Thống kê, biểu đồ 7 ngày, lọc khoảng ngày
    ├── chat/
    │   ├── ChatScreen.kt                Tab 3: Trợ lý AI SET tư vấn tài chính
    │   ├── ChatBubble.kt                UI bong bóng tin nhắn (Theme sync)
    │   └── ChatMessage.kt               Model tin nhắn chat
    ├── notification/
    │   ├── NotificationManager.kt       Quản lý dữ liệu thông báo & số lượng chưa đọc
    │   ├── NotificationBottomSheet.kt   Modal xem thông báo & điều hướng tab
    │   ├── NotificationList.kt          UI danh sách thông báo
    │   └── NotificationItem.kt          Model thông báo hệ thống
    ├── profile/
    │   ├── ProfileScreen.kt             Màn hình hồ sơ, thống kê Streak/Hóa đơn/Ngân sách
    │   └── EditProfileBottomSheet.kt    Modal đổi ảnh avatar, tên, email, hạn mức tháng
    ├── navigation/
    │   ├── AppNavHost.kt                Host điều hướng chính của toàn ứng dụng
    │   └── Screen.kt                    Định nghĩa các route
    └── util/
        ├── ThemeManager.kt              Quản lý bảng màu Neon & đổi theme động
        ├── UserManager.kt               Lưu trữ trạng thái người dùng & DataStore
        └── ThemeColorBottomSheet.kt     Modal chọn nhanh 8 màu Neon chủ đạo
```

---

## 4. Cấu trúc thư mục Server

```
server/src/main/kotlin/com/smartexpense/api/
├── domain/                          ← KHÔNG phụ thuộc gì
│   ├── model/                       Entity thuần Kotlin (Data class)
│   ├── repository/                  Interface repository (contract)
│   └── exception/                   Domain-level exception
│
├── application/                     ← Chỉ phụ thuộc Domain
│   ├── usecase/
│   │   ├── auth/                    Use case xác thực
│   │   └── expense/                 Use case chi tiêu
│   ├── dto/
│   │   ├── request/                 Dữ liệu đầu vào từ client
│   │   └── response/               Dữ liệu trả về cho client
│   └── port/
│       ├── in/                      Input port interface
│       └── out/                     Output port interface (DB, AI)
│
├── infrastructure/                  ← Phụ thuộc Application + Domain
│   ├── persistence/
│   │   ├── entity/                  JPA Entity (mapping bảng DB)
│   │   ├── repository/             JPA Repository implementation
│   │   └── mapper/                 Entity ↔ Domain Model mapper
│   ├── security/                   JWT + Spring Security
│   ├── config/                     Cấu hình ứng dụng
│   └── ai/                         Adapter gọi Gemini API
│
└── presentation/                    ← Phụ thuộc Application + Domain
    ├── controller/                  REST Controller
    └── handler/                    Global exception handler
```

---

## 5. Quy tắc đặt file khi code

| Loại file | Server đặt tại | Android đặt tại |
|---|---|---|
| Entity thuần (Data class) | `domain/model/` | `domain/model/` |
| Repository Interface | `domain/repository/` | `domain/repository/` |
| UseCase | `application/usecase/` | `domain/usecase/` |
| DTO | `application/dto/` | `data/remote/dto/` |
| DB Entity (JPA/Room) | `infrastructure/persistence/entity/` | `data/local/entity/` |
| DB Repository Impl | `infrastructure/persistence/repository/` | `data/repository/` |
| Mapper | `infrastructure/persistence/mapper/` | `data/remote/mapper/` |
| Security (JWT) | `infrastructure/security/` | — |
| AI Adapter | `infrastructure/ai/` | — |
| REST Controller | `presentation/controller/` | — |
| Compose Screen | — | `presentation/{feature}/` |
| ViewModel / UI Component | — | `presentation/{feature}/` |
| State Manager / Util | — | `presentation/util/` |

---

## 6. Luồng xử lý Hình ảnh (Image & Media Pipeline) - Giai đoạn hiện tại

Để đảm bảo tốc độ và giảm thiểu tài nguyên trong giai đoạn phát triển ban đầu, hệ thống xử lý ảnh theo kiến trúc **Local URI Caching**:

1. **Chụp ảnh (Android)**: `CameraFragment` dùng CameraX chụp ảnh và lưu trực tiếp vào thư mục bộ nhớ đệm (cache) của ứng dụng (`/data/user/0/.../cache/`).
2. **Giao tiếp (API)**: Ứng dụng gửi trực tiếp chuỗi định vị file nội bộ (vd: `file:///data/user/0/.../cache/img.jpg`) qua API cho Spring Boot Backend.
3. **Lưu trữ (Backend)**: Database PostgreSQL lưu trữ chuỗi `file://` này vào cột `photo_url` dưới dạng văn bản (text). Server không thực sự lưu trữ file ảnh vật lý.
4. **Hiển thị (Glide)**: Khi người dùng lướt xem lịch sử chi tiêu, API trả về chuỗi `file://`. Thư viện `Glide` trên Android nhận diện chuỗi này và tự động tải ảnh từ bộ nhớ đệm nội bộ để hiển thị lên UI (`HistoryFullscreenAdapter`, `HistoryGridAdapter`, `TimelineFeedAdapter`).

*Lưu ý kiến trúc: Cách tiếp cận này chỉ hoạt động khi xem ảnh trên cùng một thiết bị đã chụp ảnh. Kiến trúc này sẽ được nâng cấp lên luồng Multipart Upload hoặc Cloud Storage (AWS S3/Firebase) trong tương lai.*
