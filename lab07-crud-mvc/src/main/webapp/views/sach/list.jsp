<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sách - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Sách</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sách Sách</h2>
            <div class="subtitle">Quản lý thư viện sách trong hệ thống</div>
        </div>
        <div class="header-actions">
            <a href="sach?action=new" class="btn btn-primary">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                Thêm sách mới
            </a>
        </div>
    </div>

    <div class="search-bar">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--text-muted)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <form action="sach" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" value="${keyword}" placeholder="Tìm kiếm theo tên hoặc tác giả...">
            <button type="submit">Tìm kiếm</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã</th>
                    <th>Tên Sách</th>
                    <th>Tác Giả</th>
                    <th>Nhà XB</th>
                    <th>Năm XB</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${books}">
                    <tr>
                        <td><span class="badge badge-primary">${s.id}</span></td>
                        <td class="fw-bold">${s.name}</td>
                        <td>${s.author}</td>
                        <td class="text-muted">${s.publisher}</td>
                        <td>${s.publishYear}</td>
                        <td>
                            <div class="actions">
                                <a href="sach?action=edit&id=${s.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="sach?action=delete&id=${s.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xác nhận xóa sách ${s.name}?')">Xóa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty books}">
                    <tr>
                        <td colspan="6">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M4 19.5A2.5 2.5 0 016.5 17H20"/>
                                    <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/>
                                </svg>
                                <div>Chưa có sách nào trong thư viện.</div>
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
