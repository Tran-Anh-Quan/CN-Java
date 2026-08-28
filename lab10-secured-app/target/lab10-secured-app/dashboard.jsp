<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="Dashboard - Lab 10 Secured App" />
</jsp:include>

<style>
    .welcome-banner {
        background: linear-gradient(135deg, rgba(139, 92, 246, 0.2), rgba(59, 130, 246, 0.2));
        border: 1px solid var(--border-card);
        border-radius: 20px;
        padding: 2.5rem;
        margin-bottom: 2rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 20px;
    }

    .welcome-text h1 {
        font-size: 2rem;
        font-weight: 800;
        margin-bottom: 0.5rem;
    }

    .welcome-text p {
        color: var(--text-secondary);
        font-size: 1rem;
    }

    .grid-container {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
        gap: 1.5rem;
        margin-bottom: 2rem;
    }

    .card {
        background: var(--bg-card);
        border: 1px solid var(--border-card);
        border-radius: 16px;
        padding: 1.75rem;
        transition: transform 0.2s ease, border-color 0.2s ease;
    }

    .card:hover {
        transform: translateY(-4px);
        border-color: rgba(139, 92, 246, 0.4);
    }

    .card-header {
        display: flex;
        align-items: center;
        gap: 14px;
        margin-bottom: 1.25rem;
    }

    .card-icon {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.4rem;
    }

    .icon-admin {
        background: rgba(239, 68, 68, 0.2);
        color: #fca5a5;
    }

    .icon-staff {
        background: rgba(245, 158, 11, 0.2);
        color: #fde047;
    }

    .icon-user {
        background: rgba(59, 130, 246, 0.2);
        color: #93c5fd;
    }

    .icon-security {
        background: rgba(16, 185, 129, 0.2);
        color: #6ee7b7;
    }

    .card-title {
        font-size: 1.2rem;
        font-weight: 700;
    }

    .card-body p {
        color: var(--text-secondary);
        font-size: 0.95rem;
        line-height: 1.6;
        margin-bottom: 1.25rem;
    }

    .btn-action {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 10px 18px;
        border-radius: 10px;
        font-weight: 600;
        font-size: 0.9rem;
        text-decoration: none;
        transition: all 0.2s ease;
    }

    .btn-admin-action {
        background: rgba(239, 68, 68, 0.2);
        color: #fca5a5;
        border: 1px solid rgba(239, 68, 68, 0.4);
    }

    .btn-staff-action {
        background: rgba(245, 158, 11, 0.2);
        color: #fde047;
        border: 1px solid rgba(245, 158, 11, 0.4);
    }

    .btn-user-action {
        background: rgba(59, 130, 246, 0.2);
        color: #93c5fd;
        border: 1px solid rgba(59, 130, 246, 0.4);
    }

    .table-card {
        background: var(--bg-card);
        border: 1px solid var(--border-card);
        border-radius: 16px;
        overflow: hidden;
        margin-top: 2rem;
    }

    .table-title {
        padding: 1.25rem 1.5rem;
        border-bottom: 1px solid var(--border-card);
        font-weight: 700;
        font-size: 1.1rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .log-table {
        width: 100%;
        border-collapse: collapse;
        text-align: left;
    }

    .log-table th {
        background: rgba(15, 23, 42, 0.8);
        padding: 0.85rem 1.25rem;
        font-weight: 700;
        font-size: 0.8rem;
        color: var(--text-secondary);
        text-transform: uppercase;
    }

    .log-table td {
        padding: 0.85rem 1.25rem;
        border-bottom: 1px solid rgba(255, 255, 255, 0.05);
        font-size: 0.9rem;
    }
</style>

<div class="main-container">
    <div class="welcome-banner">
        <div class="welcome-text">
            <h1>Xin chào, ${sessionScope.currentUser.fullName}! 👋</h1>
            <p>Chào mừng bạn đến với hệ thống quản lý phân quyền bảo mật <strong>Lab 10 (RBAC)</strong>.</p>
        </div>
        <div style="text-align: right;">
            <span style="font-size: 0.85rem; color: var(--text-secondary);">Tổng số người dùng:</span>
            <div style="font-size: 1.75rem; font-weight: 800; color: var(--accent-purple);">
                ${totalUsers} tài khoản
            </div>
        </div>
    </div>

    <!-- ROLE FLAGS -->
    <c:set var="isAdmin" value="false" />
    <c:set var="isStaff" value="false" />
    <c:forEach var="r" items="${sessionScope.currentUser.roles}">
        <c:if test="${r.name == 'ROLE_ADMIN'}"><c:set var="isAdmin" value="true" /></c:if>
        <c:if test="${r.name == 'ROLE_STAFF'}"><c:set var="isStaff" value="true" /></c:if>
    </c:forEach>

    <div class="grid-container">
        <!-- ADMIN MODULE CARD -->
        <c:if test="${isAdmin}">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon icon-admin">
                        <i class="fa-solid fa-user-gear"></i>
                    </div>
                    <div>
                        <div class="card-title">Quản Trị Viên (Admin)</div>
                        <span style="font-size: 0.8rem; color: var(--text-secondary);">URL: /admin/*</span>
                    </div>
                </div>
                <div class="card-body">
                    <p>Quản lý toàn bộ người dùng, thêm/sửa tài khoản, đổi vai trò, khóa/mở khóa tài khoản và tìm kiếm theo Email.</p>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn-action btn-admin-action">
                        <i class="fa-solid fa-user-shield"></i> Mở Trang Admin
                    </a>
                </div>
            </div>
        </c:if>

        <!-- STAFF MODULE CARD -->
        <c:if test="${isAdmin || isStaff}">
            <div class="card">
                <div class="card-header">
                    <div class="card-icon icon-staff">
                        <i class="fa-solid fa-briefcase"></i>
                    </div>
                    <div>
                        <div class="card-title">Nghiệp Vụ Nhân Viên (Staff)</div>
                        <span style="font-size: 0.8rem; color: var(--text-secondary);">URL: /staff/*</span>
                    </div>
                </div>
                <div class="card-body">
                    <p>Thực hiện các thao tác nghiệp vụ hệ thống dành cho Nhân viên (được truy cập bởi STAFF và ADMIN).</p>
                    <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn-action btn-staff-action">
                        <i class="fa-solid fa-cubes"></i> Mở Trang Nghiệp Vụ
                    </a>
                </div>
            </div>
        </c:if>

        <!-- USER PROFILE CARD -->
        <div class="card">
            <div class="card-header">
                <div class="card-icon icon-user">
                    <i class="fa-solid fa-id-card"></i>
                </div>
                <div>
                    <div class="card-title">Hồ Sơ Cá Nhân (User)</div>
                    <span style="font-size: 0.8rem; color: var(--text-secondary);">URL: /user/*</span>
                </div>
            </div>
            <div class="card-body">
                <p>Xem thông tin chi tiết, cập nhật Họ tên, Email liên hệ và đổi Mật khẩu cá nhân an toàn.</p>
                <a href="${pageContext.request.contextPath}/user/profile" class="btn-action btn-user-action">
                    <i class="fa-solid fa-user-pen"></i> Quản Lý Hồ Sơ
                </a>
            </div>
        </div>
    </div>

    <!-- AUDIT LOGS SECTION (Bài 12) - Chỉ ADMIN mới thấy -->
    <c:if test="${isAdmin}">
    <div class="table-card">
        <div class="table-title">
            <i class="fa-solid fa-clock-rotate-left" style="color: var(--accent-purple);"></i>
            <span>Nhật Ký Đăng Nhập & Thao Tác Hệ Thống (Audit Logs)</span>
        </div>
        <table class="log-table">
            <thead>
                <tr>
                    <th>Thời gian</th>
                    <th>Tài khoản</th>
                    <th>Hành động</th>
                    <th>Chi tiết thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="log" items="${recentLogs}">
                    <tr>
                        <td style="color: var(--text-secondary); width: 180px;">${log.createdAt}</td>
                        <td><strong>${log.username}</strong></td>
                        <td>
                            <span class="role-badge ${log.action.contains('LOGIN') ? 'role-user' : (log.action.contains('ADMIN') || log.action.contains('LOCK') ? 'role-admin' : 'role-staff')}">
                                ${log.action}
                            </span>
                        </td>
                        <td>${log.details}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    </c:if>
</div>

<jsp:include page="/views/footer.jsp" />
