<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="Hồ Sơ Cá Nhân - User - Lab 10" />
</jsp:include>

<style>
    .user-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
        gap: 1.5rem;
    }

    .profile-card {
        background: var(--bg-card);
        border: 1px solid var(--border-card);
        border-radius: 20px;
        padding: 2rem;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4);
    }

    .profile-header {
        display: flex;
        align-items: center;
        gap: 16px;
        margin-bottom: 1.5rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid var(--border-card);
    }

    .profile-avatar {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        color: #fff;
    }

    .form-group {
        margin-bottom: 1.25rem;
    }

    .form-group label {
        display: block;
        font-size: 0.85rem;
        font-weight: 600;
        color: var(--text-secondary);
        margin-bottom: 0.4rem;
    }

    .form-control {
        width: 100%;
        padding: 10px 14px;
        background: rgba(15, 23, 42, 0.7);
        border: 1px solid var(--border-card);
        border-radius: 10px;
        color: var(--text-primary);
        font-size: 0.95rem;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--accent-purple);
    }

    .btn-submit {
        width: 100%;
        padding: 12px;
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        border: none;
        border-radius: 10px;
        color: #fff;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.2s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
    }

    .btn-submit:hover {
        opacity: 0.95;
    }

    .alert {
        padding: 10px 14px;
        border-radius: 8px;
        font-size: 0.88rem;
        margin-bottom: 1rem;
    }

    .alert-success {
        background: rgba(16, 185, 129, 0.15);
        border: 1px solid rgba(16, 185, 129, 0.3);
        color: #6ee7b7;
    }

    .alert-danger {
        background: rgba(239, 68, 68, 0.15);
        border: 1px solid rgba(239, 68, 68, 0.3);
        color: #fca5a5;
    }
</style>

<div class="main-container">
    <div class="user-grid">
        <!-- BÀI 8: UPDATE PROFILE -->
        <div class="profile-card">
            <div class="profile-header">
                <div class="profile-avatar">
                    <i class="fa-solid fa-id-card"></i>
                </div>
                <div>
                    <h3 style="font-size: 1.3rem;">Thông Tin Cá Nhân</h3>
                    <p style="color: var(--text-secondary); font-size: 0.85rem;">Cập nhật Họ và Tên, Email liên hệ</p>
                </div>
            </div>

            <c:if test="${not empty profileSuccessMessage}">
                <div class="alert alert-success">
                    <i class="fa-solid fa-check"></i> ${profileSuccessMessage}
                </div>
            </c:if>
            <c:if test="${not empty profileErrorMessage}">
                <div class="alert alert-danger">
                    <i class="fa-solid fa-exclamation"></i> ${profileErrorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/user/profile/update" method="post">
                <div class="form-group">
                    <label>Tên đăng nhập (Username)</label>
                    <input type="text" class="form-control" value="${sessionScope.currentUser.username}" readonly style="opacity: 0.7;">
                </div>

                <div class="form-group">
                    <label>Họ và Tên</label>
                    <input type="text" name="fullName" class="form-control" value="${sessionScope.currentUser.fullName}" required>
                </div>

                <div class="form-group">
                    <label>Email liên hệ</label>
                    <input type="email" name="email" class="form-control" value="${sessionScope.currentUser.email}" required>
                </div>

                <div class="form-group">
                    <label>Vai trò hệ thống</label>
                    <div style="display: flex; gap: 8px; margin-top: 4px;">
                        <c:forEach var="role" items="${sessionScope.currentUser.roles}">
                            <span class="role-badge ${role.name == 'ROLE_ADMIN' ? 'role-admin' : (role.name == 'ROLE_STAFF' ? 'role-staff' : 'role-user')}">
                                ${role.name.replace('ROLE_', '')}
                            </span>
                        </c:forEach>
                    </div>
                </div>

                <button type="submit" class="btn-submit">
                    <i class="fa-solid fa-floppy-disk"></i> Cập Nhật Hồ Sơ
                </button>
            </form>
        </div>

        <!-- BÀI 9: CHANGE PASSWORD -->
        <div class="profile-card">
            <div class="profile-header">
                <div class="profile-avatar" style="background: linear-gradient(135deg, #ec4899, #8b5cf6);">
                    <i class="fa-solid fa-key"></i>
                </div>
                <div>
                    <h3 style="font-size: 1.3rem;">Đổi Mật Khẩu</h3>
                    <p style="color: var(--text-secondary); font-size: 0.85rem;">Nhập mật khẩu hiện tại và mật khẩu mới</p>
                </div>
            </div>

            <c:if test="${not empty pwdSuccessMessage}">
                <div class="alert alert-success">
                    <i class="fa-solid fa-check"></i> ${pwdSuccessMessage}
                </div>
            </c:if>
            <c:if test="${not empty pwdErrorMessage}">
                <div class="alert alert-danger">
                    <i class="fa-solid fa-exclamation"></i> ${pwdErrorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/user/change-password" method="post">
                <div class="form-group">
                    <label>Mật khẩu hiện tại</label>
                    <input type="password" name="oldPassword" class="form-control" placeholder="Mật khẩu cũ..." required>
                </div>

                <div class="form-group">
                    <label>Mật khẩu mới</label>
                    <input type="password" name="newPassword" class="form-control" placeholder="Tối thiểu 4 ký tự..." required>
                </div>

                <div class="form-group">
                    <label>Xác nhận mật khẩu mới</label>
                    <input type="password" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới..." required>
                </div>

                <button type="submit" class="btn-submit" style="background: linear-gradient(135deg, #ec4899, #8b5cf6);">
                    <i class="fa-solid fa-lock-open"></i> Đổi Mật Khẩu
                </button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/views/footer.jsp" />
