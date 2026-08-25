<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/views/header.jsp">
    <jsp:param name="title" value="Khu Vực Nghiệp Vụ - Staff - Lab 10" />
</jsp:include>

<style>
    .staff-card {
        background: var(--bg-card);
        border: 1px solid var(--border-card);
        border-radius: 20px;
        padding: 2.5rem;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4);
    }

    .staff-header {
        display: flex;
        align-items: center;
        gap: 20px;
        margin-bottom: 2rem;
        padding-bottom: 1.5rem;
        border-bottom: 1px solid var(--border-card);
    }

    .staff-avatar {
        width: 70px;
        height: 70px;
        border-radius: 16px;
        background: linear-gradient(135deg, #f59e0b, #ec4899);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2.2rem;
        color: #fff;
    }

    .grid-tools {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 1.5rem;
    }

    .tool-box {
        background: rgba(15, 23, 42, 0.6);
        border: 1px solid var(--border-card);
        border-radius: 12px;
        padding: 1.5rem;
    }

    .tool-box h4 {
        font-size: 1.1rem;
        margin-bottom: 0.5rem;
        display: flex;
        align-items: center;
        gap: 8px;
    }
</style>

<div class="main-container">
    <div class="staff-card">
        <div class="staff-header">
            <div class="staff-avatar">
                <i class="fa-solid fa-briefcase"></i>
            </div>
            <div>
                <h2>Module Nghiệp Vụ Nhân Viên (Staff Area)</h2>
                <p style="color: var(--text-secondary); font-size: 0.95rem; margin-top: 4px;">
                    Đường dẫn bảo vệ: <code>/staff/*</code> (Dành cho Vai trò <code>ROLE_STAFF</code> và <code>ROLE_ADMIN</code>)
                </p>
            </div>
        </div>

        <div class="grid-tools">
            <div class="tool-box">
                <h4 style="color: #fde047;">
                    <i class="fa-solid fa-boxes-stacked"></i> Quản Lý Nghiệp Vụ Dữ Liệu
                </h4>
                <p style="color: var(--text-secondary); font-size: 0.9rem; line-height: 1.5;">
                    Nhân viên có quyền thêm, chỉnh sửa dữ liệu sinh viên, sách, sản phẩm nghiệp vụ nhưng không có quyền quản lý tài khoản hệ thống.
                </p>
            </div>

            <div class="tool-box">
                <h4 style="color: #6ee7b7;">
                    <i class="fa-solid fa-file-invoice"></i> Xử Lý Đơn Hàng & Báo Cáo
                </h4>
                <p style="color: var(--text-secondary); font-size: 0.9rem; line-height: 1.5;">
                    Tạo mới hóa đơn, theo dõi lịch sử giao dịch và xuất báo cáo công việc hàng ngày.
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/views/footer.jsp" />
