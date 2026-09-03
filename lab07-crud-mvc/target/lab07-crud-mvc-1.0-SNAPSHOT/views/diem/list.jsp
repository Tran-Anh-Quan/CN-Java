<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Điểm Sinh Viên - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <span class="current">Điểm Sinh Viên</span>
    </div>

    <div class="page-header">
        <div>
            <h2>Điểm Sinh Viên</h2>
            <div class="subtitle">Quản lý điểm số và xếp loại học tập</div>
        </div>
        <div class="header-actions">
            <a href="diem?action=new" class="btn btn-primary">+ Nhập điểm mới</a>
        </div>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã QL</th>
                    <th>Mã SV</th>
                    <th>Chuyên Cần (10%)</th>
                    <th>Giữa Kỳ (30%)</th>
                    <th>Cuối Kỳ (60%)</th>
                    <th>Tổng Kết</th>
                    <th>Xếp Loại</th>
                    <th>Hành động</th>
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
                                <a href="diem?action=edit&id=${d.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="diem?action=delete&id=${d.id}" class="btn-action btn-delete"
                                   onclick="return confirm('Xác nhận xóa bản ghi điểm này?')">Xóa</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty danhSachDiem}">
                    <tr>
                        <td colspan="8">
                            <div class="empty-state">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M22 11.08V12a10 10 0 11-5.93-9.14"/>
                                    <polyline points="22 4 12 14.01 9 11.01"/>
                                </svg>
                                <div>Chưa có dữ liệu điểm.</div>
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
