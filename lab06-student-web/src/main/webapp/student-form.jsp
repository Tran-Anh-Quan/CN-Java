<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${not empty student ? 'Cập Nhật Sinh Viên' : 'Thêm Sinh Viên'}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container login-container">
        <h2>${not empty student ? 'Cập Nhật Sinh Viên' : 'Thêm Sinh Viên Mới'}</h2>
        <p style="color: var(--text-secondary); margin-bottom: 25px; font-size: 0.9rem;">
            ${not empty student ? 'Thay đổi thông tin sinh viên' : 'Nhập thông tin cho sinh viên mới'}
        </p>

        <form action="${pageContext.request.contextPath}/students" method="post">
            <!-- Hidden field to define action (add vs update) -->
            <input type="hidden" name="formAction" value="${not empty student ? 'update' : 'add'}">

            <div class="form-group">
                <label for="id">Mã sinh viên</label>
                <input type="text" id="id" name="id" class="form-control" 
                       value="${student.id}" 
                       ${not empty student ? 'readonly' : ''} 
                       placeholder="VD: SV06" required>
                <c:if test="${not empty student}">
                    <small style="color: var(--text-secondary); margin-top: 5px; font-size: 0.75rem;">
                        ⚠️ Không thể thay đổi mã sinh viên
                    </small>
                </c:if>
            </div>

            <div class="form-group">
                <label for="name">Họ tên</label>
                <input type="text" id="name" name="name" class="form-control" 
                       value="${student.name}" 
                       placeholder="VD: Nguyễn Văn A" required>
            </div>

            <div class="form-group">
                <label for="clazz">Lớp</label>
                <input type="text" id="clazz" name="clazz" class="form-control" 
                       value="${student.clazz}" 
                       placeholder="VD: CNTT1" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="${student.email}" 
                       placeholder="VD: email@example.com" required>
            </div>

            <div style="display: flex; gap: 10px; margin-top: 25px;">
                <button type="submit" class="btn btn-primary" style="flex: 1;">Lưu</button>
                <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary" style="flex: 1;">Hủy</a>
            </div>
        </form>
    </div>
</body>
</html>