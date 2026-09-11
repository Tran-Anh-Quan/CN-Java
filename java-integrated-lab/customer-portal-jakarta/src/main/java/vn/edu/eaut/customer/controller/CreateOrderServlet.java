package vn.edu.eaut.customer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderItem;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.enums.Platform;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Order Servlet - Handles order creation from customer portal.
 *
 * Integration Principles:
 * - Creates order in PENDING status
 * - Logs history in single transaction
 * - Business logic in Service (simplified here for servlet)
 * - Session-based authentication: requires logged-in user
 * - Validates quantity and stock before saving
 */
@WebServlet(name = "CreateOrderServlet", urlPatterns = {"/create-order"})
public class CreateOrderServlet extends HttpServlet {

    private DatabaseConnection db;

    @Override
    public void init() throws ServletException {
        db = DatabaseConnection.getInstance();
    }

    /**
     * Guard helper: require session, otherwise redirect to login.
     */
    private String requireSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        String username = session == null ? null : (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return username;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = requireSession(request, response);
        if (username == null) return; // redirected

        try {
            List<Product> products = db.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/views/create-order.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading products", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = requireSession(request, response);
        if (username == null) return; // redirected

        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String shippingAddress = request.getParameter("shippingAddress");

        String[] productCodes = request.getParameterValues("productCode");
        String[] quantities = request.getParameterValues("quantity");

        // Get client IP (Bài 10)
        String ipAddress = getClientIp(request);

        if (productCodes == null || productCodes.length == 0) {
            request.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm");
            forwardToForm(request, response);
            return;
        }

        try {
            List<OrderItem> items = new ArrayList<>();
            StringBuilder stockError = new StringBuilder();

            for (int i = 0; i < productCodes.length; i++) {
                String code = productCodes[i];
                int qty;
                try {
                    qty = Integer.parseInt(quantities[i]);
                } catch (NumberFormatException | NullPointerException ex) {
                    continue; // skip invalid row
                }
                if (qty <= 0) continue;

                Product product = db.getProductByCode(code);
                if (product == null) continue;

                // Validate stock
                if (product.getStockQuantity() < qty) {
                    stockError.append("• ").append(product.getProductName())
                              .append(": cần ").append(qty)
                              .append(", chỉ còn ").append(product.getStockQuantity()).append("\n");
                    continue;
                }

                OrderItem item = new OrderItem(
                        product.getProductCode(),
                        product.getProductName(),
                        qty,
                        product.getPrice()
                );
                items.add(item);
            }

            if (stockError.length() > 0) {
                request.setAttribute("error", "Không đủ hàng trong kho:\n" + stockError.toString());
                forwardToForm(request, response);
                return;
            }

            if (items.isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn số lượng lớn hơn 0 cho ít nhất một sản phẩm");
                forwardToForm(request, response);
                return;
            }

            // Basic validation
            if (customerName == null || customerName.trim().isEmpty()
                    || customerEmail == null || customerEmail.trim().isEmpty()
                    || shippingAddress == null || shippingAddress.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ Họ tên, Email và Địa chỉ giao hàng");
                forwardToForm(request, response);
                return;
            }

            // Create order
            Order order = new Order();
            order.setOrderNumber(generateOrderNumber());
            order.setCustomerName(customerName.trim());
            order.setCustomerEmail(customerEmail.trim());
            order.setCustomerPhone(customerPhone != null ? customerPhone.trim() : null);
            order.setShippingAddress(shippingAddress.trim());
            order.setItems(items);
            order.setTotalAmount(order.getTotalAmount());

            // Save order with history (transaction) - IP tracking enabled
            Long orderId = db.createOrder(order, username, Platform.JAKARTA, ipAddress, null);

            order = db.getOrderById(orderId);

            request.setAttribute("order", order);
            request.setAttribute("success", "Đơn hàng của bạn đã được tạo thành công!");
            request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error creating order", e);
        }
    }

    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> products = db.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/views/create-order.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading products", e);
        }
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

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                + "-" + (int)(Math.random() * 900 + 100); // avoid collision within same second
    }
}
