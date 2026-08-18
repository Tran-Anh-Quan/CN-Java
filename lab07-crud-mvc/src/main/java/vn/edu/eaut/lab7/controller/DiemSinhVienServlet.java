package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.DiemSinhVien;
import vn.edu.eaut.lab7.repository.DiemSinhVienRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/diem")
public class DiemSinhVienServlet extends HttpServlet {
    private DiemSinhVienRepository repo = new DiemSinhVienRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("diem", repo.findById(req.getParameter("id")));
                req.getRequestDispatcher("/views/diem/form.jsp").forward(req, resp);
                break;
            case "delete":
                repo.delete(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/admin/diem");
                break;
            case "list":
            default:
                req.setAttribute("danhSachDiem", repo.findAll());
                req.getRequestDispatcher("/views/diem/list.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        DiemSinhVien d = new DiemSinhVien(
            req.getParameter("id"),
            req.getParameter("studentId"),
            Double.parseDouble(req.getParameter("diemCC")),
            Double.parseDouble(req.getParameter("diemGK")),
            Double.parseDouble(req.getParameter("diemCK"))
        );
        repo.save(d);
        resp.sendRedirect(req.getContextPath() + "/admin/diem");
    }
}
