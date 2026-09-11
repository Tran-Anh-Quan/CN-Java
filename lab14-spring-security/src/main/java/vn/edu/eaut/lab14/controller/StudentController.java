package vn.edu.eaut.lab14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    // Du lieu mau luu trong bo nho (khong dung database)
    private static final List<String[]> STUDENTS = new ArrayList<>();

    static {
        STUDENTS.add(new String[]{"1", "Nguyen Van A", "a@eaut.edu.vn"});
        STUDENTS.add(new String[]{"2", "Tran Thi B", "b@eaut.edu.vn"});
        STUDENTS.add(new String[]{"3", "Le Van C", "c@eaut.edu.vn"});
        STUDENTS.add(new String[]{"4", "Pham Thi D", "d@eaut.edu.vn"});
        STUDENTS.add(new String[]{"5", "Hoang Van E", "e@eaut.edu.vn"});
    }

    private int nextId = 6;

    // Hien thi danh sach sinh vien
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", STUDENTS);
        return "students/list";
    }

    // Hien thi form them sinh vien (chi ADMIN)
    @GetMapping("/add")
    public String showAddForm() {
        return "students/add";
    }

    // Xu ly them sinh vien (chi ADMIN)
    @PostMapping("/add")
    public String addStudent(@RequestParam String name, @RequestParam String email) {
        STUDENTS.add(new String[]{String.valueOf(nextId++), name, email});
        return "redirect:/students";
    }

    // Xoa sinh vien theo ID (chi ADMIN)
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        STUDENTS.removeIf(s -> s[0].equals(id));
        return "redirect:/students";
    }
}
