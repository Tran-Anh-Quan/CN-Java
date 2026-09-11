package vn.edu.eaut.lab15.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.repository.CourseRepository;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Course> searchByCourseName(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public List<Course> searchByCourseCode(String keyword) {
        return courseRepository.findByCourseCodeContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public long countAllCourses() {
        return courseRepository.count();
    }
}
