package vn.edu.eaut.lab15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.eaut.lab15.service.CourseService;
import vn.edu.eaut.lab15.service.EnrollmentService;
import vn.edu.eaut.lab15.service.StudentService;

@Controller
public class DashboardController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Bai 9: Trang dashboard thong ke
    @GetMapping({"/dashboard", "/home"})
    @Transactional(readOnly = true)
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.countAllStudents());
        model.addAttribute("totalCourses", courseService.countAllCourses());
        model.addAttribute("totalEnrollments", enrollmentService.countAllEnrollments());
        return "dashboard";
    }
}
