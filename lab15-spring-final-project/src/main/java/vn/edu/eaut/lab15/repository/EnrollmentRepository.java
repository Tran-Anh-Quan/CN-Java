package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab15.entity.Enrollment;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Lay cac dang ky theo sinh vien
    List<Enrollment> findByStudent(Student student);

    // Lay cac dang ky theo mon hoc
    List<Enrollment> findByCourse(Course course);

    // Kiem tra trung lap dang ky
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
