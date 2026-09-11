package vn.edu.eaut.lab15.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.eaut.lab15.entity.AppUser;
import vn.edu.eaut.lab15.entity.Course;
import vn.edu.eaut.lab15.entity.Student;
import vn.edu.eaut.lab15.repository.AppUserRepository;
import vn.edu.eaut.lab15.repository.CourseRepository;
import vn.edu.eaut.lab15.repository.StudentRepository;

@Configuration
public class DataInitializer {

    // Khoi tao du lieu mau khi ung dung chay lan dau
    @Bean
    CommandLineRunner initData(AppUserRepository userRepo,
                                StudentRepository studentRepo,
                                CourseRepository courseRepo,
                                PasswordEncoder encoder) {
        return args -> {
            // ===== USERS =====
            if (userRepo.count() == 0) {
                userRepo.save(new AppUser("user", encoder.encode("123456"), "ROLE_USER"));
                userRepo.save(new AppUser("admin", encoder.encode("admin123"), "ROLE_ADMIN"));
                System.out.println(">>> [Lab15] Default users created: user/123456, admin/admin123");
            }

            // ===== STUDENTS =====
            if (studentRepo.count() == 0) {
                studentRepo.save(new Student("Nguyen Van A", "a@eaut.edu.vn"));
                studentRepo.save(new Student("Tran Thi B", "b@eaut.edu.vn"));
                studentRepo.save(new Student("Le Van C", "c@eaut.edu.vn"));
                studentRepo.save(new Student("Pham Thi D", "d@eaut.edu.vn"));
                studentRepo.save(new Student("Hoang Van E", "e@eaut.edu.vn"));
                System.out.println(">>> [Lab15] Sample students created (5 records)");
            }

            // ===== COURSES =====
            if (courseRepo.count() == 0) {
                courseRepo.save(new Course("CS101", "Lap trinh Java", 3));
                courseRepo.save(new Course("CS102", "Co so du lieu", 4));
                courseRepo.save(new Course("CS103", "Mang may tinh", 3));
                courseRepo.save(new Course("CS104", "Phat trien Web", 3));
                courseRepo.save(new Course("CS105", "He dieu hanh", 4));
                System.out.println(">>> [Lab15] Sample courses created (5 records)");
            }
        };
    }
}
