package vn.edu.eaut.lab9.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.eaut.lab9.model.LopHoc;
import vn.edu.eaut.lab9.model.MonHoc;
import vn.edu.eaut.lab9.model.SinhVien;

import java.util.Optional;

@WebListener
public class DataSeedListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Kiểm tra và tạo dữ liệu mẫu (Seed Data)...");
        seedData();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.closeFactory();
    }

    private void seedData() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Kiểm tra LopHoc
            Long countLop = em.createQuery("SELECT COUNT(l) FROM LopHoc l", Long.class).getSingleResult();
            if (countLop == 0) {
                LopHoc lop1 = new LopHoc("DCNTT14-01", "CNTT K14 Lớp 1", null);
                LopHoc lop2 = new LopHoc("DCNTT14-02", "CNTT K14 Lớp 2", null);
                em.persist(lop1);
                em.persist(lop2);
                
                MonHoc mon1 = new MonHoc("IT3242", "Công nghệ Java", 3);
                em.persist(mon1);

                SinhVien sv = new SinhVien();
                sv.setMaSv("SV001");
                sv.setHoTen("Nguyễn Văn A");
                sv.setEmail("nva@gmail.com");
                sv.setChuyenNganh("CNTT");
                sv.setLopHoc(lop1);
                em.persist(sv);
                
                System.out.println("Đã seed dữ liệu mẫu thành công!");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
