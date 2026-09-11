package vn.edu.eaut.lab16.shared.entity;

import vn.edu.eaut.lab16.shared.enums.HistoryAction;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.enums.Platform;

import java.time.LocalDateTime;

/**
 * Order history entity for audit trail.
 * Records every action performed on an order.
 */
public class OrderHistory {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private HistoryAction action;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private String performedBy;
    private Platform performedPlatform;
    // Tracking fields (Bài 10)
    private String ipAddress;    // For web platforms (Jakarta, Spring)
    private String hostname;     // For Swing platform
    private String notes;
    private LocalDateTime createdAt;

    public OrderHistory() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Create a new history entry for status change.
     */
    public static OrderHistory createStatusChange(Order order, OrderStatus fromStatus, 
                                                   OrderStatus toStatus, String performedBy,
                                                   Platform platform, String notes) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setOrderNumber(order.getOrderNumber());
        history.setAction(HistoryAction.STATUS_CHANGE);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setPerformedBy(performedBy);
        history.setPerformedPlatform(platform);
        history.setNotes(notes);
        return history;
    }

    /**
     * Create a new history entry for order creation.
     */
    public static OrderHistory createOrderCreation(Order order, String createdBy, Platform platform) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setOrderNumber(order.getOrderNumber());
        history.setAction(HistoryAction.CREATE);
        history.setFromStatus(null);
        history.setToStatus(OrderStatus.PENDING);
        history.setPerformedBy(createdBy);
        history.setPerformedPlatform(platform);
        history.setNotes("Khách hàng tạo đơn hàng");
        return history;
    }

    /**
     * Create a new history entry for locking.
     */
    public static OrderHistory createLock(Order order, String lockedBy, Platform platform) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setOrderNumber(order.getOrderNumber());
        history.setAction(HistoryAction.LOCK);
        history.setToStatus(order.getStatus());
        history.setPerformedBy(lockedBy);
        history.setPerformedPlatform(platform);
        history.setNotes("Đơn hàng được khóa để xử lý");
        return history;
    }

    /**
     * Create a new history entry for unlocking.
     */
    public static OrderHistory createUnlock(Order order, String unlockedBy, Platform platform) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setOrderNumber(order.getOrderNumber());
        history.setAction(HistoryAction.UNLOCK);
        history.setToStatus(order.getStatus());
        history.setPerformedBy(unlockedBy);
        history.setPerformedPlatform(platform);
        history.setNotes("Đơn hàng được mở khóa");
        return history;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public HistoryAction getAction() { return action; }
    public void setAction(HistoryAction action) { this.action = action; }

    public OrderStatus getFromStatus() { return fromStatus; }
    public void setFromStatus(OrderStatus fromStatus) { this.fromStatus = fromStatus; }

    public OrderStatus getToStatus() { return toStatus; }
    public void setToStatus(OrderStatus toStatus) { this.toStatus = toStatus; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public Platform getPerformedPlatform() { return performedPlatform; }
    public void setPerformedPlatform(Platform performedPlatform) { this.performedPlatform = performedPlatform; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
