package vn.edu.eaut.lab16.shared.entity;

import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.enums.Platform;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity with locking mechanism for multi-platform integration.
 * 
 * Key features:
 * - Status transitions validated by OrderStatus enum
 * - Locking mechanism to prevent concurrent modifications
 * - Audit trail for created_by and created_platform
 */
public class Order {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private OrderStatus status;
    
    // Locking mechanism
    private String lockedBy;
    private LocalDateTime lockedAt;
    
    // Audit trail
    private String createdBy;
    private Platform createdPlatform;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Optimistic locking version (Bài 8)
    private int version;
    
    // Track if stock has been deducted (Bài 6)
    private boolean stockDeducted;
    
    // Cancellation tracking (Bài 6)
    private String cancellationReason;
    private String cancelledBy;
    private Platform cancelledPlatform;
    private LocalDateTime cancelledAt;
    
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(String orderNumber, String customerName, String customerEmail, String shippingAddress,
                 String createdBy, Platform createdPlatform) {
        this();
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.createdBy = createdBy;
        this.createdPlatform = createdPlatform;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getLockedBy() { return lockedBy; }
    public void setLockedBy(String lockedBy) { this.lockedBy = lockedBy; }

    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Platform getCreatedPlatform() { return createdPlatform; }
    public void setCreatedPlatform(Platform createdPlatform) { this.createdPlatform = createdPlatform; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public boolean isStockDeducted() { return stockDeducted; }
    public void setStockDeducted(boolean stockDeducted) { this.stockDeducted = stockDeducted; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(String cancelledBy) { this.cancelledBy = cancelledBy; }

    public Platform getCancelledPlatform() { return cancelledPlatform; }
    public void setCancelledPlatform(Platform cancelledPlatform) { this.cancelledPlatform = cancelledPlatform; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrderId(this.id);
    }

    /**
     * Calculate total amount of the order.
     */
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Check if order is locked by another platform.
     */
    public boolean isLocked() {
        return lockedBy != null && !lockedBy.isEmpty();
    }

    /**
     * Check if order is locked by specific user/platform.
     */
    public boolean isLockedBy(String username) {
        return lockedBy != null && lockedBy.equals(username);
    }

    /**
     * Lock the order for processing.
     */
    public void lock(String username) {
        this.lockedBy = username;
        this.lockedAt = LocalDateTime.now();
    }

    /**
     * Unlock the order after processing.
     */
    public void unlock() {
        this.lockedBy = null;
        this.lockedAt = null;
    }

    /**
     * Check if status transition is valid.
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        return this.status.canTransitionTo(newStatus);
    }

    /**
     * Explicit setter for total amount.
     * Normally this is calculated from items, but can be set explicitly.
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        // Computed from items; this setter is provided for consistency with JPA/forms.
        // The value is derived from items, so we don't store it separately here.
    }
}
