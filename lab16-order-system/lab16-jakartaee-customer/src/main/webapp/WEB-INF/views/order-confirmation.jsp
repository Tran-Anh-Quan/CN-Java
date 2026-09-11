<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Order" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderItem" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác Nhận Đơn Hàng</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 700px;
            width: 100%;
        }
        .success-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .success-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        h1 {
            color: #28a745;
            margin-bottom: 10px;
        }
        .order-number {
            font-size: 1.5em;
            color: #667eea;
            font-weight: bold;
            margin: 20px 0;
            padding: 15px;
            background: #f0f4ff;
            border-radius: 10px;
            text-align: center;
        }
        .info-section {
            margin-bottom: 20px;
        }
        .info-section h2 {
            color: #555;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
            margin-bottom: 15px;
            font-size: 1.2em;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        .info-label {
            color: #666;
            font-weight: 600;
        }
        .info-value {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
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
        .total-row {
            background: #f8f8f8;
            font-weight: bold;
        }
        .total-row td {
            font-size: 1.2em;
            color: #667eea;
        }
        .actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        .btn {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .btn-secondary {
            background: #f0f0f0;
            color: #333;
        }
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .status-badge {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 20px;
            font-size: 0.9em;
            font-weight: 600;
        }
        .status-pending {
            background: #fff3cd;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="container">
        <%
            Order order = (Order) request.getAttribute("order");
            if (order != null) {
        %>

        <div class="success-header">
            <div class="success-icon">✅</div>
            <h1>Đặt Hàng Thành Công!</h1>
            <p>Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi</p>
        </div>

        <div class="order-number">
            Mã đơn hàng: <%= order.getOrderNumber() %>
        </div>

        <div class="info-section">
            <h2>👤 Thông Tin Khách Hàng</h2>
            <div class="info-row">
                <span class="info-label">Họ tên:</span>
                <span class="info-value"><%= order.getCustomerName() %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Email:</span>
                <span class="info-value"><%= order.getCustomerEmail() %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Số điện thoại:</span>
                <span class="info-value"><%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Địa chỉ giao hàng:</span>
                <span class="info-value"><%= order.getShippingAddress() %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Trạng thái:</span>
                <span class="info-value">
                    <span class="status-badge status-pending">
                        <%= order.getStatus().getDisplayName() %>
                    </span>
                </span>
            </div>
        </div>

        <div class="info-section">
            <h2>📦 Chi Tiết Đơn Hàng</h2>
            <table>
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th>SL</th>
                        <th>Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int index = 1;
                        for (OrderItem item : order.getItems()) {
                    %>
                        <tr>
                            <td><%= index++ %></td>
                            <td><%= item.getProductName() %></td>
                            <td><%= String.format("%,.0f VNĐ", item.getPrice()) %></td>
                            <td><%= item.getQuantity() %></td>
                            <td><%= String.format("%,.0f VNĐ", item.getSubtotal()) %></td>
                        </tr>
                    <% } %>
                    <tr class="total-row">
                        <td colspan="4">TỔNG CỘNG:</td>
                        <td><%= String.format("%,.0f VNĐ", order.getTotalAmount()) %></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/track-order?orderNumber=<%= order.getOrderNumber() %>"
               class="btn btn-primary">🔍 Theo Dõi Đơn Hàng</a>
            <a href="${pageContext.request.contextPath}/index" class="btn btn-secondary">🏠 Quay Về Trang Chủ</a>
        </div>

        <% } else { %>
            <div class="success-header">
                <div class="success-icon">❌</div>
                <h1>Không Tìm Thấy Đơn Hàng</h1>
            </div>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/create-order" class="btn btn-primary">← Tạo Đơn Hàng Mới</a>
            </div>
        <% } %>
    </div>
</body>
</html>
