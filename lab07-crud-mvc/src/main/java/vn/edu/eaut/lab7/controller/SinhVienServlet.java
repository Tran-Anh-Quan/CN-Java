package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.SinhVien;
import vn.edu.eaut.lab7.repository.SinhVienRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/sinhvien")
public class SinhVienServlet extends HttpServlet {
    private SinhVienRepository repository = new SinhVienRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
            case "add":
                req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
                break;
            case "edit":
                String idEdit = req.getParameter("id");
                SinhVien svEdit = repository.findById(idEdit);
                req.setAttribute("student", svEdit);
                req.getRequestDispatcher("/views/sinhvien/form.jsp").forward(req, resp);
                break;
            case "detail":
                String idDetail = req.getParameter("id");
                SinhVien svDetail = repository.findById(idDetail);
                req.setAttribute("student", svDetail);
                req.getRequestDispatcher("/views/sinhvien/detail.jsp").forward(req, resp);
                break;
            case "delete":
                String idDelete = req.getParameter("id");
                repository.delete(idDelete);
                resp.sendRedirect(req.getContextPath() + "/admin/sinhvien");
                break;
            case "list":
            default:
                int page = 1;
                int pageSize = 5;
                if (req.getParameter("page") != null) {
                    try {
                        page = Integer.parseInt(req.getParameter("page"));
                    } catch (NumberFormatException ignored) {}
                }
                List<SinhVien> list = repository.findAll(page, pageSize);
                int totalPages = repository.getTotalPages(pageSize);
                
                req.setAttribute("students", list);
                req.setAttribute("currentPage", page);
                req.setAttribute("totalPages", totalPages);
                req.getRequestDispatcher("/views/sinhvien/list.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String className = req.getParameter("className");

        if (id != null && !id.trim().isEmpty()) {
            SinhVien sv = new SinhVien(id.trim(), name, email, className);
            repository.save(sv);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/sinhvien");
    }
}
