package vn.edu.eaut.lab6.controller;

import vn.edu.eaut.lab6.model.Student;
import vn.edu.eaut.lab6.store.StudentStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = StudentStore.getAllStudents(getServletContext());
        
        int totalStudents = students.size();
        
        Map<String, Integer> classCount = new HashMap<>();
        for (Student s : students) {
            if (s.getClazz() != null && !s.getClazz().trim().isEmpty()) {
                classCount.put(s.getClazz(), classCount.getOrDefault(s.getClazz(), 0) + 1);
            }
        }
        
        req.setAttribute("totalStudents", totalStudents);
        req.setAttribute("classCount", classCount);
        
        req.getRequestDispatcher("/welcome.jsp").forward(req, resp);
    }
}
