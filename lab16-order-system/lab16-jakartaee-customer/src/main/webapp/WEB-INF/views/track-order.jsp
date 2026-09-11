<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Order" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderItem" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Theo Dõi Đơn Hàng</title>
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
            border-bottom: 3px solid #f5576c;
            padding-bottom: 15px;
        }
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #f5576c;
            text-decoration: none;
            font-weight: 600;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .search-box {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
        }
        .search-box label {
            display: block;
            color: white;
            font-size: 1.1em;
            margin-bottom: 10px;
            font-weight: 600;
        }
        .search-box input {
            width: 70%;
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 1em;
        }
        .search-box button {
            width: 25%;
            padding: 15px;
            background: white;
            color: #f5576c;
            border: none;
            border-radius: 10px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            margin-left: 10px;
        }
        .error {
            background: #fee;
            color: #c00;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #c00;
        }
        .success {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #155724;
        }
        .order-details {
            margin-top: 30px;
        }
        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .order-number {
            font-size: 1.5em;
            color: #f5576c;
            font-weight: bold;
        }
        .status-badge {
            padding: 10px 25px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 1em;
        }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-confirmed { background: #cce5ff; color: #004085; }
        .status-processing { background: #e2e3e5; color: #383d41; }
        .status-packed { background: #d1ecf1; color: #0c5460; }
        .status-approved { background: #d4edda; color: #155724; }
        .status-shipped { background: #cce5ff; color: #002752; }
        .status-completed { background: #d4edda; color: #28a745; }
        .status-cancelled { background: #f8d7da; color: #721c24; }

        .info-section {
            margin-bottom: 25px;
        }
        .info-section h2 {
            color: #555;
            border-bottom: 2px solid #f5576c;
            padding-bottom: 10px;
            margin-bottom: 15px;
        }
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        .info-item {
            background: #f8f8f8;
            padding: 15px;
            border-radius: 8px;
        }
        .info-item label {
            display: block;
            color: #666;
            font-size: 0.9em;
            margin-bottom: 5px;
        }
        .info-item span {
            color: #333;
            font-weight: 600;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background: #f5576c;
            color: white;
        }
        .timeline {
            margin-top: 30px;
            padding: 20px;
            background: #f8f8f8;
            border-radius: 10px;
        }
        .timeline h2 {
            margin-bottom: 20px;
        }
        .confirm-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1.1em;
            font-weight: 600;
            cursor: pointer;
            margin-top: 20px;
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

        <h1>🔍 Theo Dõi Đơn Hàng</h1>

        <div class="search-box">
            <form method="get" action="${pageContext.request.contextPath}/track-order">
                <label for="orderNumber">Nhập mã đơn hàng của bạn:</label>
                <input type="text" id="orderNumber" name="orderNumber"
                       placeholder="Ví dụ: ORD-20260909-123456"
                       value="${param.orderNumber}" required>
                <button type="submit">🔍 Tra cứu</button>
            </form>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if (request.getAttribute("success") != null) { %>
            <div class="success"><%= request.getAttribute("success") %></div>
        <% } %>

        <%
            Order order = (Order) request.getAttribute("order");
            if (order != null) {
        %>

        <div class="order-details">
            <div class="order-header">
                <span class="order-number">Mã: <%= order.getOrderNumber() %></span>
                <span class="status-badge status-<%= order.getStatus().name().toLowerCase() %>">
                    <%= order.getStatus().getDisplayName() %>
                </span>
            </div>

            <div class="info-section">
                <h2>👤 Thông Tin Giao Hàng</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Khách hàng</label>
                        <span><%= order.getCustomerName() %></span>
                    </div>
                    <div class="info-item">
                        <label>Email</label>
                        <span><%= order.getCustomerEmail() %></span>
                    </div>
                    <div class="info-item">
                        <label>Số điện thoại</label>
                        <span><%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %></span>
                    </div>
                    <div class="info-item">
                        <label>Ngày đặt</label>
                        <span><%= order.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></span>
                    </div>
                </div>
                <div class="info-item" style="margin-top: 15px;">
                    <label>Địa chỉ giao hàng</label>
                    <span><%= order.getShippingAddress() %></span>
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
                        <tr style="background: #f0f0f0; font-weight: bold;">
                            <td colspan="4">TỔNG CỘNG:</td>
                            <td><%= String.format("%,.0f VNĐ", order.getTotalAmount()) %></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="timeline">
                <h2>📋 Tiến Trình Đơn Hàng</h2>
                <p style="color: #666;">
                    <strong>Ngày tạo:</strong> <%= order.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %><br>
                    <strong>Cập nhật lần cuối:</strong> <%= order.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %>
                </p>
            </div>

            <% if (order.getStatus() == Order.OrderStatus.SHIPPED) { %>
                <form method="post" action="${pageContext.request.contextPath}/track-order">
                    <input type="hidden" name="orderNumber" value="<%= order.getOrderNumber() %>">
                    <input type="hidden" name="action" value="confirm">
                    <button type="submit" class="confirm-btn">
                        ✅ Xác Nhận Đã Nhận Hàng
                    </button>
                </form>
            <% } %>

            <% if (order.getStatus() == Order.OrderStatus.COMPLETED) { %>
                <div style="text-align: center; padding: 20px; background: #d4edda; border-radius: 10px; margin-top: 20px;">
                    <span style="font-size: 2em;">🎉</span>
                    <p style="color: #155724; font-weight: 600; margin-top: 10px;">
                        Cảm ơn bạn đã xác nhận! Đơn hàng đã hoàn thành.
                    </p>
                </div>
            <% } %>
        </div>

        <% } %>
    </div>
</body>
</html>
