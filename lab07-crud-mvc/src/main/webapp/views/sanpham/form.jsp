<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${sp == null ? 'Them San Pham' : 'Cap nhat San Pham'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="sanpham">San Pham</a>
        <span class="sep">›</span>
        <span class="current">${sp == null ? 'Them moi' : 'Cap nhat'}</span>
    </div>

    <div class="page-header">
        <h2>${sp == null ? 'Them San Pham moi' : 'Cap nhat San Pham'}</h2>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="sanpham" method="post">
        <div class="form-group">
            <label for="id">Ma San Pham</label>
            <input type="text" id="id" name="id" value="${sp.id}" ${sp != null ? 'readonly' : ''} required placeholder="VD: SP001">
        </div>
        <div class="form-group">
            <label for="name">Ten San Pham</label>
            <input type="text" id="name" name="name" value="${sp.name}" required placeholder="Nhap ten san pham">
        </div>
        <div class="form-group">
            <label for="description">Mo ta</label>
            <input type="text" id="description" name="description" value="${sp.description}" placeholder="Mo ta ngan ve san pham">
        </div>
        <div class="form-group">
            <label for="price">Gia (VND)</label>
            <input type="number" step="0.01" id="price" name="price" value="${sp.price}" required placeholder="0">
        </div>
        <div class="form-group">
            <label for="quantity">So luong</label>
            <input type="number" id="quantity" name="quantity" value="${sp.quantity}" required placeholder="0">
        </div>
        <div class="form-footer">
            <button type="submit">Luu</button>
            <a href="sanpham" class="btn btn-secondary">Huy</a>
        </div>
    </form>

</div>
</body>
</html>
