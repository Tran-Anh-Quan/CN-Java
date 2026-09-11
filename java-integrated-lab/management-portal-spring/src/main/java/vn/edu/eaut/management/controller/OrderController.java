package vn.edu.eaut.management.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderHistory;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.management.service.ManagerService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Order Controller.
 * 
 * Supports:
 * - Bài 6: Cancel order with stock restoration
 * - Bài 7: Multi-condition search with pagination
 * - Bài 8: Optimistic locking with version check
 * - Bài 9: Dashboard statistics
 * - Bài 10: IP tracking in history
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final ManagerService managerService;

    public OrderController() {
        this.managerService = new ManagerService();
    }

    /**
     * List orders with multi-condition search and pagination (Bài 7).
     */
    @GetMapping
    public String listOrders(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Model model) {
        try {
            OrderStatus orderStatus = null;
            if (status != null && !status.isEmpty()) {
                try {
                    orderStatus = OrderStatus.valueOf(status);
                } catch (IllegalArgumentException ignored) {}
            }

            Map<String, Object> searchResult = managerService.searchOrders(
                    orderNumber, customerName, orderStatus, fromDate, toDate,
                    minAmount, maxAmount, page, pageSize);
            
            model.addAttribute("orders", searchResult.get("orders"));
            model.addAttribute("totalCount", searchResult.get("totalCount"));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", searchResult.get("totalPages"));
            model.addAttribute("pageSize", pageSize);
            
            // Preserve filter values
            model.addAttribute("orderNumber", orderNumber);
            model.addAttribute("customerName", customerName);
            model.addAttribute("selectedStatus", status);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("minAmount", minAmount);
            model.addAttribute("maxAmount", maxAmount);
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi tải đơn hàng: " + e.getMessage());
        }
        return "orders/list";
    }

    /**
     * View order details with history.
     */
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        try {
            Order order = managerService.getOrderById(id);
            if (order != null) {
                model.addAttribute("order", order);
                List<OrderHistory> history = managerService.getOrderHistory(id);
                model.addAttribute("history", history);
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng");
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi tải chi tiết đơn hàng: " + e.getMessage());
        }
        return "orders/view";
    }

    /**
     * Approve order (READY → SHIPPING) with optimistic locking (Bài 8).
     */
    @PostMapping("/{id}/approve")
    public String approveOrder(@PathVariable Long id, 
                                @RequestParam int version,
                                Authentication auth, 
                                jakarta.servlet.http.HttpServletRequest request,
                                Model model) {
        String username = auth != null ? auth.getName() : "manager";
        String ipAddress = managerService.getClientIp(request);
        
        try {
            String result = managerService.approveOrder(id, username, version, ipAddress);
            
            if ("SUCCESS".equals(result)) {
                model.addAttribute("success", "Đơn hàng đã được phê duyệt giao hàng!");
            } else if (result.startsWith("CONFLICT:")) {
                model.addAttribute("error", "XUNG ĐỘT DỮ LIỆU: " + result.substring(9) + 
                        " Vui lòng tải lại trang.");
            } else {
                model.addAttribute("error", result);
            }
            
            Order order = managerService.getOrderById(id);
            model.addAttribute("order", order);
            List<OrderHistory> history = managerService.getOrderHistory(id);
            model.addAttribute("history", history);
            
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi phê duyệt: " + e.getMessage());
        }
        return "orders/view";
    }

    /**
     * Cancel order (Bài 6).
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                               @RequestParam String reason,
                               Authentication auth,
                               jakarta.servlet.http.HttpServletRequest request,
                               Model model) {
        String username = auth != null ? auth.getName() : "manager";
        String ipAddress = managerService.getClientIp(request);
        
        try {
            String result = managerService.cancelOrder(id, reason, username, ipAddress);
            
            if ("SUCCESS".equals(result)) {
                model.addAttribute("success", "Đơn hàng đã được hủy. Kho đã được hoàn lại (nếu có).");
            } else {
                model.addAttribute("error", result);
            }
            
            Order order = managerService.getOrderById(id);
            model.addAttribute("order", order);
            List<OrderHistory> history = managerService.getOrderHistory(id);
            model.addAttribute("history", history);
            
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi hủy đơn: " + e.getMessage());
        }
        return "orders/view";
    }

    /**
     * Confirm receipt (SHIPPING → COMPLETED).
     */
    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Long id,
                                 @RequestParam int version,
                                 Authentication auth,
                                 jakarta.servlet.http.HttpServletRequest request,
                                 Model model) {
        String username = auth != null ? auth.getName() : "manager";
        String ipAddress = managerService.getClientIp(request);
        
        try {
            String result = managerService.confirmReceipt(id, username, version, ipAddress);
            
            if ("SUCCESS".equals(result)) {
                model.addAttribute("success", "Đơn hàng đã hoàn thành!");
            } else if (result.startsWith("CONFLICT:")) {
                model.addAttribute("error", "XUNG ĐỘT: " + result.substring(9));
            } else {
                model.addAttribute("error", result);
            }
            
            Order order = managerService.getOrderById(id);
            model.addAttribute("order", order);
            List<OrderHistory> history = managerService.getOrderHistory(id);
            model.addAttribute("history", history);
            
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "orders/view";
    }
}
