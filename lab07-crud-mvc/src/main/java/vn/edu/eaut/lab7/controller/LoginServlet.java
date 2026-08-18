package vn.edu.eaut.lab7.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String u = req.getParameter("username");
        String p = req.getParameter("password");
        
        if ("admin".equals(u) && "admin123".equals(p)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", u);
            resp.sendRedirect(req.getContextPath() + "/admin/sinhvien");
        } else {
            req.setAttribute("error", "Sai tài khoản hoặc mật khẩu (admin/admin123)");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
