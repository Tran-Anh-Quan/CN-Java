<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lop == null ? 'Them lop hoc' : 'Cap nhat lop hoc'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="lophoc">Lop Hoc</a>
        <span class="sep">›</span>
        <span class="current">${lop == null ? 'Them moi' : 'Cap nhat'}</span>
    </div>

    <div class="page-header">
        <h2>${lop == null ? 'Them lop hoc moi' : 'Cap nhat lop hoc'}</h2>
    </div>

    <form action="lophoc" method="post">
        <div class="form-group">
            <label for="id">Ma Lop</label>
            <input type="text" id="id" name="id" value="${lop.classId}" ${lop != null ? 'readonly' : ''} required placeholder="VD: CNTT01">
        </div>
        <div class="form-group">
            <label for="name">Ten Lop</label>
            <input type="text" id="name" name="name" value="${lop.className}" required placeholder="Nhap ten lop">
        </div>
        <div class="form-group">
            <label for="advisor">Co van hoc tap</label>
            <input type="text" id="advisor" name="advisor" value="${lop.advisor}" required placeholder="Ten giao vien co van">
        </div>
        <div class="form-group">
            <label for="studentCount">Si so</label>
            <input type="number" id="studentCount" name="studentCount" value="${lop.studentCount}" required placeholder="0">
        </div>
        <div class="form-footer">
            <button type="submit">Luu</button>
            <a href="lophoc" class="btn btn-secondary">Huy</a>
        </div>
    </form>

</div>
</body>
</html>
