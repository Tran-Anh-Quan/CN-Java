<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="Đăng nhập - Lab 10 Secured App" />
</jsp:include>

<style>
    .auth-wrapper {
        min-height: calc(100vh - 120px);
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 2rem 1rem;
    }

    .auth-card {
        background: rgba(30, 41, 59, 0.85);
        backdrop-filter: blur(16px);
        border: 1px solid rgba(255, 255, 255, 0.12);
        border-radius: 20px;
        width: 100%;
        max-width: 460px;
        padding: 2.5rem;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
    }

    .auth-header {
        text-align: center;
        margin-bottom: 1.75rem;
    }

    .auth-header i {
        font-size: 3rem;
        background: linear-gradient(135deg, #8b5cf6, #3b82f6);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        margin-bottom: 0.75rem;
    }

    .auth-header h2 {
        font-size: 1.75rem;
        font-weight: 700;
        letter-spacing: -0.5px;
    }

    .auth-header p {
        color: var(--text-secondary);
        font-size: 0.9rem;
        margin-top: 0.4rem;
    }

    .tab-buttons {
        display: flex;
        background: rgba(15, 23, 42, 0.6);
        padding: 4px;
        border-radius: 12px;
        margin-bottom: 1.5rem;
        border: 1px solid var(--border-card);
    }

    .tab-btn {
        flex: 1;
        padding: 10px;
        border: none;
        background: transparent;
        color: var(--text-secondary);
        font-weight: 600;
        font-size: 0.9rem;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s ease;
    }

    .tab-btn.active {
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        color: #fff;
        box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
    }

    .form-group {
        margin-bottom: 1.25rem;
    }

    .form-group label {
        display: block;
        font-size: 0.85rem;
        font-weight: 600;
        color: var(--text-secondary);
        margin-bottom: 0.5rem;
    }

    .input-icon-wrapper {
        position: relative;
    }

    .input-icon-wrapper i {
        position: absolute;
        left: 14px;
        top: 50%;
        transform: translateY(-50%);
        color: var(--text-secondary);
        font-size: 1rem;
    }

    .form-control {
        width: 100%;
        padding: 12px 14px 12px 42px;
        background: rgba(15, 23, 42, 0.7);
        border: 1px solid var(--border-card);
        border-radius: 10px;
        color: var(--text-primary);
        font-size: 0.95rem;
        transition: all 0.2s ease;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--accent-purple);
        box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.25);
    }

    .select-control {
        padding-left: 42px;
        appearance: none;
        background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%2394a3b8'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-position: right 14px center;
        background-size: 16px;
    }

    .btn-submit {
        width: 100%;
        padding: 14px;
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        border: none;
        border-radius: 10px;
        color: #fff;
        font-weight: 700;
        font-size: 1rem;
        cursor: pointer;
        transition: all 0.2s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
        margin-top: 1rem;
    }

    .btn-submit:hover {
        opacity: 0.95;
        transform: translateY(-1px);
        box-shadow: 0 6px 20px rgba(139, 92, 246, 0.4);
    }

    .alert {
        padding: 12px 16px;
        border-radius: 10px;
        font-size: 0.88rem;
        margin-bottom: 1.25rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .alert-danger {
        background: rgba(239, 68, 68, 0.15);
        border: 1px solid rgba(239, 68, 68, 0.3);
        color: #fca5a5;
    }

    .alert-success {
        background: rgba(16, 185, 129, 0.15);
        border: 1px solid rgba(16, 185, 129, 0.3);
        color: #6ee7b7;
    }

    .alert-info {
        background: rgba(59, 130, 246, 0.15);
        border: 1px solid rgba(59, 130, 246, 0.3);
        color: #93c5fd;
    }

    .demo-credentials {
        margin-top: 1.5rem;
        padding-top: 1.25rem;
        border-top: 1px solid var(--border-card);
        font-size: 0.82rem;
        color: var(--text-secondary);
    }

    .demo-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: rgba(15, 23, 42, 0.5);
        padding: 8px 12px;
        border-radius: 8px;
        margin-top: 6px;
        border: 1px dashed var(--border-card);
    }
</style>

