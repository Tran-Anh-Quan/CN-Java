package vn.edu.eaut.lab10.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.eaut.lab10.config.JPAUtil;
import vn.edu.eaut.lab10.service.AuthService;

@WebListener
public class AppListener implements ServletContextListener {

    private final AuthService authService = new AuthService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println("   LAB 10 SECURED APP - CONTEXT INITIALIZED       ");
        System.out.println("==================================================");
        try {
            authService.initSeedData();
        } catch (Exception e) {
            System.err.println("Context initialization seed failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("   LAB 10 SECURED APP - SHUTTING DOWN             ");
        JPAUtil.shutdown();
    }
}
