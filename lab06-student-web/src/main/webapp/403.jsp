<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>403 - Truy Cập Bị Từ Chối</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container login-container text-center">
        <div class="error-icon">🚫</div>
        <h2>403 - Lỗi Phân Quyền</h2>
        <p class="text-danger mb-20" style="font-weight: 500;">
            Bạn không có quyền truy cập chức năng này!
        </p>
        <p style="color: var(--text-secondary); margin-bottom: 30px; font-size: 0.95rem; line-height: 1.5;">
            Tài khoản của bạn là <strong>${sessionScope.user}</strong> (vai trò: <span class="badge badge-user">${sessionScope.role}</span>). Chức năng này chỉ dành cho tài khoản <span class="badge badge-admin">admin</span>.
        </p>
        <div style="display: flex; gap: 10px; justify-content: center;">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Quay lại Dashboard</a>
            <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">Xem danh sách SV</a>
        </div>
    </div>
</body>
</html>
