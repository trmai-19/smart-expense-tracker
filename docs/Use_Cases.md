# Use Cases

> Tài liệu mô tả các luồng nghiệp vụ (use case) của hệ thống SET (Smart Expense Tracker).

---

## 1. Authentication (Xác thực người dùng)

### UC01: Đăng nhập (Login)
- **Actor**: Người dùng đã có tài khoản.
- **Mô tả**: Người dùng nhập Tên đăng nhập/Email và Mật khẩu để đăng nhập vào hệ thống.
- **Luồng chính**:
  1. Người dùng mở màn hình Đăng nhập `LoginActivity`.
  2. Nhập thông tin tài khoản và mật khẩu.
  3. Hệ thống xác thực và chuyển hướng mượt mà vào màn hình chính `MainActivity` (mặc định mở Camera).

### UC02: Đăng ký (Register)
- **Actor**: Người dùng mới.
- **Mô tả**: Tạo tài khoản mới với tên hiển thị, email và mật khẩu.
- **Luồng chính**:
  1. Người dùng chọn "Đăng ký ngay" từ màn hình Đăng nhập.
  2. Nhập Tên hiển thị, Email/Tên đăng nhập, Mật khẩu và Xác nhận mật khẩu.
  3. Hệ thống kiểm tra tính hợp lệ và khởi tạo tài khoản trong `UserManager`.
  4. Chuyển hướng trực tiếp vào màn hình chính `MainActivity`.

---

## 2. Expense Management & Capture (Quản lý & Ghi nhận chi tiêu)

### UC03: Chụp & Đăng chi tiêu phong cách Locket (Capture & Post Expense)
- **Actor**: Người dùng.
- **Mô tả**: Chụp ảnh hóa đơn hoặc bữa ăn/món đồ theo khung chuẩn 3:4, đè caption và đăng lên dòng thời gian.
- **Luồng chính**:
  1. Người dùng ở màn hình Camera Preview (Trang 0 của `CameraFragment`).
  2. Bấm nút Chụp Locket hoặc chọn ảnh từ Thư viện (Gallery).
  3. Chuyển sang màn hình `ConfirmActivity` xem trước ảnh.
  4. Nhập ghi chú/caption (ví dụ: "Cơm trưa văn phòng 50k") trên thanh bán trong suốt đè lên ảnh.
  5. Nhấn nút "ĐĂNG". Hệ thống sẽ gửi ảnh và caption lên AI (Gemini) để tự động phân tích ra "Danh mục" (tạo mới nếu chưa có) và "Số tiền", sau đó lưu chi tiêu vào lịch sử.

### UC04: Duyệt dòng thời gian chi tiêu (Vertical Timeline & Widget Grid)
- **Actor**: Người dùng.
- **Mô tả**: Xem lại các hóa đơn và bài đăng chi tiêu theo thứ tự thời gian.
- **Luồng chính**:
  1. Từ Camera, người dùng lướt ngón tay dọc xuống để xem từng trang bài đăng chi tiêu toàn màn hình.
  2. Mỗi trang hiển thị đầy đủ: Ảnh chụp 3:4, Caption trên ảnh, Avatar viền accent + Tên người dùng, và thông tin `Danh mục · Thời gian · Số tiền`.
  3. Người dùng cũng có thể chuyển sang Tab 0 (Lưới Widget) để xem danh sách thu nhỏ dạng album Locket.

---

## 3. Analytics & Reporting (Thống kê & Báo cáo)

### UC05: Thống kê chi tiêu & Lọc theo khoảng ngày (Dashboard & Date Filter)
- **Actor**: Người dùng.
- **Mô tả**: Theo dõi tổng tiền đã chi trong kỳ, xem phân bổ phần trăm theo danh mục và biểu đồ 7 ngày gần nhất.
- **Luồng chính**:
  1. Người dùng chọn Tab 1 (Dashboard) từ Bottom bar hoặc vuốt ngang.
  2. Xem tổng chi tiêu, tỷ lệ danh mục (Ăn uống, Mua sắm, Di chuyển...) và biểu đồ cột.
  3. Nhấn nút Bộ lọc ngày để mở hộp thoại chọn khoảng ngày (Từ ngày → Đến ngày).
  4. Hệ thống cập nhật lại các chỉ số và biểu đồ theo đúng khoảng thời gian đã chọn.

