<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${sach == null ? 'Them sach' : 'Cap nhat sach'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="sach">Sach</a>
        <span class="sep">›</span>
        <span class="current">${sach == null ? 'Them moi' : 'Cap nhat'}</span>
    </div>

    <div class="page-header">
        <h2>${sach == null ? 'Them sach moi' : 'Cap nhat sach'}</h2>
    </div>

    <form action="sach" method="post">
        <div class="form-group">
            <label for="id">Ma Sach</label>
            <input type="text" id="id" name="id" value="${sach.id}" ${sach != null ? 'readonly' : ''} required placeholder="VD: SACH001">
        </div>
        <div class="form-group">
            <label for="name">Ten Sach</label>
            <input type="text" id="name" name="name" value="${sach.name}" required placeholder="Nhap ten sach">
        </div>
        <div class="form-group">
            <label for="author">Tac gia</label>
            <input type="text" id="author" name="author" value="${sach.author}" required placeholder="Ten tac gia">
        </div>
        <div class="form-group">
            <label for="publisher">Nha Xuat Ban</label>
            <input type="text" id="publisher" name="publisher" value="${sach.publisher}" required placeholder="Ten nha xuat ban">
        </div>
        <div class="form-group">
            <label for="publishYear">Nam Xuat Ban</label>
            <input type="number" id="publishYear" name="publishYear" value="${sach.publishYear}" required placeholder="VD: 2024">
        </div>
        <div class="form-footer">
            <button type="submit">Luu</button>
            <a href="sach" class="btn btn-secondary">Huy</a>
        </div>
    </form>

</div>
</body>
</html>
