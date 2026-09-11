package vn.edu.eaut.lab15.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.entity.Enrollment;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.repository.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    /**
     * Dang ky mon hoc cho sinh vien.
     * Tra ve true neu dang ky thanh cong, false neu bi trung.
     */
    @Transactional
    public boolean enrollStudent(Student student, Course course) {
        // Kiem tra da dang ky mon nay chua
        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existing.isPresent()) {
            return false;
        }
        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);
        return true;
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    @Transactional
    public void updateGrade(Long id, Double grade) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
        if (enrollment != null) {
            enrollment.setGrade(grade);
            enrollmentRepository.save(enrollment);
        }
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourse(Course course) {
        return enrollmentRepository.findByCourse(course);
    }

    @Transactional(readOnly = true)
    public long countAllEnrollments() {
        return enrollmentRepository.count();
    }
}
