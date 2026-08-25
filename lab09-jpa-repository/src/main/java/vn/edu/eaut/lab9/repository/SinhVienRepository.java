package vn.edu.eaut.lab9.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import vn.edu.eaut.lab9.config.JPAUtil;
import vn.edu.eaut.lab9.model.SinhVien;

import java.util.List;

public class SinhVienRepository extends BaseRepository<SinhVien, String> {

    public SinhVienRepository() {
        super(SinhVien.class);
    }

    public List<SinhVien> searchByName(String keyword) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT s FROM SinhVien s WHERE s.hoTen LIKE :keyword", SinhVien.class)
                     .setParameter("keyword", "%" + keyword + "%")
                     .getResultList();
        }
    }

    public List<SinhVien> searchAndPaginate(String keyword, int page, int size) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String jpql = "SELECT s FROM SinhVien s";
            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql += " WHERE s.hoTen LIKE :keyword OR s.lopHoc.tenLop LIKE :keyword";
            }
            TypedQuery<SinhVien> query = em.createQuery(jpql, SinhVien.class);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }
            
            // Phân trang
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            
            return query.getResultList();
        }
    }
}
