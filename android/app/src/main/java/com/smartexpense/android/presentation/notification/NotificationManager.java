package com.smartexpense.android.presentation.notification;

import com.smartexpense.android.data.model.NotificationItem;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static NotificationManager instance;
    private final List<NotificationItem> notifications = new ArrayList<>();

    private NotificationManager() {
        initDefaultNotifications();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    private void initDefaultNotifications() {
        notifications.clear();
        
        // 1. Cảnh báo hạn mức (Vàng cam)
        notifications.add(new NotificationItem(
                "notif_1",
                "Cảnh báo hạn mức chi tiêu ⚠️",
                "Chi tiêu 'Ăn uống' tháng này đã đạt 85% hạn mức (1.700.000 ₫ / 2.000.000 ₫). Hãy cân nhắc phân bổ ngân sách nhé!",
                "10 phút trước",
                NotificationItem.Type.BUDGET_ALERT,
                false,
                1 // Tab 1: Dashboard Thống kê
        ));

        // 2. Gợi ý thông minh từ Trợ lý AI (Màu Accent)
        notifications.add(new NotificationItem(
                "notif_2",
                "Lời khuyên tài chính từ AI 💡",
                "Tuần này bạn đã tiết kiệm được 250.000 ₫ so với tuần trước nhờ hạn chế mua sắm ngẫu hứng. Tiếp tục phát huy nhé!",
                "1 giờ trước",
                NotificationItem.Type.AI_INSIGHT,
                false,
                3 // Tab 3: Chatbot AI
        ));

        // 3. Nhắc nhở chụp ảnh chi tiêu (Xanh lá)
        notifications.add(new NotificationItem(
                "notif_3",
                "Nhắc nhở ghi chép chi tiêu 📸",
                "Bạn chưa ghi nhận chi tiêu tối nay. Chụp ngay một bức ảnh để duy trì chuỗi Streak 5 ngày liên tiếp!",
                "Hôm nay 19:30",
                NotificationItem.Type.REMINDER,
                false,
                2 // Tab 2: Camera Feed
        ));

        // 4. Báo cáo tổng kết tuần (Xanh lam)
        notifications.add(new NotificationItem(
                "notif_4",
                "Tổng kết tài chính tuần qua 📊",
                "Tổng chi tiêu tuần này là 1.450.000 ₫ qua 12 giao dịch. Danh mục ăn uống chiếm tỷ trọng cao nhất (60%).",
                "Hôm qua",
                NotificationItem.Type.WEEKLY_REPORT,
                true,
                1 // Tab 1: Dashboard Thống kê
        ));

        // 5. Cập nhật tính năng hệ thống (Tím)
        notifications.add(new NotificationItem(
                "notif_5",
                "Tính năng mới đã sẵn sàng ✨",
                "Đã kích hoạt bộ lọc theo khoảng ngày (Từ ngày → Đến ngày) và tùy chỉnh màu sắc cá nhân hóa cho bạn.",
                "3 ngày trước",
                NotificationItem.Type.SYSTEM,
                true,
                0 // Tab 0: Lưới Widget
        ));
    }

    public List<NotificationItem> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public int getUnreadCount() {
        int count = 0;
        for (NotificationItem item : notifications) {
            if (!item.isRead()) {
                count++;
            }
        }
        return count;
    }

    public void markAsRead(String id) {
        for (NotificationItem item : notifications) {
            if (item.getId().equals(id)) {
                item.setRead(true);
                break;
            }
        }
    }

    public void markAllAsRead() {
        for (NotificationItem item : notifications) {
            item.setRead(true);
        }
    }
}
