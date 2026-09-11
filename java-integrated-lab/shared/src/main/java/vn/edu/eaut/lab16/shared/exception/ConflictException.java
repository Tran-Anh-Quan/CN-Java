package vn.edu.eaut.lab16.shared.exception;

/**
 * Exception thrown when an optimistic locking conflict is detected.
 * This happens when two platforms try to modify the same order.
 */
public class ConflictException extends RuntimeException {
    private final String orderNumber;
    private final String currentVersion;
    private final String attemptedVersion;
    private final String currentStatus;

    public ConflictException(String orderNumber, String currentStatus, 
                             String currentVersion, String attemptedVersion) {
        super(String.format(
            "Đơn hàng '%s' đã bị thay đổi bởi người khác. " +
            "Trạng thái hiện tại: %s. Vui lòng tải lại dữ liệu.",
            orderNumber, currentStatus));
        this.orderNumber = orderNumber;
        this.currentStatus = currentStatus;
        this.currentVersion = currentVersion;
        this.attemptedVersion = attemptedVersion;
    }

    public String getOrderNumber() { return orderNumber; }
    public String getCurrentStatus() { return currentStatus; }
    public String getCurrentVersion() { return currentVersion; }
    public String getAttemptedVersion() { return attemptedVersion; }
}
