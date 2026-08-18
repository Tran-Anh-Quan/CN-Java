package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.Sach;
import vn.edu.eaut.lab7.repository.SachRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/sach")
public class SachServlet extends HttpServlet {
    private SachRepository repo = new SachRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("sach", repo.findById(req.getParameter("id")));
                req.getRequestDispatcher("/views/sach/form.jsp").forward(req, resp);
                break;
            case "delete":
                repo.delete(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/admin/sach");
                break;
            case "search":
                String kw = req.getParameter("keyword");
                req.setAttribute("books", repo.search(kw));
                req.setAttribute("keyword", kw);
                req.getRequestDispatcher("/views/sach/list.jsp").forward(req, resp);
                break;
            case "list":
            default:
                req.setAttribute("books", repo.findAll());
                req.getRequestDispatcher("/views/sach/list.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Sach s = new Sach(
            req.getParameter("id"),
            req.getParameter("name"),
            req.getParameter("author"),
            req.getParameter("publisher"),
            Integer.parseInt(req.getParameter("publishYear"))
        );
        repo.save(s);
        resp.sendRedirect(req.getContextPath() + "/admin/sach");
    }
}
