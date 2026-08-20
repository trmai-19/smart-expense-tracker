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
| Server | Java 21, Spring Boot 3.4.x | Gradle (`build.gradle`) |
| Android | Java, Android Native (SDK 34) | Gradle (`build.gradle`) |
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
android/app/src/main/java/com/smartexpense/android/
├── domain/                              ← Core business logic & models
│   ├── model/                           Entity thuần Java (POJO)
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
└── presentation/                        ← UI / View Layer (Android Jetpack)
    ├── auth/
    │   ├── login/                       LoginActivity (Màn hình Đăng nhập)
    │   └── register/                    RegisterActivity (Màn hình Đăng ký)
    ├── camera/
    │   ├── CameraFragment.java          Trang CameraX 3:4 & Vertical Timeline
    │   ├── TimelineFeedAdapter.java     Adapter timeline dọc (Trang 0 preview, 1..N history)
    │   └── confirm/
    │       └── ConfirmActivity.java     Xác nhận ảnh chụp, nhập caption, đăng bài
    ├── widget/
    │   ├── WidgetGridFragment.java      Tab 0: Lưới widget chi tiêu Locket
    │   └── WidgetGridAdapter.java       Adapter danh sách ảnh lưới
    ├── dashboard/
    │   └── DashboardFragment.java       Tab 1: Thống kê, biểu đồ 7 ngày, lọc khoảng ngày
    ├── chat/
    │   ├── ChatFragment.java            Tab 3: Trợ lý AI SET tư vấn tài chính
    │   ├── ChatAdapter.java             Adapter bong bóng tin nhắn (Theme sync)
    │   └── ChatMessage.java             Model tin nhắn chat
    ├── notification/
    │   ├── NotificationManager.java     Quản lý dữ liệu thông báo & số lượng chưa đọc
    │   ├── NotificationBottomSheet.java Modal xem thông báo & điều hướng tab
    │   ├── NotificationAdapter.java     Adapter danh sách thông báo
    │   └── NotificationItem.java        Model thông báo hệ thống
    ├── profile/
    │   ├── ProfileActivity.java         Màn hình hồ sơ, thống kê Streak/Hóa đơn/Ngân sách
    │   └── EditProfileBottomSheet.java  Modal đổi ảnh avatar, tên, email, hạn mức tháng
    ├── main/
    │   ├── MainActivity.java            Host Activity điều phối Top bar, ViewPager2, Bottom bar
    │   └── MainPagerAdapter.java       Adapter 4 tab [Widget | Dashboard | Camera | Chat]
    └── util/
        ├── ThemeManager.java            Quản lý bảng màu Neon & đổi theme động
        ├── UserManager.java             Lưu trữ trạng thái người dùng & SharedPreferences
        └── ThemeColorBottomSheet.java   Modal chọn nhanh 8 màu Neon chủ đạo
```

---

## 4. Cấu trúc thư mục Server

```
server/src/main/java/com/smartexpense/api/
├── domain/                          ← KHÔNG phụ thuộc gì
│   ├── model/                       Entity thuần Java (POJO)
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
| Entity thuần (POJO) | `domain/model/` | `domain/model/` |
| Repository Interface | `domain/repository/` | `domain/repository/` |
| UseCase | `application/usecase/` | `domain/usecase/` |
| DTO | `application/dto/` | `data/remote/dto/` |
| DB Entity (JPA/Room) | `infrastructure/persistence/entity/` | `data/local/entity/` |
| DB Repository Impl | `infrastructure/persistence/repository/` | `data/repository/` |
| Mapper | `infrastructure/persistence/mapper/` | `data/remote/mapper/` |
| Security (JWT) | `infrastructure/security/` | — |
| AI Adapter | `infrastructure/ai/` | — |
| REST Controller | `presentation/controller/` | — |
| Activity/Fragment | — | `presentation/{feature}/` |
| ViewModel / Adapter | — | `presentation/{feature}/` |
| State Manager / Util | — | `presentation/util/` |

---

## 6. Luồng xử lý Hình ảnh (Image & Media Pipeline) - Giai đoạn hiện tại

Để đảm bảo tốc độ và giảm thiểu tài nguyên trong giai đoạn phát triển ban đầu, hệ thống xử lý ảnh theo kiến trúc **Local URI Caching**:

1. **Chụp ảnh (Android)**: `CameraFragment` dùng CameraX chụp ảnh và lưu trực tiếp vào thư mục bộ nhớ đệm (cache) của ứng dụng (`/data/user/0/.../cache/`).
2. **Giao tiếp (API)**: Ứng dụng gửi trực tiếp chuỗi định vị file nội bộ (vd: `file:///data/user/0/.../cache/img.jpg`) qua API cho Spring Boot Backend.
3. **Lưu trữ (Backend)**: Database PostgreSQL lưu trữ chuỗi `file://` này vào cột `photo_url` dưới dạng văn bản (text). Server không thực sự lưu trữ file ảnh vật lý.
4. **Hiển thị (Glide)**: Khi người dùng lướt xem lịch sử chi tiêu, API trả về chuỗi `file://`. Thư viện `Glide` trên Android nhận diện chuỗi này và tự động tải ảnh từ bộ nhớ đệm nội bộ để hiển thị lên UI (`HistoryFullscreenAdapter`, `HistoryGridAdapter`, `TimelineFeedAdapter`).

*Lưu ý kiến trúc: Cách tiếp cận này chỉ hoạt động khi xem ảnh trên cùng một thiết bị đã chụp ảnh. Kiến trúc này sẽ được nâng cấp lên luồng Multipart Upload hoặc Cloud Storage (AWS S3/Firebase) trong tương lai.*
