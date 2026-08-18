package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.LopHoc;
import vn.edu.eaut.lab7.repository.LopHocRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/lophoc")
public class LopHocServlet extends HttpServlet {
    private LopHocRepository repo = new LopHocRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("lop", repo.findById(req.getParameter("id")));
                req.getRequestDispatcher("/views/lophoc/form.jsp").forward(req, resp);
                break;
            case "delete":
                repo.delete(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/admin/lophoc");
                break;
            case "search":
                String kw = req.getParameter("keyword");
                req.setAttribute("lophocs", repo.search(kw));
                req.setAttribute("keyword", kw);
                req.getRequestDispatcher("/views/lophoc/list.jsp").forward(req, resp);
                break;
            case "list":
            default:
                req.setAttribute("lophocs", repo.findAll());
                req.getRequestDispatcher("/views/lophoc/list.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LopHoc l = new LopHoc(
            req.getParameter("id"),
            req.getParameter("name"),
            req.getParameter("advisor"),
            Integer.parseInt(req.getParameter("studentCount"))
        );
        repo.save(l);
        resp.sendRedirect(req.getContextPath() + "/admin/lophoc");
    }
}
