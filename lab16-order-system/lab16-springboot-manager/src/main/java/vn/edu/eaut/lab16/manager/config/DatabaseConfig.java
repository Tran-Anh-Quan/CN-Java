package vn.edu.eaut.lab16.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

@Configuration
public class DatabaseConfig {

    @Bean
    public DatabaseConnection databaseConnection() {
        return DatabaseConnection.getInstance();
    }
}
