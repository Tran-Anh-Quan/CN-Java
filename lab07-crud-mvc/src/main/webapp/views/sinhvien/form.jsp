<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${student == null ? 'Them sinh vien' : 'Cap nhat sinh vien'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="sinhvien">Sinh Vien</a>
        <span class="sep">›</span>
        <span class="current">${student == null ? 'Them moi' : 'Cap nhat'}</span>
    </div>

    <div class="page-header">
        <h2>${student == null ? 'Them sinh vien moi' : 'Cap nhat sinh vien'}</h2>
    </div>

    <form action="sinhvien" method="post">
        <input type="hidden" name="action" value="${student == null ? 'insert' : 'update'}">
        <div class="form-group">
            <label for="id">Ma Sinh Vien</label>
            <input type="text" id="id" name="id" value="${student.id}" ${student != null ? 'readonly' : ''} placeholder="VD: SV001">
        </div>
        <div class="form-group">
            <label for="name">Ho va Ten</label>
            <input type="text" id="name" name="name" value="${student.name}" placeholder="Nhap ho va ten">
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="text" id="email" name="email" value="${student.email}" placeholder="example@email.com">
        </div>
        <div class="form-group">
            <label for="className">Lop</label>
            <input type="text" id="className" name="className" value="${student.className}" placeholder="VD: CNTT01">
        </div>
        <div class="form-footer">
            <button type="submit">Luu</button>
            <a href="sinhvien" class="btn btn-secondary">Huy</a>
        </div>
    </form>

</div>
</body>
</html>
