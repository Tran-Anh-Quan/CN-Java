<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${diem == null ? 'Nhập điểm mới' : 'Cập nhật điểm'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <a href="diem">Điểm Sinh Viên</a>
        <span class="sep">›</span>
        <span class="current">${diem == null ? 'Nhập mới' : 'Cập nhật'}</span>
    </div>

    <div class="page-header">
        <h2>${diem == null ? 'Nhập điểm mới' : 'Cập nhật điểm'}</h2>
    </div>

    <form action="diem" method="post">
        <div class="form-group">
            <label for="id">Mã Quản Lý</label>
            <input type="text" id="id" name="id" value="${diem.id}" ${diem != null ? 'readonly' : ''} required placeholder="VD: DIEM001">
        </div>
        <div class="form-group">
            <label for="studentId">Mã Sinh Viên</label>
            <input type="text" id="studentId" name="studentId" value="${diem.studentId}" required placeholder="VD: SV001">
        </div>
        <div class="form-group">
            <label for="diemCC">Điểm Chuyên Cần (trọng số 10%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemCC" name="diemCC" value="${diem.diemCC}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-group">
            <label for="diemGK">Điểm Giữa Kỳ (trọng số 30%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemGK" name="diemGK" value="${diem.diemGK}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-group">
            <label for="diemCK">Điểm Cuối Kỳ (trọng số 60%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemCK" name="diemCK" value="${diem.diemCK}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-footer">
            <button type="submit">Lưu</button>
            <a href="diem" class="btn btn-secondary">Hủy</a>
        </div>
    </form>

</div>
</body>
</html>
