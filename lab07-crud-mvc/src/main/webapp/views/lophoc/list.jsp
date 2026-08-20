<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Lớp học - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Lớp Học</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sách Lớp học</h2>
            <div class="subtitle">Quản lý toàn bộ danh sách lớp học trong hệ thống</div>
        </div>
        <div class="header-actions">
            <a href="lophoc?action=new" class="btn btn-primary">+ Thêm lớp học mới</a>
        </div>
    </div>

    <div class="search-bar">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--text-muted)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <form action="lophoc" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" value="${keyword}" placeholder="Tìm kiếm theo mã hoặc tên lớp...">
            <button type="submit">Tìm kiếm</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã Lớp</th>
                    <th>Tên Lớp</th>
                    <th>Cố Vấn</th>
                    <th>Sĩ số</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="l" items="${lophocs}">
                    <tr>
                        <td><span class="badge badge-primary">${l.classId}</span></td>
                        <td class="fw-bold">${l.className}</td>
                        <td>${l.advisor}</td>
                        <td>${l.studentCount}</td>
                        <td>
                            <div class="actions">
                                <a href="lophoc?action=edit&id=${l.classId}" class="btn-action btn-edit">Sửa</a>
                                <a href="lophoc?action=delete&id=${l.classId}" class="btn-action btn-delete"
                                   onclick="return confirm('Xác nhận xóa lớp ${l.className}?')">Xóa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lophocs}">
                    <tr>
                        <td colspan="5">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
                                    <line x1="8" y1="21" x2="16" y2="21"/>
                                    <line x1="12" y1="17" x2="12" y2="21"/>
                                </svg>
                                <div>Không có dữ liệu lớp học.</div>
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