<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-header">
            <i class="fa-solid fa-user-lock"></i>
            <h2>Cổng Bảo Mật Lab 10</h2>
            <p>Xác thực người dùng & Phân quyền truy cập (RBAC)</p>
        </div>

        <c:if test="${not empty errorMessage || not empty sessionScope.errorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-circle-exclamation"></i>
                <span>${errorMessage != null ? errorMessage : sessionScope.errorMessage}</span>
            </div>
            <% session.removeAttribute("errorMessage"); %>
        </c:if>

        <c:if test="${not empty registerErrorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-circle-exclamation"></i>
                <span>${registerErrorMessage}</span>
            </div>
        </c:if>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                <span>${successMessage}</span>
            </div>
        </c:if>

        <c:if test="${param.logout == 'success'}">
            <div class="alert alert-info">
                <i class="fa-solid fa-circle-info"></i>
                <span>Bạn đã đăng xuất thành công!</span>
            </div>
        </c:if>

        <div class="tab-buttons">
            <button type="button" class="tab-btn ${empty registerErrorMessage ? 'active' : ''}" onclick="switchTab('login')">Đăng Nhập</button>
            <button type="button" class="tab-btn ${not empty registerErrorMessage ? 'active' : ''}" onclick="switchTab('register')">Đăng Ký</button>
        </div>

        <!-- LOGIN FORM -->
        <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post" style="display: ${empty registerErrorMessage ? 'block' : 'none'};">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-user"></i>
                    <input type="text" id="username" name="username" class="form-control" placeholder="Nhập username..." value="${username}" required autofocus>
                </div>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-key"></i>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu..." required>
                </div>
            </div>

            <button type="submit" class="btn-submit">
                <i class="fa-solid fa-right-to-bracket"></i> Đăng Nhập Hệ Thống
            </button>
        </form>

        <!-- REGISTER FORM -->
        <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post" style="display: ${not empty registerErrorMessage ? 'block' : 'none'};">
            <div class="form-group">
                <label for="reg_username">Tên đăng nhập</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-user"></i>
                    <input type="text" id="reg_username" name="username" class="form-control" placeholder="Tạo username..." value="${username}" required>
                </div>
            </div>

            <div class="form-group">
                <label for="reg_fullName">Họ và Tên</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-id-card"></i>
                    <input type="text" id="reg_fullName" name="fullName" class="form-control" placeholder="Nguyễn Văn A..." value="${fullName}" required>
                </div>
            </div>

            <div class="form-group">
                <label for="reg_email">Email liên hệ</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-envelope"></i>
                    <input type="email" id="reg_email" name="email" class="form-control" placeholder="email@eaut.edu.vn..." value="${email}" required>
                </div>
            </div>

            <div class="form-group">
                <label for="reg_password">Mật khẩu</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-lock"></i>
                    <input type="password" id="reg_password" name="password" class="form-control" placeholder="Nhập mật khẩu..." required>
                </div>
            </div>

            <div class="form-group">
                <label for="reg_role">Vai trò (Role)</label>
                <div class="input-icon-wrapper">
                    <i class="fa-solid fa-shield-cat"></i>
                    <select id="reg_role" name="roleName" class="form-control select-control">
                        <option value="ROLE_USER">USER - Người dùng thông thường</option>
                        <option value="ROLE_STAFF">STAFF - Nhân viên nghiệp vụ</option>
                        <option value="ROLE_ADMIN">ADMIN - Quản trị viên</option>
                    </select>
                </div>
            </div>

            <button type="submit" class="btn-submit">
                <i class="fa-solid fa-user-plus"></i> Tạo Tài Khoản
            </button>
        </form>

        <div class="demo-credentials">
            <div style="font-weight: 600; margin-bottom: 6px; color: var(--accent-purple);">
                <i class="fa-solid fa-lightbulb"></i> Tài khoản thử nghiệm có sẵn:
            </div>
            <div class="demo-box">
                <span><strong>Admin:</strong> admin / admin123</span>
                <span class="role-badge role-admin">ADMIN</span>
            </div>
            <div class="demo-box">
                <span><strong>Staff:</strong> staff / staff123</span>
                <span class="role-badge role-staff">STAFF</span>
            </div>
            <div class="demo-box">
                <span><strong>User:</strong> user / user123</span>
                <span class="role-badge role-user">USER</span>
            </div>
        </div>
    </div>
</div>

<script>
    function switchTab(tab) {
        const loginForm = document.getElementById('loginForm');
        const registerForm = document.getElementById('registerForm');
        const buttons = document.querySelectorAll('.tab-btn');

        if (tab === 'login') {
            loginForm.style.display = 'block';
            registerForm.style.display = 'none';
            buttons[0].classList.add('active');
            buttons[1].classList.remove('active');
        } else {
            loginForm.style.display = 'none';
            registerForm.style.display = 'block';
            buttons[0].classList.remove('active');
            buttons[1].classList.add('active');
        }
    }
</script>

<jsp:include page="/views/footer.jsp" />
