package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/sanpham")
public class SanPhamServlet extends HttpServlet {
    private SanPhamRepository repo = new SanPhamRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("sp", repo.findById(req.getParameter("id")));
                req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
                break;
            case "delete":
                repo.delete(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/admin/sanpham");
                break;
            case "list":
            default:
                req.setAttribute("products", repo.findAll());
                req.getRequestDispatcher("/views/sanpham/list.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            double price = Double.parseDouble(req.getParameter("price"));
            int qty = Integer.parseInt(req.getParameter("quantity"));

            if (price <= 0 || qty < 0) {
                req.setAttribute("error", "Giá phải > 0 và Số lượng phải >= 0!");
                req.setAttribute("sp", new SanPham(req.getParameter("id"), req.getParameter("name"), req.getParameter("description"), price, qty));
                req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
                return;
            }

            SanPham sp = new SanPham(req.getParameter("id"), req.getParameter("name"), req.getParameter("description"), price, qty);
            repo.save(sp);
            resp.sendRedirect(req.getContextPath() + "/admin/sanpham");
        } catch (Exception e) {
            req.setAttribute("error", "Dữ liệu không hợp lệ!");
            req.getRequestDispatcher("/views/sanpham/form.jsp").forward(req, resp);
        }
    }
}
