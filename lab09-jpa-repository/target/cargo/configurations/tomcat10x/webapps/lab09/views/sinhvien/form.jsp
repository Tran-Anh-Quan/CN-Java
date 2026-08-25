<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${sinhVien != null ? 'Cập nhật' : 'Thêm'} Sinh Viên</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">${sinhVien != null ? 'Cập nhật' : 'Thêm mới'} Sinh Viên</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${sinhVien != null}">
                            <form action="${pageContext.request.contextPath}/sinh-vien/update" method="post">
                        </c:if>
                        <c:if test="${sinhVien == null}">
                            <form action="${pageContext.request.contextPath}/sinh-vien/insert" method="post">
                        </c:if>

                            <div class="mb-3">
                                <label class="form-label">Mã SV</label>
                                <input type="text" class="form-control" name="maSv" value="<c:out value='${sinhVien.maSv}' />" required ${sinhVien != null ? 'readonly' : ''} />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Họ Tên</label>
                                <input type="text" class="form-control" name="hoTen" value="<c:out value='${sinhVien.hoTen}' />" required />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" value="<c:out value='${sinhVien.email}' />" required />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Chuyên Ngành</label>
                                <input type="text" class="form-control" name="chuyenNganh" value="<c:out value='${sinhVien.chuyenNganh}' />" required />
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Điểm TB</label>
                                <input type="number" step="0.1" class="form-control" name="diemTb" value="<c:out value='${sinhVien.diemTb}' />" required />
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/sinh-vien/list" class="btn btn-secondary">Quay Lại</a>
                                <button type="submit" class="btn btn-success">Lưu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
