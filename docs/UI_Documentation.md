# UI Documentation

> Tài liệu mô tả toàn diện giao diện người dùng và thiết kế của ứng dụng Android SET (Smart Expense Tracker) theo phong cách tối giản Locket.

---

## 1. Nguyên tắc thiết kế (Design Principles)

- **Pure Dark Mode (#0D0D0D / #1A1A1A / #1E1E1E)**: Toàn bộ ứng dụng chạy trên nền OLED tối sang trọng, độ tương phản cao, tối ưu hiển thị.
- **Tỷ lệ ảnh chuẩn 3:4 với góc bo lớn (`28dp`)**: Khung hình chụp hóa đơn & lịch sử chi tiêu mô phỏng nguyên bản trải nghiệm Locket widget.
- **Caption Overlaid trên ảnh**: Chữ ghi chú được đặt trực tiếp trên một thanh bán trong suốt (`bg_caption_overlay`) đè ở góc dưới bức ảnh.
- **Dòng thời gian dọc & Lưới Widget**:
  - Lịch sử chi tiêu hiển thị dạng cuộn dọc toàn màn hình (Vertical ViewPager2) kèm thông tin người dùng, avatar đồng bộ màu theme, thông tin chi tiêu (`Danh mục · Thời gian · Số tiền`).
  - Lưới widget (Tab 0) hỗ trợ xem tổng quan dạng album lưới Locket và lọc theo danh mục.
- **Hệ thống Màu chủ đạo Neon (Accent Color Palette)**: Người dùng có thể tùy biến màu sắc thương hiệu hiển thị tức thì trên toàn bộ ứng dụng qua `ThemeColorBottomSheet`:
  1. Vàng Cyber Neon (`#FFE600`) - Mặc định
  2. Xanh Lime Neon (`#00FF87`)
  3. Xanh Cyan Neon (`#00F0FF`)
  4. Hồng Neon (`#FF80BF`)
  5. Tím Lilac Neon (`#C77DFF`)
  6. Cam Blaze Neon (`#FF6B00`)
  7. Xanh Mint Neon (`#00FFCC`)
  8. Đỏ Coral Neon (`#FF3366`)

---

## 2. Cấu trúc thư mục Presentation Layer

```
presentation/
├── auth/
│   ├── login/                     # LoginScreen.kt (Đăng nhập, branding SET, transition mượt mà)
│   └── register/                  # RegisterScreen.kt (Đăng ký tài khoản mới)
├── camera/
│   ├── CameraScreen.kt            # CameraX 3:4 viewfinder, Shutter Locket, vertical swipe timeline
│   ├── TimelineFeed.kt            # Vertical feed UI: Trang 0 là Camera Preview, Trang 1...N là lịch sử
│   ├── LocketFeed.kt              # Feed UI tiện ích
│   └── confirm/
│       └── ConfirmScreen.kt       # Màn hình xác nhận ảnh + viết caption đè + nút ĐĂNG
├── widget/
│   ├── WidgetGridScreen.kt        # Tab 0: Lưới widget chi tiêu Locket
│   └── WidgetGrid.kt              # UI lưới album chi tiêu
├── dashboard/
│   ├── DashboardScreen.kt         # Tab 1: Tổng chi tiêu, bộ lọc ngày, biểu đồ 7 ngày
│   └── DateRangePickerBottomSheet.kt # Dialog chọn khoảng ngày
├── chat/
│   ├── ChatScreen.kt              # Tab 3: Trợ lý AI tư vấn tài chính, keyboard insets handling
│   ├── ChatBubble.kt              # UI bong bóng chat (Bot surface & User theme accent bubble)
│   └── ChatMessage.kt             # Model tin nhắn chat
├── notification/
│   ├── NotificationManager.kt     # Quản lý dữ liệu thông báo & số lượng chưa đọc
│   ├── NotificationBottomSheet.kt # Bottom sheet danh sách thông báo & điều hướng tab
│   └── NotificationList.kt        # UI thẻ thông báo phân loại màu sắc
├── profile/
│   ├── ProfileScreen.kt           # Màn hình hồ sơ, thống kê Streak/Hóa đơn/Ngân sách, menu chức năng
│   └── EditProfileBottomSheet.kt  # Bottom sheet đổi tên, email, tải ảnh avatar, hạn mức tháng
├── history/
│   ├── HistoryFullscreen.kt       # UI xem lại lịch sử chi tiết
│   └── ExpenseHistoryItem.kt      # Model dữ liệu item lịch sử
├── navigation/
│   ├── AppNavHost.kt              # Host điều hướng chính: Top bar cố định, Navigation logic, Bottom bar
│   └── Screen.kt                  # Định nghĩa các tuyến đường
└── util/
    ├── ThemeManager.kt            # Quản lý & áp dụng Accent Color động
    ├── UserManager.kt             # Lưu trữ tên, email, URL ảnh avatar, hạn mức ngân sách tháng
    └── ThemeColorBottomSheet.kt   # Modal bảng màu Neon cá nhân hóa
```

---

## 3. Danh sách màn hình & Thành phần UI

| Màn hình / Component | Compose UI | Mô tả tính năng |
|---|---|---|
| **Đăng nhập** | `LoginScreen.kt` | Tên app "Smart Expense Tracker", form đăng nhập với `TextField` viền focus động tinh tế chống lỗi render nhãn, chuyển cảnh mượt mà |
| **Đăng ký** | `RegisterScreen.kt` | Form tạo tài khoản, xác nhận mật khẩu, đồng bộ tên hiển thị ban đầu, sử dụng `TextField` viền focus động |
| **Màn hình chính (Host)** | `AppNavHost.kt` | Top bar (Avatar, Tên app/Bộ lọc, Chuông thông báo), Navigation Host, Bottom bar 5 slot cách đều nhau với nền mờ Glassmorphism |
| **Tab 0: Lưới Widget** | `WidgetGridScreen.kt` | Lưới ảnh widget chi tiêu 3:4 phong cách Locket (Dùng `Coil` tải ảnh & đồng bộ API thật), xem lại ảnh, lọc danh mục |
| **Tab 1: Thống kê** | `DashboardScreen.kt` | Tổng chi tiêu, phân bổ %, biểu đồ 7 cột, bộ lọc khoảng ngày linh hoạt |
| **Tab 2: Camera & Timeline** | `CameraScreen.kt`, `TimelineFeed.kt` | Trang 0: Camera preview 3:4, nút chụp trong suốt lõi trắng mờ viền neon, hint "Lịch sử" trắng đậm; Trang 1..N: Lịch sử dọc |
| **Tab 3: Trợ lý AI SET** | `ChatScreen.kt`, `ChatBubble.kt` | Nhắn tin tư vấn tài chính, nền tin nhắn người dùng đổi theo theme, tự động ẩn bottom bar khi mở bàn phím |
| **Bảng Thông báo** | `NotificationBottomSheet.kt` | Danh sách thông báo (Cảnh báo hạn mức, Lời khuyên AI, Nhắc nhở, Báo cáo), nút Đọc tất cả, chuyển tab thông minh khi chạm |
| **Hồ sơ Cá nhân** | `ProfileScreen.kt` | Avatar lớn viền accent, thẻ thống kê (🔥 Streak, 📸 Hóa đơn, 🎯 Ngân sách), menu Bạn bè/Mã SET, Bảo mật, Đăng xuất (Đảm bảo bảng mã UTF-8 chống lỗi font) |
| **Chỉnh sửa Hồ sơ** | `EditProfileBottomSheet.kt` | Đổi tên hiển thị, email, số điện thoại, tải ảnh đại diện từ thiết bị, cài đặt hạn mức chi tiêu tháng |
| **Bảng chọn màu** | `ThemeColorBottomSheet.kt` | Bottom Sheet chọn nhanh 8 màu Neon chủ đạo cho toàn hệ thống |
| **Xác nhận chi tiêu** | `ConfirmScreen.kt` | Xem lại ảnh 3:4 đã chụp/chọn, nhập caption đè lên ảnh, xác nhận đăng bài |

---

## 4. Cơ chế Đảm bảo Tương thích Đa Thiết bị & Không Lệch Form (Responsive Strategy)

Nhằm đảm bảo ứng dụng **không bị lệch form, vỡ layout, tràn màn hình hoặc mất chữ** trên bất kỳ thiết bị Android nào (từ điện thoại màn hình nhỏ 16:9, màn hình siêu dài 20:9/21:9, màn hình gập Foldables, máy tính bảng Tablets cho đến các cài đặt kích thước chữ Accessibility lớn), hệ thống áp dụng bộ nguyên tắc chuẩn hóa giao diện:

### 4.1. Khóa tỷ lệ khung hình 3:4 an toàn với 2 chiều Constraint
- Sử dụng `app:layout_constraintDimensionRatio="3:4"` kết hợp đồng thời `app:layout_constrainedHeight="true"` và `app:layout_constrainedWidth="true"`.
- Trên các màn hình ngắn (16:9), card ảnh và viewfinder tự động co tỉ lệ theo chiều cao khả dụng mà không bao giờ đẩy các nút điều khiển (Shutter, Thư viện, Lật camera) hoặc thanh tóm tắt thông tin ra ngoài màn hình.
- Trên các màn hình dài (19.5:9, 20:9), khung 3:4 tận dụng tối đa chiều rộng và tự động giữ khoảng cách cân đối ở phần đáy.

### 4.2. Chống mất chữ & Tràn nội dung (Text Overflow Protection)
- **Tên người dùng & Header**: Thiết lập `android:maxLines="1"`, `android:ellipsize="end"` cùng giới hạn `maxWidth` linh hoạt để tên người dùng dài không đẩy tràn các biểu tượng xung quanh.
- **Dòng thông tin chi tiêu (`Danh mục · Thời gian · Số tiền`)**: Thiết lập `android:maxLines="2"`, `android:ellipsize="end"` và `android:paddingStart/End` an toàn, căn giữa chuẩn xác, không bị cắt bớt số tiền khi người dùng tăng kích thước font hệ thống.
- **Bong bóng chat & Thông báo**: Sử dụng `layout_width="0dp"` với `app:layout_constraintStart/End` hoặc `layout_weight="1"` giúp text tự động xuống dòng linh hoạt theo bề rộng màn hình thiết bị.

### 4.3. Bảo vệ Cuộn Viewport (Scrolling & Viewport Integrity)
- Mọi màn hình nhập liệu hoặc chi tiết (`LoginScreen`, `RegisterScreen`, `ProfileScreen`, `DashboardScreen`, `EditProfileBottomSheet`) đều được bao bọc trong scroll modifier (`verticalScroll`) để hỗ trợ cuộn linh hoạt.
- Khi bàn phím ảo xuất hiện trên màn hình nhỏ, toàn bộ giao diện tự động cuộn mượt mà, không che khuất các ô nhập liệu hoặc nút bấm xác nhận.
- Sử dụng `LazyColumn` / `LazyRow` / `HorizontalPager` / `VerticalPager` trong các màn hình danh sách và BottomSheet để tối ưu hóa hiệu năng hiển thị và hỗ trợ cuộn lồng nhau (nested scrolling).

