<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiet Sinh Vien - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chu</a>
        <span class="sep">›</span>
        <a href="sinhvien">Sinh Vien</a>
        <span class="sep">›</span>
        <span class="current">Chi tiet</span>
    </div>

    <div class="page-header">
        <h2>Chi tiet Sinh Vien</h2>
        <div class="header-actions">
            <a href="sinhvien?action=edit&id=${student.id}" class="btn btn-primary">Chinh sua</a>
        </div>
    </div>

    <div class="detail-card">
        <div class="detail-row">
            <div class="detail-label">Ma SV</div>
            <div class="detail-value"><span class="badge badge-info">${student.id}</span></div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Ho va Ten</div>
            <div class="detail-value fw-bold">${student.name}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Email</div>
            <div class="detail-value">${student.email}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Lop</div>
            <div class="detail-value">${student.className}</div>
        </div>
    </div>

    <a href="sinhvien" class="btn btn-secondary" style="margin-top:1rem; display:inline-flex;">Quay lai danh sach</a>

</div>
</body>
</html>
