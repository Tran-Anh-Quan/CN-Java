package vn.edu.eaut.lab14.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    // Du lieu mau (khong dung database)
    private static final List<String[]> COURSES = Arrays.asList(
            new String[]{"CS101", "Lap trinh Java", "3"},
            new String[]{"CS102", "Co so du lieu", "4"},
            new String[]{"CS103", "Mang may tinh", "3"},
            new String[]{"CS104", "Phat trien Web", "3"}
    );

    // Bai 6: Chi ADMIN moi truy cap duoc /courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", COURSES);
        return "courses/list";
    }
}
