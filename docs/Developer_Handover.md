# Bàn giao dự án (Developer Handover)

> Tài liệu này mô tả thực trạng hệ thống ở thời điểm hiện tại và các công việc cần làm tiếp theo dành cho lập trình viên nhận bàn giao.

## 1. Các công việc ĐÃ HOÀN THÀNH gần đây

1. **Sửa lỗi AI Chat bị cắt ngang (Gemini Output Tokens)**
   - **Tình trạng:** Tiếng Việt tốn rất nhiều tokens. Các câu trả lời dài thường bị cắt ngang nửa chừng.
   - **Cách giải quyết:** Đã cập nhật `maxOutputTokens` trong `GeminiAiAdapter.kt` từ `1024` lên `4096`. 
   - **Data truyền vào AI:** Giữ nguyên việc gửi toàn bộ danh sách chi tiêu chi tiết (không group by) để đảm bảo AI có thể trả lời các câu hỏi cực kỳ chi tiết cho mục đích báo cáo đồ án.

2. **Triển khai Refresh Token (Fix lỗi Login ảo 403)**
   - **Tình trạng:** Khi Access Token hết hạn sau 24h, người dùng mở app vẫn thấy giao diện nhưng gọi API bị báo lỗi HTTP 403.
   - **Cách giải quyết:** Đã viết API `/api/auth/refresh` ở Backend. Trên Android (Ktor/OkHttp), đã sử dụng `TokenAuthenticator` để tự động chặn các request lỗi 401/403, gọi API refresh token lấy token mới, sau đó tự động gửi lại request bị lỗi. Trải nghiệm người dùng nay đã liền mạch.

3. **Cập nhật Docs**
   - Đã cập nhật lại `Use_Cases.md`, đánh dấu rõ ràng các logic nghiệp vụ đã chốt cho Profile, Streak, Hạn mức, và Notification.

---

## 2. Các công việc CHƯA TRIỂN KHAI (Dành cho Dev tiếp theo)

Đây là những yêu cầu đã được **chốt logic** nhưng chưa viết code.

### 2.1. Đồng bộ Hạn mức chi tiêu (Monthly Budget)
- **Frontend (`EditProfileSheet.kt`)**: Hiện tại nút Lưu chỉ đang lưu vào local (`UserManager.setMonthlyBudget`). Cần sửa lại để gọi API `profileViewModel.updateProfile(...)` truyền thông tin này lên server.
- **Backend**: Entity `UserEntity` đã có sẵn cột `monthlyBudget`. Cần đảm bảo API Update Profile có nhận và lưu trường này.

### 2.2. Tính toán Chuỗi ngày (Streak Days) tự động
- **Logic chốt:** Không dùng Cron job. Streak sẽ được tính toán trực tiếp bên trong `ExpenseUseCaseImpl.createExpense`.
- **Cách làm:** Khi user thêm 1 khoản chi tiêu mới, Backend query khoản chi tiêu gần nhất:
  - Nếu là *hôm qua* -> `user.streakDays += 1`.
  - Nếu là *hôm nay* -> Bỏ qua.
  - Nếu là *trước hôm qua* -> `user.streakDays = 1`.
  - Lưu lại `UserEntity`.

### 2.3. Hệ thống Local Notification bằng WorkManager
- **Giới thiệu:** Sử dụng `WorkManager` và `AlarmManager` của Android để gửi Local Notification, không cần dùng Firebase Push Notification.
- **Loại 1: Daily Reminder (20:00 hằng ngày)**
  - Kiểm tra xem trong ngày hôm nay local DB / API có giao dịch nào chưa. Nếu chưa có -> Gửi thông báo nhắc nhở ghi chép để giữ Streak.
- **Loại 2: Daily Budget Alert**
  - **Logic:** Hạn mức mỗi ngày = Hạn mức tháng / Số ngày trong tháng. Nếu tổng chi tiêu trong ngày vượt quá hạn mức này -> Gửi thông báo cảnh báo.
- **Loại 3: Monthly 80% Budget Alert**
  - **Logic:** Nếu tổng chi tháng > 80% hạn mức tháng, CHỈ gửi thông báo nếu thỏa mãn điều kiện toán học: `(Số tiền còn lại / Số ngày còn lại) < Hạn mức mỗi ngày`.
  - Check điều kiện này mỗi khi user thêm thành công 1 khoản chi tiêu mới.

## Chúc bạn code vui vẻ! 🚀
