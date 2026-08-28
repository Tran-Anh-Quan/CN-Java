<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'Lab 10 - Security App'}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --bg-primary: #0f172a;
            --bg-card: rgba(30, 41, 59, 0.75);
            --border-card: rgba(255, 255, 255, 0.1);
            --accent-purple: #8b5cf6;
            --accent-blue: #3b82f6;
            --accent-pink: #ec4899;
            --text-primary: #f8fafc;
            --text-secondary: #94a3b8;
            --success: #10b981;
            --warning: #f59e0b;
            --danger: #ef4444;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Be Vietnam Pro', 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
        }

        body {
            background-color: var(--bg-primary);
            background-image: 
                radial-gradient(at 0% 0%, rgba(139, 92, 246, 0.15) 0px, transparent 50%),
                radial-gradient(at 100% 100%, rgba(59, 130, 246, 0.15) 0px, transparent 50%),
                radial-gradient(at 50% 50%, rgba(236, 72, 153, 0.08) 0px, transparent 50%);
            background-attachment: fixed;
            color: var(--text-primary);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        .navbar {
            background: rgba(15, 23, 42, 0.85);
            backdrop-filter: blur(12px);
            border-bottom: 1px solid var(--border-card);
            padding: 1rem 2rem;
            position: sticky;
            top: 0;
            z-index: 100;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .nav-brand {
            display: flex;
            align-items: center;
            gap: 12px;
            text-decoration: none;
            color: var(--text-primary);
            font-weight: 700;
            font-size: 1.25rem;
        }

        .nav-brand i {
            background: linear-gradient(135deg, var(--accent-purple), var(--accent-pink));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            font-size: 1.6rem;
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 20px;
            list-style: none;
        }

        .nav-links a {
            color: var(--text-secondary);
            text-decoration: none;
            font-size: 0.95rem;
            font-weight: 500;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            gap: 6px;
            padding: 6px 12px;
            border-radius: 8px;
        }

        .nav-links a:hover, .nav-links a.active {
            color: var(--text-primary);
            background: rgba(255, 255, 255, 0.05);
        }

        .user-pill {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid var(--border-card);
            padding: 6px 14px;
            border-radius: 9999px;
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 0.9rem;
        }

        .role-badge {
            font-size: 0.7rem;
            font-weight: 700;
            padding: 2px 8px;
            border-radius: 9999px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .role-admin {
            background: rgba(239, 68, 68, 0.2);
            color: #fca5a5;
            border: 1px solid rgba(239, 68, 68, 0.4);
        }

        .role-staff {
            background: rgba(245, 158, 11, 0.2);
            color: #fde047;
            border: 1px solid rgba(245, 158, 11, 0.4);
        }

        .role-user {
            background: rgba(59, 130, 246, 0.2);
            color: #93c5fd;
            border: 1px solid rgba(59, 130, 246, 0.4);
        }

        .btn-logout {
            color: #f87171 !important;
            background: rgba(239, 68, 68, 0.1);
            padding: 6px 12px;
            border-radius: 8px;
            border: 1px solid rgba(239, 68, 68, 0.2);
            transition: all 0.2s ease;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 600;
        }

        .btn-logout:hover {
            background: rgba(239, 68, 68, 0.25) !important;
        }

        .main-container {
            flex: 1;
            padding: 2.5rem 2rem;
            max-width: 1200px;
            width: 100%;
            margin: 0 auto;
        }
    </style>
</head>
<body>

<c:if test="${sessionScope.currentUser != null}">
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard" class="nav-brand">
        <i class="fa-solid fa-shield-halved"></i>
        <span>Lab10 Security</span>
    </a>
    
    <ul class="nav-links">
        <li>
            <a href="${pageContext.request.contextPath}/dashboard">
                <i class="fa-solid fa-chart-pie"></i> Dashboard
            </a>
        </li>
        
        <!-- Role flags -->
        <c:set var="isAdmin" value="false" />
        <c:set var="isStaff" value="false" />
        <c:forEach var="r" items="${sessionScope.currentUser.roles}">
            <c:if test="${r.name == 'ROLE_ADMIN'}"><c:set var="isAdmin" value="true" /></c:if>
            <c:if test="${r.name == 'ROLE_STAFF'}"><c:set var="isStaff" value="true" /></c:if>
        </c:forEach>

        <!-- ADMIN Menu -->
        <c:if test="${isAdmin}">
            <li>
                <a href="${pageContext.request.contextPath}/admin/users">
                    <i class="fa-solid fa-user-shield"></i> Quản Lý User (Admin)
                </a>
            </li>
        </c:if>

        <!-- STAFF Menu -->
        <c:if test="${isAdmin || isStaff}">
            <li>
                <a href="${pageContext.request.contextPath}/staff/dashboard">
                    <i class="fa-solid fa-briefcase"></i> Nghiệp Vụ (Staff)
                </a>
            </li>
        </c:if>

        <!-- USER Menu -->
        <li>
            <a href="${pageContext.request.contextPath}/user/profile">
                <i class="fa-solid fa-id-card"></i> Hồ Sơ Cá Nhân
            </a>
        </li>
    </ul>

    <div style="display: flex; align-items: center; gap: 15px;">
        <div class="user-pill">
            <i class="fa-solid fa-circle-user" style="color: var(--accent-purple);"></i>
            <span>${sessionScope.currentUser.fullName}</span>
            <c:forEach var="role" items="${sessionScope.currentUser.roles}">
                <span class="role-badge ${role.name == 'ROLE_ADMIN' ? 'role-admin' : (role.name == 'ROLE_STAFF' ? 'role-staff' : 'role-user')}">
                    ${role.name.replace('ROLE_', '')}
                </span>
            </c:forEach>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            <i class="fa-solid fa-right-from-bracket"></i> Thoát
        </a>
    </div>
</nav>
</c:if>
