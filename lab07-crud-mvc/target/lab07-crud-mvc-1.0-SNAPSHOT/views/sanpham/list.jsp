<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản Phẩm - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Sản Phẩm</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sách Sản Phẩm</h2>
            <div class="subtitle">Quản lý kho sản phẩm và tồn kho</div>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/admin/cart" class="btn btn-secondary">Giỏ hàng</a>
            <a href="sanpham?action=new" class="btn btn-primary">+ Thêm sản phẩm mới</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã SP</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Mô tả</th>
                    <th>Giá (VNĐ)</th>
                    <th>Số lượng</th>
                    <th>Hành động</th>
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
                                <a href="sanpham?action=edit&id=${p.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="sanpham?action=delete&id=${p.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xác nhận xóa sản phẩm ${p.name}?')">Xóa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr>
                        <td colspan="6">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/>
                                    <polyline points="3.27 6.96 12 12.01 20.73 6.96"/>
                                    <line x1="12" y1="22.08" x2="12" y2="12"/>
                                </svg>
                                <div>Chưa có sản phẩm nào.</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
