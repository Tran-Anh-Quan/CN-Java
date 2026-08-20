<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${student == null ? 'Thêm sinh viên' : 'Cập nhật sinh viên'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <a href="sinhvien">Sinh Viên</a>
        <span class="sep">›</span>
        <span class="current">${student == null ? 'Thêm mới' : 'Cập nhật'}</span>
    </div>

    <div class="page-header">
        <h2>${student == null ? 'Thêm sinh viên mới' : 'Cập nhật sinh viên'}</h2>
    </div>

    <form action="sinhvien" method="post">
        <input type="hidden" name="action" value="${student == null ? 'insert' : 'update'}">
        <div class="form-group">
            <label for="id">Mã Sinh Viên</label>
            <input type="text" id="id" name="id" value="${student.id}" ${student != null ? 'readonly' : ''} required placeholder="VD: SV001">
        </div>
        <div class="form-group">
            <label for="name">Họ và Tên</label>
            <input type="text" id="name" name="name" value="${student.name}" required placeholder="Nhập họ và tên">
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${student.email}" required placeholder="example@email.com">
        </div>
        <div class="form-group">
            <label for="className">Lớp</label>
            <input type="text" id="className" name="className" value="${student.className}" required placeholder="VD: CNTT01">
        </div>
        <div class="form-footer">
            <button type="submit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                Lưu
            </button>
            <a href="sinhvien" class="btn btn-secondary">Hủy</a>
        </div>
    </form>

</div>
</body>
</html>
