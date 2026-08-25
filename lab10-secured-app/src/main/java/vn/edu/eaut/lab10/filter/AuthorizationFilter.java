package vn.edu.eaut.lab10.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab10.model.User;

import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/admin/*", "/staff/*", "/user/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        String uri = req.getRequestURI();

        if (currentUser != null) {
            // Check /admin/* -> requires ROLE_ADMIN
            if (uri.contains("/admin/")) {
                if (!currentUser.hasRole("ROLE_ADMIN")) {
                    req.setAttribute("errorMessage", "Truy cập bị từ chối! Bạn không có quyền Administrator để vào " + uri);
                    req.getRequestDispatcher("/error/403.jsp").forward(req, res);
                    return;
                }
            }

            // Check /staff/* -> requires ROLE_ADMIN or ROLE_STAFF
            if (uri.contains("/staff/")) {
                if (!currentUser.hasRole("ROLE_ADMIN") && !currentUser.hasRole("ROLE_STAFF")) {
                    req.setAttribute("errorMessage", "Truy cập bị từ chối! Bạn không có quyền Nhân viên (STAFF) để vào " + uri);
                    req.getRequestDispatcher("/error/403.jsp").forward(req, res);
                    return;
                }
            }

            // Check /user/* -> requires any logged-in user role
            if (uri.contains("/user/")) {
                if (currentUser.getRoles() == null || currentUser.getRoles().isEmpty()) {
                    req.setAttribute("errorMessage", "Truy cập bị từ chối! Tài khoản không có vai trò hợp lệ.");
                    req.getRequestDispatcher("/error/403.jsp").forward(req, res);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
