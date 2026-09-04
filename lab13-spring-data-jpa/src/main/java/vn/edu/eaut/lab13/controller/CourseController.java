package vn.edu.eaut.lab13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab13.entity.Course;
import vn.edu.eaut.lab13.service.CourseService;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Hien thi danh sach mon hoc (ho tro tim kiem)
    @GetMapping
    public String listCourses(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Course> courses;
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseService.searchByName(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            courses = courseService.getAllCourses();
        }
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    // Hien thi form them moi mon hoc
    @GetMapping("/new")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    // Luu mon hoc (them moi hoac cap nhat)
    @PostMapping
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // Hien thi form chinh sua mon hoc
    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "courses/form";
    }

    // Xoa mon hoc theo ID
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }
}
