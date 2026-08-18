<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gio hang - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">Gio Hang</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Gio hang cua ban</h2>
            <div class="subtitle">Xem lai va cap nhat don hang truoc khi thanh toan</div>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/admin/sanpham" class="btn btn-secondary">Tiep tuc mua hang</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ten San Pham</th>
                    <th>Don Gia (VND)</th>
                    <th>So Luong</th>
                    <th>Thanh Tien (VND)</th>
                    <th>Hanh dong</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${sessionScope.cart}">
                    <tr>
                        <td class="fw-bold">${item.product.name}</td>
                        <td>${item.product.price}</td>
                        <td>
                            <form action="cart" method="post" class="cart-qty-form">
                                <input type="hidden" name="id" value="${item.product.id}">
                                <input type="number" name="quantity" value="${item.quantity}" min="1">
                                <button type="submit">Cap nhat</button>
                            </form>
                        </td>
                        <td><span class="badge badge-success">${item.totalPrice}</span></td>
                        <td>
                            <a href="cart?action=remove&id=${item.product.id}" class="btn-action btn-delete"
                               onclick="return confirm('Xoa san pham nay khoi gio hang?')">Xoa</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty sessionScope.cart}">
                    <tr><td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Gio hang cua ban dang trong.
                    </td></tr>
                </c:if>
            </tbody>
        </table>
        <c:if test="${not empty sessionScope.cart}">
            <div class="total-bar">
                Tong tien: <span>${totalMoney} VND</span>
            </div>
        </c:if>
    </div>

</div>
</body>
</html>
