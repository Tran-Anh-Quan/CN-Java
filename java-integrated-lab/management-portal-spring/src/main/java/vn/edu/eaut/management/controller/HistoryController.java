package vn.edu.eaut.management.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.eaut.lab16.shared.enums.Platform;
import vn.edu.eaut.management.service.ManagerService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * History Controller for Bài 10 - Log filter screen.
 * 
 * Allows filtering order history by:
 * - Order number
 * - Action type
 * - Platform
 * - Performer
 * - Date range
 */
@Controller
public class HistoryController {

    private final ManagerService managerService;

    public HistoryController() {
        this.managerService = new ManagerService();
    }

    /**
     * History filter screen with pagination.
     */
    @GetMapping("/history")
    public String history(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            Authentication auth,
            Model model) {
        try {
            Platform platEnum = null;
            if (platform != null && !platform.isEmpty()) {
                try {
                    platEnum = Platform.valueOf(platform);
                } catch (IllegalArgumentException ignored) {}
            }

            Map<String, Object> result = managerService.searchHistory(
                    orderNumber, action, platEnum, performedBy, fromDate, toDate, page, pageSize);
            
            model.addAttribute("history", result.get("history"));
            model.addAttribute("totalCount", result.get("totalCount"));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", result.get("totalPages"));
            model.addAttribute("pageSize", pageSize);
            
            // Preserve filters
            model.addAttribute("orderNumber", orderNumber);
            model.addAttribute("selectedAction", action);
            model.addAttribute("selectedPlatform", platform);
            model.addAttribute("performedBy", performedBy);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            
            // User info
            if (auth != null) {
                model.addAttribute("username", auth.getName());
            }
            
            model.addAttribute("actions", List.of("CREATE", "LOCK", "UNLOCK", 
                    "STATUS_CHANGE", "APPROVE", "SHIP", "COMPLETE", "CANCEL"));
            model.addAttribute("platforms", Platform.values());
            
        } catch (SQLException e) {
            model.addAttribute("error", "Lỗi tải nhật ký: " + e.getMessage());
        }
        return "history/list";
    }
}
