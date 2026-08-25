<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="Quản lý Người Dùng - Admin - Lab 10" />
</jsp:include>

<style>
    .admin-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 2rem;
        flex-wrap: wrap;
        gap: 15px;
    }

    .admin-title h2 {
        font-size: 1.8rem;
        font-weight: 800;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .search-box {
        display: flex;
        gap: 10px;
    }

    .search-input {
        background: rgba(15, 23, 42, 0.8);
        border: 1px solid var(--border-card);
        padding: 10px 16px;
        border-radius: 10px;
        color: #fff;
        font-size: 0.9rem;
        width: 280px;
    }

    .search-input:focus {
        outline: none;
        border-color: var(--accent-purple);
    }

    .btn-search {
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        border: none;
        color: #fff;
        padding: 10px 18px;
        border-radius: 10px;
        font-weight: 600;
        cursor: pointer;
    }

    .table-card {
        background: var(--bg-card);
        border: 1px solid var(--border-card);
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
    }

    .custom-table {
        width: 100%;
        border-collapse: collapse;
        text-align: left;
    }

    .custom-table th {
        background: rgba(15, 23, 42, 0.8);
        padding: 1rem 1.25rem;
        font-weight: 700;
        font-size: 0.85rem;
        color: var(--text-secondary);
        text-transform: uppercase;
        letter-spacing: 0.5px;
        border-bottom: 1px solid var(--border-card);
    }

    .custom-table td {
        padding: 1rem 1.25rem;
        border-bottom: 1px solid rgba(255, 255, 255, 0.05);
        font-size: 0.95rem;
    }

    .custom-table tr:hover {
        background: rgba(255, 255, 255, 0.02);
    }

    .status-badge {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 4px 10px;
        border-radius: 9999px;
        font-size: 0.8rem;
        font-weight: 600;
    }

    .status-active {
        background: rgba(16, 185, 129, 0.15);
        color: #6ee7b7;
        border: 1px solid rgba(16, 185, 129, 0.3);
    }

    .status-locked {
        background: rgba(239, 68, 68, 0.15);
        color: #fca5a5;
        border: 1px solid rgba(239, 68, 68, 0.3);
    }

    .btn-sm {
        padding: 6px 12px;
        border-radius: 6px;
        font-size: 0.82rem;
        font-weight: 600;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 4px;
        cursor: pointer;
        border: none;
        transition: all 0.2s ease;
    }

    .btn-lock {
        background: rgba(239, 68, 68, 0.2);
        color: #fca5a5;
        border: 1px solid rgba(239, 68, 68, 0.3);
    }

    .btn-unlock {
        background: rgba(16, 185, 129, 0.2);
        color: #6ee7b7;
        border: 1px solid rgba(16, 185, 129, 0.3);
    }

    .btn-edit {
        background: rgba(59, 130, 246, 0.2);
        color: #93c5fd;
        border: 1px solid rgba(59, 130, 246, 0.3);
    }

    /* Modal styling */
    .modal-backdrop {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.7);
        backdrop-filter: blur(6px);
        display: none;
        align-items: center;
        justify-content: center;
        z-index: 1000;
    }

    .modal-content {
        background: rgba(30, 41, 59, 0.95);
        border: 1px solid var(--border-card);
        border-radius: 16px;
        width: 90%;
        max-width: 480px;
        padding: 2rem;
    }

    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
    }
</style>

