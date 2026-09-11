<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="vn.edu.eaut.lab16.shared.enums.UserRole" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cửa Hàng Điện Tử - Trang Chủ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .hero {
            background: var(--bg-gradient);
            color: white;
            padding: 50px 40px;
            border-radius: var(--radius);
            margin-bottom: 30px;
            text-align: center;
        }
        .hero h1 { color: white; font-size: 2.4em; }
        .hero .subtitle { color: rgba(255,255,255,0.9); font-size: 1.15em; margin-bottom: 0; }
        .hero .emoji { font-size: 3em; margin-bottom: 10px; }
        .menu-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 18px;
        }
        .menu-card {
            background: white;
            padding: 30px;
            border-radius: var(--radius);
            box-shadow: var(--shadow-sm);
            text-decoration: none;
            color: var(--text);
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
            border: 2px solid transparent;
        }
        .menu-card:hover {
            transform: translateY(-4px);
            box-shadow: var(--shadow-md);
            border-color: var(--primary);
        }
        .menu-card .icon {
            font-size: 3em;
            margin-bottom: 12px;
            display: block;
        }
        .menu-card h3 { color: var(--primary); margin-bottom: 6px; }
        .menu-card p { color: var(--text-muted); font-size: 0.92em; }
        .flow-strip {
            background: #f8f9fc;
            padding: 16px;
            border-radius: var(--radius);
            margin-top: 30px;
            text-align: center;
            font-size: 0.9em;
            color: var(--text-muted);
        }
        @media (max-width: 600px) { .menu-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <% String username = (String) session.getAttribute("username");
       String fullName = (String) session.getAttribute("fullName");
       if (username != null) { %>
        <nav class="top-nav">
            <div class="nav-left">
                <a href="${pageContext.request.contextPath}/index">🏪 Trang Chủ</a>
            </div>
            <div class="nav-right">
                <span class="user-chip">👤 <%= fullName != null ? fullName : username %></span>
                <a href="${pageContext.request.contextPath}/logout">🚪 Đăng Xuất</a>
            </div>
        </nav>
    <% } %>

    <div class="container" style="margin: 24px auto;">
        <% if (username == null) { %>
            <div style="text-align: center; margin-bottom: 20px;">
                <span class="platform-badge">🛍️ Customer Portal - Jakarta EE</span>
            </div>
        <% } %>

        <div class="hero">
            <div class="emoji">🏪</div>
            <h1>Cửa Hàng Điện Tử</h1>
            <p class="subtitle">Hệ thống quản lý đơn hàng trực tuyến đa nền tảng</p>
        </div>

        <% if (username == null) { %>
            <div style="text-align: center; margin-bottom: 24px;">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">🔐 Đăng Nhập Để Tiếp Tục</a>
            </div>
        <% } %>

        <div class="menu-grid">
            <a href="${pageContext.request.contextPath}/create-order" class="menu-card">
                <span class="icon">📦</span>
                <h3>Tạo Đơn Hàng</h3>
                <p>Chọn sản phẩm, nhập thông tin và gửi đơn hàng</p>
            </a>
            <a href="${pageContext.request.contextPath}/track-order" class="menu-card">
                <span class="icon">🔍</span>
                <h3>Theo Dõi Đơn</h3>
                <p>Tra cứu trạng thái và lịch sử đơn hàng của bạn</p>
            </a>
        </div>

        <div class="flow-strip">
            🔄 <strong>Luồng:</strong> Khách tạo đơn → Kho xử lý → Quản lý duyệt → Giao hàng → Xác nhận hoàn thành
        </div>
    </div>
</body>
</html>
