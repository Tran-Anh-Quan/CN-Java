package vn.edu.eaut.customer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderHistory;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.enums.Platform;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Track Order Servlet - Handles order tracking and confirmation.
 *
 * Supports:
 * - Bài 6: Customer can cancel PENDING orders
 * - Bài 10: IP tracking for Jakarta EE platform
 *
 * Session-based auth:
 * - GET /track-order: anonymous allowed (anyone can track)
 * - POST confirm/cancel: requires session login
 */
@WebServlet(name = "TrackOrderServlet", urlPatterns = {"/track-order"})
public class TrackOrderServlet extends HttpServlet {

    private DatabaseConnection db;

    @Override
    public void init() throws ServletException {
        db = DatabaseConnection.getInstance();
    }

    /**
     * Get session username, or null if not logged in.
     */
    private String getSessionUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (String) session.getAttribute("username");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderNumber = request.getParameter("orderNumber");

        if (orderNumber != null && !orderNumber.trim().isEmpty()) {
            try {
                Order order = db.getOrderByNumber(orderNumber.trim());
                if (order != null) {
                    request.setAttribute("order", order);
                    List<OrderHistory> history = db.getOrderHistory(order.getId());
                    request.setAttribute("history", history);
                } else {
                    request.setAttribute("error", "Không tìm thấy đơn hàng với mã: " + orderNumber);
                }
            } catch (SQLException e) {
                throw new ServletException("Error tracking order", e);
            }
        }

        // Pass login state for UI
        String username = getSessionUsername(request);
        request.setAttribute("isLoggedIn", username != null);
        request.getRequestDispatcher("/WEB-INF/views/track-order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderNumber = request.getParameter("orderNumber");
        String action = request.getParameter("action");

        // Require session for actions
        String username = getSessionUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String ipAddress = getClientIp(request);

        if (orderNumber == null) {
            doGet(request, response);
            return;
        }

        try {
            Order order = db.getOrderByNumber(orderNumber);

            if (order == null) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                doGet(request, response);
                return;
            }

            String resultMessage = null;

            if ("confirm".equals(action)) {
                // Customer confirms receipt (SHIPPING → COMPLETED)
                if (order.getStatus() == OrderStatus.SHIPPING) {
                    db.updateOrderStatus(order.getId(), OrderStatus.COMPLETED, username,
                            Platform.JAKARTA, "Khách hàng xác nhận đã nhận hàng",
                            order.getVersion(), ipAddress, null);
                    resultMessage = "Cảm ơn bạn! Đơn hàng đã được xác nhận hoàn thành.";
                } else if (order.getStatus() == OrderStatus.COMPLETED) {
                    resultMessage = "Đơn hàng đã được xác nhận hoàn thành trước đó.";
                } else {
                    resultMessage = "Đơn hàng chưa được giao. Trạng thái hiện tại: "
                            + order.getStatus().getDisplayName();
                }
            } else if ("cancel".equals(action)) {
                // Customer cancels PENDING order (Bài 6)
                String reason = request.getParameter("reason");
                if (reason == null || reason.isEmpty()) {
                    reason = "Khách hàng yêu cầu hủy";
                }

                if (order.getStatus() == OrderStatus.PENDING) {
                    db.cancelOrder(order.getId(), reason, username, Platform.JAKARTA, ipAddress, null);
                    resultMessage = "Đơn hàng đã được hủy thành công.";
                } else {
                    resultMessage = "Khách hàng chỉ có thể hủy đơn ở trạng thái PENDING. "
                            + "Trạng thái hiện tại: " + order.getStatus().getDisplayName();
                }
            }

            // Reload order
            order = db.getOrderByNumber(orderNumber);
            List<OrderHistory> history = db.getOrderHistory(order.getId());
            request.setAttribute("history", history);
            request.setAttribute("order", order);
            request.setAttribute("isLoggedIn", true);

            if (resultMessage != null) {
                request.setAttribute("success", resultMessage);
            }

        } catch (SQLException e) {
            throw new ServletException("Error processing order", e);
        }

        doGet(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
