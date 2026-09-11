package vn.edu.eaut.lab16.shared.exception;

/**
 * Exception thrown when attempting to modify a locked order.
 */
public class OrderLockedException extends RuntimeException {
    private final String orderNumber;
    private final String lockedBy;

    public OrderLockedException(String orderNumber, String lockedBy) {
        super(String.format("Đơn hàng '%s' đang được xử lý bởi '%s'", orderNumber, lockedBy));
        this.orderNumber = orderNumber;
        this.lockedBy = lockedBy;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getLockedBy() {
        return lockedBy;
    }
}
