<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách Sinh Viên</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .container { margin-top: 50px; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center text-primary mb-4">Quản Lý Sinh Viên - JPA</h2>
        <div class="d-flex justify-content-between mb-3">
            <a href="${pageContext.request.contextPath}/sinh-vien/new" class="btn btn-success">
                <i class="fas fa-plus"></i> Thêm Sinh Viên Mới
            </a>
            <form action="${pageContext.request.contextPath}/sinh-vien/search" method="get" class="d-flex">
                <input type="text" name="keyword" class="form-control me-2" placeholder="Tìm kiếm theo tên..." value="${keyword}">
                <button type="submit" class="btn btn-outline-primary">Tìm</button>
            </form>
        </div>
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
                <tr>
                    <th>Mã SV</th>
                    <th>Họ Tên</th>
                    <th>Email</th>
                    <th>Chuyên Ngành</th>
                    <th>Điểm TB</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="sv" items="${listSinhVien}">
                    <tr>
                        <td><c:out value="${sv.maSv}" /></td>
                        <td><c:out value="${sv.hoTen}" /></td>
                        <td><c:out value="${sv.email}" /></td>
                        <td><c:out value="${sv.chuyenNganh}" /></td>
                        <td><c:out value="${sv.diemTb}" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/sinh-vien/edit?maSv=${sv.maSv}" class="btn btn-sm btn-primary">
                                <i class="fas fa-edit"></i> Sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/sinh-vien/delete?maSv=${sv.maSv}" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên này?');">
                                <i class="fas fa-trash"></i> Xóa
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
