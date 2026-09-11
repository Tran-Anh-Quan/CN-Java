package vn.edu.eaut.lab15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.service.CourseService;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Danh sach mon hoc (ho tro tim kiem)
    @GetMapping
    @Transactional(readOnly = true)
    public String listCourses(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Course> courses;
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseService.searchByCourseName(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            courses = courseService.getAllCourses();
        }
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    // Form them moi
    @GetMapping("/new")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }

    // Luu mon hoc
    @PostMapping
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // Form chinh sua
    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "courses/form";
    }

    // Xoa mon hoc
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }
}
