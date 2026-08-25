package vn.edu.eaut.lab10.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.eaut.lab10.model.User;

import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/dashboard", "/admin/*", "/staff/*", "/user/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            String requestURI = req.getRequestURI();
            String queryString = req.getQueryString();
            String fullTargetUrl = requestURI + (queryString != null ? "?" + queryString : "");

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("redirectUrl", fullTargetUrl);
            newSession.setAttribute("errorMessage", "Bạn cần đăng nhập để truy cập trang này.");

            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
