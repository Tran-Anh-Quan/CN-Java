<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${diem == null ? 'Nhap diem moi' : 'Cap nhat diem'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="diem">Diem Sinh Vien</a>
        <span class="sep">›</span>
        <span class="current">${diem == null ? 'Nhap moi' : 'Cap nhat'}</span>
    </div>

    <div class="page-header">
        <h2>${diem == null ? 'Nhap diem moi' : 'Cap nhat diem'}</h2>
    </div>

    <form action="diem" method="post">
        <div class="form-group">
            <label for="id">Ma Quan Ly</label>
            <input type="text" id="id" name="id" value="${diem.id}" ${diem != null ? 'readonly' : ''} required placeholder="VD: DIEM001">
        </div>
        <div class="form-group">
            <label for="studentId">Ma Sinh Vien</label>
            <input type="text" id="studentId" name="studentId" value="${diem.studentId}" required placeholder="VD: SV001">
        </div>
        <div class="form-group">
            <label for="diemCC">Diem Chuyen Can (trong so 10%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemCC" name="diemCC" value="${diem.diemCC}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-group">
            <label for="diemGK">Diem Giua Ky (trong so 30%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemGK" name="diemGK" value="${diem.diemGK}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-group">
            <label for="diemCK">Diem Cuoi Ky (trong so 60%)</label>
            <input type="number" step="0.1" min="0" max="10" id="diemCK" name="diemCK" value="${diem.diemCK}" required placeholder="0.0 - 10.0">
        </div>
        <div class="form-footer">
            <button type="submit">Luu</button>
            <a href="diem" class="btn btn-secondary">Huy</a>
        </div>
    </form>

</div>
</body>
</html>
