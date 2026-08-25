package vn.edu.eaut.lab9.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.service.SinhVienService;

import java.io.IOException;
import java.util.List;

@WebServlet("/sinh-vien/*")
public class SinhVienController extends HttpServlet {
    
    private final SinhVienService service = new SinhVienService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list";
        }
        
        switch (action) {
            case "/new":
                showNewForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/delete":
                deleteSinhVien(request, response);
                break;
            case "/search":
                searchSinhVien(request, response);
                break;
            case "/list":
            default:
                listSinhVien(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/insert".equals(action)) {
            insertSinhVien(request, response);
        } else if ("/update".equals(action)) {
            updateSinhVien(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/sinh-vien/list");
        }
    }

    private void listSinhVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SinhVien> listSinhVien = service.getAllSinhVien();
        request.setAttribute("listSinhVien", listSinhVien);
        request.getRequestDispatcher("/views/sinhvien/list.jsp").forward(request, response);
    }

    private void searchSinhVien(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<SinhVien> listSinhVien = service.searchSinhVien(keyword);
        request.setAttribute("listSinhVien", listSinhVien);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/sinhvien/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maSv = request.getParameter("maSv");
        SinhVien existingSinhVien = service.getSinhVienById(maSv);
        request.setAttribute("sinhVien", existingSinhVien);
        request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
    }

    private void insertSinhVien(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maSv = request.getParameter("maSv");
        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String chuyenNganh = request.getParameter("chuyenNganh");
        double diemTb = Double.parseDouble(request.getParameter("diemTb"));
        
        SinhVien newSinhVien = new SinhVien(maSv, hoTen, email, chuyenNganh, diemTb, null, null);
        try {
            service.addSinhVien(newSinhVien);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/sinh-vien/list");
    }

    private void updateSinhVien(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maSv = request.getParameter("maSv");
        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String chuyenNganh = request.getParameter("chuyenNganh");
        double diemTb = Double.parseDouble(request.getParameter("diemTb"));

        SinhVien sinhVien = new SinhVien(maSv, hoTen, email, chuyenNganh, diemTb, null, null);
        try {
            service.updateSinhVien(sinhVien);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/sinh-vien/list");
    }

    private void deleteSinhVien(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maSv = request.getParameter("maSv");
        service.deleteSinhVien(maSv);
        response.sendRedirect(request.getContextPath() + "/sinh-vien/list");
    }
}
