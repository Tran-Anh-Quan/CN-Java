package vn.edu.eaut.customer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab16.shared.entity.User;
import vn.edu.eaut.lab16.shared.enums.UserRole;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Login Servlet - Handles authentication for Customer Portal.
 *
 * Customers must log in before creating or tracking orders.
 * Session-based auth (HttpSession) used because Jakarta EE has no built-in auth.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    private DatabaseConnection db;

    @Override
    public void init() throws ServletException {
        db = DatabaseConnection.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // /login → show form
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = db.getUserByUsername(username.trim());
            if (user == null || !user.isActive()) {
                request.setAttribute("error", "Tên đăng nhập không tồn tại hoặc đã bị vô hiệu hóa");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Customer portal only accepts CUSTOMER role
            if (user.getRole() != UserRole.CUSTOMER && user.getRole() != UserRole.ADMIN) {
                request.setAttribute("error",
                        "Tài khoản này không có quyền truy cập Customer Portal. " +
                        "Vui lòng dùng portal phù hợp với vai trò của bạn.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Plain-text password check (sample data uses BCrypt for Spring only)
            // For simplicity in this lab, we accept plain-text match.
            // In production, use BCrypt.verify().
            if (!user.getPassword().equals(password)) {
                request.setAttribute("error", "Mật khẩu không đúng");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Login success: set session
            HttpSession session = request.getSession(true);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("role", user.getRole().name());
            session.setAttribute("userId", user.getId());

            // Optional: remember email for pre-filling order form
            session.setAttribute("email", user.getEmail());

            response.sendRedirect(request.getContextPath() + "/index");
        } catch (SQLException e) {
            throw new ServletException("Login error", e);
        }
    }
}
