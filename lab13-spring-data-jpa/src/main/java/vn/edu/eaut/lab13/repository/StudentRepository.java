package vn.edu.eaut.lab13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.eaut.lab13.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Tìm kiếm sinh viên theo tên (chứa keyword, không phân biệt hoa thường)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Tìm kiếm sinh viên theo email
    List<Student> findByEmailContainingIgnoreCase(String email);
}
