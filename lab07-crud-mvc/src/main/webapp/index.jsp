<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Lab 07 CRUD MVC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="topbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="brand">
            Lab 07 &mdash; CRUD MVC
        </a>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">Dang nhap</a>
    </div>

    <div class="hero">
        <h1>He thong Quan ly</h1>
        <p>Chon module ben duoi de bat dau quan ly du lieu cua ban.</p>
    </div>

    <nav class="menu">
        <a href="${pageContext.request.contextPath}/admin/sinhvien">Sinh Vien</a>
        <a href="${pageContext.request.contextPath}/admin/sach">Sach</a>
        <a href="${pageContext.request.contextPath}/admin/sanpham">San Pham</a>
        <a href="${pageContext.request.contextPath}/admin/lophoc">Lop Hoc</a>
        <a href="${pageContext.request.contextPath}/admin/diem">Diem Sinh Vien</a>
        <a href="${pageContext.request.contextPath}/admin/cart">Gio Hang</a>
    </nav>

</div>
</body>
</html>
