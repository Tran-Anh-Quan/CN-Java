package vn.edu.eaut.lab11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.eaut.lab11.model.Course;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    @GetMapping("/courses")
    public String listCourses(Model model) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("INT3110", "Lập trình Java nâng cao", 3));
        courses.add(new Course("INT3120", "Cơ sở dữ liệu", 3));
        courses.add(new Course("INT2215", "Lập trình Web", 4));
        courses.add(new Course("INT2204", "Hệ điều hành", 3));
        courses.add(new Course("INT2201", "Cấu trúc dữ liệu và giải thuật", 4));

        model.addAttribute("pageTitle", "Danh Sách Khóa Học - Lab 11");
        model.addAttribute("courses", courses);
        return "courses";
    }
}
