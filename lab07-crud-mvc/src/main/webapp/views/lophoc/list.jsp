<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quan ly Lop hoc - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">Lop Hoc</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sach Lop hoc</h2>
            <div class="subtitle">Quan ly toan bo danh sach lop hoc trong he thong</div>
        </div>
        <div class="header-actions">
            <a href="lophoc?action=new" class="btn btn-primary">+ Them lop hoc</a>
        </div>
    </div>

    <div class="search-bar">
        <form action="lophoc" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" value="${keyword}" placeholder="Tim kiem theo ma hoac ten lop...">
            <button type="submit">Tim kiem</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ma Lop</th>
                    <th>Ten Lop</th>
                    <th>Co Van</th>
                    <th>Si so</th>
                    <th>Hanh dong</th>
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
                                <a href="lophoc?action=edit&id=${l.classId}" class="btn-action btn-edit">Sua</a>
                                <a href="lophoc?action=delete&id=${l.classId}" class="btn-action btn-delete"
                                   onclick="return confirm('Xac nhan xoa lop ${l.className}?')">Xoa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lophocs}">
                    <tr><td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Khong co du lieu lop hoc.
                    </td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
