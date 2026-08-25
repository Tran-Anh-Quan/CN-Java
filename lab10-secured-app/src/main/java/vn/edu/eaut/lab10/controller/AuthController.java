package vn.edu.eaut.lab10.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab10.model.AuditLog;
import vn.edu.eaut.lab10.model.User;
import vn.edu.eaut.lab10.repository.AuditLogRepository;
import vn.edu.eaut.lab10.repository.UserRepository;
import vn.edu.eaut.lab10.service.AuthService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AuthController", urlPatterns = {
        "/login", "/logout", "/dashboard", "/register",
        "/admin/users", "/admin/user/toggle-status", "/admin/user/edit",
        "/user/profile", "/user/profile/update", "/user/change-password",
        "/staff/dashboard"
})
public class AuthController extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final UserRepository userRepository = new UserRepository();
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        switch (action) {
            case "/login" -> showLoginForm(req, resp);
            case "/logout" -> handleLogout(req, resp);
            case "/dashboard" -> showDashboard(req, resp);
            case "/admin/users" -> showAdminUsers(req, resp);
            case "/admin/user/toggle-status" -> handleToggleStatus(req, resp);
            case "/user/profile" -> showUserProfile(req, resp);
            case "/staff/dashboard" -> showStaffDashboard(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        switch (action) {
            case "/login" -> handleLogin(req, resp);
            case "/register" -> handleRegister(req, resp);
            case "/logout" -> handleLogout(req, resp);
            case "/admin/user/edit" -> handleAdminEditUser(req, resp);
            case "/user/profile/update" -> handleUpdateProfile(req, resp);
            case "/user/change-password" -> handleChangePassword(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void showLoginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Optional<User> userOpt = authService.authenticate(username, password);

        if (userOpt.isPresent()) {
            HttpSession session = req.getSession(true);
            User user = userOpt.get();
            session.setAttribute("currentUser", user);

            String redirectUrl = (String) session.getAttribute("redirectUrl");
            session.removeAttribute("redirectUrl");
            session.removeAttribute("errorMessage");

            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            }
        } else {
            req.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác (hoặc tài khoản đã bị khóa)!");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String roleName = req.getParameter("roleName");

        if (roleName == null || roleName.trim().isEmpty()) {
            roleName = "ROLE_USER";
        }

        try {
            authService.registerUser(username, password, fullName, email, roleName);
            req.setAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("registerErrorMessage", e.getMessage());
            req.setAttribute("username", username);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser != null) {
                authService.logAction(currentUser.getUsername(), "LOGOUT", "Người dùng đã đăng xuất");
            }
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/login?logout=success");
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        
        List<AuditLog> recentLogs = auditLogRepository.findAllRecent();
        req.setAttribute("recentLogs", recentLogs);
        req.setAttribute("totalUsers", userRepository.count());

        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }

    private void showAdminUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("kw");
        List<User> userList = userRepository.search(keyword);
        req.setAttribute("users", userList);
        req.setAttribute("kw", keyword);
        req.getRequestDispatcher("/admin/index.jsp").forward(req, resp);
    }

    private void handleToggleStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Long userId = Long.parseLong(idStr);
            authService.toggleUserActiveStatus(userId, currentUser.getUsername());
            session.setAttribute("successMessage", "Thay đổi trạng thái tài khoản thành công!");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void handleAdminEditUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Long userId = Long.parseLong(req.getParameter("userId"));
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String roleName = req.getParameter("roleName");
            boolean active = "on".equals(req.getParameter("active")) || "true".equals(req.getParameter("active"));

            authService.updateUserByAdmin(userId, fullName, email, roleName, active, currentUser.getUsername());
            session.setAttribute("successMessage", "Cập nhật thông tin tài khoản thành công!");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void showUserProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        // Refresh user from DB
        userRepository.findById(currentUser.getId()).ifPresent(freshUser -> session.setAttribute("currentUser", freshUser));
        
        List<AuditLog> userLogs = auditLogRepository.findByUsername(currentUser.getUsername());
        req.setAttribute("userLogs", userLogs);

        req.getRequestDispatcher("/user/index.jsp").forward(req, resp);
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");

        try {
            authService.updateProfile(currentUser.getId(), fullName, email, currentUser.getUsername());
            userRepository.findById(currentUser.getId()).ifPresent(updatedUser -> session.setAttribute("currentUser", updatedUser));
            req.setAttribute("profileSuccessMessage", "Cập nhật thông tin cá nhân thành công!");
        } catch (IllegalArgumentException e) {
            req.setAttribute("profileErrorMessage", e.getMessage());
        }
        showUserProfile(req, resp);
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            authService.changePassword(currentUser.getId(), oldPassword, newPassword, confirmPassword, currentUser.getUsername());
            req.setAttribute("pwdSuccessMessage", "Đổi mật khẩu thành công! Vui lòng nhớ mật khẩu mới của bạn.");
        } catch (IllegalArgumentException e) {
            req.setAttribute("pwdErrorMessage", e.getMessage());
        }
        showUserProfile(req, resp);
    }

    private void showStaffDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/staff/index.jsp").forward(req, resp);
    }
}
