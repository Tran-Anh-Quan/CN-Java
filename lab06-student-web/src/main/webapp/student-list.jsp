<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh Sách Sinh Viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="flex-between mb-20">
            <div>
                <h2>Danh Sách Sinh Viên</h2>
                <span class="badge ${sessionScope.role == 'admin' ? 'badge-admin' : 'badge-user'}">
                    Tài khoản: ${sessionScope.user} (${sessionScope.role == 'admin' ? 'ADMIN' : 'USER'})
                </span>
            </div>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Quay lại Dashboard</a>
        </div>

        <!-- Search Form (Bài 6) -->
        <form action="${pageContext.request.contextPath}/students" method="get" class="search-form">
            <input type="text" name="search" class="form-control" placeholder="Tìm kiếm sinh viên theo tên..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            <c:if test="${not empty param.search}">
                <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">Đặt lại</a>
            </c:if>
        </form>

        <c:choose>
            <c:when test="${empty students}">
                <div class="text-center text-danger mb-20" style="padding: 20px; background: rgba(239, 68, 68, 0.05); border: 1px dashed rgba(239, 68, 68, 0.3); border-radius: 8px; font-weight: 500;">
                    🔍 Không tìm thấy sinh viên nào phù hợp.
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Mã sinh viên</th>
                                <th>Họ tên</th>
                                <th>Lớp</th>
                                <th>Email</th>
                                <c:if test="${sessionScope.role == 'admin'}">
                                    <th>Hành động</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${students}" var="s">
                                <tr>
                                    <td><strong>${s.id}</strong></td>
                                    <td>${s.name}</td>
                                    <td><span class="badge badge-user">${s.clazz}</span></td>
                                    <td>${s.email}</td>
                                    <c:if test="${sessionScope.role == 'admin'}">
                                        <td class="actions-cell">
                                            <a href="${pageContext.request.contextPath}/students?action=edit&id=${s.id}">✏️ Sửa</a> | 
                                            <a href="${pageContext.request.contextPath}/students?action=delete&id=${s.id}" class="delete-act" onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên ${s.name}?')">🗑️ Xóa</a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 30px; display: flex; gap: 15px;">
            <c:if test="${sessionScope.role == 'admin'}">
                <a href="${pageContext.request.contextPath}/students?action=add" class="btn btn-primary">
                    ➕ Thêm sinh viên mới
                </a>
            </c:if>
        </div>
    </div>
</body>
</html>