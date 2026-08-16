package vn.edu.eaut.lab6.store;

import vn.edu.eaut.lab6.model.Student;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class StudentStore {
    @SuppressWarnings("unchecked")
    public static List<Student> getAllStudents(ServletContext context) {
        List<Student> list = (List<Student>) context.getAttribute("students");
        if (list == null) {
            list = new ArrayList<>();
            context.setAttribute("students", list);
        }
        return list;
    }

    public static void addStudent(ServletContext context, Student student) {
        getAllStudents(context).add(student);
    }
}