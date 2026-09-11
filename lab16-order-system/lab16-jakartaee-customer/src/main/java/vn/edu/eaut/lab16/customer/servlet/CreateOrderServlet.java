package vn.edu.eaut.lab16.customer.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderItem;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "CreateOrderServlet", urlPatterns = {"/create-order"})
public class CreateOrderServlet extends HttpServlet {

    private DatabaseConnection db;

    @Override
    public void init() throws ServletException {
        db = DatabaseConnection.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String shippingAddress = request.getParameter("shippingAddress");

        // Get selected products
        String[] productCodes = request.getParameterValues("productCode");
        String[] quantities = request.getParameterValues("quantity");

        if (productCodes == null || productCodes.length == 0) {
            request.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm");
            doGet(request, response);
            return;
        }

        try {
            // Generate order number
            String orderNumber = generateOrderNumber();

            // Create order
            Order order = new Order();
            order.setOrderNumber(orderNumber);
            order.setCustomerName(customerName);
            order.setCustomerEmail(customerEmail);
            order.setCustomerPhone(customerPhone);
            order.setShippingAddress(shippingAddress);
            order.setStatus(Order.OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            // Add items to order
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < productCodes.length; i++) {
                String code = productCodes[i];
                int qty = Integer.parseInt(quantities[i]);

                if (qty > 0) {
                    Product product = db.getProductByCode(code);
                    if (product != null) {
                        OrderItem item = new OrderItem(
                                product.getProductName(),
                                product.getProductCode(),
                                qty,
                                product.getPrice()
                        );
                        order.addItem(item);
                        total = total.add(item.getSubtotal());
                    }
                }
            }

            if (order.getItems().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn số lượng lớn hơn 0");
                doGet(request, response);
                return;
            }

            order.setTotalAmount(total);

            // Save order
            Long orderId = db.saveOrder(order);
            order.setId(orderId);

            // Store order info for confirmation
            request.setAttribute("order", order);
            request.setAttribute("success", "Đơn hàng của bạn đã được tạo thành công!");
            request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error creating order", e);
        }
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }
}
