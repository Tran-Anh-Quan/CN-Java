package vn.edu.eaut.lab16.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.eaut.lab16.manager.service.OrderService;
import vn.edu.eaut.lab16.shared.entity.Order;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final OrderService orderService;

    public DashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        try {
            // Get statistics
            Map<String, Long> stats = new HashMap<>();
            stats.put("total", orderService.getTotalOrders());
            stats.put("pending", orderService.getPendingOrders());
            stats.put("processing", orderService.getProcessingOrders());
            stats.put("completed", orderService.getCompletedOrders());

            // Get recent orders
            List<Order> recentOrders = orderService.getAllOrders();
            if (recentOrders.size() > 10) {
                recentOrders = recentOrders.subList(0, 10);
            }

            model.addAttribute("stats", stats);
            model.addAttribute("recentOrders", recentOrders);

        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi kết nối database: " + e.getMessage());
        }
        return "dashboard";
    }
}
