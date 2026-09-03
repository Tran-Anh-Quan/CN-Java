package vn.edu.eaut.lab12.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab12.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private long currentId = 1;

    public StudentService() {
        students.add(new Student(currentId++, "Nguyen Van A", "nva@eaut.edu.vn", "IT"));
        students.add(new Student(currentId++, "Tran Thi B", "ttb@eaut.edu.vn", "Business"));
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Optional<Student> getStudentById(Long id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public void save(Student student) {
        if (student.getId() == null) {
            student.setId(currentId++);
            students.add(student);
        } else {
            Optional<Student> existing = getStudentById(student.getId());
            if (existing.isPresent()) {
                Student s = existing.get();
                s.setName(student.getName());
                s.setEmail(student.getEmail());
                s.setMajor(student.getMajor());
            }
        }
    }

    public void delete(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }
}
