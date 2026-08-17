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
│   ├── login/                     # LoginActivity (Đăng nhập, branding SET, transition mượt mà)
│   └── register/                  # RegisterActivity (Đăng ký tài khoản mới)
├── camera/
│   ├── CameraFragment.java        # CameraX 3:4 viewfinder, Shutter Locket, vertical swipe timeline
│   ├── TimelineFeedAdapter.java   # Vertical feed adapter: Trang 0 là Camera Preview, Trang 1...N là lịch sử
│   ├── LocketFeedAdapter.java     # Feed adapter tiện ích
│   └── confirm/
│       └── ConfirmActivity.java    # Màn hình xác nhận ảnh + viết caption đè + nút ĐĂNG
├── widget/
│   ├── WidgetGridFragment.java    # Tab 0: Lưới widget chi tiêu Locket
│   └── WidgetGridAdapter.java     # Adapter lưới album chi tiêu
├── dashboard/
│   ├── DashboardFragment.java     # Tab 1: Tổng chi tiêu, bộ lọc ngày từ ngày -> đến ngày, biểu đồ 7 ngày
│   └── DateRangePickerBottomSheet # Dialog chọn khoảng ngày bắt đầu - kết thúc
├── chat/
│   ├── ChatFragment.java          # Tab 3: Trợ lý AI tư vấn tài chính, keyboard insets handling
│   ├── ChatAdapter.java           # Adapter bong bóng chat (Bot surface & User theme accent bubble)
│   └── ChatMessage.java           # Model tin nhắn chat
├── notification/
│   ├── NotificationManager.java   # Quản lý dữ liệu thông báo & số lượng chưa đọc
│   ├── NotificationBottomSheet.java # Bottom sheet danh sách thông báo & điều hướng tab
│   └── NotificationAdapter.java   # Adapter thẻ thông báo phân loại màu sắc
├── profile/
│   ├── ProfileActivity.java       # Màn hình hồ sơ, thống kê Streak/Hóa đơn/Ngân sách, menu chức năng
│   └── EditProfileBottomSheet.java# Bottom sheet đổi tên, email, tải ảnh avatar, hạn mức tháng
├── history/
│   ├── HistoryFullscreenAdapter.java # Adapter xem lại lịch sử chi tiết
│   └── ExpenseHistoryItem.java    # Model dữ liệu item lịch sử
├── main/
│   ├── MainActivity.java          # Host Activity: Top bar cố định, ViewPager2 4 tab, Bottom bar 5 slot
│   └── MainPagerAdapter.java     # Adapter 4 tab: [0: Widget | 1: Dashboard | 2: Camera | 3: Chat]
└── util/
    ├── ThemeManager.java          # Quản lý & áp dụng Accent Color động
    ├── UserManager.java           # Lưu trữ tên, email, URL ảnh avatar, hạn mức ngân sách tháng
    └── ThemeColorBottomSheet.java # Modal bảng màu Neon cá nhân hóa
```

---

## 3. Danh sách màn hình & Thành phần UI

| Màn hình / Component | Layout XML | Controller Java | Mô tả tính năng |
|---|---|---|---|
| **Đăng nhập** | `activity_login.xml` | `LoginActivity.java` | Tên app "Smart Expense Tracker", form đăng nhập, hiệu ứng chuyển cảnh mượt mà |
| **Đăng ký** | `activity_register.xml` | `RegisterActivity.java` | Form tạo tài khoản, xác nhận mật khẩu, đồng bộ tên hiển thị ban đầu |
| **Màn hình chính (Host)** | `activity_main.xml` | `MainActivity.java` | Top bar (Avatar, Tên app/Bộ lọc, Chuông thông báo + Red badge), ViewPager2 4 tab ngang, Bottom bar 5 slot |
| **Tab 0: Lưới Widget** | `fragment_widget_grid.xml` | `WidgetGridFragment.java` | Lưới ảnh widget chi tiêu 3:4 phong cách Locket, xem lại ảnh, lọc danh mục |
| **Tab 1: Thống kê** | `fragment_dashboard.xml` | `DashboardFragment.java` | Tổng chi tiêu, phân bổ %, biểu đồ 7 cột, bộ lọc khoảng ngày linh hoạt |
| **Tab 2: Camera & Timeline** | `fragment_camera.xml`, `item_feed_camera.xml`, `item_history_fullscreen.xml` | `CameraFragment.java`, `TimelineFeedAdapter.java` | Trang 0: Camera preview 3:4 + nút chụp Locket viền đôi; Trang 1..N: Cuộn dọc xem từng chi tiêu kèm tên người dùng và avatar viền accent |
| **Tab 3: Trợ lý AI SET** | `fragment_chat.xml`, `item_chat_message.xml` | `ChatFragment.java`, `ChatAdapter.java` | Nhắn tin tư vấn tài chính, nền tin nhắn người dùng đổi theo theme, tự động ẩn bottom bar khi mở bàn phím |
| **Bảng Thông báo** | `bottom_sheet_notifications.xml`, `item_notification.xml` | `NotificationBottomSheet.java`, `NotificationAdapter.java` | Danh sách thông báo (Cảnh báo hạn mức, Lời khuyên AI, Nhắc nhở, Báo cáo), nút Đọc tất cả, chuyển tab thông minh khi chạm |
| **Hồ sơ Cá nhân** | `activity_profile.xml` | `ProfileActivity.java` | Avatar lớn viền accent, thẻ thống kê (🔥 Streak, 📸 Hóa đơn, 🎯 Ngân sách), menu Bạn bè/Mã SET, Bảo mật, Đăng xuất |
| **Chỉnh sửa Hồ sơ** | `bottom_sheet_edit_profile.xml` | `EditProfileBottomSheet.java` | Đổi tên hiển thị, email, số điện thoại, tải ảnh đại diện từ thiết bị, cài đặt hạn mức chi tiêu tháng |
| **Bảng chọn màu** | `bottom_sheet_color_palette.xml` | `ThemeColorBottomSheet.java` | Bottom Sheet chọn nhanh 8 màu Neon chủ đạo cho toàn hệ thống |
| **Xác nhận chi tiêu** | `activity_confirm.xml` | `ConfirmActivity.java` | Xem lại ảnh 3:4 đã chụp/chọn, nhập caption đè lên ảnh, xác nhận đăng bài |

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
- Mọi màn hình nhập liệu hoặc chi tiết (`LoginActivity`, `RegisterActivity`, `ProfileActivity`, `DashboardFragment`, `EditProfileBottomSheet`) đều được bao bọc trong `ScrollView` với thuộc tính `android:fillViewport="true"`.
- Khi bàn phím ảo xuất hiện trên màn hình nhỏ, toàn bộ giao diện tự động cuộn mượt mà, không che khuất các ô nhập liệu hoặc nút bấm xác nhận.
- `RecyclerView` trong các BottomSheet được cấu hình `android:layout_height="wrap_content"` và `android:nestedScrollingEnabled="true"` để hoạt động hoàn hảo trên mọi độ phân giải.

