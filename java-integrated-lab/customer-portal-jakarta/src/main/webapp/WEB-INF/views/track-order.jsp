<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.Order" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderItem" %>
<%@ page import="vn.edu.eaut.lab16.shared.entity.OrderHistory" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Theo Dõi Đơn Hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .order-number { font-size: 1.5em; color: var(--accent); font-weight: bold; }
        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            flex-wrap: wrap;
            gap: 12px;
        }
        .info-section { margin-bottom: 24px; }
        .info-section h2 { margin-bottom: 14px; }
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 14px;
        }
        .info-card {
            background: #f8f9fc;
            padding: 14px;
            border-radius: 8px;
            border-left: 3px solid var(--primary);
        }
        .info-card label {
            display: block;
            color: #888;
            font-size: 0.85em;
            margin-bottom: 4px;
        }
        .info-card span { color: var(--text); font-weight: 600; }
        .login-prompt {
            background: #fff3cd;
            border: 2px solid var(--warning);
            border-radius: var(--radius);
            padding: 20px;
            margin-top: 20px;
            text-align: center;
        }
        .login-prompt a { color: var(--primary); font-weight: 600; text-decoration: none; }
        .cancel-form input {
            padding: 12px 14px;
            border: 2px solid var(--border);
            border-radius: 8px;
            font-size: 1em;
            flex: 1;
        }
    </style>
