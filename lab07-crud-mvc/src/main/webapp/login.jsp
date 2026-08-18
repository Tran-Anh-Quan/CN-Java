<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dang nhap - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">

<div class="login-card">
    <div class="login-logo">
        <h2>Dang nhap</h2>
        <p>Vui long nhap thong tin tai khoan</p>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" style="max-width:100%; padding:0; border:none; box-shadow:none; background:transparent; animation:none;">
        <div class="form-group">
            <label for="username">Tai khoan</label>
            <input type="text" id="username" name="username" placeholder="Nhap ten tai khoan" required>
        </div>
        <div class="form-group">
            <label for="password">Mat khau</label>
            <input type="password" id="password" name="password" placeholder="Nhap mat khau" required>
        </div>
        <div class="form-footer">
            <button type="submit" style="width:100%; justify-content:center;">Dang nhap</button>
        </div>
    </form>

    <p style="text-align:center; margin-top:1.5rem; font-size:0.85rem;">
        <a href="${pageContext.request.contextPath}/index.jsp">Ve trang chu</a>
    </p>
</div>

</body>
</html>
