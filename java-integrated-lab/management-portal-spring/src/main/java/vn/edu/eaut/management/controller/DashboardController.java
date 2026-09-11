package vn.edu.eaut.management.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.eaut.management.service.ManagerService;

import java.sql.SQLException;
import java.util.Map;

/**
 * Dashboard Controller.
 * 
 * Bài 9: Dashboard statistics including:
 * - Total orders today
 * - Order count by status
 * - Revenue from COMPLETED orders
 * - Top 5 products
 * - Top 5 customers
 */
@Controller
public class DashboardController {

    private final ManagerService managerService;

    public DashboardController() {
        this.managerService = new ManagerService();
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, Authentication auth) {
        try {
            // Get statistics (Bài 9)
            Map<String, Object> stats = managerService.getDashboardStats();
            
            model.addAttribute("totalOrders", stats.get("totalOrders"));
            model.addAttribute("ordersToday", stats.get("ordersToday"));
            model.addAttribute("completedRevenue", stats.get("completedRevenue"));
            model.addAttribute("orderCountByStatus", stats.get("orderCountByStatus"));
            model.addAttribute("topProducts", stats.get("topProducts"));
            model.addAttribute("topCustomers", stats.get("topCustomers"));

            // Get recent orders ready for approval
            int readyCount = managerService.getOrdersReadyForApproval().size();
            model.addAttribute("readyOrdersCount", readyCount);

            if (auth != null) {
                model.addAttribute("username", auth.getName());
                model.addAttribute("role", auth.getAuthorities().iterator().next().getAuthority());
            }

        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi kết nối database: " + e.getMessage());
        }
        return "dashboard";
    }
}
