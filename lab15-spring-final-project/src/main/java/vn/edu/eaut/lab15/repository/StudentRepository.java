package vn.edu.eaut.lab15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab15.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Tim kiem theo ten (khong phan biet hoa thuong)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Tim kiem theo email
    List<Student> findByEmailContainingIgnoreCase(String email);
}
