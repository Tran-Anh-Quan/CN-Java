<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="500 - Server Error" />
</jsp:include>

<style>
    .error-card {
        background: var(--bg-card);
        border: 1px solid rgba(239, 68, 68, 0.4);
        border-radius: 20px;
        max-width: 550px;
        margin: 4rem auto;
        padding: 3rem;
        text-align: center;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
    }

    .error-code {
        font-size: 5rem;
        font-weight: 900;
        background: linear-gradient(135deg, #ef4444, #ec4899);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        line-height: 1;
        margin-bottom: 1rem;
    }

    .error-title {
        font-size: 1.5rem;
        font-weight: 700;
        margin-bottom: 0.75rem;
    }

    .error-desc {
        color: var(--text-secondary);
        font-size: 0.95rem;
        margin-bottom: 2rem;
        line-height: 1.6;
    }

    .btn-home {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 12px 24px;
        background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
        color: #fff;
        text-decoration: none;
        font-weight: 700;
        border-radius: 10px;
        transition: all 0.2s ease;
    }

    .btn-home:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(139, 92, 246, 0.4);
    }
</style>

<div class="main-container">
    <div class="error-card">
        <div class="error-code">500</div>
        <div class="error-title">Lỗi Máy Chủ Nội Bộ (Internal Server Error)</div>
        <div class="error-desc">
            Đã xảy ra sự cố xử lý trên hệ thống máy chủ. Chi tiết lỗi đã được ghi nhận.
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-home">
            <i class="fa-solid fa-house"></i> Quay Về Dashboard
        </a>
    </div>
</div>

<jsp:include page="/views/footer.jsp" />
