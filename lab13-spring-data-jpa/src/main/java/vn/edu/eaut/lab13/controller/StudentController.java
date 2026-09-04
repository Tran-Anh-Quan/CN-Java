package vn.edu.eaut.lab13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab13.entity.Student;
import vn.edu.eaut.lab13.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Hiển thị danh sách sinh viên (hỗ trợ tìm kiếm theo keyword)
    @GetMapping
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

    // Hiển thị form thêm mới sinh viên
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    // Lưu sinh viên (thêm mới hoặc cập nhật)
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // Hiển thị form chỉnh sửa sinh viên
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/form";
    }

    // Xóa sinh viên theo ID
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}
