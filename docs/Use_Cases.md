# Use Cases

> Tài liệu mô tả các luồng nghiệp vụ (use case) của hệ thống SET (Smart Expense Tracker).

---

## 1. Authentication (Xác thực người dùng)

### UC01: Đăng nhập (Login) - `[Đã hoàn thành]`
- **Actor**: Người dùng đã có tài khoản.
- **Mô tả**: Người dùng nhập Tên đăng nhập/Email và Mật khẩu để đăng nhập vào hệ thống. *Ghi chú: Đã triển khai hoàn thiện hệ thống Access Token (24h) và Refresh Token (7 ngày) chống lỗi 403.*
- **Luồng chính**:
  1. Người dùng mở màn hình Đăng nhập `LoginActivity`.
  2. Nhập thông tin tài khoản và mật khẩu.
  3. Hệ thống xác thực và chuyển hướng mượt mà vào màn hình chính `MainActivity` (mặc định mở Camera).

### UC02: Đăng ký (Register) - `[Đã hoàn thành]`
- **Actor**: Người dùng mới.
- **Mô tả**: Tạo tài khoản mới với tên hiển thị, email và mật khẩu.
- **Luồng chính**:
  1. Người dùng chọn "Đăng ký ngay" từ màn hình Đăng nhập.
  2. Nhập Tên hiển thị, Email/Tên đăng nhập, Mật khẩu và Xác nhận mật khẩu.
  3. Hệ thống kiểm tra tính hợp lệ và khởi tạo tài khoản trong `UserManager`.
  4. Chuyển hướng trực tiếp vào màn hình chính `MainActivity`.

---

## 2. Expense Management & Capture (Quản lý & Ghi nhận chi tiêu)

### UC03: Chụp & Đăng chi tiêu phong cách Locket (Capture & Post Expense) - `[Đã hoàn thành]`
- **Actor**: Người dùng.
- **Mô tả**: Chụp ảnh hóa đơn hoặc bữa ăn/món đồ theo khung chuẩn 3:4, đè caption và đăng lên dòng thời gian.
- **Luồng chính**:
  1. Người dùng ở màn hình Camera Preview (Trang 0 của `CameraFragment`).
  2. Bấm nút Chụp Locket hoặc chọn ảnh từ Thư viện (Gallery).
  3. Chuyển sang màn hình `ConfirmActivity` xem trước ảnh.
  4. Nhập ghi chú/caption (ví dụ: "Cơm trưa văn phòng 50k") trên thanh bán trong suốt đè lên ảnh.
  5. Nhấn nút "ĐĂNG". Hệ thống sẽ gửi ảnh và caption lên AI (Gemini) để tự động phân tích ra "Danh mục" (tạo mới nếu chưa có) và "Số tiền", sau đó lưu chi tiêu vào lịch sử.

### UC04: Duyệt dòng thời gian chi tiêu (Vertical Timeline & Widget Grid) - `[Đã hoàn thành]`
- **Actor**: Người dùng.
- **Mô tả**: Xem lại các hóa đơn và bài đăng chi tiêu theo thứ tự thời gian.
- **Luồng chính**:
  1. Từ Camera, người dùng lướt ngón tay dọc xuống để xem từng trang bài đăng chi tiêu toàn màn hình.
  2. Mỗi trang hiển thị đầy đủ: Ảnh chụp 3:4, Caption trên ảnh, Avatar viền accent + Tên người dùng, và thông tin `Danh mục · Thời gian · Số tiền`.
  3. Người dùng cũng có thể chuyển sang Tab 0 (Lưới Widget) để xem danh sách thu nhỏ dạng album Locket.

---

## 3. Analytics & Reporting (Thống kê & Báo cáo)

### UC05: Thống kê chi tiêu & Lọc theo khoảng ngày (Dashboard & Date Filter) - `[Đã hoàn thành]`
- **Actor**: Người dùng.
- **Mô tả**: Theo dõi tổng tiền đã chi trong kỳ, xem phân bổ phần trăm theo danh mục và biểu đồ 7 ngày gần nhất.
- **Luồng chính**:
  1. Người dùng chọn Tab 1 (Dashboard) từ Bottom bar hoặc vuốt ngang.
  2. Xem tổng chi tiêu, tỷ lệ danh mục (Ăn uống, Mua sắm, Di chuyển...) và biểu đồ cột.
  3. Nhấn nút Bộ lọc ngày để mở hộp thoại chọn khoảng ngày (Từ ngày → Đến ngày).
  4. Hệ thống cập nhật lại các chỉ số và biểu đồ theo đúng khoảng thời gian đã chọn.

---

## 4. AI Financial Assistant (Trợ lý AI SET)

### UC06: Tư vấn tài chính thông minh (AI Chat Assistant) - `[Đã hoàn thành]`
- **Actor**: Người dùng.
- **Mô tả**: Trò chuyện với Trợ lý AI để nhận lời khuyên quản lý ngân sách và phân tích thói quen tiêu dùng. *Ghi chú: Đã fix triệt để lỗi AI trả lời bị cắt ngang ở tiếng Việt (đã tối ưu `maxOutputTokens=4096`).*
- **Luồng chính**:
  1. Người dùng chọn Tab 3 (AI Chat) từ Bottom bar hoặc vuốt ngang.
  2. Gửi câu hỏi hoặc yêu cầu tư vấn (ví dụ: "Tháng này tôi nên tiết kiệm thế nào?").
  3. Trợ lý AI SET phản hồi tức thì với những phân tích hữu ích.
  4. Nền bong bóng chat của người dùng đồng bộ tự động theo màu chủ đạo của ứng dụng.

