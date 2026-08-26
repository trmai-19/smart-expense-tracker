# Navigation Flow

> Tài liệu mô tả chi tiết toàn bộ luồng điều hướng (Navigation Flow) trong ứng dụng Android SET.

---

## 1. Sơ đồ luồng tổng quan (Navigation Architecture)

```
[Khởi động App]
       │
       ▼
[LoginScreen] ──(Chưa có tài khoản)──► [RegisterScreen]
       │                                         │
 (Đăng nhập OK)                           (Đăng ký OK)
       │                                         │
       └──────────────────┬──────────────────────┘
                          │
                          ▼
                   [AppNavHost]
  ┌───────────────────────┴───────────────────────┐
  ▼                                               ▼
[Top Bar: Cố định]                       [Bottom Bar: 5 Slot]
 ├─ Avatar ──► [ProfileScreen]            ├─ Slot 1 ──► [Tab 0: Widget Grid]
 │                 │                      ├─ Slot 2 ──► [Tab 1: Dashboard]
 │                 ├─ (Chỉnh sửa)         ├─ Slot 3 ──► [Tab 2: Camera (Mặc định)]
 │                 │    └──► [EditProfile]├─ Slot 4 ──► [Tab 3: AI Chat]
 │                 └─ (Đăng xuất)         └─ Slot 5 ──► [ThemeColorBottomSheet]
 │                      └──► [LoginScreen]
 └─ Chuông ──► [NotificationBottomSheet]
                   │
                   └──(Nhấn thông báo)──► Chuyển ngay đến Tab tương ứng
```

---

## 2. Chi tiết các luồng chuyển màn hình (Screen Transitions)

### 2.1. Luồng Xác thực (Authentication Flow)
1. **Khởi động**: Ứng dụng mở `LoginScreen`.
2. **Chuyển sang Đăng ký**: Nhấn *"Chưa có tài khoản? Đăng ký ngay"* → Điều hướng đến `RegisterScreen` với animation mượt mà.
3. **Quay lại Đăng nhập**: Nhấn nút Back hoặc *"Đã có tài khoản? Đăng nhập"* → Đóng `RegisterScreen` (pop back stack).
4. **Vào ứng dụng chính**: Nhấn *"ĐĂNG NHẬP"* hoặc *"ĐĂNG KÝ"* thành công → Khởi chạy luồng chính trong `AppNavHost` và xóa back stack để tránh quay lại màn auth.

---

### 2.2. Luồng Điều hướng Chính (Main ViewPager2 & 5-Slot Bottom Bar)
1. **Tab mặc định khi vào App**: Màn hình chính khởi động ngay tại **Tab 2: Camera** đúng chuẩn trải nghiệm Locket.
2. **Thao tác chuyển Tab**:
   - **Cách 1: Vuốt ngang (Swipe Horizontal)**: Vuốt qua lại mượt mà giữa cả 4 tab `[Tab 0: Widget | Tab 1: Dashboard | Tab 2: Camera | Tab 3: Chat]` thông qua `HorizontalPager`.
   - **Cách 2: Thanh Bottom Bar 5 Slot**:
     - **Slot 1 (Trái cùng)**: Tab 0 - Lưới Widget (`WidgetGridScreen`).
     - **Slot 2**: Tab 1 - Dashboard Thống kê (`DashboardScreen`).
     - **Slot 3 (Chính giữa)**: Tab 2 - Camera (`CameraScreen`) với nút tròn viền sáng accent.
     - **Slot 4**: Tab 3 - Trợ lý AI SET (`ChatScreen`).
     - **Slot 5 (Phải cùng)**: Bật ngay `ThemeColorBottomSheet` (Modal chọn 8 màu Neon chủ đạo).

---

### 2.3. Luồng Camera & Dòng thời gian chi tiêu (Vertical Timeline Flow)
1. Trong **Tab 2 (CameraScreen)**, màn hình sử dụng `VerticalPager` cuộn dọc:
   - **Trang 0**: Camera Preview trực tiếp tỉ lệ 3:4, nút Chụp Locket, nút Tải ảnh từ thư viện, nút Đổi camera trước/sau, gợi ý vuốt xuống xem lịch sử.
   - **Trang 1 ... N**: Lướt dọc xem các hóa đơn/bài đăng chi tiêu toàn màn hình phong cách Locket.
2. **Chụp / Tải ảnh mới**:
   - Nhấn nút Chụp hoặc chọn ảnh từ Gallery → Chuyển sang `ConfirmScreen`.
   - Xem lại ảnh, nhập caption đè lên ảnh bán trong suốt, bấm *"ĐĂNG"* để ghi nhận chi tiêu và tự động quay về timeline.

---

### 2.4. Luồng Thông báo (Notification Flow)
1. Tại Top Bar của màn hình chính, nhấn vào **Icon Chuông thông báo**:
   - Mở `NotificationBottomSheet` hiển thị danh sách các thông báo phân loại (Cảnh báo chi tiêu, Lời khuyên AI, Nhắc nhở, Báo cáo).
   - Huy hiệu đỏ (Red Dot Badge) trên chuông sẽ tự động cập nhật khi có thông báo mới hoặc khi đã đọc hết.
2. **Tương tác thông báo**:
   - Nhấn nút *"Đọc tất cả"*: Đánh dấu toàn bộ là đã đọc và xóa huy hiệu đỏ.
   - Nhấn vào một thông báo cụ thể: Đánh dấu đã đọc thông báo đó, tự động đóng Bottom Sheet và **chuyển ngay đến Tab liên quan** (Ví dụ: bấm cảnh báo ngân sách → chuyển sang Tab Dashboard; bấm gợi ý AI → chuyển sang Tab Chat).

---

### 2.5. Luồng Hồ sơ & Chỉnh sửa Cá nhân (Profile & Edit Profile Flow)
1. Tại Top Bar của màn hình chính, nhấn vào **Avatar**:
   - Mở `ProfileScreen` hiển thị thông tin người dùng, ảnh đại diện, các chỉ số thống kê (🔥 Chuỗi ngày Streak, 📸 Tổng hóa đơn, 🎯 Hạn mức chi tiêu tháng).
2. **Chỉnh sửa Hồ sơ**:
   - Nhấn vào Avatar hoặc nút *"Chỉnh sửa hồ sơ"*: Mở `EditProfileBottomSheet`.
   - Tải ảnh đại diện (avatar) từ thiết bị, nhập Tên mới, Email, SĐT, Hạn mức tháng.
   - Bấm *"LƯU THAY ĐỔI"*: Dữ liệu cập nhật ngay tức thì vào `UserManager` và đồng bộ trên toàn ứng dụng.
3. **Các tính năng phụ**:
   - Nhấn *"Bạn bè & Chia sẻ"*: Xem mã kết nối `SET-8899` và sao chép mã nhanh vào Clipboard.
   - Nhấn *"Bảo mật & Quyền riêng tư"*: Xem trạng thái mã hóa và bảo vệ dữ liệu.
   - Nhấn *"Đăng xuất"*: Hiển thị hộp thoại xác nhận, xóa session và đưa người dùng về `LoginScreen`.

---

### 2.6. Luồng Lọc Khoảng Ngày Thống kê (Date Range Filter Flow)
1. Tại **Tab 1 (DashboardScreen)**, nhấn vào nút lọc ngày:
   - Hiển thị pop-up / dialog chọn khoảng ngày (Từ ngày → Đến ngày) dạng layer năm → tháng → ngày gọn gàng.
   - Không dàn trải danh sách dài, giúp thao tác trực quan, tiết kiệm tài nguyên và mượt mà.
