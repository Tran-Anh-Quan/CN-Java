package vn.edu.eaut.lab15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.entity.Enrollment;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.service.CourseService;
import vn.edu.eaut.lab15.service.EnrollmentService;
import vn.edu.eaut.lab15.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // Danh sach tat ca dang ky
    @GetMapping
    @Transactional(readOnly = true)
    public String listEnrollments(@RequestParam(value = "studentId", required = false) Long studentId,
                                  Model model) {
        List<Enrollment> enrollments;
        if (studentId != null) {
            Student student = studentService.getStudentById(studentId);
            enrollments = enrollmentService.getEnrollmentsByStudent(student);
            model.addAttribute("filterStudent", student);
        } else {
            enrollments = enrollmentService.getAllEnrollments();
        }
        model.addAttribute("enrollments", enrollments);
        return "enrollments/list";
    }

    // Form dang ky mon hoc moi
    @GetMapping("/new")
    public String createEnrollmentForm(Model model) {
        // Lay danh sach sinh vien va mon hoc cho dropdown
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "enrollments/form";
    }

    // Xu ly dang ky
    @PostMapping
    public String saveEnrollment(@RequestParam Long studentId,
                                  @RequestParam Long courseId,
                                  RedirectAttributes redirectAttributes) {
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (student == null || course == null) {
            redirectAttributes.addFlashAttribute("error", "Sinh vien hoac mon hoc khong ton tai!");
            return "redirect:/enrollments/new";
        }

        boolean success = enrollmentService.enrollStudent(student, course);
        if (success) {
            redirectAttributes.addFlashAttribute("message",
                    "Dang ky thanh cong: " + student.getName() + " -> " + course.getCourseName());
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Sinh vien " + student.getName() + " da dang ky mon " + course.getCourseName() + " roi!");
        }
        return "redirect:/enrollments";
    }

    // Cap nhat diem (chi ADMIN)
    @PostMapping("/grade/{id}")
    public String updateGrade(@PathVariable Long id,
                              @RequestParam Double grade,
                              RedirectAttributes redirectAttributes) {
        enrollmentService.updateGrade(id, grade);
        redirectAttributes.addFlashAttribute("message", "Cap nhat diem thanh cong!");
        return "redirect:/enrollments";
    }

    // Xoa dang ky (chi ADMIN)
    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/enrollments";
    }
}
