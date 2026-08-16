<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Đăng Nhập Hệ Thống</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container login-container">
        <div class="text-center">
            <h2>Đăng Nhập</h2>
            <p style="color: var(--text-secondary); margin-bottom: 25px; font-size: 0.9rem;">
                Hệ thống Quản lý Sinh viên - Lab 6
            </p>
        </div>
        
        <c:if test="${not empty error}">
            <div class="text-danger mb-20 text-center" style="font-size: 0.9rem; font-weight: 500;">
                ⚠️ ${error}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tài khoản</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Nhập tài khoản (admin/user)..." required>
            </div>
            
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu (123456)..." required>
            </div>
            
            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">
                Đăng Nhập
            </button>
        </form>
    </div>
</body>
</html>