package vn.edu.eaut.lab16.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab16.manager.service.OrderService;
import vn.edu.eaut.lab16.shared.entity.Order;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(@RequestParam(required = false) String status, Model model) {
        try {
            List<Order> orders;
            if (status != null && !status.isEmpty()) {
                Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
                orders = orderService.getOrdersByStatus(orderStatus);
            } else {
                orders = orderService.getAllOrders();
            }
            model.addAttribute("orders", orders);
            model.addAttribute("selectedStatus", status);
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi tải đơn hàng: " + e.getMessage());
        }
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                model.addAttribute("order", order);
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng");
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi tải chi tiết đơn hàng: " + e.getMessage());
        }
        return "orders/view";
    }

    @PostMapping("/{id}/approve")
    public String approveOrder(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order != null && order.getStatus() == Order.OrderStatus.PACKED) {
                orderService.updateOrderStatus(id, Order.OrderStatus.APPROVED);
                model.addAttribute("success", "Đơn hàng đã được phê duyệt thành công!");
            } else if (order != null) {
                model.addAttribute("error", "Đơn hàng chưa được đóng gói, không thể phê duyệt.");
                model.addAttribute("order", order);
                return "orders/view";
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng");
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi phê duyệt đơn hàng: " + e.getMessage());
        }
        return "redirect:/orders/view/" + id;
    }

    @PostMapping("/{id}/ship")
    public String shipOrder(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order != null && order.getStatus() == Order.OrderStatus.APPROVED) {
                orderService.updateOrderStatus(id, Order.OrderStatus.SHIPPED);
                model.addAttribute("success", "Đơn hàng đã được giao cho đơn vị vận chuyển!");
            } else if (order != null) {
                model.addAttribute("error", "Đơn hàng chưa được phê duyệt, không thể giao.");
                model.addAttribute("order", order);
                return "orders/view";
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng");
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/orders/view/" + id;
    }

    @PostMapping("/{id}/reject")
    public String rejectOrder(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                orderService.updateOrderStatus(id, Order.OrderStatus.CANCELLED);
                model.addAttribute("success", "Đơn hàng đã bị hủy!");
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng");
            }
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi hủy đơn hàng: " + e.getMessage());
        }
        return "redirect:/orders";
    }
}
