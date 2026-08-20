<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Giỏ Hàng</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Giỏ hàng của bạn</h2>
            <div class="subtitle">Xem lại và cập nhật đơn hàng trước khi thanh toán</div>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/admin/sanpham" class="btn btn-secondary">Tiếp tục mua hàng</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Tên Sản Phẩm</th>
                    <th>Đơn Giá (VNĐ)</th>
                    <th>Số Lượng</th>
                    <th>Thành Tiền (VNĐ)</th>
                    <th>Hành động</th>
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
                                <button type="submit">Cập nhật</button>
                            </form>
                        </td>
                        <td><span class="badge badge-success">${item.totalPrice}</span></td>
                        <td>
                            <a href="cart?action=remove&id=${item.product.id}" class="btn-action btn-delete"
                               onclick="return confirm('Xóa sản phẩm này khỏi giỏ hàng?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty sessionScope.cart}">
                    <tr>
                        <td colspan="5">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <circle cx="9" cy="21" r="1"/>
                                    <circle cx="20" cy="21" r="1"/>
                                    <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/>
                                </svg>
                                <div>Giỏ hàng của bạn đang trống.</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
        <c:if test="${not empty sessionScope.cart}">
            <div class="total-bar">
                Tổng tiền: <span>${totalMoney} VNĐ</span>
            </div>
        </c:if>
    </div>

</div>
</body>
</html>