---

## 4. AI Financial Assistant (Trợ lý AI SET)

### UC06: Tư vấn tài chính thông minh (AI Chat Assistant)
- **Actor**: Người dùng.
- **Mô tả**: Trò chuyện với Trợ lý AI để nhận lời khuyên quản lý ngân sách và phân tích thói quen tiêu dùng.
- **Luồng chính**:
  1. Người dùng chọn Tab 3 (AI Chat) từ Bottom bar hoặc vuốt ngang.
  2. Gửi câu hỏi hoặc yêu cầu tư vấn (ví dụ: "Tháng này tôi nên tiết kiệm thế nào?").
  3. Trợ lý AI SET phản hồi tức thì với những phân tích hữu ích.
  4. Nền bong bóng chat của người dùng đồng bộ tự động theo màu chủ đạo của ứng dụng.

---

## 5. Notifications & Alerts (Thông báo & Cảnh báo)

### UC07: Nhận thông báo & Điều hướng thông minh (Notification System)
- **Actor**: Người dùng.
- **Mô tả**: Nhận cảnh báo hạn mức, lời khuyên tài chính, nhắc nhở định kỳ và điều hướng nhanh đến tab liên quan.
- **Luồng chính**:
  1. Người dùng nhìn thấy chấm đỏ trên chuông thông báo ở Top Bar.
  2. Nhấn vào Chuông để mở `NotificationBottomSheet`.
  3. Xem danh sách thông báo phân loại màu sắc và thời gian.
  4. Nhấn "Đọc tất cả" để xóa trạng thái chưa đọc, hoặc nhấn vào thông báo để chuyển ngay đến màn hình tương ứng (Dashboard, Chat, v.v.).

---

## 6. Personalization & Profile (Cá nhân hóa & Hồ sơ)

### UC08: Tùy biến màu chủ đạo Neon (Accent Color Theme)
- **Actor**: Người dùng.
- **Mô tả**: Thay đổi màu sắc giao diện theo sở thích với 8 gam màu Neon nổi bật.
- **Luồng chính**:
  1. Người dùng bấm nút "Đổi màu" ở slot thứ 5 trên Bottom Bar.
  2. Hộp thoại `ThemeColorBottomSheet` hiển thị bảng màu Neon.
  3. Chọn màu mong muốn → Toàn bộ nút bấm, viền avatar, số tiền, và điểm nhấn giao diện đổi màu lập tức.

### UC09: Quản lý Hồ sơ & Thiết lập Hạn mức (Profile & Budget Settings)
- **Actor**: Người dùng.
- **Mô tả**: Xem thống kê chuỗi ngày streak, tổng số hóa đơn, tải ảnh avatar từ thiết bị, đổi tên, email và cài đặt ngân sách tháng.
- **Luồng chính**:
  1. Người dùng bấm vào Avatar ở Top Bar để mở `ProfileActivity`.
  2. Xem các chỉ số: 🔥 Chuỗi Streak, 📸 Tổng hóa đơn, 🎯 Hạn mức tháng.
  3. Bấm "Chỉnh sửa hồ sơ" để mở `EditProfileBottomSheet`.
  4. Tải ảnh đại diện từ điện thoại, chỉnh sửa Tên, Email, SĐT và Hạn mức chi tiêu tháng.
  5. Bấm "Lưu thay đổi" để cập nhật dữ liệu vào `UserManager`.
  6. Ngoài ra có thể sao chép Mã kết nối bạn bè `SET-8899` hoặc Đăng xuất khỏi tài khoản.
