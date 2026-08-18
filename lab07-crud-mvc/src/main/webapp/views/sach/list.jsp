<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quan ly Sach - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">Sach</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Danh sach Sach</h2>
            <div class="subtitle">Quan ly thu vien sach trong he thong</div>
        </div>
        <div class="header-actions">
            <a href="sach?action=new" class="btn btn-primary">+ Them sach moi</a>
        </div>
    </div>

    <div class="search-bar">
        <form action="sach" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" value="${keyword}" placeholder="Tim kiem theo ten hoac tac gia...">
            <button type="submit">Tim kiem</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ma</th>
                    <th>Ten Sach</th>
                    <th>Tac Gia</th>
                    <th>Nha XB</th>
                    <th>Nam XB</th>
                    <th>Hanh dong</th>
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
                                <a href="sach?action=edit&id=${s.id}" class="btn-action btn-edit">Sua</a>
                                <a href="sach?action=delete&id=${s.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xac nhan xoa sach ${s.name}?')">Xoa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty books}">
                    <tr><td colspan="6" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Chua co sach nao trong thu vien.
                    </td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
