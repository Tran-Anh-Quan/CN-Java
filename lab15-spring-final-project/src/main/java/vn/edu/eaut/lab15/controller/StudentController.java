package vn.edu.eaut.lab15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.service.EnrollmentService;
import vn.edu.eaut.lab15.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    // Danh sach sinh vien (ho tro tim kiem)
    @GetMapping
    @Transactional(readOnly = true)
    public String listStudents(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentService.searchByName(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            students = studentService.getAllStudents();
        }
        model.addAttribute("students", students);
        return "students/list";
    }

    // Form them moi
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    // Luu sinh vien (them moi hoac cap nhat)
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // Form chinh sua
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/form";
    }

    // Xoa sinh vien
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }

    // Bai 8: Xem cac mon hoc ma sinh vien nay da dang ky
    @GetMapping("/{id}/courses")
    @Transactional(readOnly = true)
    public String viewStudentCourses(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("courses", enrollmentService.getEnrollmentsByStudent(student));
        return "students/courses";
    }
}
