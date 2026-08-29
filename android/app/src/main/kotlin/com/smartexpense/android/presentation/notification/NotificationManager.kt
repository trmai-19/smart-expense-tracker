package com.smartexpense.android.presentation.notification

import com.smartexpense.android.data.model.NotificationItem

class NotificationManager private constructor() {

    private val notifications = mutableListOf<NotificationItem>()

    init {
        initDefaultNotifications()
    }

    companion object {
        @Volatile
        private var instance: NotificationManager? = null

        fun getInstance(): NotificationManager {
            return instance ?: synchronized(this) {
                instance ?: NotificationManager().also { instance = it }
            }
        }
    }

    private fun initDefaultNotifications() {
        notifications.clear()

        notifications.add(
            NotificationItem(
                "notif_1",
                "Cảnh báo hạn mức chi tiêu ⚠️",
                "Chi tiêu 'Ăn uống' tháng này đã đạt 85% hạn mức (1.700.000 ₫ / 2.000.000 ₫). Hãy cân nhắc phân bổ ngân sách nhé!",
                "10 phút trước",
                NotificationItem.Type.BUDGET_ALERT,
                false,
                1
            )
        )

        notifications.add(
            NotificationItem(
                "notif_2",
                "Lời khuyên tài chính từ AI 💡",
                "Tuần này bạn đã tiết kiệm được 250.000 ₫ so với tuần trước nhờ hạn chế mua sắm ngẫu hứng. Tiếp tục phát huy nhé!",
                "1 giờ trước",
                NotificationItem.Type.AI_INSIGHT,
                false,
                3
            )
        )

        notifications.add(
            NotificationItem(
                "notif_3",
                "Nhắc nhở ghi chép chi tiêu 📸",
                "Bạn chưa ghi nhận chi tiêu tối nay. Chụp ngay một bức ảnh để duy trì chuỗi Streak 5 ngày liên tiếp!",
                "Hôm nay 19:30",
                NotificationItem.Type.REMINDER,
                false,
                2
            )
        )

        notifications.add(
            NotificationItem(
                "notif_4",
                "Tổng kết tài chính tuần qua 📊",
                "Tổng chi tiêu tuần này là 1.450.000 ₫ qua 12 giao dịch. Danh mục ăn uống chiếm tỷ trọng cao nhất (60%).",
                "Hôm qua",
                NotificationItem.Type.WEEKLY_REPORT,
                true,
                1
            )
        )

        notifications.add(
            NotificationItem(
                "notif_5",
                "Tính năng mới đã sẵn sàng ✨",
                "Đã kích hoạt bộ lọc theo khoảng ngày (Từ ngày → Đến ngày) và tùy chỉnh màu sắc cá nhân hóa cho bạn.",
                "3 ngày trước",
                NotificationItem.Type.SYSTEM,
                true,
                0
            )
        )
    }

    fun getNotifications(): List<NotificationItem> {
        return ArrayList(notifications)
    }

    fun getUnreadCount(): Int {
        return notifications.count { !it.isRead }
    }

    fun markAsRead(id: String) {
        for (item in notifications) {
            if (item.id == id) {
                item.isRead = true
                break
            }
        }
    }

    fun markAllAsRead() {
        for (item in notifications) {
            item.isRead = true
        }
    }
}
