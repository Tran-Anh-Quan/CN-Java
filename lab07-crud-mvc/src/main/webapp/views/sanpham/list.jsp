<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quan ly San Pham - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">San Pham</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sach San Pham</h2>
            <div class="subtitle">Quan ly kho san pham va ton kho</div>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/admin/cart" class="btn btn-secondary">Gio hang</a>
            <a href="sanpham?action=new" class="btn btn-primary">+ Them san pham</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ma SP</th>
                    <th>Ten San Pham</th>
                    <th>Mo ta</th>
                    <th>Gia (VND)</th>
                    <th>So luong</th>
                    <th>Hanh dong</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td><span class="badge badge-primary">${p.id}</span></td>
                        <td class="fw-bold">${p.name}</td>
                        <td class="text-muted">${p.description}</td>
                        <td><span class="badge badge-success">${p.price}</span></td>
                        <td>${p.quantity}</td>
                        <td>
                            <div class="actions">
                                <a href="cart?action=add&id=${p.id}" class="btn-action btn-buy">Mua</a>
                                <a href="sanpham?action=edit&id=${p.id}" class="btn-action btn-edit">Sua</a>
                                <a href="sanpham?action=delete&id=${p.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xac nhan xoa san pham ${p.name}?')">Xoa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr><td colspan="6" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Chua co san pham nao.
                    </td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
