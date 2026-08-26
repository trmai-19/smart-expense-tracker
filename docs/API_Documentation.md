# API Documentation

> Tài liệu mô tả chi tiết các REST API endpoints của hệ thống SET.
> Cập nhật file này mỗi khi hoàn thành một endpoint.

---

## Quy tắc đặt file API

| Loại file | Server | Android |
|---|---|---|
| REST Controller | `presentation/controller/` | — |
| Request DTO | `application/dto/request/` | — |
| Response DTO | `application/dto/response/` | — |
| Exception Handler | `presentation/handler/` | — |
| Input Port | `application/port/in/` | — |
| Retrofit API Interface | — | `data/remote/api/` |
| Network DTO | — | `data/remote/dto/` |
| DTO ↔ Model Mapper | — | `data/remote/mapper/` |

---

## 1. Auth API
- `POST /api/auth/register`: Đăng ký tài khoản mới (Body: `email`, `password`, `displayName`). Trả về JWT Token và User Info.
- `POST /api/auth/login`: Đăng nhập (Body: `email`, `password`). Trả về JWT Token và User Info.

## 2. User API
- `GET /api/users/me`: Lấy thông tin hồ sơ cá nhân của user đang đăng nhập (Yêu cầu JWT Token).
- `PUT /api/users/me`: Cập nhật thông tin hồ sơ (Body tuỳ chọn: `displayName`, `avatarUrl`, `monthlyBudget`, `themeColor`) (Yêu cầu JWT Token).

## 3. Expense API
- `GET /api/expenses`: Lấy danh sách lịch sử chi tiêu, sắp xếp từ mới đến cũ (Yêu cầu JWT Token).
- `POST /api/expenses`: Tạo mới một hoá đơn chi tiêu cơ bản (Body: `amount`, `category`, `photoUrl`, `caption`, `expenseDate`) (Yêu cầu JWT Token).

## 4. Notification API
- `GET /api/notifications`: Lấy danh sách thông báo của user, sắp xếp từ mới đến cũ (Yêu cầu JWT Token).
- `PATCH /api/notifications/{id}/read`: Đánh dấu một thông báo cụ thể là "Đã đọc" (Yêu cầu JWT Token).
- `PATCH /api/notifications/read-all`: Đánh dấu tất cả thông báo của user là "Đã đọc" (Yêu cầu JWT Token).

## 5. Chat AI API
- `POST /api/chat/send`: Gửi tin nhắn tới SET AI và nhận phản hồi từ Gemini 1.5 Flash (Body: `message: String`) (Yêu cầu JWT Token).
  - Lịch sử hội thoại (tối đa 10 tin nhắn gần nhất) được tự động gửi kèm làm context cho AI.
  - Phản hồi: `{ "reply": "..." }`
  - Cả tin nhắn user và AI reply đều được lưu vào bảng `chat_messages`.
