package vn.edu.eaut.lab15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // URL cong khai (su dung AntPathRequestMatcher de tranh xung dot voi H2 console)
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/dashboard")).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                // ADMIN moi duoc them/sua/xoa Student
                .requestMatchers(new AntPathRequestMatcher("/students/new")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/students/edit/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/students/delete/**")).hasRole("ADMIN")
                // ADMIN moi duoc them/sua/xoa Course
                .requestMatchers(new AntPathRequestMatcher("/courses/new")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/courses/edit/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/courses/delete/**")).hasRole("ADMIN")
                // ADMIN moi duoc xoa dang ky va cap nhat diem
                .requestMatchers(new AntPathRequestMatcher("/enrollments/delete/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/enrollments/grade/**")).hasRole("ADMIN")
                // URL can dang nhap (USER hoac ADMIN)
                .requestMatchers(new AntPathRequestMatcher("/students/**")).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/courses/**")).hasAnyRole("USER", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/enrollments/**")).hasAnyRole("USER", "ADMIN")
                // Cac URL con lai phai xac thuc
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403")
            );

        // Cho phep H2 console (su dung AntPathRequestMatcher)
        http.csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
