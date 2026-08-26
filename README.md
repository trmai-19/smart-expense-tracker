# SET (Smart Expense Tracker)

Ứng dụng di động quản lý chi tiêu cá nhân theo phong cách Locket — chụp ảnh, viết caption, AI tự động phân tích chi tiêu.

## Công nghệ

| Thành phần | Công nghệ |
|---|---|
| Server | Java, Spring Boot, Gradle |
| Android | Android Native (Kotlin, Jetpack Compose), Gradle |
| Database | PostgreSQL |
| AI | Gemini API |

## Cấu trúc dự án

```
mobile-app/
├── docs/       ← Tài liệu toàn hệ thống
├── server/     ← Spring Boot API (Gradle)
├── android/    ← Android Native App (Gradle)
└── README.md
```

## Kiến trúc

Áp dụng **Clean Architecture** cho cả Server và Android:
- **Domain** — Entity, Repository Interface, Exception
- **Application / UseCase** — Business logic, DTO, Port
- **Infrastructure / Data** — Database, API Client, Security, AI Adapter
- **Presentation / UI** — REST Controller / Compose Screen, ViewModel

## Quy tắc làm việc

> **BẮT BUỘC**: Khi hoàn thành xong một công việc, tính năng hoặc cấu hình, bắt buộc phải ghi chép và mô tả chi tiết vào các file tương ứng trong thư mục `docs/`. Tài liệu có thể là về API, usecase, database, kiến trúc hệ thống, luồng UI,... Điều này đảm bảo rằng khi đọc code có thể hiểu nhanh dự án mà không cần đọc từng file và cấu hình.