---

## 5. Notifications & Alerts (Thông báo & Cảnh báo)

### UC07: Nhận thông báo & Điều hướng thông minh (Notification System) - `[Chưa triển khai]`
- **Trạng thái**: Đã chốt requirements, chờ Dev tiếp theo code.
- **Actor**: Người dùng.
- **Mô tả**: Hệ thống tự động gửi các thông báo (Local Notification) dựa trên hành vi chi tiêu của người dùng, giúp nhắc nhở và cảnh báo hạn mức.
- **Các loại thông báo tự động**:
  1. **Nhắc nhở Streak hằng ngày (Daily Reminder):** 
     - Gửi vào lúc 20:00 mỗi tối với nội dung: *"Bạn chưa ghi chép chi tiêu hôm nay! Hãy dành 1 phút để cập nhật nhé để không mất Streak 🔥"*
     - Chỉ gửi nếu hệ thống nhận thấy trong ngày hôm đó user chưa thêm bất kỳ khoản chi tiêu nào.
  2. **Cảnh báo vượt hạn mức ngày (Daily Budget Alert):**
     - Hệ thống tự tính `Hạn mức mỗi ngày = Ngân sách tháng / Số ngày trong tháng`.
     - Nếu tổng chi tiêu trong 1 ngày bất kỳ vượt quá Hạn mức mỗi ngày này, gửi thông báo cảnh báo chi tiêu lố trong ngày.
  3. **Cảnh báo vượt 80% ngân sách tháng (Monthly Budget Alert):**
     - Gửi khi tổng chi tiêu chạm mốc 80% của ngân sách tháng.
     - **Điều kiện khắt khe:** Chỉ gửi cảnh báo này NẾU `(Số tiền còn lại / Số ngày còn lại trong tháng) < Hạn mức mỗi ngày`. (Tránh spam thông báo nếu user đạt 80% nhưng là vào những ngày cuối cùng của tháng, khi mà số tiền chia đều cho các ngày còn lại vẫn ở mức an toàn).
- **Luồng chính**:
  1. Tới chu kỳ hoặc khi có sự kiện trigger, hệ thống hiển thị thông báo bằng `WorkManager`.
  2. Người dùng nhìn thấy chấm đỏ trên chuông thông báo ở Top Bar.
  3. Nhấn vào Chuông để mở `NotificationBottomSheet`.
  4. Xem danh sách thông báo phân loại màu sắc và thời gian.
  5. Nhấn "Đọc tất cả" để xóa trạng thái chưa đọc, hoặc nhấn vào thông báo để chuyển ngay đến màn hình tương ứng.

---

## 6. Personalization & Profile (Cá nhân hóa & Hồ sơ)

### UC08: Tùy biến màu chủ đạo Neon (Accent Color Theme) - `[Đã hoàn thành]`
- **Actor**: Người dùng.
- **Mô tả**: Thay đổi màu sắc giao diện theo sở thích với 8 gam màu Neon nổi bật.
- **Luồng chính**:
  1. Người dùng bấm nút "Đổi màu" ở slot thứ 5 trên Bottom Bar.
  2. Hộp thoại `ThemeColorBottomSheet` hiển thị bảng màu Neon.
  3. Chọn màu mong muốn → Toàn bộ nút bấm, viền avatar, số tiền, và điểm nhấn giao diện đổi màu lập tức.

### UC09: Quản lý Hồ sơ & Thiết lập Hạn mức (Profile & Budget Settings) - `[Chưa triển khai tính năng đồng bộ]`
- **Trạng thái**: UI cơ bản đã hoàn thành. Chờ Dev tiếp theo làm API tính Streak và API lưu Budget.
- **Actor**: Người dùng.
- **Mô tả**: Xem thống kê chuỗi ngày streak, tổng số hóa đơn, tải ảnh avatar từ thiết bị, đổi tên, email và cài đặt ngân sách tháng.
- **Tính toán Chuỗi (Streak)**: Streak được tính toán mỗi khi user thêm một hóa đơn chi tiêu:
  - Nếu hóa đơn gần nhất là *hôm qua* -> Tăng Streak thêm 1.
  - Nếu hóa đơn gần nhất là *hôm nay* -> Giữ nguyên.
  - Nếu hóa đơn gần nhất là *trước hôm qua* -> Reset Streak về 1.
- **Luồng chính**:
  1. Người dùng bấm vào Avatar ở Top Bar để mở `ProfileActivity`.
  2. Xem các chỉ số: 🔥 Chuỗi Streak, 📸 Tổng hóa đơn, 🎯 Hạn mức tháng.
  3. Bấm "Chỉnh sửa hồ sơ" để mở `EditProfileBottomSheet`.
  4. Tải ảnh đại diện từ điện thoại, chỉnh sửa Tên, Email, SĐT và Hạn mức chi tiêu tháng.
  5. Bấm "Lưu thay đổi". Dữ liệu Hạn mức ngân sách (Budget) và các thông tin khác sẽ được **gọi API đồng bộ lưu thẳng xuống Database của Backend**, đảm bảo tính toàn vẹn dữ liệu.
  6. Ngoài ra có thể sao chép Mã kết nối bạn bè `SET-8899` hoặc Đăng xuất khỏi tài khoản.
