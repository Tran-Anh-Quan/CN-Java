<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quan ly Diem Sinh Vien - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <span class="current">Diem Sinh Vien</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Diem Sinh Vien</h2>
            <div class="subtitle">Quan ly diem so va xep loai hoc tap</div>
        </div>
        <div class="header-actions">
            <a href="diem?action=new" class="btn btn-primary">+ Nhap diem moi</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Ma QL</th>
                    <th>Ma SV</th>
                    <th>Chuyen Can (10%)</th>
                    <th>Giua Ky (30%)</th>
                    <th>Cuoi Ky (60%)</th>
                    <th>Tong Ket</th>
                    <th>Xep Loai</th>
                    <th>Hanh dong</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="d" items="${danhSachDiem}">
                    <tr>
                        <td><span class="badge badge-primary">${d.id}</span></td>
                        <td><span class="badge badge-info">${d.studentId}</span></td>
                        <td>${d.diemCC}</td>
                        <td>${d.diemGK}</td>
                        <td>${d.diemCK}</td>
                        <td class="fw-bold">${d.tongKet}</td>
                        <td>
                            <c:choose>
                                <c:when test="${d.xepLoai == 'F'}">
                                    <span class="badge badge-danger">${d.xepLoai}</span>
                                </c:when>
                                <c:when test="${d.xepLoai == 'A'}">
                                    <span class="badge badge-success">${d.xepLoai}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-warning">${d.xepLoai}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="actions">
                                <a href="diem?action=edit&id=${d.id}" class="btn-action btn-edit">Sua</a>
                                <a href="diem?action=delete&id=${d.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xac nhan xoa ban ghi diem nay?')">Xoa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty danhSachDiem}">
                    <tr><td colspan="8" style="text-align:center; padding:2rem; color:var(--text-muted);">
                        Chua co du lieu diem.
                    </td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
