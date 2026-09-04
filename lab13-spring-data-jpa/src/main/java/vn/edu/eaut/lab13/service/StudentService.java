package vn.edu.eaut.lab13.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.eaut.lab13.entity.Student;
import vn.edu.eaut.lab13.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    // Tìm kiếm sinh viên theo tên
    public List<Student> searchByName(String keyword) {
        return studentRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Tìm kiếm sinh viên theo email
    public List<Student> searchByEmail(String keyword) {
        return studentRepository.findByEmailContainingIgnoreCase(keyword);
    }
}
