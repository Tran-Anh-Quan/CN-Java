package vn.edu.eaut.lab7.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class AppLogListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========== [APP LOG] Ứng dụng đã khởi động ==========");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("========== [APP LOG] Ứng dụng đã dừng ==========");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("========== [APP LOG] Một Session mới được tạo: " + se.getSession().getId() + " ==========");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("========== [APP LOG] Session đã bị hủy: " + se.getSession().getId() + " ==========");
    }
}
