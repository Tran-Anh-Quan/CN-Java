package vn.edu.eaut.lab16.shared.enums;

/**
 * Order status enum with defined status transitions.
 * 
 * Status Flow:
 * PENDING → PROCESSING → READY → SHIPPING → COMPLETED
 *    ↓          ↓           ↓
 * CANCELLED  CANCELLED  CANCELLED
 */
public enum OrderStatus {
    PENDING("Chờ xử lý", 1),
    PROCESSING("Đang xử lý", 2),
    READY("Sẵn sàng giao", 3),
    SHIPPING("Đang giao hàng", 4),
    COMPLETED("Hoàn thành", 5),
    CANCELLED("Đã hủy", 0);

    private final String displayName;
    private final int order;

    OrderStatus(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getOrder() {
        return order;
    }

    /**
     * Check if transition from current status to new status is valid.
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        if (this == newStatus) {
            return true;
        }

        switch (this) {
            case PENDING:
                return newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING:
                return newStatus == READY || newStatus == CANCELLED;
            case READY:
                return newStatus == SHIPPING || newStatus == CANCELLED;
            case SHIPPING:
                return newStatus == COMPLETED || newStatus == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }

    /**
     * Get the next valid status for this status.
     */
    public OrderStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return PROCESSING;
            case PROCESSING:
                return READY;
            case READY:
                return SHIPPING;
            case SHIPPING:
                return COMPLETED;
            default:
                return null;
        }
    }

    /**
     * Check if this status is a terminal state.
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
