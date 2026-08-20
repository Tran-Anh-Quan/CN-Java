<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${sach == null ? 'Thêm sách' : 'Cập nhật sách'} - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <a href="sach">Sách</a>
        <span class="sep">›</span>
        <span class="current">${sach == null ? 'Thêm mới' : 'Cập nhật'}</span>
    </div>

    <div class="page-header">
        <h2>${sach == null ? 'Thêm sách mới' : 'Cập nhật sách'}</h2>
    </div>

    <form action="sach" method="post">
        <div class="form-group">
            <label for="id">Mã Sách</label>
            <input type="text" id="id" name="id" value="${sach.id}" ${sach != null ? 'readonly' : ''} required placeholder="VD: SACH001">
        </div>
        <div class="form-group">
            <label for="name">Tên Sách</label>
            <input type="text" id="name" name="name" value="${sach.name}" required placeholder="Nhập tên sách">
        </div>
        <div class="form-group">
            <label for="author">Tác giả</label>
            <input type="text" id="author" name="author" value="${sach.author}" required placeholder="Tên tác giả">
        </div>
        <div class="form-group">
            <label for="publisher">Nhà Xuất Bản</label>
            <input type="text" id="publisher" name="publisher" value="${sach.publisher}" required placeholder="Tên nhà xuất bản">
        </div>
        <div class="form-group">
            <label for="publishYear">Năm Xuất Bản</label>
            <input type="number" id="publishYear" name="publishYear" value="${sach.publishYear}" required placeholder="VD: 2024">
        </div>
        <div class="form-footer">
            <button type="submit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                Lưu
            </button>
            <a href="sach" class="btn btn-secondary">Hủy</a>
        </div>
    </form>

</div>
</body>
</html>
