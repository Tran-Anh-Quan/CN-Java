package vn.edu.eaut.warehouse.service;

import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderHistory;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.enums.Platform;
import vn.edu.eaut.lab16.shared.exception.ConflictException;
import vn.edu.eaut.lab16.shared.exception.OrderLockedException;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

/**
 * Warehouse Service - Business logic for warehouse operations.
 * 
 * Supports:
 * - Bài 6: Cancel order with stock restoration
 * - Bài 8: Optimistic locking with version check
 * - Bài 10: Hostname tracking for Swing platform
 */
public class WarehouseService {
    private final DatabaseConnection db;
    private final String currentUser;
    private final String hostname;

    public WarehouseService(String currentUser) {
        this.db = DatabaseConnection.getInstance();
        this.currentUser = currentUser;
        this.hostname = DatabaseConnection.getHostname();
    }

    /**
     * Get orders for warehouse processing: PENDING, PROCESSING, READY.
     */
    public List<Order> getOrdersForWarehouse() throws SQLException {
        List<OrderStatus> statuses = List.of(
                OrderStatus.PENDING,
                OrderStatus.PROCESSING,
                OrderStatus.READY
        );
        return db.getOrdersByStatuses(statuses);
    }

    public Order getOrderById(Long orderId) throws SQLException {
        return db.getOrderById(orderId);
    }

    public List<OrderHistory> getOrderHistory(Long orderId) throws SQLException {
        return db.getOrderHistory(orderId);
    }

    /**
     * Accept order for processing (PENDING → PROCESSING).
     * Uses optimistic locking via version column (Bài 8).
     */
    public String acceptOrder(Long orderId) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        if (order.isLocked() && !order.isLockedBy(currentUser)) {
            throw new OrderLockedException(order.getOrderNumber(), order.getLockedBy());
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return "ERROR: Chỉ có thể tiếp nhận đơn ở trạng thái PENDING";
        }

        if (!db.lockOrder(orderId, currentUser)) {
            return "ERROR: Không thể khóa đơn hàng";
        }

        // Reload to get new version
        order = db.getOrderById(orderId);
        
        // Add lock history
        OrderHistory lockHistory = OrderHistory.createLock(order, currentUser, Platform.SWING);
        lockHistory.setHostname(hostname);
        db.addHistory(lockHistory);

        // Update status to PROCESSING with optimistic locking
        try {
            db.updateOrderStatus(orderId, OrderStatus.PROCESSING, currentUser, Platform.SWING, 
                    "Kho tiếp nhận xử lý đơn hàng", order.getVersion(), null, hostname);
        } catch (ConflictException e) {
            db.unlockOrder(orderId, currentUser);
            throw e;
        }

        return "SUCCESS";
    }

    /**
     * Process order stock (check and deduct).
     */
    public String processStock(Long orderId) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        if (order.isLocked() && !order.isLockedBy(currentUser)) {
            throw new OrderLockedException(order.getOrderNumber(), order.getLockedBy());
        }

        if (order.getStatus() != OrderStatus.PROCESSING) {
            return "ERROR: Đơn hàng phải ở trạng thái PROCESSING";
        }

        // Check stock availability
        StringBuilder stockReport = new StringBuilder();
        boolean allAvailable = true;

        for (var item : order.getItems()) {
            Product product = db.getProductByCode(item.getProductCode());
            if (product == null || product.getStockQuantity() < item.getQuantity()) {
                allAvailable = false;
                stockReport.append(String.format("- %s: Cần %d, Còn %d\n",
                        item.getProductName(), item.getQuantity(),
                        product != null ? product.getStockQuantity() : 0));
            }
        }

        if (!allAvailable) {
            return "WARNING: Không đủ hàng:\n" + stockReport;
        }

        // Deduct stock
        for (var item : order.getItems()) {
            db.updateProductStock(item.getProductCode(), -item.getQuantity());
        }
        db.markStockDeducted(orderId);

        return "SUCCESS";
    }

    /**
     * Pack order (PROCESSING → READY).
     */
    public String packOrder(Long orderId) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        if (order.isLocked() && !order.isLockedBy(currentUser)) {
            throw new OrderLockedException(order.getOrderNumber(), order.getLockedBy());
        }

        if (order.getStatus() != OrderStatus.PROCESSING) {
            return "ERROR: Đơn hàng phải ở trạng thái PROCESSING";
        }

        // Update status with optimistic locking
        db.updateOrderStatus(orderId, OrderStatus.READY, currentUser, Platform.SWING, 
                "Đã đóng gói xong, chờ quản lý phê duyệt", order.getVersion(), null, hostname);

        // Unlock
        db.unlockOrder(orderId, currentUser);
        OrderHistory unlockHistory = OrderHistory.createUnlock(order, currentUser, Platform.SWING);
        unlockHistory.setHostname(hostname);
        db.addHistory(unlockHistory);

        return "SUCCESS";
    }

    /**
     * Cancel order (Bài 6).
     * Warehouse can cancel PROCESSING orders.
     * If stock was deducted, restore it in same transaction.
     */
    public String cancelOrder(Long orderId, String reason) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        // Warehouse can cancel PROCESSING
        if (order.getStatus() != OrderStatus.PROCESSING) {
            return "ERROR: Nhân viên kho chỉ có thể hủy đơn ở trạng thái PROCESSING";
        }

        // Check lock
        if (order.isLocked() && !order.isLockedBy(currentUser)) {
            throw new OrderLockedException(order.getOrderNumber(), order.getLockedBy());
        }

        // Cancel with stock restoration (Bài 6) and hostname tracking (Bài 10)
        db.cancelOrder(orderId, reason, currentUser, Platform.SWING, null, hostname);
        
        // Unlock
        db.unlockOrder(orderId, currentUser);

        return "SUCCESS";
    }

    /**
     * Release order lock.
     */
    public String releaseOrder(Long orderId) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        if (!order.isLockedBy(currentUser)) {
            return "ERROR: Bạn không sở hữu khóa trên đơn hàng này";
        }

        db.unlockOrder(orderId, currentUser);
        OrderHistory history = OrderHistory.createUnlock(order, currentUser, Platform.SWING);
        history.setHostname(hostname);
        db.addHistory(history);

        return "SUCCESS";
    }

    public List<Product> getAllProducts() throws SQLException {
        return db.getAllProducts();
    }

    public String getHostname() {
        return hostname;
    }
}
