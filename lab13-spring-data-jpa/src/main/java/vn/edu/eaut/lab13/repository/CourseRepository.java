package vn.edu.eaut.lab13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab13.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Tim kiem mon hoc theo ten
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);
}