</head>
<body>
    <% String username = (String) session.getAttribute("username");
       String fullName = (String) session.getAttribute("fullName");
       Boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn");
       if (isLoggedIn == null) isLoggedIn = (username != null);
    %>

    <nav class="top-nav">
        <div class="nav-left">
            <a href="${pageContext.request.contextPath}/index">🏪 Trang Chủ</a>
        </div>
        <div class="nav-right">
            <% if (isLoggedIn) { %>
                <span class="user-chip">👤 <%= fullName != null ? fullName : username %></span>
                <a href="${pageContext.request.contextPath}/logout">🚪 Đăng Xuất</a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login">🔐 Đăng Nhập</a>
            <% } %>
        </div>
    </nav>

    <div class="container" style="margin: 24px auto; max-width: 1000px;">
        <div style="text-align: center; margin-bottom: 8px;">
            <span class="platform-badge">🛍️ Customer Portal - Jakarta EE</span>
        </div>
        <a href="${pageContext.request.contextPath}/index" class="back-link">← Quay về trang chủ</a>

        <h1>🔍 Theo Dõi Đơn Hàng</h1>

        <!-- Search -->
        <div class="section" style="background: var(--bg-gradient-pink); border: none; padding: 24px;">
            <form method="get" action="${pageContext.request.contextPath}/track-order">
                <div style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
                    <input type="text" name="orderNumber" placeholder="Nhập mã đơn hàng, ví dụ: ORD-20260909-123456"
                           value="${param.orderNumber}" required
                           style="flex: 1; padding: 14px; border: none; border-radius: 10px; font-size: 1em;">
                    <button type="submit" class="btn btn-secondary" style="background: white; color: var(--accent);">
                        🔍 Tra cứu
                    </button>
                </div>
            </form>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">❌ <%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">✅ <%= request.getAttribute("success") %></div>
        <% } %>

        <%
            Order order = (Order) request.getAttribute("order");
            List<OrderHistory> history = (List<OrderHistory>) request.getAttribute("history");
            if (order != null) {
                String statusClass = "status-" + order.getStatus().name().toLowerCase();
        %>
        <div class="order-header">
            <span class="order-number">📋 Mã: <%= order.getOrderNumber() %></span>
            <span class="status-badge <%= statusClass %>"><%= order.getStatus().getDisplayName() %></span>
        </div>

        <!-- Customer Info -->
        <div class="section">
            <h2>👤 Thông Tin Giao Hàng</h2>
            <div class="info-grid">
                <div class="info-card"><label>Khách hàng</label><span><%= order.getCustomerName() %></span></div>
                <div class="info-card"><label>Email</label><span><%= order.getCustomerEmail() %></span></div>
                <div class="info-card"><label>Số điện thoại</label><span><%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %></span></div>
                <div class="info-card"><label>Ngày đặt</label><span><%= order.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %></span></div>
            </div>
            <div class="info-card" style="margin-top: 14px; grid-column: 1 / -1;">
                <label>Địa chỉ giao hàng</label>
                <span><%= order.getShippingAddress() %></span>
            </div>
        </div>

        <!-- Order Items -->
        <div class="section">
            <h2>📦 Chi Tiết Đơn Hàng</h2>
            <table>
                <thead><tr><th>#</th><th>Sản phẩm</th><th>Giá</th><th>SL</th><th>Thành tiền</th></tr></thead>
                <tbody>
                    <% int idx = 1;
                       java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
                       for (OrderItem item : order.getItems()) { %>
                        <tr>
                            <td><%= idx++ %></td>
                            <td><strong><%= item.getProductName() %></strong></td>
                            <td><%= nf.format(item.getUnitPrice()) %> ₫</td>
                            <td><%= item.getQuantity() %></td>
                            <td><strong><%= nf.format(item.getSubtotal()) %> ₫</strong></td>
                        </tr>
                    <% } %>
                    <tr style="background: #f0f4ff; font-weight: bold;">
                        <td colspan="4">💰 TỔNG CỘNG:</td>
                        <td style="color: var(--primary);"><%= nf.format(order.getTotalAmount()) %> ₫</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- History Timeline -->
        <% if (history != null && !history.isEmpty()) { %>
        <div class="section">
            <h2>📜 Lịch Sử Đơn Hàng</h2>
            <div class="timeline">
                <% for (OrderHistory h : history) { %>
                <div class="timeline-item">
                    <div class="timeline-dot">✓</div>
                    <div class="timeline-content">
                        <h4><%= h.getAction().getDisplayName() %></h4>
                        <p>
                            <% if (h.getFromStatus() != null) { %>
                                <strong><%= h.getFromStatus().getDisplayName() %></strong> → <strong><%= h.getToStatus().getDisplayName() %></strong>
                            <% } else { %>
                                Trạng thái: <strong><%= h.getToStatus().getDisplayName() %></strong>
                            <% } %>
                        </p>
                        <% if (h.getNotes() != null) { %>
                            <p>📝 <%= h.getNotes() %></p>
                        <% } %>
                        <span class="meta">
                            👤 <strong><%= h.getPerformedBy() %></strong> •
                            🖥️ <em><%= h.getPerformedPlatform().getDisplayName() %></em> •
                            ⏰ <%= h.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) %>
                        </span>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
        <% } %>

        <!-- Action Buttons -->
        <% if (!isLoggedIn) { %>
            <div class="login-prompt">
                <p style="margin-bottom: 12px; color: var(--text);">
                    🔐 Bạn cần <strong>đăng nhập</strong> để thực hiện thao tác trên đơn hàng.
                </p>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Đăng Nhập</a>
            </div>
        <% } else if (order.getStatus().name().equals("SHIPPING")) { %>
            <div class="section" style="border-left: 4px solid var(--success);">
                <h2 style="border-color: var(--success);">✅ Xác Nhận Nhận Hàng</h2>
                <p style="color: var(--text-muted); margin-bottom: 16px;">
                    Đơn hàng đang được giao. Vui lòng xác nhận khi đã nhận đủ hàng.
                </p>
                <form method="post" action="${pageContext.request.contextPath}/track-order">
                    <input type="hidden" name="orderNumber" value="<%= order.getOrderNumber() %>">
                    <input type="hidden" name="action" value="confirm">
                    <button type="submit" class="submit-btn" style="background: var(--bg-gradient-green);"
                            onclick="return confirm('Xác nhận đã nhận đủ hàng?');">
                        ✅ Xác Nhận Đã Nhận Hàng
                    </button>
                </form>
            </div>
        <% } else if (order.getStatus().name().equals("PENDING")) { %>
            <div class="section" style="border-left: 4px solid var(--danger);">
                <h2 style="border-color: var(--danger);">❌ Hủy Đơn Hàng</h2>
                <form method="post" action="${pageContext.request.contextPath}/track-order">
                    <input type="hidden" name="orderNumber" value="<%= order.getOrderNumber() %>">
                    <input type="hidden" name="action" value="cancel">
                    <div class="form-group">
                        <label>Lý do hủy (tùy chọn):</label>
                        <input type="text" name="reason" placeholder="Ví dụ: Đổi ý, không cần nữa...">
                    </div>
                    <button type="submit" class="submit-btn" style="background: var(--bg-gradient-red);"
                            onclick="return confirm('Xác nhận hủy đơn hàng này?');">
                        ❌ Hủy Đơn Hàng
                    </button>
                </form>
            </div>
        <% } else if (order.getStatus().name().equals("COMPLETED")) { %>
            <div class="section" style="border-left: 4px solid var(--success); background: #f0fff4;">
                <h2 style="border-color: var(--success);">🎉 Đơn Hàng Hoàn Thành</h2>
                <p style="text-align: center; color: var(--success); font-size: 1.1em; margin-top: 10px;">
                    Cảm ơn bạn đã xác nhận! Đơn hàng đã hoàn thành.
                </p>
            </div>
        <% } else { %>
            <div class="section" style="border-left: 4px solid var(--warning);">
                <h2 style="border-color: var(--warning);">⏳ Trạng Thái Hiện Tại</h2>
                <p style="color: var(--text-muted);">
                    Đơn hàng đang trong quá trình xử lý.
                    Trạng thái hiện tại: <strong><%= order.getStatus().getDisplayName() %></strong>.
                    Vui lòng chờ thông báo tiếp theo qua email.
                </p>
            </div>
        <% } %>

        <% } else { %>
            <!-- No order found yet -->
            <div style="text-align: center; padding: 60px 20px; color: var(--text-muted);">
                <div style="font-size: 4em; margin-bottom: 16px;">📦</div>
                <h3 style="margin-bottom: 8px;">Tra cứu đơn hàng của bạn</h3>
                <p>Nhập mã đơn hàng phía trên để xem chi tiết và trạng thái.</p>
            </div>
        <% } %>
    </div>
</body>
</html>
