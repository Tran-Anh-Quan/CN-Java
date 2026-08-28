package vn.edu.eaut.lab11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.eaut.lab11.model.Student;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("SV001", "Nguyễn Văn An", "an.nv@eaut.edu.vn", "Công nghệ thông tin", 3.65));
        students.add(new Student("SV002", "Trần Thị Bình", "binh.tt@eaut.edu.vn", "Khoa học máy tính", 3.80));
        students.add(new Student("SV003", "Lê Hoàng Cường", "cuong.lh@eaut.edu.vn", "Hệ thống thông tin", 3.40));
        students.add(new Student("SV004", "Phạm Minh Đức", "duc.pm@eaut.edu.vn", "Kỹ thuật phần mềm", 3.92));
        students.add(new Student("SV005", "Vũ Thị Giang", "giang.vt@eaut.edu.vn", "Công nghệ thông tin", 3.55));

        model.addAttribute("pageTitle", "Danh Sách Sinh Viên - Lab 11");
        model.addAttribute("students", students);
        return "students";
    }
}
