<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Customer Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-bg">
    <div class="login-container">
        <div class="platform-badge">🛍️ Customer Portal - Jakarta EE</div>
        <div class="success-icon" style="font-size: 60px;">🔐</div>
        <h1>Đăng Nhập</h1>
        <p class="subtitle">Đăng nhập để tạo và theo dõi đơn hàng</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">❌ <%= request.getAttribute("error") %></div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="username">👤 Tên đăng nhập</label>
                <input type="text" id="username" name="username" required autofocus
                       placeholder="customer1">
            </div>

            <div class="form-group">
                <label for="password">🔑 Mật khẩu</label>
                <input type="password" id="password" name="password" required
                       placeholder="••••••••">
            </div>

            <button type="submit" class="submit-btn">🚀 Đăng Nhập</button>
        </form>

        <div class="demo-info">
            <h4>📋 Tài khoản demo (mật khẩu: <code>password123</code>):</h4>
            <p><strong>customer1</strong> - Nguyễn Văn An</p>
            <p><strong>customer2</strong> - Trần Thị Bình</p>
            <p><strong>customer3</strong> - Lê Minh Cường</p>
        </div>

        <p style="text-align: center; margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/index"
               style="color: #667eea; text-decoration: none; font-size: 0.9em;">
                ← Quay về trang chủ
            </a>
        </p>
    </div>
</body>
</html>
