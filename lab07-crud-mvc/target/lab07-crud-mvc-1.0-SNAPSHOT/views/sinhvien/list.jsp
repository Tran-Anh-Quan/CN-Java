<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Sinh Viên - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Sinh Viên</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sách Sinh Viên</h2>
            <div class="subtitle">Quản lý hồ sơ sinh viên trong hệ thống</div>
        </div>
        <div class="header-actions">
            <a href="sinhvien?action=new" class="btn btn-primary">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                Thêm sinh viên
            </a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã SV</th>
                    <th>Họ Tên</th>
                    <th>Email</th>
                    <th>Lớp</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="sv" items="${students}">
                    <tr>
                        <td><span class="badge badge-info">${sv.id}</span></td>
                        <td class="fw-bold">${sv.name}</td>
                        <td class="text-muted">${sv.email}</td>
                        <td>${sv.className}</td>
                        <td>
                            <div class="actions">
                                <a href="sinhvien?action=detail&id=${sv.id}" class="btn-action btn-view">Chi tiết</a>
                                <a href="sinhvien?action=edit&id=${sv.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="sinhvien?action=delete&id=${sv.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xác nhận xóa sinh viên ${sv.name}?')">Xóa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty students}">
                    <tr>
                        <td colspan="5">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/>
                                    <circle cx="9" cy="7" r="4"/>
                                    <path d="M23 21v-2a4 4 0 00-3-3.87"/>
                                    <path d="M16 3.13a4 4 0 010 7.75"/>
                                </svg>
                                <div>Không có sinh viên nào.</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}"><strong>${i}</strong></c:when>
                    <c:otherwise><a href="sinhvien?page=${i}">${i}</a></c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:if>

</div>
</body>
</html>
