<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dashboard - Quản Lý Sinh Viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="flex-between mb-20" style="border-bottom: 1px solid var(--card-border); padding-bottom: 20px;">
            <div>
                <h2>Chào mừng, ${sessionScope.user}!</h2>
                <div style="margin-top: 5px;">
                    <span class="badge ${sessionScope.role == 'admin' ? 'badge-admin' : 'badge-user'}">
                        Quyền: ${sessionScope.role == 'admin' ? 'ADMIN' : 'USER'}
                    </span>
                    <span style="color: var(--text-secondary); font-size: 0.85rem; margin-left: 15px;">
                        Thời gian đăng nhập: ${sessionScope.loginTime}
                    </span>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary" style="padding: 8px 16px; font-size: 0.85rem;">Đăng xuất</a>
        </div>

        <h3 class="mb-20">Tổng quan dữ liệu</h3>
        <div class="dashboard-grid">
            <div class="metric-card" style="grid-column: span 1; border-color: var(--accent-blue);">
                <div class="metric-title" style="color: var(--accent-blue);">Tổng số sinh viên</div>
                <div class="metric-val">${totalStudents}</div>
                <div style="font-size: 0.75rem; color: var(--text-secondary);">sinh viên trong hệ thống</div>
            </div>
            
            <c:forEach items="${classCount}" var="entry">
                <div class="metric-card">
                    <div class="metric-title">Lớp ${entry.key}</div>
                    <div class="metric-val">${entry.value}</div>
                    <div style="font-size: 0.75rem; color: var(--text-secondary);">sinh viên</div>
                </div>
            </c:forEach>
        </div>

        <div style="display: flex; gap: 15px; margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/students" class="btn btn-primary">
                📊 Quản lý danh sách sinh viên
            </a>
            <c:if test="${sessionScope.role == 'admin'}">
                <a href="${pageContext.request.contextPath}/students?action=add" class="btn btn-secondary">
                    ➕ Thêm sinh viên mới
                </a>
            </c:if>
        </div>
    </div>
</body>
</html>