package vn.edu.eaut.lab16.customer.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TrackOrderServlet", urlPatterns = {"/track-order"})
public class TrackOrderServlet extends HttpServlet {

    private DatabaseConnection db;

    @Override
    public void init() throws ServletException {
        db = DatabaseConnection.getInstance();
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
                } else {
                    request.setAttribute("error", "Không tìm thấy đơn hàng với mã: " + orderNumber);
                }
            } catch (SQLException e) {
                throw new ServletException("Error tracking order", e);
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/track-order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderNumber = request.getParameter("orderNumber");
        String action = request.getParameter("action");

        if ("confirm".equals(action) && orderNumber != null) {
            try {
                Order order = db.getOrderByNumber(orderNumber);
                if (order != null && order.getStatus() == Order.OrderStatus.SHIPPED) {
                    db.updateOrderStatus(order.getId(), Order.OrderStatus.COMPLETED);
                    request.setAttribute("success", "Cảm ơn bạn! Đơn hàng đã được xác nhận hoàn thành.");
                    request.setAttribute("order", db.getOrderByNumber(orderNumber));
                } else if (order != null) {
                    request.setAttribute("error", "Đơn hàng chưa được giao, không thể xác nhận hoàn thành.");
                    request.setAttribute("order", order);
                }
            } catch (SQLException e) {
                throw new ServletException("Error confirming order", e);
            }
        }

        doGet(request, response);
    }
}
