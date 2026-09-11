package vn.edu.eaut.lab14.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Ma hoa mat khau bang BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bai 10: Khong can userDetailsService() o day nua
    // vi da dung CustomUserDetailsService luu trong CSDL

    // Cau hinh phan quyen URL
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // URL cong khai - ai cung truy cap duoc
                .requestMatchers("/", "/home", "/login", "/css/**").permitAll()
                // Bai 6: URL /courses/** chi cho ADMIN truy cap
                .requestMatchers("/courses/**").hasRole("ADMIN")
                // Bai 9: Bao ve chuc nang xoa - chi ADMIN duoc xoa
                .requestMatchers("/students/add", "/students/delete/**").hasRole("ADMIN")
                // URL can dang nhap (USER hoac ADMIN)
                .requestMatchers("/students/**").hasAnyRole("USER", "ADMIN")
                // Cac URL con lai phai xac thuc
                .anyRequest().authenticated()
            )
            // Cau hinh form login tuy chinh
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/students", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Bai 7: Trang bao loi 403 khi khong co quyen
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );

        return http.build();
    }
}
