package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab15.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Tim kiem theo ma mon hoc
    List<Course> findByCourseCodeContainingIgnoreCase(String courseCode);

    // Tim kiem theo ten mon hoc
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
}
