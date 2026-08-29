# Database Schema

> Tài liệu mô tả cấu trúc database của hệ thống SET.
> Database: **PostgreSQL**
> Cập nhật file này mỗi khi thêm/sửa bảng hoặc quan hệ.

---

## Quy tắc đặt file Database

| Loại file | Server | Android |
|---|---|---|
| Domain Model (POJO) | `domain/model/` | `domain/model/` |
| DB Entity (JPA/Room) | `infrastructure/persistence/entity/` | `data/local/entity/` |
| DB Repository Interface | `domain/repository/` | `domain/repository/` |
| DB Repository Impl | `infrastructure/persistence/repository/` | `data/repository/` |
| Mapper (Entity ↔ Model) | `infrastructure/persistence/mapper/` | `data/remote/mapper/` |
| DAO (Room) | — | `data/local/dao/` |
| Output Port | `application/port/out/` | — |

> **QUAN TRỌNG**: Domain Model (`domain/model/`) và DB Entity (`infrastructure/` hoặc `data/`) là **2 class khác nhau**. Domain Model không có annotation `@Entity`, `@Table`, `@Column`. Mapper chuyển đổi giữa chúng.

---

## 2. Cấu trúc các bảng (Tables)

### 2.1. Bảng `users`
Lưu trữ thông tin xác thực và hồ sơ cá nhân hóa của người dùng.

| Cột | Kiểu dữ liệu | Ràng buộc | Mặc định | Ghi chú |
|---|---|---|---|---|
| `id` | UUID | PRIMARY KEY | `uuid_generate_v4()` | |
| `email` | VARCHAR(255) | UNIQUE, NOT NULL | | Tên đăng nhập / Email |
| `password_hash` | VARCHAR(255) | NOT NULL | | |
| `display_name` | VARCHAR(100) | NOT NULL | | |
| `avatar_url` | VARCHAR(500) | | `NULL` | Link ảnh đại diện (người dùng tải lên) |
| `monthly_budget` | DECIMAL(15,2) | | `0.00` | Hạn mức chi tiêu tháng |
| `streak_days` | INTEGER | | `0` | Chuỗi ngày ghi chép liên tục |
| `theme_color` | VARCHAR(20) | | `'#FFE600'` | Mã màu Neon chủ đạo |
| `created_at` | TIMESTAMP | NOT NULL | `NOW()` | |
| `updated_at` | TIMESTAMP | NOT NULL | `NOW()` | |

### 2.2. Bảng `expenses`
Lưu trữ các hóa đơn/chi tiêu.

| Cột | Kiểu dữ liệu | Ràng buộc | Mặc định | Ghi chú |
|---|---|---|---|---|
| `id` | UUID | PRIMARY KEY | `uuid_generate_v4()` | |
| `user_id` | UUID | FOREIGN KEY | | Liên kết bảng `users` |
| `amount` | DECIMAL(15,2) | NOT NULL | | Số tiền (AI đọc từ ảnh/caption) |
| `category` | VARCHAR(100) | NOT NULL | | AI tự sinh linh hoạt (Ăn uống, Học tập, Mua sắm, Đầu tư...) |
| `photo_url` | VARCHAR(500) | NOT NULL | | Link ảnh hóa đơn 3:4 |
| `caption` | VARCHAR(255) | | | Caption đè trên ảnh do user viết |
| `expense_date` | TIMESTAMP | NOT NULL | | Thời gian chi tiêu |
| `created_at` | TIMESTAMP | NOT NULL | `NOW()` | |
| `updated_at` | TIMESTAMP | NOT NULL | `NOW()` | |

### 2.3. Bảng `chat_messages`
Lưu lịch sử trò chuyện với Trợ lý AI SET.

| Cột | Kiểu dữ liệu | Ràng buộc | Mặc định | Ghi chú |
|---|---|---|---|---|
| `id` | UUID | PRIMARY KEY | `uuid_generate_v4()` | |
| `user_id` | UUID | FOREIGN KEY | | Liên kết bảng `users` |
| `role` | VARCHAR(20) | NOT NULL | | `'USER'` hoặc `'AI'` |
| `content` | TEXT | NOT NULL | | Nội dung tin nhắn |
| `created_at` | TIMESTAMP | NOT NULL | `NOW()` | |

### 2.4. Bảng `notifications`
Hệ thống thông báo.

| Cột | Kiểu dữ liệu | Ràng buộc | Mặc định | Ghi chú |
|---|---|---|---|---|
| `id` | UUID | PRIMARY KEY | `uuid_generate_v4()` | |
| `user_id` | UUID | FOREIGN KEY | | Liên kết bảng `users` |
| `type` | VARCHAR(50) | NOT NULL | | `'BUDGET_WARNING'`, `'AI_TIP'`, ... |
| `content` | TEXT | NOT NULL | | Nội dung thông báo |
| `is_read` | BOOLEAN | NOT NULL | `FALSE` | Trạng thái đọc (tính Red Dot) |
| `created_at` | TIMESTAMP | NOT NULL | `NOW()` | |

---

## 3. Quan hệ (Relationships)

- `users (1) - (N) expenses`
- `users (1) - (N) chat_messages`
- `users (1) - (N) notifications`

---

## 4. Chỉ mục (Indexes)

- `idx_expenses_user_date` trên `expenses(user_id, expense_date)`: Tối ưu truy vấn Dashboard và Timeline.
- `idx_chat_messages_user_time` trên `chat_messages(user_id, created_at)`: Tải nhanh lịch sử chat.
- `idx_notifications_user_read` trên `notifications(user_id, is_read)`: Đếm thông báo chưa đọc.
