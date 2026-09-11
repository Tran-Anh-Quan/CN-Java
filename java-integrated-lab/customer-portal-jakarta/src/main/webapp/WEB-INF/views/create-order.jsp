<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Product" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Order" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderItem" %>
<%@ page import="vn.edu.eaut.lab16.shared.enums.OrderStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    String username = (String) session.getAttribute("username");
    String fullName = (String) session.getAttribute("fullName");
    String userEmail = (String) session.getAttribute("email");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Đơn Hàng Mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .product-image {
            width: 50px;
            height: 50px;
            background: var(--bg-gradient);
            border-radius: 8px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.5em;
        }
        .product-row { display: flex; align-items: center; gap: 12px; }
        .product-name { font-weight: 600; }
        .product-desc { font-size: 0.85em; color: var(--text-muted); }
        .stock-warn { color: var(--danger); font-size: 0.85em; }
        .stock-low { color: var(--warning); font-size: 0.85em; }
    </style>
</head>
<body>
    <nav class="top-nav">
        <div class="nav-left">
            <a href="${pageContext.request.contextPath}/index">🏪 Trang Chủ</a>
        </div>
        <div class="nav-right">
            <span class="user-chip">👤 <%= fullName != null ? fullName : username %></span>
            <a href="${pageContext.request.contextPath}/logout">🚪 Đăng Xuất</a>
        </div>
    </nav>

    <div class="container" style="margin: 24px auto; max-width: 950px;">
        <div style="text-align: center; margin-bottom: 8px;">
            <span class="platform-badge">🛍️ Customer Portal - Jakarta EE</span>
        </div>

        <!-- Stepper -->
        <div class="stepper">
            <div class="step active"><div class="step-circle">1</div><div class="step-label">Thông tin</div></div>
            <div class="step active"><div class="step-circle">2</div><div class="step-label">Sản phẩm</div></div>
            <div class="step"><div class="step-circle">3</div><div class="step-label">Xác nhận</div></div>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">❌ <%= request.getAttribute("error") %></div>
        <% } %>

        <a href="${pageContext.request.contextPath}/index" class="back-link">← Quay về trang chủ</a>

        <h1>📦 Tạo Đơn Hàng Mới</h1>

        <form method="post" action="${pageContext.request.contextPath}/create-order" id="orderForm">

            <!-- Customer info -->
            <div class="section">
                <h2>👤 Thông Tin Khách Hàng</h2>
                <div class="form-row">
                    <div class="form-group">
                        <label for="customerName">Họ và tên *</label>
                        <input type="text" id="customerName" name="customerName"
                               value="<%= fullName != null ? fullName : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label for="customerPhone">Số điện thoại</label>
                        <input type="tel" id="customerPhone" name="customerPhone" placeholder="0912345678">
                    </div>
                </div>
                <div class="form-group">
                    <label for="customerEmail">Email *</label>
                    <input type="email" id="customerEmail" name="customerEmail"
                           value="<%= userEmail != null ? userEmail : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="shippingAddress">Địa chỉ giao hàng *</label>
                    <textarea id="shippingAddress" name="shippingAddress" required rows="2"
                              placeholder="123 Đường ABC, Phường XYZ, Quận 1, TP.HCM"></textarea>
                </div>
            </div>

            <!-- Products -->
            <div class="section">
                <h2>🛍️ Chọn Sản Phẩm</h2>
                <table>
                    <thead>
                        <tr>
                            <th style="width: 50px;">Chọn</th>
                            <th>Mã SP</th>
                            <th>Sản phẩm</th>
                            <th>Giá</th>
                            <th>Tồn kho</th>
                            <th style="width: 100px;">Số lượng</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Product> products = (List<Product>) request.getAttribute("products");
                            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
                            if (products != null) {
                                for (Product product : products) {
                                    String stockClass = product.getStockQuantity() <= 0 ? "stock-warn"
                                            : product.getStockQuantity() < 10 ? "stock-low" : "";
                                    String stockText = product.getStockQuantity() <= 0 ? "Hết hàng"
                                            : product.getStockQuantity() < 10 ? "Sắp hết (" + product.getStockQuantity() + ")" 
                                            : String.valueOf(product.getStockQuantity());
                        %>
                            <tr>
                                <td><input type="checkbox" class="product-checkbox" onchange="toggleQuantity(this)"
                                           <%= product.getStockQuantity() <= 0 ? "disabled" : "" %>></td>
                                <td><strong><%= product.getProductCode() %></strong></td>
                                <td>
                                    <div class="product-name"><%= product.getProductName() %></div>
                                    <div class="product-desc"><%= product.getDescription() != null ? product.getDescription() : "" %></div>
                                </td>
                                <td><%= nf.format(product.getPrice()) %> ₫</td>
                                <td class="<%= stockClass %>"><%= stockText %></td>
                                <td>
                                    <input type="number" class="quantity-input" name="quantity"
                                           min="1" max="<%= product.getStockQuantity() %>"
                                           value="1" disabled
                                           data-product-code="<%= product.getProductCode() %>">
                                </td>
                            </tr>
                        <% } } %>
                    </tbody>
                </table>
                <div id="selectedProducts"></div>
            </div>

            <button type="submit" class="submit-btn">🛒 Tạo Đơn Hàng</button>
        </form>
    </div>

    <script>
        function toggleQuantity(checkbox) {
            const row = checkbox.closest('tr');
            const quantityInput = row.querySelector('.quantity-input');
            const productCode = quantityInput.getAttribute('data-product-code');
            if (checkbox.checked) {
                quantityInput.disabled = false;
                quantityInput.focus();
                addHiddenInput(productCode);
            } else {
                quantityInput.disabled = true;
                quantityInput.value = 1;
                removeHiddenInput(productCode);
            }
            updateTotal();
        }
        function addHiddenInput(productCode) {
            const container = document.getElementById('selectedProducts');
            if (!document.querySelector(`input[name="productCode"][value="${productCode}"]`)) {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'productCode';
                input.value = productCode;
                container.appendChild(input);
            }
        }
        function removeHiddenInput(productCode) {
            const input = document.querySelector(`input[name="productCode"][value="${productCode}"]`);
            if (input) input.remove();
            updateTotal();
        }
        function updateTotal() {
            // Optional: real-time total calculation
        }
        document.getElementById('orderForm').addEventListener('submit', function(e) {
            const checked = document.querySelectorAll('.product-checkbox:checked');
            if (checked.length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất một sản phẩm!');
            }
        });
    </script>
</body>
</html>
