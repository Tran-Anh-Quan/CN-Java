package vn.edu.eaut.lab6.controller;

import vn.edu.eaut.lab6.model.Student;
import vn.edu.eaut.lab6.store.StudentStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if ("add".equals(action)) {
            if (!"admin".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/403.jsp");
                return;
            }
            req.getRequestDispatcher("/student-form.jsp").forward(req, resp);
            
        } else if ("edit".equals(action)) {
            if (!"admin".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/403.jsp");
                return;
            }
            String id = req.getParameter("id");
            Student student = null;
            if (id != null) {
                for (Student s : StudentStore.getAllStudents(getServletContext())) {
                    if (s.getId().equals(id)) {
                        student = s;
                        break;
                    }
                }
            }
            if (student != null) {
                req.setAttribute("student", student);
                req.getRequestDispatcher("/student-form.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/students");
            }
            
        } else if ("delete".equals(action)) {
            if (!"admin".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/403.jsp");
                return;
            }
            String id = req.getParameter("id");
            if (id != null) {
                List<Student> list = StudentStore.getAllStudents(getServletContext());
                list.removeIf(s -> s.getId().equals(id));
            }
            resp.sendRedirect(req.getContextPath() + "/students");
            
        } else {
            // Default view list & search (Bài 6)
            String search = req.getParameter("search");
            List<Student> allStudents = StudentStore.getAllStudents(getServletContext());
            List<Student> resultList = allStudents;

            if (search != null && !search.trim().isEmpty()) {
                String keyword = search.trim().toLowerCase();
                resultList = new ArrayList<>();
                for (Student s : allStudents) {
                    if (s.getName() != null && s.getName().toLowerCase().contains(keyword)) {
                        resultList.add(s);
                    }
                }
            }
            req.setAttribute("students", resultList);
            req.getRequestDispatcher("/student-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (!"admin".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/403.jsp");
            return;
        }

        String formAction = req.getParameter("formAction");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String clazz = req.getParameter("clazz");
        String email = req.getParameter("email");

        if ("update".equals(formAction)) {
            if (id != null) {
                for (Student s : StudentStore.getAllStudents(getServletContext())) {
                    if (s.getId().equals(id)) {
                        s.setName(name);
                        s.setClazz(clazz);
                        s.setEmail(email);
                        break;
                    }
                }
            }
        } else {
            // Default is add (Bài 7 & 8)
            if (id != null && name != null && clazz != null && email != null) {
                StudentStore.addStudent(getServletContext(), new Student(id, name, clazz, email));
            }
        }
        resp.sendRedirect(req.getContextPath() + "/students");
    }
}