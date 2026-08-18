<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sach Sinh Vien - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">Sinh Vien</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sach Sinh Vien</h2>
            <div class="subtitle">Quan ly ho so sinh vien trong he thong</div>
        </div>
        <div class="header-actions">
            <a href="sinhvien?action=add" class="btn btn-primary">+ Them sinh vien</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ma SV</th>
                    <th>Ho Ten</th>
                    <th>Email</th>
                    <th>Lop</th>
                    <th>Hanh dong</th>
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
                                <a href="sinhvien?action=detail&id=${sv.id}" class="btn-action btn-view">Chi tiet</a>
                                <a href="sinhvien?action=edit&id=${sv.id}" class="btn-action btn-edit">Sua</a>
                                <a href="sinhvien?action=delete&id=${sv.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xac nhan xoa sinh vien ${sv.name}?')">Xoa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty students}">
                    <tr><td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Khong co sinh vien nao.
                    </td></tr>
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
