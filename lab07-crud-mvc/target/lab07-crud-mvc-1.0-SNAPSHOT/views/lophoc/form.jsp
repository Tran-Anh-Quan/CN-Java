<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lop == null ? 'Thêm lớp học' : 'Cập nhật lớp học'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <a href="lophoc">Lớp Học</a>
        <span class="sep">›</span>
        <span class="current">${lop == null ? 'Thêm mới' : 'Cập nhật'}</span>
    </div>

    <div class="page-header">
        <h2>${lop == null ? 'Thêm lớp học mới' : 'Cập nhật lớp học'}</h2>
    </div>

    <form action="lophoc" method="post">
        <div class="form-group">
            <label for="id">Mã Lớp</label>
            <input type="text" id="id" name="id" value="${lop.classId}" ${lop != null ? 'readonly' : ''} required placeholder="VD: CNTT01">
        </div>
        <div class="form-group">
            <label for="name">Tên Lớp</label>
            <input type="text" id="name" name="name" value="${lop.className}" required placeholder="Nhập tên lớp">
        </div>
        <div class="form-group">
            <label for="advisor">Cố vấn học tập</label>
            <input type="text" id="advisor" name="advisor" value="${lop.advisor}" required placeholder="Tên giáo viên cố vấn">
        </div>
        <div class="form-group">
            <label for="studentCount">Sĩ số</label>
            <input type="number" id="studentCount" name="studentCount" value="${lop.studentCount}" required placeholder="0">
        </div>
        <div class="form-footer">
            <button type="submit">Lưu</button>
            <a href="lophoc" class="btn btn-secondary">Hủy</a>
        </div>
    </form>

</div>
</body>
</html>