<div class="main-container">
    <c:if test="${not empty sessionScope.successMessage}">
        <div style="background: rgba(16, 185, 129, 0.15); border: 1px solid rgba(16, 185, 129, 0.3); color: #6ee7b7; padding: 1rem; border-radius: 10px; margin-bottom: 1.5rem;">
            <i class="fa-solid fa-circle-check"></i> ${sessionScope.successMessage}
        </div>
        <% session.removeAttribute("successMessage"); %>
    </c:if>

    <div class="admin-header">
        <div class="admin-title">
            <h2>
                <i class="fa-solid fa-user-shield" style="color: var(--accent-purple);"></i>
                Quản Lý Người Dùng (Admin CRUD)
            </h2>
            <p style="color: var(--text-secondary); font-size: 0.9rem; margin-top: 4px;">
                Tìm kiếm theo email, chỉnh sửa thông tin, phân vai trò & khóa/mở tài khoản.
            </p>
        </div>

        <form action="${pageContext.request.contextPath}/admin/users" method="get" class="search-box">
            <input type="text" name="kw" class="search-input" placeholder="Tìm theo username hoặc email..." value="${kw}">
            <button type="submit" class="btn-search">
                <i class="fa-solid fa-magnifying-glass"></i> Tìm
            </button>
        </form>
    </div>

    <div class="table-card">
        <table class="custom-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Họ và Tên</th>
                    <th>Email</th>
                    <th>Vai Trò (Roles)</th>
                    <th>Trạng Thái</th>
                    <th style="text-align: right;">Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><strong>#${u.id}</strong></td>
                        <td>
                            <div style="display: flex; align-items: center; gap: 8px; font-weight: 600;">
                                <i class="fa-solid fa-circle-user" style="color: var(--text-secondary);"></i>
                                ${u.username}
                            </div>
                        </td>
                        <td>${u.fullName}</td>
                        <td style="color: var(--text-secondary);">${u.email}</td>
                        <td>
                            <c:forEach var="role" items="${u.roles}">
                                <span class="role-badge ${role.name == 'ROLE_ADMIN' ? 'role-admin' : (role.name == 'ROLE_STAFF' ? 'role-staff' : 'role-user')}">
                                    ${role.name.replace('ROLE_', '')}
                                </span>
                            </c:forEach>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.active}">
                                    <span class="status-badge status-active">
                                        <i class="fa-solid fa-check" style="font-size: 0.7rem;"></i> Hoạt động
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-locked">
                                        <i class="fa-solid fa-lock" style="font-size: 0.7rem;"></i> Đã khóa
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="text-align: right;">
                            <button class="btn-sm btn-edit" onclick="openEditModal(${u.id}, '${u.username}', '${u.fullName}', '${u.email}', ${u.active})">
                                <i class="fa-solid fa-pen"></i> Sửa
                            </button>
                            <a href="${pageContext.request.contextPath}/admin/user/toggle-status?id=${u.id}" class="btn-sm ${u.active ? 'btn-lock' : 'btn-unlock'}">
                                <i class="fa-solid ${u.active ? 'fa-lock' : 'fa-lock-open'}"></i> ${u.active ? 'Khóa' : 'Mở'}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- EDIT MODAL -->
<div id="editModal" class="modal-backdrop">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fa-solid fa-user-pen" style="color: var(--accent-purple);"></i> Chỉnh Sửa Tài Khoản</h3>
            <button onclick="closeEditModal()" style="background: none; border: none; color: #fff; font-size: 1.2rem; cursor: pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/user/edit" method="post">
            <input type="hidden" id="modal_userId" name="userId">
            
            <div style="margin-bottom: 1rem;">
                <label style="font-size: 0.85rem; color: var(--text-secondary);">Username:</label>
                <input type="text" id="modal_username" class="search-input" style="width: 100%; margin-top: 4px;" readonly>
            </div>

            <div style="margin-bottom: 1rem;">
                <label style="font-size: 0.85rem; color: var(--text-secondary);">Họ và Tên:</label>
                <input type="text" id="modal_fullName" name="fullName" class="search-input" style="width: 100%; margin-top: 4px;" required>
            </div>

            <div style="margin-bottom: 1rem;">
                <label style="font-size: 0.85rem; color: var(--text-secondary);">Email:</label>
                <input type="email" id="modal_email" name="email" class="search-input" style="width: 100%; margin-top: 4px;" required>
            </div>

            <div style="margin-bottom: 1rem;">
                <label style="font-size: 0.85rem; color: var(--text-secondary);">Vai trò chính (Role):</label>
                <select name="roleName" class="search-input" style="width: 100%; margin-top: 4px;">
                    <option value="ROLE_USER">USER - Người dùng</option>
                    <option value="ROLE_STAFF">STAFF - Nhân viên</option>
                    <option value="ROLE_ADMIN">ADMIN - Quản trị viên</option>
                </select>
            </div>

            <div style="margin-bottom: 1.5rem; display: flex; align-items: center; gap: 8px;">
                <input type="checkbox" id="modal_active" name="active">
                <label for="modal_active">Trạng thái kích hoạt (Active)</label>
            </div>

            <button type="submit" class="btn-search" style="width: 100%;">
                <i class="fa-solid fa-floppy-disk"></i> Lưu Thay Đổi
            </button>
        </form>
    </div>
</div>

<script>
    function openEditModal(id, username, fullName, email, active) {
        document.getElementById('modal_userId').value = id;
        document.getElementById('modal_username').value = username;
        document.getElementById('modal_fullName').value = fullName;
        document.getElementById('modal_email').value = email;
        document.getElementById('modal_active').checked = active;
        document.getElementById('editModal').style.display = 'flex';
    }

    function closeEditModal() {
        document.getElementById('editModal').style.display = 'none';
    }
</script>

<jsp:include page="/views/footer.jsp" />
