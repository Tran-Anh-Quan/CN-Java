package vn.edu.eaut.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Lab16ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab16ManagerApplication.class, args);
    }
}
