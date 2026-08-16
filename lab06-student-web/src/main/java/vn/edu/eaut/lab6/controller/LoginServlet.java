package vn.edu.eaut.lab6.controller;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if ("admin".equals(username) && "123456".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", username);
            session.setAttribute("role", "admin");
            session.setAttribute("loginTime", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else if ("user".equals(username) && "123456".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", username);
            session.setAttribute("role", "user");
            session.setAttribute("loginTime", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}