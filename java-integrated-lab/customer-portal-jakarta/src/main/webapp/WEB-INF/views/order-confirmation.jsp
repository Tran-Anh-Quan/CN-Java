<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Order" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderItem" %>
<%@ page import="vn.edu.eaut.lab16.shared.enums.OrderStatus" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    String username = (String) session.getAttribute("username");
    String fullName = (String) session.getAttribute("fullName");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Hàng Thành Công</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .confirmation-hero {
            background: var(--bg-gradient);
            color: white;
            padding: 40px;
            border-radius: var(--radius) var(--radius) 0 0;
            text-align: center;
        }
        .confirmation-body {
            background: white;
            padding: 30px 35px;
            border-radius: 0 0 var(--radius) var(--radius);
            box-shadow: var(--shadow-md);
        }
        .order-number-box {
            background: #f0f4ff;
            border: 2px dashed var(--primary);
            padding: 14px 24px;
            border-radius: var(--radius);
            text-align: center;
            font-size: 1.2em;
            color: var(--primary);
            font-weight: bold;
            margin: 20px 0;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid var(--border);
            font-size: 0.95em;
        }
        .info-row .label { color: var(--text-muted); }
        .info-row .value { color: var(--text); font-weight: 600; text-align: right; }
        .total-row { background: #f0f4ff; font-weight: bold; }
        .total-row td { font-size: 1.1em; color: var(--primary); }
    </style>
</head>
<body style="background: var(--bg); display: flex; justify-content: center; align-items: center; min-height: 100vh; padding: 20px;">
    <div style="max-width: 700px; width: 100%;">
        <%
            Order order = (Order) request.getAttribute("order");
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            if (order != null) {
        %>

        <div class="confirmation-hero">
            <div style="font-size: 4em; margin-bottom: 12px;">✅</div>
            <h1 style="color: white; font-size: 2em; margin-bottom: 6px;">Đặt Hàng Thành Công!</h1>
            <p style="color: rgba(255,255,255,0.9); font-size: 1.05em;">
                Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi
            </p>
        </div>

        <div class="confirmation-body">
            <div class="order-number-box">
                📋 Mã đơn hàng: <%= order.getOrderNumber() %>
            </div>

            <!-- Customer info -->
            <h3 style="color: var(--text); margin-bottom: 14px; border-bottom: 2px solid var(--primary); padding-bottom: 8px;">
                👤 Thông Tin Khách Hàng
            </h3>
            <div class="info-row">
                <span class="label">Họ tên</span>
                <span class="value"><%= order.getCustomerName() %></span>
            </div>
            <div class="info-row">
                <span class="label">Email</span>
                <span class="value"><%= order.getCustomerEmail() %></span>
            </div>
            <div class="info-row">
                <span class="label">Số điện thoại</span>
                <span class="value"><%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %></span>
            </div>
            <div class="info-row">
                <span class="label">Địa chỉ giao hàng</span>
                <span class="value" style="max-width: 50%; text-align: right; word-break: break-word;">
                    <%= order.getShippingAddress() %>
                </span>
            </div>
            <div class="info-row">
                <span class="label">Trạng thái</span>
                <span class="value">
                    <span class="status-badge status-pending">⏳ <%= order.getStatus().getDisplayName() %></span>
                </span>
            </div>

            <!-- Order items -->
            <h3 style="color: var(--text); margin: 22px 0 14px; border-bottom: 2px solid var(--primary); padding-bottom: 8px;">
                📦 Chi Tiết Đơn Hàng
            </h3>
            <table>
                <thead><tr><th>#</th><th>Sản phẩm</th><th>Giá</th><th>SL</th><th>Thành tiền</th></tr></thead>
                <tbody>
                    <% int idx = 1; for (OrderItem item : order.getItems()) { %>
                        <tr>
                            <td><%= idx++ %></td>
                            <td><%= item.getProductName() %></td>
                            <td><%= nf.format(item.getUnitPrice()) %> ₫</td>
                            <td><%= item.getQuantity() %></td>
                            <td><strong><%= nf.format(item.getSubtotal()) %> ₫</strong></td>
                        </tr>
                    <% } %>
                    <tr class="total-row">
                        <td colspan="4">💰 TỔNG CỘNG:</td>
                        <td><%= nf.format(order.getTotalAmount()) %> ₫</td>
                    </tr>
                </tbody>
            </table>

            <!-- Action buttons -->
            <div class="actions" style="margin-top: 28px;">
                <a href="${pageContext.request.contextPath}/track-order?orderNumber=<%= order.getOrderNumber() %>"
                   class="btn btn-primary" style="flex: 1;">
                    🔍 Theo Dõi Đơn Hàng
                </a>
                <a href="${pageContext.request.contextPath}/create-order"
                   class="btn btn-accent" style="flex: 1;">
                    🛒 Tạo Đơn Mới
                </a>
            </div>
        </div>

        <% } else { %>
        <div class="confirmation-hero">
            <div style="font-size: 4em; margin-bottom: 12px;">❌</div>
            <h1 style="color: white;">Không Tìm Thấy Đơn Hàng</h1>
        </div>
        <div class="confirmation-body" style="text-align: center; padding: 40px;">
            <p style="color: var(--text-muted); margin-bottom: 20px;">Đơn hàng không tồn tại hoặc đã bị hủy.</p>
            <a href="${pageContext.request.contextPath}/create-order" class="btn btn-primary">
                ← Tạo Đơn Hàng Mới
            </a>
        </div>
        <% } %>
    </div>
</body>
</html>
