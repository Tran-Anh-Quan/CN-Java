package vn.edu.eaut.lab14.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.eaut.lab14.entity.AppUser;
import vn.edu.eaut.lab14.repository.AppUserRepository;

@Configuration
public class DataInitializer {

    // Tao du lieu user mac dinh khi ung dung khoi dong
    @Bean
    CommandLineRunner initUsers(AppUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new AppUser("user", encoder.encode("123456"), "ROLE_USER"));
                repo.save(new AppUser("admin", encoder.encode("admin123"), "ROLE_ADMIN"));
                System.out.println(">>> Default users created: user/123456, admin/admin123");
            }
        };
    }
}
