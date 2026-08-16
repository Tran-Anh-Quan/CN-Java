package vn.edu.eaut.lab6.listener;

import vn.edu.eaut.lab6.model.Student;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("SV01", "Nguyen Van A", "CNTT1", "a@eaut.edu.vn"));
        list.add(new Student("SV02", "Le Thi B", "CNTT2", "b@eaut.edu.vn"));
        list.add(new Student("SV03", "Tran Van C", "CNTT1", "c@eaut.edu.vn"));
        list.add(new Student("SV04", "Pham Thi D", "CNTT3", "d@eaut.edu.vn"));
        list.add(new Student("SV05", "Hoang Van E", "CNTT2", "e@eaut.edu.vn"));
        
        sce.getServletContext().setAttribute("students", list);
        System.out.println("[APP LIFECYCLE] Application initialized with " + list.size() + " sample students.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void contextDestroyed(ServletContextEvent sce) {
        List<Student> list = (List<Student>) sce.getServletContext().getAttribute("students");
        int count = (list != null) ? list.size() : 0;
        System.out.println("[APP LIFECYCLE] Application destroyed. Remaining student count: " + count);
    }
}