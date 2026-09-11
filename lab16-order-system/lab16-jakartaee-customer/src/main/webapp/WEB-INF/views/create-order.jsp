<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Product" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Đơn Hàng Mới</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            padding: 20px;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
            text-align: center;
            border-bottom: 3px solid #667eea;
            padding-bottom: 15px;
        }
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .section {
            margin-bottom: 30px;
        }
        .section h2 {
            color: #555;
            margin-bottom: 15px;
            font-size: 1.3em;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #555;
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 1em;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background: #667eea;
            color: white;
        }
        tr:hover {
            background: #f8f8f8;
        }
        .quantity-input {
            width: 80px;
            padding: 8px;
            border: 2px solid #ddd;
            border-radius: 5px;
            text-align: center;
        }
        .submit-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1.2em;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }
        .error {
            background: #fee;
            color: #c00;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #c00;
        }
        .platform-badge {
            display: inline-block;
            background: #e8e8e8;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.8em;
            color: #666;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <span class="platform-badge">🖥️ Jakarta EE Platform</span>
        <a href="${pageContext.request.contextPath}/index" class="back-link">← Quay về trang chủ</a>

        <h1>📦 Tạo Đơn Hàng Mới</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/create-order">
            <div class="section">
                <h2>👤 Thông Tin Khách Hàng</h2>
                <div class="form-row">
                    <div class="form-group">
                        <label for="customerName">Họ và tên *</label>
                        <input type="text" id="customerName" name="customerName" required placeholder="Nguyễn Văn A">
                    </div>
                    <div class="form-group">
                        <label for="customerPhone">Số điện thoại</label>
                        <input type="tel" id="customerPhone" name="customerPhone" placeholder="0912345678">
                    </div>
                </div>
                <div class="form-group">
                    <label for="customerEmail">Email *</label>
                    <input type="email" id="customerEmail" name="customerEmail" required placeholder="email@example.com">
                </div>
                <div class="form-group">
                    <label for="shippingAddress">Địa chỉ giao hàng *</label>
                    <textarea id="shippingAddress" name="shippingAddress" required rows="3" placeholder="123 Đường ABC, Phường XYZ, Quận 1, TP.HCM"></textarea>
                </div>
            </div>

            <div class="section">
                <h2>🛍️ Chọn Sản Phẩm</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Chọn</th>
                            <th>Mã SP</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá</th>
                            <th>Tồn kho</th>
                            <th>Số lượng</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Product> products = (List<Product>) request.getAttribute("products");
                            if (products != null) {
                                for (Product product : products) {
                        %>
                            <tr>
                                <td>
                                    <input type="checkbox" class="product-checkbox" onchange="toggleQuantity(this)">
                                </td>
                                <td><%= product.getProductCode() %></td>
                                <td><%= product.getProductName() %></td>
                                <td><%= String.format("%,.0f VNĐ", product.getPrice()) %></td>
                                <td><%= product.getStockQuantity() %></td>
                                <td>
                                    <input type="number" class="quantity-input" name="quantity"
                                           min="1" max="<%= product.getStockQuantity() %>"
                                           value="1" disabled
                                           data-product-code="<%= product.getProductCode() %>">
                                </td>
                            </tr>
                        <%
                                }
                            }
                        %>
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
                addHiddenInput(productCode);
            } else {
                quantityInput.disabled = true;
                quantityInput.value = 1;
                removeHiddenInput(productCode);
            }
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
            if (input) {
                input.remove();
            }
        }

        // Add change listeners to quantity inputs
        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', function() {
                const productCode = this.getAttribute('data-product-code');
                const hiddenInput = document.querySelector(`input[name="productCode"][value="${productCode}"]`);
                if (hiddenInput) {
                    // Ensure hidden input exists
                }
            });
        });
    </script>
</body>
</html>
