package vn.edu.eaut.lab16.shared.enums;

/**
 * Actions recorded in order history for audit trail.
 */
public enum HistoryAction {
    CREATE("Tạo đơn hàng"),
    LOCK("Khóa đơn hàng"),
    UNLOCK("Mở khóa đơn hàng"),
    STATUS_CHANGE("Thay đổi trạng thái"),
    APPROVE("Phê duyệt"),
    SHIP("Giao hàng"),
    COMPLETE("Hoàn thành"),
    CANCEL("Hủy đơn hàng");

    private final String displayName;

    HistoryAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
