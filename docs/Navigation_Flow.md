# Navigation Flow

> Tài liệu mô tả chi tiết toàn bộ luồng điều hướng (Navigation Flow) trong ứng dụng Android SET.

---

## 1. Sơ đồ luồng tổng quan (Navigation Architecture)

```
[Khởi động App]
       │
       ▼
[LoginActivity] ──(Chưa có tài khoản)──► [RegisterActivity]
       │                                         │
 (Đăng nhập OK)                           (Đăng ký OK)
       │                                         │
       └──────────────────┬──────────────────────┘
                          │
                          ▼
                   [MainActivity]
  ┌───────────────────────┴───────────────────────┐
  ▼                                               ▼
[Top Bar: Cố định]                       [Bottom Bar: 5 Slot]
 ├─ Avatar ──► [ProfileActivity]          ├─ Slot 1 ──► [Tab 0: Widget Grid]
 │                 │                      ├─ Slot 2 ──► [Tab 1: Dashboard]
 │                 ├─ (Chỉnh sửa)         ├─ Slot 3 ──► [Tab 2: Camera (Mặc định)]
 │                 │    └──► [EditProfile]├─ Slot 4 ──► [Tab 3: AI Chat]
 │                 └─ (Đăng xuất)         └─ Slot 5 ──► [ThemeColorBottomSheet]
 │                      └──► [LoginActivity]
 └─ Chuông ──► [NotificationBottomSheet]
                   │
                   └──(Nhấn thông báo)──► Chuyển ngay đến Tab tương ứng
```

---

## 2. Chi tiết các luồng chuyển màn hình (Screen Transitions)

### 2.1. Luồng Xác thực (Authentication Flow)
1. **Khởi động**: Ứng dụng mở `LoginActivity`.
2. **Chuyển sang Đăng ký**: Nhấn *"Chưa có tài khoản? Đăng ký ngay"* → Mở `RegisterActivity` với animation mượt mà.
3. **Quay lại Đăng nhập**: Nhấn nút Back hoặc *"Đã có tài khoản? Đăng nhập"* → Đóng `RegisterActivity`.
4. **Vào ứng dụng chính**: Nhấn *"ĐĂNG NHẬP"* hoặc *"ĐĂNG KÝ"* thành công → Khởi chạy `MainActivity` và xóa back stack để tránh quay lại màn auth.

---

### 2.2. Luồng Điều hướng Chính (Main ViewPager2 & 5-Slot Bottom Bar)
1. **Tab mặc định khi vào App**: `MainActivity` khởi động ngay tại **Tab 2: Camera** đúng chuẩn trải nghiệm Locket.
2. **Thao tác chuyển Tab**:
   - **Cách 1: Vuốt ngang (Swipe Horizontal)**: Vuốt qua lại mượt mà giữa cả 4 tab `[Tab 0: Widget | Tab 1: Dashboard | Tab 2: Camera | Tab 3: Chat]` thông qua `ViewPager2`.
   - **Cách 2: Thanh Bottom Bar 5 Slot**:
     - **Slot 1 (Trái cùng)**: Tab 0 - Lưới Widget (`WidgetGridFragment`).
     - **Slot 2**: Tab 1 - Dashboard Thống kê (`DashboardFragment`).
     - **Slot 3 (Chính giữa)**: Tab 2 - Camera (`CameraFragment`) với nút tròn viền sáng accent.
     - **Slot 4**: Tab 3 - Trợ lý AI SET (`ChatFragment`).
     - **Slot 5 (Phải cùng)**: Bật ngay `ThemeColorBottomSheet` (Modal chọn 8 màu Neon chủ đạo).

---

### 2.3. Luồng Camera & Dòng thời gian chi tiêu (Vertical Timeline Flow)
1. Trong **Tab 2 (CameraFragment)**, màn hình sử dụng một `ViewPager2` cuộn dọc (`ORIENTATION_VERTICAL`):
   - **Trang 0**: Camera Preview trực tiếp tỉ lệ 3:4, nút Chụp Locket, nút Tải ảnh từ thư viện, nút Đổi camera trước/sau, gợi ý vuốt xuống xem lịch sử.
   - **Trang 1 ... N**: Lướt dọc xem các hóa đơn/bài đăng chi tiêu toàn màn hình phong cách Locket.
2. **Chụp / Tải ảnh mới**:
   - Nhấn nút Chụp hoặc chọn ảnh từ Gallery → Chuyển sang `ConfirmActivity`.
   - Xem lại ảnh, nhập caption đè lên ảnh bán trong suốt, bấm *"ĐĂNG"* để ghi nhận chi tiêu và tự động quay về timeline.

---

### 2.4. Luồng Thông báo (Notification Flow)
1. Tại Top Bar của `MainActivity`, nhấn vào **Icon Chuông thông báo**:
   - Mở `NotificationBottomSheet` hiển thị danh sách các thông báo phân loại (Cảnh báo chi tiêu, Lời khuyên AI, Nhắc nhở, Báo cáo).
   - Huy hiệu đỏ (Red Dot Badge) trên chuông sẽ tự động cập nhật khi có thông báo mới hoặc khi đã đọc hết.
2. **Tương tác thông báo**:
   - Nhấn nút *"Đọc tất cả"*: Đánh dấu toàn bộ là đã đọc và xóa huy hiệu đỏ.
   - Nhấn vào một thông báo cụ thể: Đánh dấu đã đọc thông báo đó, tự động đóng Bottom Sheet và **chuyển ngay đến Tab liên quan** (Ví dụ: bấm cảnh báo ngân sách → chuyển sang Tab Dashboard; bấm gợi ý AI → chuyển sang Tab Chat).

---

### 2.5. Luồng Hồ sơ & Chỉnh sửa Cá nhân (Profile & Edit Profile Flow)
1. Tại Top Bar của `MainActivity`, nhấn vào **Avatar**:
   - Mở `ProfileActivity` hiển thị thông tin người dùng, huy hiệu emoji, các chỉ số thống kê (🔥 Chuỗi ngày Streak, 📸 Tổng hóa đơn, 🎯 Hạn mức chi tiêu tháng).
2. **Chỉnh sửa Hồ sơ**:
   - Nhấn vào Avatar hoặc nút *"Chỉnh sửa hồ sơ"*: Mở `EditProfileBottomSheet`.
   - Chọn Emoji đại diện nhanh (⭐, 👑, 🚀, 🐱, 🔥...), nhập Tên mới, Email, SĐT, Hạn mức tháng.
   - Bấm *"LƯU THAY ĐỔI"*: Dữ liệu cập nhật ngay tức thì vào `UserManager` và đồng bộ trên toàn ứng dụng.
3. **Các tính năng phụ**:
   - Nhấn *"Bạn bè & Chia sẻ"*: Xem mã kết nối `SET-8899` và sao chép mã nhanh vào Clipboard.
   - Nhấn *"Bảo mật & Quyền riêng tư"*: Xem trạng thái mã hóa và bảo vệ dữ liệu.
   - Nhấn *"Đăng xuất"*: Hiển thị hộp thoại xác nhận, xóa session và đưa người dùng về `LoginActivity`.

---

### 2.6. Luồng Lọc Khoảng Ngày Thống kê (Date Range Filter Flow)
1. Tại **Tab 1 (DashboardFragment)**, nhấn vào nút lọc ngày:
   - Hiển thị pop-up / dialog chọn khoảng ngày (Từ ngày → Đến ngày) dạng layer năm → tháng → ngày gọn gàng.
   - Không dàn trải danh sách dài, giúp thao tác trực quan, tiết kiệm tài nguyên và mượt mà.
