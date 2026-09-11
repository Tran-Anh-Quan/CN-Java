package vn.edu.eaut.management.service;

import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderHistory;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.enums.Platform;
import vn.edu.eaut.lab16.shared.exception.ConflictException;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Manager Service - Business logic for management operations.
 * 
 * Supports:
 * - Bài 6: Cancel order with stock restoration
 * - Bài 7: Multi-condition search with paging
 * - Bài 8: Optimistic locking for conflict detection
 * - Bài 9: Dashboard statistics
 * - Bài 10: IP tracking for Spring Boot platform
 */
public class ManagerService {
    private final DatabaseConnection db;

    public ManagerService() {
        this.db = DatabaseConnection.getInstance();
    }

    /**
     * Helper method to extract client IP from request.
     */
    public String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // ==================== Order Operations ====================

    public List<Order> getAllOrders() throws SQLException {
        return db.getAllOrders();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) throws SQLException {
        return db.getOrdersByStatus(status);
    }

    public List<Order> getOrdersReadyForApproval() throws SQLException {
        return db.getOrdersByStatus(OrderStatus.READY);
    }

    public List<Order> getOrdersShipping() throws SQLException {
        return db.getOrdersByStatus(OrderStatus.SHIPPING);
    }

    public Order getOrderById(Long orderId) throws SQLException {
        return db.getOrderById(orderId);
    }

    public List<OrderHistory> getOrderHistory(Long orderId) throws SQLException {
        return db.getOrderHistory(orderId);
    }

    /**
     * Approve order (READY → SHIPPING) with optimistic locking.
     */
    public String approveOrder(Long orderId, String managerUsername, int version, 
                               String ipAddress) throws SQLException {
        try {
            db.updateOrderStatus(orderId, OrderStatus.SHIPPING, managerUsername, 
                    Platform.SPRING, "Quản lý phê duyệt giao hàng", version, ipAddress, null);
            return "SUCCESS";
        } catch (ConflictException e) {
            return "CONFLICT:" + e.getMessage();
        }
    }

    /**
     * Cancel order (Bài 6).
     * Manager can cancel READY orders.
     */
    public String cancelOrder(Long orderId, String reason, String managerUsername,
                              String ipAddress) throws SQLException {
        Order order = db.getOrderById(orderId);
        if (order == null) return "ERROR: Không tìm thấy đơn hàng";

        if (order.getStatus() == OrderStatus.SHIPPING || 
            order.getStatus() == OrderStatus.COMPLETED ||
            order.getStatus() == OrderStatus.CANCELLED) {
            return "ERROR: Không thể hủy đơn ở trạng thái này";
        }

        db.cancelOrder(orderId, reason, managerUsername, Platform.SPRING, ipAddress, null);
        return "SUCCESS";
    }

    /**
     * Confirm receipt (SHIPPING → COMPLETED).
     */
    public String confirmReceipt(Long orderId, String managerUsername, int version,
                                 String ipAddress) throws SQLException {
        try {
            db.updateOrderStatus(orderId, OrderStatus.COMPLETED, managerUsername, 
                    Platform.SPRING, "Xác nhận khách đã nhận hàng", version, ipAddress, null);
            return "SUCCESS";
        } catch (ConflictException e) {
            return "CONFLICT:" + e.getMessage();
        }
    }

    // ==================== Bài 7: Search and Filter ====================

    /**
     * Search orders with multi-conditions and pagination.
     */
    public Map<String, Object> searchOrders(String orderNumber, String customerName, 
                                             OrderStatus status, LocalDate fromDate, LocalDate toDate,
                                             BigDecimal minAmount, BigDecimal maxAmount,
                                             int page, int pageSize) throws SQLException {
        List<Order> orders = db.searchOrdersWithPaging(
                orderNumber, customerName, status, fromDate, toDate, 
                minAmount, maxAmount, page, pageSize);
        int totalCount = db.countOrders(
                orderNumber, customerName, status, fromDate, toDate, 
                minAmount, maxAmount);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("orders", orders);
        result.put("totalCount", totalCount);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (int) Math.ceil((double) totalCount / pageSize));
        return result;
    }

    // ==================== Bài 9: Dashboard Statistics ====================

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalOrders", db.getAllOrders().size());
        stats.put("ordersToday", db.getOrdersTodayCount());
        stats.put("completedRevenue", db.getCompletedRevenue());
        stats.put("orderCountByStatus", db.getOrderCountByStatus());
        stats.put("topProducts", db.getTopProducts(5));
        stats.put("topCustomers", db.getTopCustomers(5));
        return stats;
    }

    public Map<String, Long> getOrderStatusCounts() throws SQLException {
        return db.getOrderCountByStatus();
    }

    // ==================== Bài 10: History Filter ====================

    public Map<String, Object> searchHistory(String orderNumber, String action, Platform platform,
                                              String performedBy, LocalDate fromDate, LocalDate toDate,
                                              int page, int pageSize) throws SQLException {
        List<OrderHistory> history = db.searchHistory(
                orderNumber, action, platform, performedBy, fromDate, toDate, page, pageSize);
        int totalCount = db.countHistory(
                orderNumber, action, platform, performedBy, fromDate, toDate);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("history", history);
        result.put("totalCount", totalCount);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (int) Math.ceil((double) totalCount / pageSize));
        return result;
    }

    public List<Product> getAllProducts() throws SQLException {
        return db.getAllProducts();
    }
}
