<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Sinh Viên - Lab 07</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page-wrapper">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a>
        <span class="sep">›</span>
        <a href="sinhvien">Sinh Viên</a>
        <span class="sep">›</span>
        <span class="current">Chi tiết</span>
    </div>

    <div class="page-header">
        <h2>Chi tiết Sinh Viên</h2>
        <div class="header-actions">
            <a href="sinhvien?action=edit&id=${student.id}" class="btn btn-primary">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                Chỉnh sửa
            </a>
        </div>
    </div>

    <div class="detail-card">
        <div class="detail-row">
            <div class="detail-label">Mã SV</div>
            <div class="detail-value"><span class="badge badge-info">${student.id}</span></div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Họ và Tên</div>
            <div class="detail-value fw-bold">${student.name}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Email</div>
            <div class="detail-value">${student.email}</div>
        </div>
        <div class="detail-row">
            <div class="detail-label">Lớp</div>
            <div class="detail-value">${student.className}</div>
        </div>
    </div>

    <a href="sinhvien" class="btn btn-secondary" style="margin-top:1.25rem; display:inline-flex;">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
        Quay lại danh sách
    </a>

</div>
</body>
</html>
